package net.eclipse.why.editeur;

import java.io.File;
import java.util.Vector;

/**
 * This class represents a PO(proof obligation) object
 * 
 * @author A. Oudot
 */
public class PO {

	private String name; // name
	private int num; // goal(po) number
	private int num_in_f; // place in function
	private String kind, text, title; // type parameters
	private int fnum; // number of its function
	private String file; // source file
	private int[] loc; // location of corresponding code in source file (line,
						// first and last characters)
	// private boolean is_checked; //is the goal checked?
	private boolean is_proved; // is the goal proved
	private boolean item_expanded; // is the item in Prover View expanded?
	private boolean workingOn; // is a prover working on?
	private Vector<Integer> etat; // state of the goal for all provers
	private String xplFile;

	public PO() {
		name = "";
		kind = "";
		text = "";
		title = "";
		xplFile = "";
		num = 0;
		num_in_f = 0;
		fnum = 0;
		file = "";
		loc = new int[] { 0, 0, 0 };
		// is_checked = false;
		is_proved = false;
		item_expanded = false;
		workingOn = false;
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
		return workingOn;
	}

	/**
	 * Puts the working boolean variable to true
	 */
	public void workOn() {
		workingOn = true;
	}

	/**
	 * Puts the working boolean variable to false
	 */
	public void loafAbout() {
		workingOn = false;
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
	 * @param k
	 *            the kind value
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
	 * @param x
	 *            the text value
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
	 * @param t
	 *            the ID string of this goal
	 */
	public void setTitle(String t) {
		this.title = t;
	}

	/**
	 * Make the title value using kind and text values
	 */
	public void makeTitle() {
		String x = GoalTitles.getTitle(kind, text);
		title = ((x == null) ? "" : x);
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
	 * @param file
	 *            the source file
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
		return FileInfos.functions.get(fnum - 1).getName();
	}

	/**
	 * Sets the number of the function
	 * 
	 * @param fnum
	 *            the number of the function
	 */
	public void setFnum(int fnum) {
		this.fnum = fnum;
	}

	/**
	 * Gets one of the corresponding code position parameter
	 * 
	 * @param x
	 *            position of the parameter in the board, from 0 to 2
	 * @return the corresponding code position parameter
	 */
	public int getLoc(int x) {
		return loc[x];
	}

	/**
	 * Sets the code location board
	 * 
	 * @param loc
	 *            the code location board
	 */
	public void setLoc(int[] loc) {
		this.loc = loc;
	}

	/**
	 * Sets one of the corresponding code position parameter
	 * 
	 * @param x
	 *            position of the parameter in the board, from 0 to 2
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
	 * @param name
	 *            the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the number of this goal
	 * 
	 * @return the number of this goal
	 */
	public int getNum() {
		return num;
	}

	/**
	 * Sets the number of this goal
	 * 
	 * @param num
	 *            the number of this goal
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
	 * @param n_in_f
	 *            the position of this goal in its function
	 */
	public void setNum_in_f(int n_in_f) {
		this.num_in_f = n_in_f;
	}

	/**
	 * The goal is proved. It's necessary to modify parent goal and
	 * function fields...
	 */
	public void gProve() {

		if (is_proved) { // security
			return;
		}

		this.is_proved = true; // the goal is proved
		Function f =  FileInfos.functions.get(fnum - 1);
		
		// one more PO proved in the function
		f.increase_po_proved();
	}

	/**
	 * Puts the 'is_proved' variable to true.
	 */
	public void prove() {
		is_proved = true;
	}

	/**
	 * The goal is unproved. It's necessary to modify parent goal's and function's fields...
	 */
	public void gUnprove() {

		if (!is_proved) { // security
			return;
		}

		this.is_proved = false; // unproved!
		Function f = FileInfos.functions.get(fnum - 1);

		f.decrease_po_proved(); // one less proved PO in the function
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
	 * Initialization of proof states
	 */
	public void initStates() {
		etat.removeAllElements();
		int l = FileInfos.provers.length;
		for (int m = 0; m < l; m++) {
			etat.add(Integer.valueOf(0));
		}
	}

	/**
	 * Sets the states of this goal for each provers
	 * 
	 * @param v
	 *            the vector containing the states of this goal for each provers
	 */
	public void setStates(Vector<Integer> v) {
		etat = v;
	}

	/**
	 * Adds a new state value for a new prover
	 * 
	 * @param v
	 *            the state's value
	 */
	public void addState(int v) {
		etat.add(Integer.valueOf(v));
	}

	/**
	 * Changes the state value of a prover
	 * 
	 * @param r
	 *            the row of the prover
	 * @param v
	 *            the new state value
	 */
	public void changeStateValue(int r, int v) {
		etat.remove(r);
		etat.add(r, Integer.valueOf(v));
	}

	/**
	 * Sets a prover state value and make all necessary modifications in
	 * objects.
	 * 
	 * @param rang
	 *            the row of the prover
	 * @param value
	 *            the new value for the state
	 */
	public void setState(int rang, int value) {

		changeStateValue(rang, value); // change the state value

		if (value == 1) { // if the goal has been proved

			if (!is_proved) { // if this goal has just been proved
				gProve();
			}

		} else { // the goal hasn't been unproved

			if (is_proved) { // if the goal was proved
				boolean proved = false;
				// we check if it's again the case
				for (int b = 0; b < etat.size(); b++) {
					if (((Integer) etat.get(b)).intValue() == 1) {
						proved = true;
						break;
					}
				}
				if (!proved) { // if the goal is now unproved
					gUnprove();
				}
			}
		}
	}

	/**
	 * Gets the state of the PO for a given prover
	 * 
	 * @param rang
	 *            the prover row
	 * @return the state of the PO
	 */
	public int getState(int rang) {
		return ((Integer) etat.get(rang)).intValue();
	}

	/**
	 * Invert, add or delete values in the list of states of the goal, list
	 * ordered by provers.
	 * 
	 * @param ptab
	 *            set of permutations to do
	 */
	public void pswitch(int[] ptab) {
		Vector<Integer> e = new Vector<Integer>();
		for (int i = 0; i < ptab.length; i++) {
			if (ptab[i] >= 0) {
				int v = ((Integer) etat.get(ptab[i])).intValue();
				e.add(Integer.valueOf(v));
			} else {
				e.add(Integer.valueOf(0));
			}
		}
		etat = e;
	}

	/**
	 * Initialize the state to the unproved value
	 */
	public void init() {
		unprove();
		initStates();
	}
	
	public String getXplFile() {
		return xplFile;
	}
	
	public void setXplFile(String file) {
		xplFile = file;
	}
	
	public String getWhyFile () {
		File loc = new File(xplFile);
		return loc.getName().replaceAll(".xpl", ".why");
	}
	public String getErgoFile () {
		File loc = new File(xplFile);
		return loc.getName().replaceAll(".xpl", "_why.why");
	}
}
