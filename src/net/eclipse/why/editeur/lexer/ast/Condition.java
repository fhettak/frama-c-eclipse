package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class Condition implements Visitable {

	Loc lo; Ident h; Assert a;
	
	public Condition(Loc loc, Ident id, Assert ass) {
		lo = loc;
		h = id;
		a = ass;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(h);
		WhyElement.add(" => ");
		visitor.visit(a);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(h);
		WhyCode.add(" => ");
		visitor.svisit(a);
	}
	
}
