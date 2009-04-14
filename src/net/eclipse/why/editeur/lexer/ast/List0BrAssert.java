package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class List0BrAssert implements Visitable {

	Loc lo; List1BrAssert list;
	
	public List0BrAssert(Loc loc, List1BrAssert l) {
		lo = loc;
		list = l;
	}

	public void accept(ReflectiveVisitor visitor) {
		if(list!=null) {
			visitor.visit(list);
		} else {
			WhyElement.add("{}");
		}
	}

	public void saccept(ReflectiveVisitor visitor) {
		if(list!=null) {
			visitor.svisit(list);
		} else {
			WhyCode.add("{}");
		}
	}

}
