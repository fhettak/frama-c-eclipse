package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class QIdentAt extends QIdent implements Visitable {

	Loc lo; String str; String ttr;
	
	public QIdentAt(Loc loc, String id1, String id2) {
		lo = loc;
		str = id1;
		ttr = id2;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(str);
		WhyElement.add("@");
		if(ttr!=null) {
			visitor.visit(ttr);
		}
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(str);
		WhyCode.add("@");
		if(ttr!=null) {
			visitor.svisit(ttr);
		}
	}

}
