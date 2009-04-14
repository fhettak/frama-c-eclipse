package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class Triggers implements Visitable {

	Loc lo; ListTrigger list;
	
	public Triggers(Loc loc, ListTrigger lt) {
		lo = loc;
		list = lt;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.setTriggerMode();
		WhyElement.add(" [");
		visitor.visit(list);
		WhyElement.add("]");
		WhyElement.setExprMode();
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("[");
		visitor.svisit(list);
		WhyCode.add("]");
	}

}
