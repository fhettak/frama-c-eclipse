package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class DLetRec extends Declaration implements Visitable {

	Loc lo; Recfun rf;
	
	public DLetRec(Loc loc, Recfun r) {
		lo = loc;
		rf = r;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.setLetMode();
		WhyElement.add("let rec ");
		WhyElement.setTextMode();
		visitor.visit(rf);
		WhyElement.whiteLine();
	}
	
	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("let rec ");
		visitor.svisit(rf);
		WhyCode.newLine();
	}

}
