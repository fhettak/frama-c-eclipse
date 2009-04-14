package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;


public class LExprIdStr extends LExpr implements Visitable {

	Loc lo; Object o; public LExpr lexpr;
	
	public LExprIdStr(Loc loc, Object id, LExpr e) {
		lo = loc;
		o = id;
		lexpr = e;
	}

	public void accept(ReflectiveVisitor visitor) {
		//visitor.visit(o);
		//Goal.add(":");
		if(lexpr instanceof LExprPar)
			lexpr = ((LExprPar)lexpr).lexpr;
		visitor.visit(lexpr);
	}

	public void saccept(ReflectiveVisitor visitor) {
		if(o instanceof String) {
			WhyCode.add("\"" + o.toString() + "\"");
		} else {
			visitor.svisit(o);
		}
		//visitor.svisit(o);
		WhyCode.add(": ");
		visitor.svisit(lexpr);
	}

}
