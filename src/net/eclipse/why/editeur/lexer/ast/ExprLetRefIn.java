package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprLetRefIn extends Expr implements Visitable {

	Loc lo; Ident i; Expr expr; Expr fxpr;
	
	public ExprLetRefIn(Loc loc, Ident id, Expr e, Expr f) {
		lo = loc;
		i = id;
		expr = e;
		fxpr = f;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("let ");
		visitor.visit(i);
		WhyElement.add(" = ref ");
		visitor.visit(expr);
		WhyElement.add(" in ");
		visitor.visit(fxpr);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("let ");
		visitor.svisit(i);
		WhyCode.add(" = ref ");
		visitor.svisit(expr);
		WhyCode.add(" in ");
		visitor.svisit(fxpr);
	}

}
