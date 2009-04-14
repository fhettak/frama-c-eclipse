package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class DLetEq2 extends Declaration implements Visitable {

	Loc lo; Ident w; Object x; List0BrAssert lba; Expr expr;
	
	public DLetEq2(Loc loc, Ident i, Object b, List0BrAssert l0, Expr e) {
		lo = loc;
		w = i;
		x = b;
		lba = l0;
		expr = e;
	}

	public void accept(ReflectiveVisitor visitor) {
		Pointer.outArrow();
		WhyElement.setLetMode();
		WhyElement.add("let ");
		WhyElement.setTextMode();
		visitor.visit(w);
		WhyElement.newLine();
		visitor.visit(x);
		WhyElement.setLetMode();
		WhyElement.add(" =");
		WhyElement.newLine();
		WhyElement.setTextMode();
		Pointer.inArrow();
		visitor.visit(lba);
		visitor.visit(expr);
		Pointer.outArrow();
		WhyElement.whiteLine();
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("let ");
		visitor.svisit(w);
		visitor.svisit(x);
		WhyCode.add("=");
		visitor.svisit(lba);
		visitor.svisit(expr);
		WhyCode.newLine();
	}
	
}
