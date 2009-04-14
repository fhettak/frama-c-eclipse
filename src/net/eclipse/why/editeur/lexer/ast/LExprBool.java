package net.eclipse.why.editeur.lexer.ast;


public class LExprBool extends LExpr implements Visitable {

	Boolean bool;
	
	public LExprBool(boolean b) {
		bool = new Boolean(b);
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(bool);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(bool);
	}

}
