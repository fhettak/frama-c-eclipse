package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprIfThenElse extends Expr implements Visitable {

	Loc lo; Expr expr; Expr fxpr; Object o;
	
	public ExprIfThenElse(Loc loc, Expr e, Expr f, Object object) {
		lo = loc;
		expr = e;
		fxpr = f;
		o = object;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("if ");
		visitor.visit(expr);
		WhyElement.add(" then ");
		visitor.visit(fxpr);
		if(o!=null) {
			WhyElement.newLine();
			WhyElement.add(" else ");
			visitor.visit(o);
		}
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("if ");
		visitor.svisit(expr);
		WhyCode.add(" then ");
		visitor.svisit(fxpr);
		if(o!=null) {
			WhyCode.newLine();
			WhyCode.add("else ");
			visitor.svisit(o);
		}
	}

}
