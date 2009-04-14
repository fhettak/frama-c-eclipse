package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprWhile extends Expr implements Visitable {

	Loc lo; Expr expr; InvVar i; Expr fxpr;
	
	public ExprWhile(Loc loc, Expr e, InvVar inv, Expr f) {
		lo = loc;
		expr = e;
		i = inv;
		fxpr = f;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("while ");
		visitor.visit(expr);
		WhyElement.add(" do ");
		visitor.visit(i);
		visitor.visit(fxpr);
		WhyElement.newLine();
		WhyElement.add("done");
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("while ");
		visitor.svisit(expr);
		WhyCode.add(" do ");
		visitor.svisit(i);
		visitor.svisit(fxpr);
		WhyCode.add(" done");
	}

}
