package net.eclipse.why.editeur.lexer.ast;

public class LogicType4 extends LogicType implements Visitable {

	Loc lo; PType pt;
	
	public LogicType4(Loc loc, PType p) {
		lo = loc;
		pt = p;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(pt);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(pt);
	}

}
