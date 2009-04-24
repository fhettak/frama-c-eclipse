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
 * @author A.Oudot
 */
public class ProverThread extends Thread implements Runnable {

	private Thread thread;						// The thread
	private long identity;						// Single id for the thread
	private ArrayList<Integer> goals;			// {goals}
	private ProverViewUpdater uwv;  			// View updater object
	private boolean all;
	private int goal;							// Goal number
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
	public ProverThread(ArrayList<Integer> goals, boolean proveall, ProverViewUpdater u) {
		thread = new Thread(this);
		this.identity = thread.getId();
		this.goals = goals;
		this.uwv = u;
		this.goal = 1;
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
		
		
		while (goals.size() > 0) { //while there are goals
			
			//If stop button has clicked, thread stop
			if(!carryOn) break;
			
			goal = (int)goals.get(0);
			
			PO po = ((PO)FileInfos.goals.get(goal-1));
			boolean prv = FileInfos.status[proverTmp].equals("prover");
			
			boolean remove = false; //do we have to remove the goal from the set
			
			//if we don't have to execute provers on ever proved goals
			//or being-proved goals, we jump to the next
			if(prv && !all && (po.isProved() || po.isWorkinOn())) {
				remove = true;
			} else {
				po.workOn(); //the prover works on this po now
				
				// We put orange color or orange balls in goal and function buttons
				parent.getDisplay().syncExec(new Runnable() {
					public void run() {
						try {
							uwv.color(goal, proverTmp);
						} catch (PartInitException e) {
							TraceView.print(MessageType.ERROR, "ProverThread ~> UpdateWhyView.color() : " + e);
						}
					}
				});
			
				//Prover execution
				ProverExecutor ex = new ProverExecutor();
				result = ex.prove(goal, proverTmp);
			
				//updates
				parent.getDisplay().syncExec(new Runnable() {
					public void run() {
						try {
							uwv.update(goal, proverTmp);
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
					if(result>1) { 
						//the goal is not proved yet
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
				goals.remove(0);
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
