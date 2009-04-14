package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprVoid extends Expr implements Visitable {

	public ExprVoid() {
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("void");
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("void");
	}

}
