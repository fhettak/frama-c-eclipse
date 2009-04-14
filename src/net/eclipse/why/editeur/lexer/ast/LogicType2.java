package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class LogicType2 extends LogicType implements Visitable {

	Loc lo;
	
	public LogicType2(Loc loc) {
		lo = loc;
	}

	public void accept(ReflectiveVisitor visitor) {
		//visitor.visit(null);
		WhyElement.add("prop");
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("prop");
	}

}
