package net.eclipse.why.editeur.lexer.ast;

import java.util.Vector;

public class ListSimpleExpr implements Visitable {

	Vector<Expr> v;
	
	public ListSimpleExpr() {
		v = new Vector<Expr>();
	}

	public void add(Expr se) {
		v.add(se);
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
