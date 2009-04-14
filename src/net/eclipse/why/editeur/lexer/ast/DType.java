package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class DType extends Declaration implements Visitable {

	Loc lo; External e; Ident x;
	
	public DType(Loc loc, External ext, Ident id) {
		lo = loc;
		e = ext;
		x = id;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.setTypeMode();
		visitor.visit(e);
		WhyElement.add("type ");
		WhyElement.setTextMode();
		visitor.visit(x);
		WhyElement.whiteLine();
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(e);
		WhyCode.add("type ");
		visitor.svisit(x);
		WhyCode.whiteLine();
	}

}
