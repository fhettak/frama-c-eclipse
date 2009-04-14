package net.eclipse.why.editeur;

import java.util.ArrayList;
import java.util.Vector;

/**
 * This class represents a PO(proof obligation) object
 * 
 * @author oudot
 */
public class PO {
	
	private String name; //name
	private int num; //goal(po) number
	private int subnum; //subgoal number (if this goal is a subgoal)
	private int num_in_f; //place in function
	private String kind, text, title; //type parameters
	private int fnum; //number of its function
	private String file; //source file
	private int[] loc; //location of corresponding code in source file (line, first and last characters)
	//private boolean is_checked; //is the goal checked?
	private boolean is_proved; //is the goal proved
	private boolean item_expanded; //is the item in Prover View expanded?
	private boolean workinOn; //is a prover workin on?
	
	private ArrayList<PO> subGoals; //set of subgoals
	private Vector<Integer> etat; //state of the goal for all provers

	/**
	 * Class constructor
	 */
	public PO() {
		name = "";
		kind = ""; text = ""; title = "";
		num = 0;
		subnum = 0;
		num_in_f = 0;
		fnum = 0;
		file = "";
		loc = new int[]{0,0,0};
		//is_checked = false;
		is_proved = false;
		item_expanded = false;
		workinOn = false;
		subGoals = new ArrayList<PO>();
		etat = new Vector<Integer>();
	}
	
	
	/**
	 * Puts the expand boolean variable to true
	 */
	public void expand() {
		this.item_expanded = true;
	}
	
	/**
	 * Puts the expand boolean variable to false
	 */
	public void collapse() {
		this.item_expanded = false;
	}
	
	/**
	 * Is this goal's item expanded?
	 * 
	 * @return true if the item of this goal is expanded
	 */
	public boolean isItem_expanded() {
		return item_expanded;
	}
	
	/**
	 * Is a prover working on this goal?
	 * 
	 * return true if a prover is working on this goal
	 */
	public boolean isWorkinOn() {
		return workinOn;
	}
	
	/**
	 * Puts the working boolean variable to true
	 */
	public void workOn() {
		workinOn = true;
	}
	
	/**
	 * Puts the working boolean variable to false
	 */
	public void loafAbout() {
		workinOn = false;
	}
	
	/**
	 * Returns the kind value
	 * 
	 * @return the kind value
	 */
	public String getKind() {
		return kind;
	}
	
	/**
	 * Sets the kind value
	 * 
	 * @param k the kind value
	 */
	public void setKind(String k) {
		kind = k;
	}
	
	/**
	 * Returns the text value
	 * 
	 * @return the text value
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Sets the text value
	 * 
	 * @param x the text value
	 */
	public void setText(String x) {
		text = x;
	}

	/**
	 * Gets the ID string of this goal
	 * 
	 * @return the ID string of this goal
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * Sets the ID string of this goal
	 * 
	 * @param t the ID string of this goal
	 */
	public void setTitle(String t) {
		this.title = t;
	}
	
	/**
	 * Make the title value using kind and text values
	 */
	public void makeTitle() {
		String x = GoalTitles.getTitle(kind,text);
		title = ( (x == null) ? "" : x );
	}


	/**
	 * Gets the source file
	 * 
	 * @return the source file
	 */
	public String getFile() {
		return file;
	}


	/**
	 * Sets the source file
	 * 
	 * @param file the source file
	 */
	public void setFile(String file) {
		this.file = file;
	}


	/**
	 * Gets the number of the function
	 * 
	 * @return the number of the function
	 */
	public int getFnum() {
		return fnum;
	}
	
	/**
	 * Gets the name of the function
	 * 
	 * @return the name of the function
	 */
	public String getFname() {
		return ((Function)FileInfos.functions.get(fnum-1)).getName();
	}


	/**
	 * Sets the number of the function
	 * 
	 * @param fnum the number of the function
	 */
	public void setFnum(int fnum) {
		this.fnum = fnum;
	}


	/**
	 * Gets one of the corresponding code position parameter
	 * 
	 * @param x position of the parameter in the board, from 0 to 2
	 * @return the corresponding code position parameter
	 */
	public int getLoc(int x) {
		return loc[x];
	}


