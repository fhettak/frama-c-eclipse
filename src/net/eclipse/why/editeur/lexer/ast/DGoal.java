package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class DGoal extends Declaration implements Visitable {

	Loc lo; Ident k; LExpr lexpr;
	
	public DGoal(Loc loc, Ident i, LExpr le) {
		lo = loc;
		k = i;
		lexpr = le;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.setGoalMode();
		visitor.visit(k);
		WhyElement.whiteLine();
		visitor.visit(lexpr);
	}
	
	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("goal ");
		visitor.svisit(k);
		WhyCode.add(":");
		WhyCode.newLine();
		visitor.svisit(lexpr);
	}

}
