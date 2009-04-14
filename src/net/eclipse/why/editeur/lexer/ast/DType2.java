package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class DType2 extends Declaration implements Visitable {

	Loc lo; External e; TypeVar t; Ident x;
	
	public DType2(Loc loc, External ext, TypeVar tv, Ident i) {
		lo = loc;
		e = ext;
		t = tv;
		x = i;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.setTypeMode();
		visitor.visit(e);
		WhyElement.add("type ");
		WhyElement.setTextMode();
		visitor.visit(t);
		visitor.visit(x);
		WhyElement.whiteLine();
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(e);
		WhyCode.add("type ");
		visitor.svisit(t);
		visitor.svisit(x);
		WhyCode.whiteLine();
	}
}
