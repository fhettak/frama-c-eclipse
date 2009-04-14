package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprBang extends Expr implements Visitable {

	Loc lo; Ident i;
	
	public ExprBang(Loc loc, Ident id) {
		lo = loc;
		i = id;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("!");
		visitor.visit(i);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("!");
		visitor.svisit(i);
	}

}
