package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprLetRec extends Expr implements Visitable {

	Loc lo; Recfun rf;
	
	public ExprLetRec(Loc loc, Recfun r) {
		lo = loc;
		rf = r;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("let rec ");
		visitor.visit(rf);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("let rec ");
		visitor.svisit(rf);
	}

}
