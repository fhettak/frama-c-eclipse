package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class PostCond implements Visitable {

	Loc lo; Assert o; ListCondition list;
	
	public PostCond(Loc loc, Assert object, ListCondition l1) {
		lo = loc;
		o = object;
		list = l1;
	}

	public void accept(ReflectiveVisitor visitor) {
		if(o!=null) {
			visitor.visit(o);
		}
		if(list!=null) {
			WhyElement.add("|");
			visitor.visit(list);
		}
	}

	public void saccept(ReflectiveVisitor visitor) {
		if(o!=null) {
			visitor.svisit(o);
		}
		if(list!=null) {
			WhyCode.add("|");
			visitor.svisit(list);
		}
	}

}
