package net.eclipse.why.editeur.lexer.ast;


public class LExprQIdent extends LExpr implements Visitable {

	Loc lo; QIdent qid;
	
	public LExprQIdent(Loc loc, QIdent q) {
		lo = loc;
		qid = q;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(qid);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(qid);
	}

}
