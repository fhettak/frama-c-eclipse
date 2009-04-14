package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class DParam extends Declaration implements Visitable {

	Loc lo; External ext; List1IdentSep lis; TypeV tv;
	
	public DParam(Loc loc, External e, List1IdentSep l1, TypeV t) {
		lo = loc;
		ext = e;
		lis = l1;
		tv = t;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.setParameterMode();
		visitor.visit(ext);
		WhyElement.add("parameter ");
		WhyElement.setTextMode();
		visitor.visit(lis);
		WhyElement.add(" : ");
		visitor.visit(tv);
		WhyElement.whiteLine();
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(ext);
		WhyCode.add("parameter ");
		visitor.svisit(lis);
		WhyCode.add(":");
		visitor.svisit(tv);
		WhyCode.newLine();
	}

}
