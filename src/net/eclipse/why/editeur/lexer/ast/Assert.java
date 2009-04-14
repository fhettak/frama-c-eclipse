package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class Assert implements Visitable {
	
	Loc lo;
	LExpr lexpr;
	Ident h;

	public Assert(Loc loc, LExpr l, Ident id) {
		lo = loc;
		lexpr = l;
		h = id;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(lexpr);
		if(h!=null) {
			WhyElement.add(" as ");
			visitor.visit(h);
		}
	}
	
	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(lexpr);
		if(h!=null) {
			WhyCode.add(" as ");
			visitor.svisit(h);
		}
	}

}
