package net.eclipse.why.editeur.lexer.ast;

public class OptAssert extends Opt implements Visitable {

	Loc lo; Assert ass;
	
	public OptAssert(Loc loc, Assert a) {
		lo = loc;
		ass = a;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(ass);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(ass);
	}

}
