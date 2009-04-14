package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class PTypePrim extends PType implements Visitable {

	Loc lo; PType p; Ident x;
	
	public PTypePrim(Loc loc, PType pt, Ident id) {
		lo = loc;
		p = pt;
		x = id;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(p);
		WhyElement.add(" ");
		visitor.visit(x);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(p);
		WhyCode.add(" ");
		visitor.svisit(x);
	}

}
