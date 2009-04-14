package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprAmpAmp extends Expr implements Visitable {

	Loc lo; Expr ex; Expr fx;
	
	public ExprAmpAmp(Loc loc, Expr e, Expr f) {
		lo = loc;
		ex = e;
		fx = f;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(ex);
		WhyElement.add(" && ");
		visitor.visit(fx);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(ex);
		WhyCode.add(" && ");
		visitor.svisit(fx);
	}

}
