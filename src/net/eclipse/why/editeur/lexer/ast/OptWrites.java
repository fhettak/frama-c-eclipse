package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class OptWrites extends Opt implements Visitable {

	Loc lo; Object o;
	
	public OptWrites(Loc loc, Object l0) {
		lo = loc;
		o = l0;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("writes ");
		visitor.visit(o);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("writes ");
		visitor.svisit(o);
	}

}
