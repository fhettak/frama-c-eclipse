package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class TypeV implements Visitable {

	Loc lo; Ident x; SType s; Object o;
	
	public TypeV(Loc loc, Ident id, SType st, Object object) {
		lo = loc;
		x = id;
		s = st;
		o = object;
	}

	public void accept(ReflectiveVisitor visitor) {
		if(x!=null) {
			visitor.visit(x);
			WhyElement.add(":");
		}
		visitor.visit(s);
		if(o!=null) {
			WhyElement.add(" -> ");
			visitor.visit(o);
		}
	}

	public void saccept(ReflectiveVisitor visitor) {
		if(x!=null) {
			visitor.svisit(x);
			WhyCode.add(":");
		}
		visitor.svisit(s);
		if(o!=null) {
			WhyCode.add(" -> ");
			visitor.svisit(o);
		}
	}

}