	/**
	 * Sets the code location board
	 * 
	 * @param loc the code location board
	 */
	public void setLoc(int[] loc) {
		this.loc = loc;
	}
	
	/**
	 * Sets one of the corresponding code position parameter
	 * 
	 * @param x position of the parameter in the board, from 0 to 2
	 */
	public void setLoc(int x, int v) {
		loc[x] = v;
	}


	/**
	 * Gets the name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * Sets the name
	 * 
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Gets the number of this goaal
	 * 
	 * @return the number of this goaal
	 */
	public int getNum() {
		return num;
	}


	/**
	 * Sets the number of this subgoal
	 * 
	 * @param snum the number of this subgoal
	 */
	public void setSubNum(int snum) {
		this.subnum = snum;
	}
	
	/**
	 * Gets the number of this subgoal
	 * 
	 * @return the number of this subgoal
	 */
	public int getSubNum() {
		return subnum;
	}


	/**
	 * Sets the number of this goal
	 * 
	 * @param num the number of this goal
	 */
	public void setNum(int num) {
		this.num = num;
	}


	/**
	 * Gets the position of this goal in its function
	 * 
	 * @return the position of this goal in its function
	 */
	public int getNum_in_f() {
		return num_in_f;
	}


	/**
	 * Sets the position of this goal in its function
	 * 
	 * @param n_in_f the position of this goal in its function
	 */
	public void setNum_in_f(int n_in_f) {
		this.num_in_f = n_in_f;
	}
	
	/*
	 * 
	 *
	public void gCheck() {
		
		if(is_checked) { //sécurité!
			return;
		}
		
		this.is_checked = true;
		Function f = (Function)FileInfos.functions.get(fnum-1);
		
		if(subnum == 0) { //Nous sommes dans un but
			
			for(int r=0; r<subGoals.size(); r++) {
				PO op = (PO)subGoals.get(r);
				if(!op.is_checked) op.check();
			}
			
			f.increase_po_checked();
			
		} else { //Nous sommes dans un sous-but
			
			PO op = (PO)FileInfos.goals.get(num-1);
			if(!op.is_checked) {
				boolean checked = true;
				for(int r=0; r<op.subGoals.size(); r++) {
					PO po = (PO)op.subGoals.get(r);
					if(!po.isChecked()) {
						checked = false;
						break;
					}
				}
				if(checked) {
					op.check();
					f.increase_po_checked();
				}
			}
		}
	}/**/
	
	/*
	 * Puts the 'is_checked' variable to true.
	 *
	public void check() {
		is_checked = true;
	}/**/
	
	/*
	 * 
	 *
	public void gUncheck() {
		
		if(!is_checked) { //sécurité!
			return;
		}
		
		this.is_checked = false; 
		Function f = (Function)FileInfos.functions.get(fnum-1);
		
		if(subnum == 0) { //Nous sommes dans un but
			
			for(int r=0; r<subGoals.size(); r++) {
				PO op = (PO)subGoals.get(r);
				if(op.is_checked) {
					op.uncheck();
				}
			}
			
			f.decrease_po_checked();
			
		} else { //Nous sommes dans un sous-but
			
			PO op = (PO)FileInfos.goals.get(num-1);
			if(op.is_checked) {
				op.uncheck();
				f.decrease_po_checked();
			}
		}
	}/**/
	
	/*
	 * Puts the 'is_checked' variable to false.
	 *
	public void uncheck() {
		is_checked = false;
	}/**/
	
