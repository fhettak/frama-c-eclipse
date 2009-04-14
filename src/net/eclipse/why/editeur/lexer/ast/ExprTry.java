package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprTry extends Expr implements Visitable {

	Loc lo; Expr expr; ListHandler lh;
	
	public ExprTry(Loc loc, Expr e, ListHandler l) {
		lo = loc;
		expr = e;
		lh = l;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("try ");
		visitor.visit(expr);
		WhyElement.add(" with ");
		visitor.visit(lh);
		WhyElement.add(" end");
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("try ");
		visitor.svisit(expr);
		WhyCode.add(" with ");
		visitor.svisit(lh);
		WhyCode.add(" end");
	}

}
