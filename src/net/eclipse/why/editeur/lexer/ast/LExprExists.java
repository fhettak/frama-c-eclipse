package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class LExprExists extends LExpr implements Visitable {

	Loc lo; Ident j; PType pt; LExpr lexpr;
	
	public LExprExists(Loc loc, Ident id, PType p, LExpr l) {
		lo = loc;
		j = id;
		pt = p;
		lexpr = l;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("exists ");
		visitor.visit(j);
		WhyElement.add(" : ");
		visitor.visit(pt);
		WhyElement.add(", ");
		WhyElement.newLine();
		visitor.visit(lexpr);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("exists ");
		visitor.svisit(j);
		WhyCode.add(":");
		visitor.svisit(pt);
		WhyCode.add(".");
		WhyCode.newLine();
		visitor.svisit(lexpr);
	}

}
