package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class PTypeInt extends PType implements Visitable {

	public PTypeInt() {
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("int");
		//visitor.visit(null);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("int");
	}

}
