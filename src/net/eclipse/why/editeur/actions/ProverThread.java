package net.eclipse.why.editeur.actions;


import java.util.ArrayList;

import net.eclipse.why.editeur.FileInfos;
import net.eclipse.why.editeur.PO;
import net.eclipse.why.editeur.views.TraceView;
import net.eclipse.why.editeur.views.TraceView.MessageType;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;


/**
 *  Run a prover on a set of goals
 * 
 * @author a.oudot
 */
public class ProverThread extends Thread implements Runnable {

	private Thread thread;						// The thread
	private long identity;						// Single id for the thread
	private ArrayList<String[]> goalsSet;		// {goals}
	private ProverViewUpdater uwv;  			// View updater object
	private boolean all;
	private int goal;							// Goal number
	private int subgoal;
	private Composite parent;
	private boolean carryOn;
	private int result;
	private int proverTmp;
	
	/**
	 * Class constructor
	 * 
	 * @param goals {goals}
	 * @param prover the prover number
	 * @param proveall is the thread going to prove all goals
	 * @param u the update object with update methods
	 */
	public ProverThread(ArrayList<String[]> goals, boolean proveall, ProverViewUpdater u) {
		thread = new Thread(this);
		this.identity = thread.getId();
		this.goalsSet = goals;
		this.uwv = u;
		this.goal = 1;
		this.subgoal = 0;
		this.all = proveall;
		this.parent = u.view.getViewer().getParent();
		carryOn = true;
		result = 0;
		thread.start();
	}
	
	/**
	 * Thread Id getter
	 * 
	 * @return long
	 */
	public long getIdentity() {
		return identity;
	}
	
	/**
	 * Send the stop signal for the thread 
	 */
	public void cease() {
		carryOn = false;
	}
	
