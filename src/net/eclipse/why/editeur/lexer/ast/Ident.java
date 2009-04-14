package net.eclipse.why.editeur.lexer.ast;



public class Ident implements Visitable {

	Loc lo;
	String s;
	
	public Ident(Loc loc, String id) {
		lo = loc;
		s = id;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(s);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(s);
	}

}
