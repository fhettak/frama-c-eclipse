package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprPCond2 extends Expr implements Visitable {

	Loc lo; Expr expr; PostCond pc;
	
	public ExprPCond2(Loc loc, Expr e, PostCond p) {
		lo = loc;
		expr = e;
		pc = p;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(expr);
		WhyElement.add("{{");
		visitor.visit(pc);
		WhyElement.add("}}");
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(expr);
		WhyCode.add("{{");
		visitor.svisit(pc);
		WhyCode.add("}}");
	}

}
