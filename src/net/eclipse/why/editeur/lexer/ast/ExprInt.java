package net.eclipse.why.editeur.lexer.ast;

public class ExprInt extends Expr implements Visitable {

	String integer;
	
	public ExprInt(String i) {
		integer = i;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(integer);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(integer);
	}

}
