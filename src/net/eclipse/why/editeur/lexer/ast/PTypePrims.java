package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class PTypePrims extends PType implements Visitable {

	Loc lo; PType pt; List1PType list; Ident h;
	
	public PTypePrims(Loc loc, PType p, List1PType l1, Ident id) {
		lo = loc;
		pt = p;
		list = l1;
		h = id;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("(");
		visitor.visit(pt);
		WhyElement.add(",");
		visitor.visit(list);
		WhyElement.add(") ");
		visitor.visit(h);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("(");
		visitor.svisit(pt);
		WhyCode.add(",");
		visitor.svisit(list);
		WhyCode.add(") ");
		visitor.svisit(h);
	}

}
