package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprBarBar extends Expr implements Visitable {

	Loc lo; Expr exp; Expr fxp;
	
	public ExprBarBar(Loc loc, Expr e, Expr f) {
		lo = loc;
		exp = e;
		fxp = f;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(exp);
		WhyElement.add(" || ");
		visitor.visit(fxp);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(exp);
		WhyCode.add(" || ");
		visitor.svisit(fxp);
	}

}
