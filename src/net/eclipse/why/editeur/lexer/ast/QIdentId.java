package net.eclipse.why.editeur.lexer.ast;


public class QIdentId extends QIdent implements Visitable {

	Loc lo; String str;
	
	public QIdentId(Loc loc, String id1) {
		lo = loc;
		str = id1;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(str);
		//Goal.add(str);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(str);
	}
	
}
