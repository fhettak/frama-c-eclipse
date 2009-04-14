package net.eclipse.why.editeur.lexer.ast;

public class PTypeId extends PType implements Visitable {

	Loc lo; Ident x;
	
	public PTypeId(Loc loc, Ident id) {
		lo = loc;
		x = id;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(x);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(x);
	}

}
