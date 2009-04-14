package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;


/**
 * a => b object
 * 
 * @author A. Oudot
 */
public class LExprArrow extends LExpr implements Visitable {

	Loc lo;
	LExpr l; // a in [a => b]
	LExpr m; // b in [a => b]
	
	
	public LExprArrow(Loc loc, LExpr l1, LExpr l2) {
		lo = loc;
		l = l1;
		m = l2;
	}
	
	/**
	 * Remove the second part of this arrow object. This function is
	 * used after getPart2() which saves in the Pointer static class
	 * what this function cleans.
	 */
	public void crack() {
		m = null;
	}
	
	/**
	 * Return the second part of this Arrow to save
	 * it in a Pointer class field
	 * 
	 * @return the second part of this Arrow
	 */
	public LExpr getPart2() {
		return m;
	}

	/**
	 * Build the second part of this Arrow expression
	 * putting parenthesis around
	 * 
	 * @param lbuilt the expression to put into
	 * the second part of this Arrow object
	 */
	public void build(Object lbuilt) {
		LExprPar epar = new LExprPar(null, (LExpr)lbuilt);
		m = epar;
	}

	
	public void accept(ReflectiveVisitor visitor) {
		
		//first arrow visited now?
		boolean c = Pointer.inArrow();
		visitor.visit(l);
		
		if(c) { //if this is the first arrow visited
			
			//we're now out of the first part and not in an
			//Arrow included in an Arrow etc. what is very
			//important to separate the first and the second
			//parts of the visited goal
			Pointer.outArrow();
			
			WhyElement.unindent();
			if(WhyElement.is_goal) {
				Pointer.setArrow(this);
			} else {
				WhyElement.add(" =>");
			}
			WhyElement.newLine();
			
			while(m instanceof LExprPar || m instanceof LExprIdStr) {
				if(m instanceof LExprPar) m = ((LExprPar)m).lexpr;
				else if(m instanceof LExprIdStr) m = ((LExprIdStr)m).lexpr;
			}
			
			if(!(m instanceof LExprForall)) {
				WhyElement.setHMode();
				WhyElement.indent();
				if(!(m instanceof LExprArrow)) {
					Pointer.inArrow();
				}
			}
		} else {
			WhyElement.add(" =>");
			WhyElement.newLine();
		}
		
		visitor.visit(m);
		
	}
	
	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(l);
		WhyCode.add(" ->");
		WhyCode.newLine();
		visitor.svisit(m);
	}

}
