package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class BrAssert implements Visitable {

	Loc lo; Assert ass;
	
	public BrAssert(Loc loc, Assert a) {
		lo = loc;
		ass = a;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("{");
		visitor.visit(ass);
		WhyElement.add("}");
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("{");
		visitor.svisit(ass);
		WhyCode.add("}");
	}
	
}