	/*
	 * @return boolean
	 *
	public boolean isChecked() {
		return is_checked;
	} /**/
	
	
	/**
	 * The goal is proved. It's necessary to modify subgoals or
	 * parent goal and function fields...
	 */
	public void gProve() {
		
		if(is_proved) { //security
			return;
		}
		
		this.is_proved = true; //the goal is proved
		Function f = (Function)FileInfos.functions.get(fnum-1);
		
		if(subnum == 0) { //the goal is a goal
			//all subgoals are proved
			for(int r=0; r<subGoals.size(); r++) {
				PO op = (PO)subGoals.get(r);
				if(!op.is_proved) op.prove();
			}
			//one more PO proved in the function
			f.increase_po_proved();
			
		} else { //the goal is a subgoal
			
			PO op = (PO)FileInfos.goals.get(num-1);
			if(!op.is_proved) { //if the parent goal is unproved
				boolean proved = true;
				//we test if all subgoals of the parent goal are proved
				for(int r=0; r<op.subGoals.size(); r++) {
					PO po = (PO)op.subGoals.get(r);
					if(!po.isProved()) {
						proved = false;
						break;
					}
				}
				if(proved) { //if it is the case
					op.prove(); //the parent goal is now proved
					f.increase_po_proved(); //and one more PO proved in the function!
				}
			}
		}
	}
	
	/**
	 * Puts the 'is_proved' variable to true.
	 */
	public void prove() {
		is_proved = true;
	}
	
	/**
	 * The goal is unproved. It's necessary to modify some of
	 * subgoals' or parent goal's and function's fields...
	 */
	public void gUnprove() {
		
		if(!is_proved) { //security
			return;
		}
		
		this.is_proved = false; //unproved!
		Function f = (Function)FileInfos.functions.get(fnum-1);
		
		if(subnum == 0) { //the goal is a goal
			//all proved subgoals are now unproved
			for(int r=0; r<subGoals.size(); r++) {
				PO op = (PO)subGoals.get(r);
				if(op.is_proved) op.unprove();
			}
			f.decrease_po_proved(); //one less proved PO in the function
			
		} else { //the goal is a subgoal
			
			PO op = (PO)FileInfos.goals.get(num-1);
			if(op.is_proved) { //if the parent goal was proved
				op.unprove(); //it's now unproved
				f.decrease_po_proved(); //one one less proved PO in the function
			}
		}
	}
	
	/**
	 * Puts the 'is_proved' variable to false.
	 */
	public void unprove() {
		is_proved = false;
	}
	
	/**
	 * Is this PO proved?
	 * 
	 * @return truz if this PO is proved
	 */
	public boolean isProved() {
		return is_proved;
	}
	
	/**
	 * Initialisation of proof states
	 */
	public void initStates() {
		etat.removeAllElements();
		int l = FileInfos.provers.length;
		for(int m=0; m<l; m++) {
			etat.add(new Integer(0));
		}
	}
	
	/**
	 * Sets the states of this goal for each provers
	 * 
	 * @param v the vector containing the states of this goal for each provers
	 */
	public void setStates(Vector<Integer> v) {
		etat = v;
	}
	
	/**
	 * Adds a new state value for a new prover
	 * 
	 * @param v the state's value
	 */
	public void addState(int v) {
		etat.add(new Integer(v));
	}
	
	/**
	 * Changes the state value of a prover
	 * 
	 * @param r the row of the prover
	 * @param v the new state value
	 */
	public void changeStateValue(int r, int v) {
		etat.remove(r);
		etat.add(r, new Integer(v));
	}
	
