package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprIdExpr extends Expr implements Visitable {

	Loc lo; Ident i; Expr expr;
	
	public ExprIdExpr(Loc loc, Ident id, Expr e) {
		lo = loc;
		i = id;
		expr = e;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(i);
		WhyElement.add(" := ");
		visitor.visit(expr);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(i);
		WhyCode.add(" := ");
		visitor.svisit(expr);
	}

}
