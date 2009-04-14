package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class OptCast implements Visitable {

	Loc lo; TypeV tv;
	
	public OptCast(Loc loc, TypeV t) {
		lo = loc;
		tv = t;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add(":");
		visitor.visit(tv);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add(":");
		visitor.svisit(tv);
	}

}
