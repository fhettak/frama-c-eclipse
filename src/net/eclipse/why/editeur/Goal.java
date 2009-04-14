package net.eclipse.why.editeur;

import java.util.Vector;

import org.eclipse.swt.custom.StyleRange;

/**
 * This class contains data used to print PO selected
 * in Prover View, in PO Viewer
 * 
 * @author A. Oudot
 */
public class Goal {

	private static String goal = ""; //the text of the goal(PO)
	private static String result = ""; //the end of goal text
	private static Vector<StyleRange> styleRanges = new Vector<StyleRange>(); //style ranges
	
	/**
	 * Cleans datas
	 */
	public static void clean() {
		goal = "";
		result = "";
		styleRanges = new Vector<StyleRange>();
	}
	
	/**
	 * Make goal strings
	 */
	public static void make() {
		int index;
		index = goal.lastIndexOf("h: ");
		if(index!=-1) {
			//split goal and result strings
			result = goal.substring(index+3);
			goal = goal.substring(0, index).trim();
			
			//unindent the result string
			String[] rt = result.split("\n");
			int i = WhyElement.getIndent();
			result = rt[0] + "\n";
			for(int q=1; q<rt.length; q++) {
				result += rt[q].substring(i) + "\n"; 
			}
			result += "\n";
		}
	}


	/**
	 * Gets the goal text
	 * 
	 * @return the goal text
	 */
	public static String getGoal() {
		return goal;
	}


	/**
	 * Sets the goal text
	 * 
	 * @param goal the goal text
	 */
	public static void setGoal(String goal) {
		Goal.goal = goal;
	}


	/**
	 * Gets the result text
	 * 
	 * @return the result text
	 */
	public static String getResult() {
		return result;
	}


	/**
	 * Gets style ranges
	 * 
	 * @return the style ranges
	 */
	public static Vector<StyleRange> getStyleRanges() {
		return styleRanges;
	}


	/**
	 * Sets style ranges
	 * 
	 * @param styleRanges the style ranges
	 */
	public static void setStyleRanges(Vector<StyleRange> styleRanges) {
		Goal.styleRanges = styleRanges;
	}
}