package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprAbsurd extends Expr implements Visitable {

	Loc lo; OptCast opt;
	
	public ExprAbsurd(Loc loc, OptCast o) {
		lo = loc;
		opt = o;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("absurd ");
		visitor.visit(opt);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("absurd ");
		visitor.svisit(opt);
	}

}
