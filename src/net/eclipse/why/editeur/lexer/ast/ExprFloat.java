package net.eclipse.why.editeur.lexer.ast;

public class ExprFloat extends Expr implements Visitable {

	String flo;
	
	public ExprFloat(String f) {
		flo = f;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(flo);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(flo);
	}

}
