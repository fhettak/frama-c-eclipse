package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class PTypeBool extends PType implements Visitable {

	public PTypeBool() {
	}

	public void accept(ReflectiveVisitor visitor) {
		//visitor.visit(null);
		WhyElement.add("bool");
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("bool");
	}

}
