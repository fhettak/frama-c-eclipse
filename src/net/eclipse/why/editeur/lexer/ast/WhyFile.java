package net.eclipse.why.editeur.lexer.ast;



public class WhyFile implements Visitable {

	List1Decl ld;
	
	public WhyFile(List1Decl l) {
		this.ld = l;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(ld);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(ld);
	}
}
