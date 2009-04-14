package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class DLetEq extends Declaration implements Visitable {

	Loc lo; Ident w; Expr expr;
	
	public DLetEq(Loc loc, Ident i, Expr e) {
		lo = loc;
		w = i;
		expr = e;
	}

	public void accept(ReflectiveVisitor visitor) {
		Pointer.outArrow();
		WhyElement.setLetMode();
		WhyElement.add("let ");
		WhyElement.setTextMode();
		visitor.visit(w);
		WhyElement.setLetMode();
		WhyElement.add(" =");
		WhyElement.newLine();
		WhyElement.setTextMode();
		Pointer.inArrow();
		visitor.visit(expr);
		Pointer.outArrow();
		WhyElement.whiteLine();
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("let ");
		visitor.svisit(w);
		WhyCode.add("=");
		visitor.svisit(expr);
		WhyCode.newLine();
	}
	
}
