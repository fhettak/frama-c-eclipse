package net.eclipse.why.editeur;

import java.util.ArrayList;

/**
 * This class represents a function object
 * 
 * @author A. Oudot
 */
public class Function {
	
	private String name; //the name
	private boolean lemma; //is this function is a lemma?
	private String behavior; //a description of behavior
	private String file; //the source file
	private int[] loc; //the location in source file of corresponding code
	private int po_proved; //number of proved PO
	private boolean item_expanded; //is the corresponding item in Prover View expanded?

	private ArrayList<PO> poList;
	
	/**
	 * Class Constructor
	 */
	public Function() {
		name = "";
		lemma = false;
		behavior = "";
		file = "";
		loc = new int[]{0,0,0};
		po_proved = 0;
		item_expanded = false;
		poList = new ArrayList<PO>();
	}
	
	/**
	 * @return the behavior details, null if it doen't exist
	 */
	public String getBehavior() {
		return behavior;
	}

	/**
	 * @param bhv the behavior details
	 */
	public void setBehavior(String bhv) {
		this.behavior = bhv;
	}

	/**
	 * @return the file, null if it doen't exist
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @param file the file
	 */
	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * @return location of code in the source file
	 */
	public int[] getLoc() {
		return loc;
	}
	
	/**
	 * Gets one of location integer
	 * 
	 * @param x position in location board
	 * @return the location field
	 */
	public int getLoc(int x) {
		return loc[x];
	}

	/**
	 * @param loc the location board
	 */
	public void setLoc(int[] loc) {
		this.loc = loc;
	}
	
	/**
	 * Sets one of location integer
	 * 
	 * @param y position in location board
	 * @param w value to set
	 */
	public void setLoc(int y, int w) {
		loc[y] = w;
	}

	/**
	 * Gets the function's name
	 * 
	 * @return the function's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the function's name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Is this function a lemma?
	 * 
	 * @return true if this function is a lemma
	 */
	public boolean isLemma() {
		return lemma;
	}
	
	/**
	 * Puts the 'lemma' boolean indicator to true
	 */
	public void putToLemma() {
		lemma = true;
	}

	/**
	 * Is this function's item expanded?
	 * 
	 * @return true if this function's item
	 */
	public boolean isItem_expanded() {
		return item_expanded;
	}

	/**
	 * Puts the 'expand' boolean indicator to true
	 */
	public void expand() {
		this.item_expanded = true;
	}
	
	/**
	 * Puts the 'expand' boolean indicator to false
	 */
	public void collapse() {
		this.item_expanded = false;
	}
	
	/**
	 * Gets the number of proved PO of this function
	 * 
	 * @return the number of proved PO of this function
	 */
	public int getPo_proved() {
		return po_proved;
	}

	/**
	 * Sets the number of proved PO of this function
	 * 
	 * @param po_proved the number of proved PO of this function
	 */
	public void setPo_proved(int po_proved) {
		this.po_proved = po_proved;
	}
		
	/**
	 * Is this function proved?
	 * 
	 * @return true if all goals of function are proved
	 */
	public boolean isProved() {
		return (po_proved == poList.size());
	}
	
	/**
	 * Puts the number of PO proved to 0.
	 */
	public void init() {
		po_proved = 0;
	}
	
	public void addPO (PO po) {
		poList.add(po);
	}
	
	public ArrayList<PO> getPOList () {
		return poList;
	}

	public void increase_po_proved() {
		po_proved++;	
	}

	public void decrease_po_proved() {
		po_proved--;	
	}
	public int getPo_unproved() {
		return poList.size() - po_proved;
	}
}
