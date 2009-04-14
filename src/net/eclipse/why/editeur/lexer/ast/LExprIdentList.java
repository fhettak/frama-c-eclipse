package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class LExprIdentList extends LExpr implements Visitable {

	Loc lo; QIdent qid; List1LExpr list;
	
	public LExprIdentList(Loc loc, QIdent q, List1LExpr l) {
		lo = loc;
		qid = q;
		list = l;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(qid);
		WhyElement.add("(");
		visitor.visit(list);
		WhyElement.add(")");
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(qid);
		WhyCode.add("(");
		visitor.svisit(list);
		WhyCode.add(")");
	}

}
