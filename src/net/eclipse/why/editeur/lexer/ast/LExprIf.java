package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class LExprIf extends LExpr implements Visitable {

	Loc lo; LExpr l; LExpr m; LExpr n;
	
	public LExprIf(Loc loc, LExpr l1, LExpr l2, LExpr l3) {
		lo = loc;
		l = l1;
		m = l2;
		n = l3;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("if ");
		visitor.visit(l);
		WhyElement.add(" then ");
		visitor.visit(m);
		WhyElement.add(" else ");
		visitor.visit(n);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("if ");
		visitor.svisit(l);
		WhyCode.add(" then ");
		visitor.svisit(m);
		WhyCode.add(" else ");
		visitor.svisit(n);
	}

}
