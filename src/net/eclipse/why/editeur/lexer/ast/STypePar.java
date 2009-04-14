package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class STypePar extends SType implements Visitable {

	Loc lo; TypeV tv;
	
	public STypePar(Loc loc, TypeV t) {
		lo = loc;
		tv = t;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("(");
		visitor.visit(tv);
		WhyElement.add(")");
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("(");
		visitor.svisit(tv);
		WhyCode.add(")");
	}

}
