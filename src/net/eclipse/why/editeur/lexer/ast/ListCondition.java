package net.eclipse.why.editeur.lexer.ast;

import java.util.Vector;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ListCondition implements Visitable {

	Vector<Condition> v;
	
	public ListCondition() {
		v = new Vector<Condition>();
	}

	public void add(Condition e) {
		v.add(e);
	}
	
	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(v.get(0));
		for(int e=1; e<v.size(); e++) {
			WhyElement.add("|");
			visitor.visit(v.get(e));
		}
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(v.get(0));
		for(int e=1; e<v.size(); e++) {
			WhyCode.add("|");
			visitor.svisit(v.get(e));
		}
	}

}
