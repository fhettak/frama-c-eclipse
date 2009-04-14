package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class OptInv implements Visitable {

	Loc lo; Assert ass;
	
	public OptInv(Loc loc, Assert a) {
		lo = loc;
		ass = a;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("invariant ");
		visitor.visit(ass);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("invariant ");
		visitor.svisit(ass);
	}

}
