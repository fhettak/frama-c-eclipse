package net.eclipse.why.editeur.lexer.ast;

import java.util.Vector;

public class ListBinder implements Visitable {
	
	Vector<Binder> v;

	public ListBinder() {
		v = new Vector<Binder>();
	}

	public void add(Binder b) {
		v.add(b);
	}
	
	public void accept(ReflectiveVisitor visitor) {
		for(int e=0; e<v.size(); e++) {
			visitor.visit(v.get(e));
		}
	}

	public void saccept(ReflectiveVisitor visitor) {
		for(int e=0; e<v.size(); e++) {
			visitor.svisit(v.get(e));
		}
	}

}
