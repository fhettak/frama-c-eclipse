package net.eclipse.why.editeur.lexer.ast;

public interface Visitable {

	public void accept(ReflectiveVisitor visitor);
	
	public void saccept(ReflectiveVisitor visitor);
	
}
