package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprLetEq extends Expr implements Visitable {

	Loc lo; Ident i; Object x; List0BrAssert lba; Expr expr; Expr fxpr;
	
	public ExprLetEq(Loc loc, Ident id, Object b, List0BrAssert l0, Expr e, Expr f) {
		lo = loc;
		i = id;
		x = b;
		lba = l0;
		expr = e;
		fxpr = f;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("let ");
		visitor.visit(i);
		visitor.visit(x);
		WhyElement.add(" = ");
		visitor.visit(lba);
		visitor.visit(expr);
		WhyElement.add(" in ");
		visitor.visit(fxpr);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("let ");
		visitor.svisit(i);
		visitor.svisit(x);
		WhyCode.add(" = ");
		visitor.svisit(lba);
		visitor.svisit(expr);
		WhyCode.add(" in ");
		visitor.svisit(fxpr);
	}

}
