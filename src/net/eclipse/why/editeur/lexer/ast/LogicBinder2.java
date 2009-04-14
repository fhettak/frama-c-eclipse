package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class LogicBinder2 extends LogicBinder implements Visitable {

	Loc lo; Ident j; PType pt;
	
	public LogicBinder2(Loc loc, Ident id, PType p) {
		lo = loc;
		j = id;
		pt = p;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(pt);
		WhyElement.add(" ");
		visitor.visit(j);
		WhyElement.add(" array");
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(pt);
		WhyCode.add(":");
		visitor.svisit(j);
		WhyCode.add(" array");
	}

}
