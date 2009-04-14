package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class External implements Visitable {

	public External() {
		
	}

	public void accept(ReflectiveVisitor visitor) {
		//visitor.visit(null);
		WhyElement.add("external ");
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("external ");
	}

}
