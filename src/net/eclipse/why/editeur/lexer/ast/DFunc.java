package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class DFunc extends Declaration implements Visitable {

	Loc lo; Ident k; Object o; PType p; LExpr lexpr;
	
	public DFunc(Loc loc, Ident i, Object l0, PType pt, LExpr le) {
		lo = loc;
		k = i;
		o = l0;
		p = pt;
		lexpr = le;
	}

	public void accept(ReflectiveVisitor visitor) {
		Pointer.outArrow();
		WhyElement.setFunctionMode();
		WhyElement.add("function ");
		WhyElement.setTextMode();
		visitor.visit(k);
		WhyElement.setFunctionMode();
		WhyElement.add(" (");
		WhyElement.setTextMode();
		WhyElement.newLine();
		visitor.visit(o);
		WhyElement.newLine();
		WhyElement.setFunctionMode();
		WhyElement.add(") : ");
		WhyElement.setTextMode();
		visitor.visit(p);
		WhyElement.add(" =");
		WhyElement.newLine();
		WhyElement.setHMode();
		Pointer.inArrow();
		visitor.visit(lexpr);
		Pointer.outArrow();
		WhyElement.whiteLine();
	}
	
	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("function ");
		visitor.svisit(k);
		WhyCode.add("(");
		visitor.svisit(o);
		WhyCode.add("):");
		visitor.svisit(p);
		WhyCode.add(" = ");
		visitor.svisit(lexpr);
		WhyCode.whiteLine();
	}

}
