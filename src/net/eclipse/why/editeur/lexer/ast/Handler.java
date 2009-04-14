package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class Handler implements Visitable {

	Loc lo; Ident x; Ident y; Expr expr;
	
	public Handler(Loc loc, Ident id, Ident jd, Expr e) {
		lo = loc;
		x = id;
		y = jd;
		expr = e;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(x);
		if(y!=null) {
			visitor.visit(y);
		}
		WhyElement.add(" -> ");
		visitor.visit(expr);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(x);
		if(y!=null) {
			visitor.svisit(y);
		}
		WhyCode.add(" -> ");
		visitor.svisit(expr);
	}

}
