package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class PTypeReal extends PType implements Visitable {

	public PTypeReal() {
	}

	public void accept(ReflectiveVisitor visitor) {
		//visitor.visit(null);
		WhyElement.add("real");
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("real");
	}

}
