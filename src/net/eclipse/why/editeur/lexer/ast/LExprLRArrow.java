package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class LExprLRArrow extends LExpr implements Visitable {

	Loc lo; LExpr l; LExpr m;
	
	public LExprLRArrow(Loc loc, LExpr l1, LExpr l2) {
		lo = loc;
		l = l1;
		m = l2;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(l);
		WhyElement.add(" <=> ");
		visitor.visit(m);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(l);
		WhyCode.add(" <-> ");
		visitor.svisit(m);
	}

}
