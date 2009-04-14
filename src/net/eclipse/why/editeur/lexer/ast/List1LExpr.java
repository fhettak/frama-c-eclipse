package net.eclipse.why.editeur.lexer.ast;

import java.util.Vector;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class List1LExpr implements Visitable {

	Vector<LExpr> v;
	
	public List1LExpr() {
		v = new Vector<LExpr>();
	}

	public void add(LExpr e) {
		v.add(0,e);
	}
	
	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(v.get(0));
		for(int e=1; e<v.size(); e++) {
			WhyElement.add(", ");
			visitor.visit(v.get(e));
		}
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(v.get(0));
		for(int e=1; e<v.size(); e++) {
			WhyCode.add(",");
			visitor.svisit(v.get(e));
		}
	}

}
