package net.eclipse.why.editeur.lexer.ast;

public class STypePType extends SType implements Visitable {

	Loc lo; PType p;
	
	public STypePType(Loc loc, PType pt) {
		lo = loc;
		p = pt;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(p);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(p);
	}

}
