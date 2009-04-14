package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class STypeRef extends SType implements Visitable {

	Loc lo; PType p;
	
	public STypeRef(Loc loc, PType pt) {
		lo = loc;
		p = pt;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(p);
		WhyElement.add(" ref");
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(p);
		WhyCode.add(" ref");
	}

}
