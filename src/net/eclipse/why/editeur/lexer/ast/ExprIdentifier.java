package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprIdentifier extends Expr implements Visitable {

	Loc lo; String id; Expr expr;
	
	public ExprIdentifier(Loc loc, String ident, Expr e) {
		lo = loc;
		id = ident;
		expr = e;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(id);
		WhyElement.add(":");
		visitor.visit(expr);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(id);
		WhyCode.add(":");
		visitor.svisit(expr);
	}

}
