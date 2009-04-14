package net.eclipse.why.editeur.lexer.ast;


public class LExprRel extends LExpr implements Visitable {

	Loc lo; LExpr l; Relation rel; LExpr m;
	
	public LExprRel(Loc loc, LExpr l1, Relation r, LExpr l2) {
		lo = loc;
		l = l1;
		rel = r;
		m = l2;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(l);
		visitor.visit(rel);
		visitor.visit(m);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(l);
		visitor.svisit(rel);
		visitor.svisit(m);
	}

}
