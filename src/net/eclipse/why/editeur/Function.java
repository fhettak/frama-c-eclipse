package net.eclipse.why.editeur;

/**
 * This class represents a function object
 * 
 * @author oudot
 */
public class Function {
	
	private String name; //the name
	private boolean lemma; //is this function is a lemma?
	private String behavior; //a description of behavior
	private String file; //the source file
	private int[] loc; //the location in source file of corresponding code
	private int first_po; //number of the first PO
	private int po; //nb of PO
	private int po_proved; //nb of proved PO
	//private int po_checked; //nb of checked PO
	//private boolean is_checked; //is this function checked?
	private boolean item_expanded; //is the corresponding item in Prover View expanded?
	

	/**
	 * Class Constructor
	 */
	public Function() {
		name = "";
		lemma = false;
		behavior = "";
		file = "";
		loc = new int[]{0,0,0};
		first_po = 0;
		po = 0;
		po_proved = 0;
		//po_checked = 0;
		//is_checked = false;
		item_expanded = false;
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
	public boolean is_lemma() {
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
	 * Gets the number of PO of this function
	 * 
	 * @return the number of PO of this function
	 */
	public int getPo() {
		return po;
	}

	/**
	 * Sets the number of PO of this function
	 * 
	 * @param po the number of PO of this function
	 */
	public void setPo(int po) {
		this.po = po;
	}
	
	/**
	 * Gets the number of unproved PO of this function
	 * 
	 * @return the number of unproved PO of this function
	 */
	public int getPo_unproved() {
		return (po-po_proved);
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
	
	/*
	 * @return the nb of po unchecked
	 *
	public int getPo_unchecked() {
		return (po-po_checked);
	}/**/
	
	/*
	 * @return the po_checked
	 *
	public int getPo_checked() {
		return po_checked;
	}/**/

	/*
	 * @param po_proved the po_proved to set
	 *
	public void setPo_checked(int po_checked) {
		this.po_checked = po_checked;
	}/**/
	
	/**
	 * (Number of PO)++
	 */
	public void increase_po() {
		this.po ++;
	}
	
	/**
	 * (Number of PO)--
	 */
	public void decrease_po() {
		if(po > 0) this.po --;
	}
	
	/**
	 * (Number of proved PO)++
	 */
	public void increase_po_proved() {
		if(po_proved < po) this.po_proved ++;
	}
	
	/**
	 * (Number of proved PO)--
	 */
	public void decrease_po_proved() {
		if(po_proved > 0) this.po_proved --;
	}
	
	/*
	 * 
	 *
	public void increase_po_checked() {
		if(po_checked < po) {
			this.po_checked ++;
		}
	}/**/
	
	/*
	 * 
	 *
	public void decrease_po_checked() {
		if(po_checked > 0) {
			this.po_checked --;
		}
	}/**/
	
	/*
	 * 
	 *
	public void gCheck() {
		
		if(isChecked()) { //sécurité
			return;
		}
		
		this.po_checked = po;
		
		int w = first_po;
		for(int v=0; v<po; v++) {
			PO po = (PO)FileInfos.goals.get(w-1);
			po.check();
			int e = po.getNbSubGoals();
			for(int u=1; u<=e; u++) {
				po.getSubGoal(u).check();
			}
			w++;
		}
	}/**/
	
	/*
	 * 
	 *
	public void gUncheck() {
		
		this.po_checked = 0;
		
		int w = first_po;
		for(int v=0; v<po; v++) {
			PO po = (PO)FileInfos.goals.get(w-1);
			po.uncheck();
			int e = po.getNbSubGoals();
			for(int u=1; u<=e; u++) {
				po.getSubGoal(u).uncheck();
			}
			w++;
		}
	}/**/
	
	/*
	 * 
	 *
	public boolean isChecked() {
		return (po_checked == po);
	}/**/
	
	/**
	 * Is this function proved?
	 * 
	 * @return true if all goals of function are proved
	 */
	public boolean isProved() {
		return (po_proved == po);
	}

	/**
	 * Gets the number of the first PO
	 * 
	 * @return the number of the first PO
	 */
	public int getFirst_po() {
		return first_po;
	}

	/**
	 * Sets the number of the first PO
	 * 
	 * @param first_po the number of the first PO
	 */
	public void setFirst_po(int first_po) {
		this.first_po = first_po;
	}
	
	/**
	 * Puts the number of PO proved to 0.
	 */
	public void init() {
		po_proved = 0;
		//po_checked = 0;
	}
}
