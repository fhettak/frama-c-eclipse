package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class PTypeUnit extends PType implements Visitable {

	public PTypeUnit() {
	}

	public void accept(ReflectiveVisitor visitor) {
		//visitor.visit(null);
		WhyElement.add("unit");
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("unit");
	}

}
