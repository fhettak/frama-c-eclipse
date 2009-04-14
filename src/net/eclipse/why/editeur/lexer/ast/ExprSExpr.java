package net.eclipse.why.editeur.lexer.ast;

public class ExprSExpr extends Expr implements Visitable {

	Loc lo; Expr expr; ListSimpleExpr lse;
	
	public ExprSExpr(Loc loc, Expr s, ListSimpleExpr l) {
		lo = loc;
		expr = s;
		lse = l;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(expr);
		visitor.visit(lse);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(expr);
		visitor.svisit(lse);
	}

}
