package net.eclipse.why.editeur.lexer.ast;

public class PTypeVar extends PType implements Visitable {

	Loc lo; TypeVar tv;
	
	public PTypeVar(Loc loc, TypeVar t) {
		lo = loc;
		tv = t;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(tv);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(tv);
	}

}
