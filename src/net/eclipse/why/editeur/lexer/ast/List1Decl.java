package net.eclipse.why.editeur.lexer.ast;

import java.util.Vector;

import net.eclipse.why.editeur.WhyElement;

public class List1Decl implements Visitable {

	private Vector<Declaration> list;
	
	public List1Decl(Declaration d) {
		list = new Vector<Declaration>();
		list.add(d);
	}
	
	public void add(Declaration e) {
		list.add(e);
	}

	public void accept(ReflectiveVisitor visitor) {
		for(int e=list.size()-1; e>=0; e--) {
			visitor.visit(list.get(e));
		}
		WhyElement.recordStyleRange();
	}

	public void saccept(ReflectiveVisitor visitor) {
		for(int e=list.size()-1; e>=0; e--) {
			visitor.svisit(list.get(e));
		}
	}
}