	/**
	 * Thread runner
	 */
	public void run() {
		
		//if the prover variable is equals to -1, it means
		//that all provers must be used to prove the goal.

		proverTmp = 0;

		//Stop button enabled
		parent.getDisplay().asyncExec(new Runnable() {
			public void run() {
				try {
					uwv.activateKillProcessButton(true);
				} catch(PartInitException e) {
					TraceView.print(MessageType.ERROR, "ProverThread ~> UpdateWhyView.activateKillProcessButton(true) : " + e);
				}
			}
		});
		
		
		while (goalsSet.size() > 0) { //while there are goals
			
			//If stop button has clicked, thread stop
			if(!carryOn) break;
			
			//We get the first goal from the set
			Object obset = goalsSet.get(0);
			String[] set = (String[])obset;
			String[] parts = set[0].split("-");
			goal = Integer.parseInt(parts[0]);
			if(parts.length > 1) {
				subgoal = Integer.parseInt(parts[1]);
			} else {
				subgoal = 0;
			}
			
			
			//if there are subgoals for this goal, we replace the goal by all its subgoals
			PO po = ((PO)FileInfos.goals.get(goal-1));
			boolean prv = FileInfos.status[proverTmp].equals("prover");
			if((subgoal == 0) && prv) {
				int e = po.getNbSubGoals();
				if(e > 0) { //there are subgoals
					goalsSet.remove(0); //we firstly remove the goal from the set
					if(!all) { //only unproved goals!
						//here we are going to create a set of String[2] objects
						//each of them representing a set of unproved goals (first and last)
						int indice = 0; //the nb of set of goals
						int subgoaltmp = 1;
						boolean inASet = false;
						String[] ens = new String[2]; //contains the first and the last subgoals
						while(subgoaltmp <= e) { //for all subgoals
							PO spo = po.getSubGoal(subgoaltmp);
							if(spo.isProved()) { //if the subgoal is proved
								if(inASet) { //we stop the set
									inASet = false;
									ens[1] = goal + "-" + (subgoaltmp-1);
									goalsSet.add(indice, ens); //we record the new set of goals
									ens = new String[2];
									indice++;
								}
							} else { //if the subgoal is not proved
								if(!inASet) { //we start the set
									inASet = true;
									ens[0] = goal + "-" + subgoaltmp;
									if(subgoal==0) subgoal = subgoaltmp;
								}
								if(subgoaltmp == e) { //maybe, we've to end the set too!
									ens[1] = goal + "-" + subgoaltmp;
									goalsSet.add(indice, ens);
									indice++;
								}
							}
							subgoaltmp++;
						}
						//we save the last set of goals if necessary
						int endgoal = Integer.parseInt(set[1]);
						if(endgoal > goal) {
							goalsSet.add(indice, new String[]{"" + (goal+1), "" + endgoal});
						}
						
					} else { //all goals! A little more simple...
						subgoal = 1;
						int endgoal = Integer.parseInt(set[1]);
						if(endgoal > goal) {
							goalsSet.add(0, new String[]{"" + (goal+1), "" + endgoal});
						}
						goalsSet.add(0, new String[]{goal + "-1", goal + "-" + e});
					}
				}
			}
			
			boolean remove = false; //do we have to remove the goal from the set
			if(subgoal > 0) po = po.getSubGoal(subgoal);
			
			//if we don't have to execute provers on ever proved goals
			//or being-proved goals, we jump to the next
			if(prv && !all && (po.isProved() || po.isWorkinOn())) {
				remove = true;
			} 
			else {

				po.workOn(); //the prover works on this po now
				
				//We put orange color or orange balls in goal and function buttons
					parent.getDisplay().syncExec(new Runnable() {
					public void run() {
						try {
							uwv.color(goal, subgoal, proverTmp);
						} catch (PartInitException e) {
							TraceView.print(MessageType.ERROR, "ProverThread ~> UpdateWhyView.color() : " + e);
						}
					}
				});
			
				//Prover execution
				ProverExecutor ex = new ProverExecutor();
				result = ex.prove(goal, subgoal, proverTmp);
			
				//updates
				parent.getDisplay().syncExec(new Runnable() {
					public void run() {
						try {
							uwv.update(goal, subgoal, proverTmp);
						} catch (PartInitException e) {
							TraceView.print(MessageType.ERROR, "ProverThread ~> UpdateWhyView.updateElementAt() : " + e);
						}
					}
				});
			
				//we change the prover (or not)
				if (proverTmp == (FileInfos.provers.length -1)) {
					//we used all provers we could => stop
					remove = true;
				} else {
					if(result>1) { //the goal is not proved yet
						//we try to find another prover :
						proverTmp++;
						while( proverTmp < FileInfos.provers.length && FileInfos.status[proverTmp].equals("assistant") ) {
							proverTmp ++; //next
						}
						if( proverTmp >= FileInfos.provers.length || ( proverTmp == (FileInfos.provers.length -1) && FileInfos.status[proverTmp].equals("assistant") )) {
							remove = true; //the end of provers' list => stop!
						}
					} else { //the goal has just been proved => we can stop!
						remove = true;
					}
				}
				
				po.loafAbout(); //the prover doesn't work on this po!
			}

			// We delete the po from the goal set (or not)
			if(remove) {
				set = (String[])goalsSet.get(0);
				parts = set[1].split("-");
				goalsSet.remove(0);
				int fgoal = Integer.parseInt(parts[0]);
				int fsubgoal = 0;
				if(parts.length > 1) {
					fsubgoal = Integer.parseInt(parts[1]);
					if(subgoal < fsubgoal) {
						String[] newSet = new String[2];
						newSet[0] = "" + goal + "-" + (subgoal+1);
						newSet[1] = "" + goal + "-" + fsubgoal;
						goalsSet.add(0, newSet);
					}
				} else if( goal < fgoal ) {
					String[] newSet = new String[2];
					newSet[0] = "" + (goal+1);
					newSet[1] = "" + fgoal;
					goalsSet.add(0, newSet);
				}
				proverTmp = 0;
			}
		}
		
		
		//To finish, we delete the thread from the list, we disable the stop button
		parent.getDisplay().asyncExec(new Runnable() {
			public void run() {
				try {
					uwv.removeThread(identity);
					uwv.activateKillProcessButton(false);
				} catch(PartInitException e) {
					TraceView.print(MessageType.ERROR, "ProverThread ~> UpdateWhyView.activateKillProcessButton(false) : " + e);
				}
			}
		});
		
		//and update of provers statistics
		parent.getDisplay().asyncExec(new Runnable() {
			public void run() {
				try {
					for(int i=0; i<FileInfos.provers.length; i++) {
							if(FileInfos.status[i].equals("prover")) {
								uwv.stats(i);
							}
					}
				} catch(PartInitException e) {
					TraceView.print(MessageType.ERROR, "ProverThread ~> UpdateWhyView.stats() : " + e);
				}
			}
		});
	}
}