	/**
	 * Sets a prover state value and make all necessary
	 * modifications in objects.
	 * 
	 * @param rang the row of the prover
	 * @param valeur the new value for the state
	 */
	public void setState(int rang, int valeur) {
		
		changeStateValue(rang, valeur); //change the state value
		
		PO po = null;
		int suretat = -1;
		if(subnum != 0) { //if the goal is a subgoal
			//we get the state of the parent
			po = (PO)FileInfos.goals.get(num-1);
			suretat = po.getState(rang);
		}
		
		
		if(valeur == 1) { //if the goal has been proved
			
			boolean surproved = false;;
			if(po != null) { //here we are in a subgoal
				if(suretat != 1) { //if parent goal was not proved by this prover
					surproved = true;
					//we have to verify if it is now
					for(int a=1; a<=po.getNbSubGoals(); a++) {
						if(po.getSubGoal(a).getState(rang) != 1) {
							surproved = false;
							break;
						}
					}
					if(surproved) { //if it is
						//we set the state of the parent goal to 1
						po.etat.remove(rang);
						po.etat.add(rang,new Integer(valeur));
					}
				}
			}
			
			if(!is_proved) { //if this goal has just been proved
				gProve();
			} else if(surproved) { //else, if the parent goal has been proved
				po.gProve();
				//po.gCheck();
			}
			
			//if(!is_checked) {
				//gCheck();
			//}
			
			
		} else { //the goal hasn't been unproved
			
			boolean surunproved = false;
			if(po != null) { //here we are in a subgoal
				//we change the state of the parent goal!
				po.etat.remove(rang);
				po.etat.add(rang,new Integer(valeur));
				surunproved = true;
				//we check now if the goal is proved by another prover
				for(int b=0; b<po.etat.size(); b++) {
					if(((Integer)po.etat.get(b)).intValue() == 1) {
						surunproved = false;
						break;
					}
				}
			}
			
			if(is_proved /*|| is_checked*/ ) { //if the goal was proved
				boolean proved = false;
				//we check if it's again the case
				for(int b=0; b<etat.size(); b++) {
					if(((Integer)etat.get(b)).intValue() == 1) {
						proved = true;
						break;
					}
				}
				if(!proved) { //if the goal is now unproved
					gUnprove();
					//gUncheck();
				}
			} else if(surunproved) { //else, if the goal isn't proved by another prover
				po.gUnprove();
				//po.gUncheck();
			}
		}
	}
	
	/**
	 * Gets the state of the PO for a given prover
	 * 
	 * @param rang the prover row
	 * @return the state of the PO
	 */
	public int getState(int rang) {
		return ((Integer)etat.get(rang)).intValue();
	}
	
	/**
	 * Invert, add or delete values in the list of states of the goal, list ordered by provers.
	 * 
	 * @param ptab set of permutations to do
	 */
	public void pswitch(int[] ptab) {
		Vector<Integer> e = new Vector<Integer>();
		for(int i=0; i<ptab.length; i++) {
			if(ptab[i] >= 0) {
				int v = ((Integer)etat.get(ptab[i])).intValue();
				e.add(new Integer(v));
			} else {
				e.add(new Integer(0));
			}
		}
		etat = e;
		if(getNbSubGoals() > 0) {
			for(int k=0; k<subGoals.size(); k++) {
				((PO)subGoals.get(k)).pswitch(ptab);
			}
		}
	}
	
	/**
	 * Adds a new subgoal in subgoals set
	 */
	public PO addSubGoal() {
		PO po = new PO();
		po.setFnum(fnum);
		po.setNum(num);
		po.setNum_in_f(num_in_f);
		int n = subGoals.size()+1;
		po.setSubNum(n);
		po.setKind(kind);
		po.setText(text);
		po.setTitle(title);
		po.setName(num_in_f + "-" + n);
		po.initStates();
		//po.setFile(file);
		//po.setLoc(loc);
		subGoals.add(po);
		return po;
	}
	
	/**
	 * Adds a given subgoal in subgoals set
	 * 
	 * @param po the new PO to add
	 */
	public void addSubGoal(PO po) {
		subGoals.add(po);
	}
	
	/**
	 * Gets a subgoal
	 * 
	 * @param sg the subgoal row
	 * @return the PO object
	 */
	public PO getSubGoal(int sg) {
		return (PO)subGoals.get(sg -1);
	}
	
	/**
	 * Gets the number of subgoals
	 * 
	 * @return the number of subgoals
	 */
	public int getNbSubGoals() {
		return subGoals.size();
	}
	
	/**
	 * Gets the number of unproved subgoals
	 * 
	 * @return the number of unproved subgoals
	 */
	public int getNbUnprovedSubGoals() {
		int E = 0;
		for(int e=1; e<=subGoals.size(); e++) {
			if(!getSubGoal(e).is_proved) E++;
		}
		return E;
	}
	
	/**
	 * Clean the subgoals set
	 */
	public void cleanSubGoals() {
		subGoals.clear();
	}
	
	/**
	 * Init the states subgoals' states to the unproved value
	 */
	public void init() {
		unprove();
		initStates();
		for(int q=0; q<subGoals.size(); q++) {
			subGoals.get(q).unprove();
			subGoals.get(q).initStates();
		}
	}
}
