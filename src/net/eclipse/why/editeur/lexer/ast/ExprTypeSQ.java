package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprTypeSQ extends Expr implements Visitable {

	Loc lo; TypeC tc;
	
	public ExprTypeSQ(Loc loc, TypeC t) {
		lo = loc;
		tc = t;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("[");
		visitor.visit(tc);
		WhyElement.add("]");
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("[");
		visitor.svisit(tc);
		WhyCode.add("]");
	}

}
