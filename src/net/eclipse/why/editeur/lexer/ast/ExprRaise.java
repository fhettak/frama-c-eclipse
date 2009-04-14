package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprRaise extends Expr implements Visitable {

	Loc lo; Ident i; OptCast opt;
	
	public ExprRaise(Loc loc, Ident id, OptCast o) {
		lo = loc;
		i = id;
		opt = o;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("raise ");
		visitor.visit(i);
		visitor.visit(opt);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("raise ");
		visitor.svisit(i);
		visitor.svisit(opt);
	}

}
