package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class Variant implements Visitable {

	Loc lo; LExpr lexpr; Ident o;
	
	public Variant(Loc loc, LExpr l, Ident object) {
		lo = loc;
		lexpr = l;
		o = object;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(lexpr);
		if(o!=null) {
			WhyElement.add(" for ");
			visitor.visit(o);
		}
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(lexpr);
		if(o!=null) {
			WhyCode.add(" for ");
			visitor.svisit(o);
		}
	}

}
