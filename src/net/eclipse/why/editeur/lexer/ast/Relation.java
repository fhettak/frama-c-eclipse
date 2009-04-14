package net.eclipse.why.editeur.lexer.ast;


public class Relation implements Visitable {

	String s;
	
	public Relation(String string) {
		s = string;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(s);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(s);
	}

}
