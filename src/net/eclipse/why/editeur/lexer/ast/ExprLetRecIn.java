package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprLetRecIn extends Expr implements Visitable {

	Loc lo; Recfun rf; Expr expr;
	
	public ExprLetRecIn(Loc loc, Recfun r, Expr e) {
		lo = loc;
		rf = r;
		expr = e;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("let rec ");
		visitor.visit(rf);
		WhyElement.add(" in ");
		visitor.visit(expr);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("let rec ");
		visitor.svisit(rf);
		WhyCode.add(" in ");
		visitor.svisit(expr);
	}

}
