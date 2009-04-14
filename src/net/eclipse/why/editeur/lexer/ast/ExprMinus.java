package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprMinus extends Expr implements Visitable {

	Loc lo; Expr expr;
	
	public ExprMinus(Loc loc, Expr e) {
		lo = loc;
		expr = e;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("-");
		visitor.visit(expr);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("-");
		visitor.svisit(expr);
	}

}
