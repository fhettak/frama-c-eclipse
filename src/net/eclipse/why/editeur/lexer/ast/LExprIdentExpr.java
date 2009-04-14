package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class LExprIdentExpr extends LExpr implements Visitable {

	Loc lo; QIdent qid; LExpr lexpr;
	
	public LExprIdentExpr(Loc loc, QIdent q, LExpr e) {
		lo = loc;
		qid = q;
		lexpr = e;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(qid);
		WhyElement.add("[");
		visitor.visit(lexpr);
		WhyElement.add("]");
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(qid);
		WhyCode.add("[");
		visitor.svisit(lexpr);
		WhyCode.add("]");
	}

}
