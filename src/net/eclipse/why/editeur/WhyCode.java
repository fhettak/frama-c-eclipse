package net.eclipse.why.editeur;


/**
 * Record the WHY code created from an abstract syntax tree
 * 
 * @author A. Oudot
 */
public class WhyCode {

	
	private static String code; //the WHY code
	
	
	/**
	 * Reset the WHY recorded text
	 */
	public static void reset() {
		code = "";
	}
	
	/**
	 * Add a text at the end of the WHY code
	 * 
	 * @param s the WHY code to add
	 */
	public static void add(String s) {
		code += s;
	}
	
	/**
	 * Add a new line at the end of the WHY code
	 */
	public static void newLine() {
		code = code.trim() + "\n";
	}
	
	/**
	 * Add a white line at the end of the WHY code
	 */
	public static void whiteLine() {
		code = code.trim() + "\n\n";
	}
	
	/**
	 * WHY code getter
	 * 
	 * @return String
	 */
	public static String getCode() {
		return code;
	}
}
