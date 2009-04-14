package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class DAxiom extends Declaration implements Visitable {

	Loc lo; Ident k; LExpr l;
	
	public DAxiom(Loc loc, Ident i, LExpr le) {
		lo = loc;
		k = i;
		l = le;
	}

	public void accept(ReflectiveVisitor visitor) {
		Pointer.outArrow();
		WhyElement.setAxiomMode();
		WhyElement.add("axiom ");
		WhyElement.setTextMode();
		visitor.visit(k);
		WhyElement.newLine();
		WhyElement.setHMode();
		Pointer.inArrow();
		visitor.visit(l);
		WhyElement.whiteLine();
		Pointer.outArrow();
	}
	
	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("axiom ");
		visitor.svisit(k);
		visitor.svisit(l);
		WhyCode.whiteLine();
	}

}
