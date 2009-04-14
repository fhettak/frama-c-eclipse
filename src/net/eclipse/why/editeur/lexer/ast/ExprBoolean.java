package net.eclipse.why.editeur.lexer.ast;

public class ExprBoolean extends Expr implements Visitable {

	Boolean bool;
	
	public ExprBoolean(boolean b) {
		bool = new Boolean(b);
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(bool);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(bool);
	}

}
