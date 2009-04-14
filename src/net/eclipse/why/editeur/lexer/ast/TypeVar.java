package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class TypeVar implements Visitable {

	Loc lo;
	Ident j;
	
	public TypeVar(Loc loc, Ident id) {
		lo = loc;
		j = id;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("'");
		visitor.visit(j);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("'");
		visitor.svisit(j);
	}

}
