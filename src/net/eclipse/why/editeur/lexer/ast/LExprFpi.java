package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class LExprFpi extends LExpr implements Visitable {

	Loc lo; LExpr lexpr; String flo1; String flo2;
	
	public LExprFpi(Loc loc, LExpr l, String f1, String f2) {
		lo = loc;
		lexpr = l;
		flo1 = f1;
		flo2 = f2;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("fpi(");
		visitor.visit(lexpr);
		WhyElement.add(",");
		visitor.visit(flo1);
		WhyElement.add(",");
		visitor.visit(flo2);
		WhyElement.add(")");
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("fpi(");
		visitor.svisit(lexpr);
		WhyCode.add(",");
		visitor.svisit(flo1);
		WhyCode.add(",");
		visitor.svisit(flo2);
		WhyCode.add(")");
	}

}
