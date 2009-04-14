package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class Binder implements Visitable {
	
	Loc lo;
	List1IdentSep lis;
	TypeV tv;
	

	public Binder(Loc loc, List1IdentSep l, TypeV t) {
		lo = loc;
		lis = l;
		tv = t;
	}


	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("(");
		if(lis!=null && tv!=null) {
			visitor.visit(lis);
			WhyElement.add(":");
			visitor.visit(tv);
		}
		WhyElement.add(")");
	}
	
	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("(");
		if(lis!=null && tv!=null) {
			visitor.svisit(lis);
			WhyCode.add(":");
			visitor.svisit(tv);
		}
		WhyCode.add(")");
	}

}
