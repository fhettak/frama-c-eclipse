package net.eclipse.why.editeur.lexer.ast;

import java.util.Vector;

import net.eclipse.why.editeur.WhyElement;

public class ListLBinder implements Visitable {

	Vector<LogicBinder> v;
	
	public ListLBinder() {
		v = new Vector<LogicBinder>();
	}

	public void add(LogicBinder lb) {
		v.add(lb);
	}
	
	public void accept(ReflectiveVisitor visitor) {
		WhyElement.setBinderMode();
		WhyElement.newLine();
		WhyElement .add("	");
		visitor.visit(v.get(0));
		for(int e=1; e<v.size(); e++) {
			WhyElement.newLine();
			WhyElement .add("	");
			//WhyElement.add(",");
			visitor.visit(v.get(e));
		}
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(v.get(0));
		for(int e=1; e<v.size(); e++) {
			visitor.svisit(v.get(e));
		}
	}

}
