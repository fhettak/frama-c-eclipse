package net.eclipse.why.editeur.lexer.ast;

public class ExprRel extends Expr implements Visitable {

	Loc lo; Expr expr; Expr fxpr;
	
	public ExprRel(Loc loc, Expr e, Expr f) {
		lo = loc;
		expr = e;
		fxpr = f;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(expr);
		visitor.visit(fxpr);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(expr);
		visitor.svisit(fxpr);
	}

}
