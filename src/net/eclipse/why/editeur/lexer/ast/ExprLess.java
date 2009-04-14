package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprLess extends Expr implements Visitable {

	Loc lo; Expr expr; Expr fxpr;
	
	public ExprLess(Loc loc, Expr e, Expr f) {
		lo = loc;
		expr = e;
		fxpr = f;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(expr);
		WhyElement.add(" - ");
		visitor.visit(fxpr);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(expr);
		WhyCode.add("-");
		visitor.svisit(fxpr);
	}

}
