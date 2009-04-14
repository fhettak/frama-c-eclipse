package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class DExceptOf extends Declaration implements Visitable {

	Loc lo; Ident w; PType p;
	
	public DExceptOf(Loc loc, Ident i, PType pt) {
		lo = loc;
		w = i;
		p = pt;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.setExceptionMode();
		WhyElement.add("exception ");
		WhyElement.setTextMode();
		visitor.visit(w);
		WhyElement.setExceptionMode();
		WhyElement.add(" of");
		WhyElement.setTextMode();
		WhyElement.newLine();
		visitor.visit(p);
		WhyElement.whiteLine();
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("exception ");
		visitor.svisit(w);
		WhyCode.add(" of ");
		visitor.svisit(p);
		WhyCode.whiteLine();
	}
	
}
