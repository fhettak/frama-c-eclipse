package net.eclipse.why.editeur.lexer.ast;

public class ExprIdent extends Expr implements Visitable {

	Ident i;
	
	public ExprIdent(Ident id) {
		i = id;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(i);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(i);
	}

}
