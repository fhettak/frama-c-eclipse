package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class DType3 extends Declaration implements Visitable {

	Loc lo; External e; List1TypeVar ltv; Ident x;
	
	public DType3(Loc loc, External ext, List1TypeVar l1, Ident id) {
		lo = loc;
		e = ext;
		ltv = l1;
		x = id;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.setTypeMode();
		visitor.visit(e);
		WhyElement.add("type(");
		WhyElement.setTextMode();
		visitor.visit(ltv);
		WhyElement.setTypeMode();
		WhyElement.add(") ");
		WhyElement.setTextMode();
		visitor.visit(x);
		WhyElement.whiteLine();
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(e);
		WhyCode.add("type(");
		visitor.svisit(ltv);
		WhyCode.add(") ");
		visitor.svisit(x);
		WhyCode.whiteLine();
	}

}
