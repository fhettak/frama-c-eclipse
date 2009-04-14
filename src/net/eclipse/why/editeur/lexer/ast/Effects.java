package net.eclipse.why.editeur.lexer.ast;

public class Effects implements Visitable {

	Loc lo; Opt o; Opt p; Opt q;
	
	public Effects(Loc loc, Opt ord, Opt owr, Opt ors) {
		lo = loc;
		o = ord;
		p = owr;
		q = ors;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(o);
		visitor.visit(p);
		visitor.visit(q);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(o);
		visitor.svisit(p);
		visitor.svisit(q);
	}

}
