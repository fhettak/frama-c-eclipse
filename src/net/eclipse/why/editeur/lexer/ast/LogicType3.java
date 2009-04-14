package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class LogicType3 extends LogicType implements Visitable {

	Loc lo; Object o; PType pt;
	
	public LogicType3(Loc loc, Object l0, PType p) {
		lo = loc;
		o = l0;
		pt = p;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(o);
		WhyElement.add(" -> ");
		visitor.visit(pt);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(o);
		WhyCode.add(" -> ");
		visitor.svisit(pt);
	}

}
