package net.eclipse.why.editeur.lexer.ast;

import java.util.Vector;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class List1IdentSep implements Visitable {

	Vector<Ident> v;
	
	public List1IdentSep() {
		v = new Vector<Ident>();
	}
	
	public void add(Ident id) {
		v.add(id);
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
