package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprNot extends Expr implements Visitable {

	Loc lo; Expr expr;
	
	public ExprNot(Loc loc, Expr e) {
		lo = loc;
		expr = e;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("not ");
		visitor.visit(expr);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("not ");
		visitor.svisit(expr);
	}

}
