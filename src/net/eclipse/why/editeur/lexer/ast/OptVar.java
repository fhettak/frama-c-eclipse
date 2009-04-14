package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class OptVar implements Visitable {

	Loc lo; Variant var;
	
	public OptVar(Loc loc, Variant v) {
		lo = loc;
		var = v;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("{variant ");
		visitor.visit(var);
		WhyElement.add("}");
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("variant ");
		visitor.svisit(var);
		WhyCode.add("}");
	}

}
