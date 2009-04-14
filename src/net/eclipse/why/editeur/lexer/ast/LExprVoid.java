package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class LExprVoid extends LExpr implements Visitable {

	public LExprVoid() {
	}

	public void accept(ReflectiveVisitor visitor) {
		//visitor.visit(null);
		WhyElement.add("void");
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("void");
	}

}
