package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class DPred extends Declaration implements Visitable {

	Loc lo; Ident k; Object o; LExpr lexpr;
	
	public DPred(Loc loc, Ident i, Object l0, LExpr le) {
		lo = loc;
		k = i;
		o = l0;
		lexpr = le;
	}

	public void accept(ReflectiveVisitor visitor) {
		Pointer.outArrow();
		WhyElement.setPredicateMode();
		WhyElement.add("predicate ");
		WhyElement.setTextMode();
		visitor.visit(k);
		WhyElement.setPredicateMode();
		WhyElement.add(" {");
		visitor.visit(o);
		WhyElement.newLine();
		WhyElement.setPredicateMode();
		WhyElement.add("} :");
		WhyElement.newLine();
		WhyElement.setExprMode();
		Pointer.inArrow();
		visitor.visit(lexpr);
		WhyElement.whiteLine();
		Pointer.outArrow();
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("predicate ");
		visitor.svisit(k);
		WhyCode.add("(");
		visitor.svisit(o);
		WhyCode.add(") =");
		WhyCode.newLine();
		visitor.svisit(lexpr);
		WhyCode.whiteLine();
	}

}
