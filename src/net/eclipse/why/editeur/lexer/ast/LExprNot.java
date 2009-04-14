package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class LExprNot extends LExpr implements Visitable {

	Loc lo; LExpr lexpr;
	
	public LExprNot(Loc loc, LExpr l) {
		lo = loc;
		lexpr = l;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("not ");
		if(lexpr instanceof LExprPar)
			((LExprPar)lexpr).p = true;
		visitor.visit(lexpr);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("not ");
		visitor.svisit(lexpr);
	}

}
