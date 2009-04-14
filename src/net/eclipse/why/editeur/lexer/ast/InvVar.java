package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class InvVar implements Visitable {

	Loc lo; OptInv opt; Variant var;
	
	public InvVar(Loc loc, OptInv o, Variant v) {
		lo = loc;
		opt = o;
		var = v;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("{");
		visitor.visit(opt);
		if(var!=null) {
			WhyElement.add(" variant ");
			visitor.visit(var);
		}
		WhyElement.add("}");
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("{");
		visitor.svisit(opt);
		if(var!=null) {
			WhyCode.add(" variant ");
			visitor.svisit(var);
		}
		WhyCode.add("}");
	}

}
