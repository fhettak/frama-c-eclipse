package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class DExcept extends Declaration implements Visitable {

	Loc lo; Ident w;
	
	public DExcept(Loc loc, Ident i) {
		lo = loc;
		w = i;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.setExceptionMode();
		WhyElement.add("exception ");
		WhyElement.setTextMode();
		visitor.visit(w);
		WhyElement.whiteLine();
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("exception ");
		visitor.svisit(w);
		WhyCode.whiteLine();
	}
	
}
