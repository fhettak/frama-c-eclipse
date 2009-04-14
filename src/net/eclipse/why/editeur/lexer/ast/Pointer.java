package net.eclipse.why.editeur.lexer.ast;


/**
 * Object which points on the last Arrow expression
 * visited by visitor in the abstract syntax tree,
 * result of the parsing of a .why goal file.
 * 
 * @author A. Oudot
 */
public class Pointer {
	
	private static Object tree; //Abstract syntax tree
	private static LExprArrow lastExpr; //last LExprArrow object visited by visitor
	private static LExpr part2; //second part of goal
	private static boolean inArrow; //is the visitor into an Arrow expression object
	

	/**
	 * Tree setter
	 * 
	 * @param o the abstract syntax tree
	 */
	public static void setTree(Object o) {
		tree = o;
	}
	
	/**
	 * Tree getter
	 * 
	 * @return Object the sub abstract syntax tree
	 */
	public static Object getTree() {
		return tree;
	}
	
	/**
	 * Return the b of the [a => b] Arrow expression
	 * 
	 * @return Object
	 */
	public static Object get() {
		return part2;
	}
	
	/**
	 * The LExprArrow object setter
	 * 
	 * @param lexpr
	 */
	public static void setArrow(LExprArrow lexpr) {
		lastExpr = lexpr;
	}
	
	/**
	 * Break the tree which has been visited by visitor during the parsing
	 * of a .why file. This function saves the second part of the last
	 * Arrow object and removes it from the abstract syntax tree
	 */
	public static void breakUp() {
		part2 = lastExpr.getPart2(); //saves
		lastExpr.crack(); //and removes
	}
	
	/**
	 * Rebuild the visited abstract syntax tree object adding
	 * an expression between parenthesis in the
	 * second part of the last Arrow expression visited
	 * 
	 * @param partII the expression to add
	 */
	public static void build(Object partII) {
		lastExpr.build(partII);
	}
	
	/**
	 * Reset fields of this class
	 */
	public static void reset() {
		part2 = null;
		lastExpr = null;
		tree = null;
		inArrow = false;
	}
	
	
	/**
	 * Visitor is visiting in an Arrow object
	 * 
	 * @return true if it's the first time (visit the higher level
	 * Arrow object), false otherwise
	 */
	public static boolean inArrow() {
		if(!inArrow) {
			inArrow = true;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Visitor is coming out the higher level Arrow object
	 */
	public static void outArrow() {
		inArrow = false;
	}
	
	/**
	 * Is the visitor visiting an Arrow object
	 * 
	 * @return boolean
	 */
	public static boolean isInArrow() {
		return inArrow;
	}
}
