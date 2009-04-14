package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprFun extends Expr implements Visitable {

	Loc lo; Object o; List0BrAssert lba; Expr expr;
	
	public ExprFun(Loc loc, Object b, List0BrAssert l0, Expr e) {
		lo = loc;
		o = b;
		lba = l0;
		expr = e;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("fun ");
		visitor.visit(o);
		WhyElement.add(" -> ");
		visitor.visit(lba);
		visitor.visit(expr);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("fun ");
		visitor.svisit(o);
		WhyCode.add(" -> ");
		visitor.svisit(lba);
		visitor.svisit(expr);
	}

}
