package net.eclipse.why.editeur.lexer.ast;

public class LExprFloat extends LExpr implements Visitable {

	String flo;
	
	public LExprFloat(String f) {
		flo = f;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(flo);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(flo);
	}

}
