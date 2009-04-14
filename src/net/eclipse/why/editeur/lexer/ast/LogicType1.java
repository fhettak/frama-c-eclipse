package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class LogicType1 extends LogicType implements Visitable {

	Loc lo; Object o;
	
	public LogicType1(Loc loc, Object l0) {
		lo = loc;
		o = l0;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(o);
		WhyElement.add(" -> prop");
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(o);
		WhyCode.add(" -> prop");
	}

}
