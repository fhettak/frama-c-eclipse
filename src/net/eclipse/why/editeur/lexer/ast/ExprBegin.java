package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprBegin extends Expr implements Visitable {

	Loc lo; Expr expr;
	
	public ExprBegin(Loc loc, Expr e) {
		lo = loc;
		expr = e;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("begin ");
		visitor.visit(expr);
		WhyElement.add(" end");
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("begin ");
		visitor.svisit(expr);
		WhyCode.add(" end");
	}

}
