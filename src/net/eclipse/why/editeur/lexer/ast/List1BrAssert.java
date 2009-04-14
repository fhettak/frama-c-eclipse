package net.eclipse.why.editeur.lexer.ast;

import java.util.Vector;

public class List1BrAssert implements Visitable {

	Vector<BrAssert> v;
	
	public List1BrAssert() {
		v = new Vector<BrAssert>();
	}

	public void add(BrAssert b) {
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
