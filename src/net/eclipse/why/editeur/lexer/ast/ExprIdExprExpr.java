package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprIdExprExpr extends Expr implements Visitable {

	Loc lo; Ident i; Expr expr; Expr fxpr;
	
	public ExprIdExprExpr(Loc loc, Ident id, Expr e, Expr f) {
		lo = loc;
		i = id;
		expr = e;
		fxpr = f;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(i);
		WhyElement.add(" [");
		visitor.visit(expr);
		WhyElement.add("] := ");
		visitor.visit(fxpr);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(i);
		WhyCode.add(" [");
		visitor.svisit(expr);
		WhyCode.add("] := ");
		visitor.svisit(fxpr);
	}

}
