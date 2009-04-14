package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class Result implements Visitable {

	Loc lo; Ident h; TypeV tv;
	
	public Result(Loc loc, Ident id, TypeV t) {
		lo = loc;
		h = id;
		tv = t;
	}

	public Result(Loc loc, TypeV t) {
		lo = loc;
		h = null;
		tv = t;
	}

	public void accept(ReflectiveVisitor visitor) {
		if(h!=null) {
			WhyElement.add("returns ");
			visitor.visit(h);
			WhyElement.add(" : ");
		}
		visitor.visit(tv);
	}

	public void saccept(ReflectiveVisitor visitor) {
		if(h!=null) {
			WhyCode.add("returns ");
			visitor.svisit(h);
			WhyCode.add(":");
		}
		visitor.svisit(tv);
	}

}
