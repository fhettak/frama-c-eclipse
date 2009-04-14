package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprAssert extends Expr implements Visitable {

	Loc lo; List1BrAssert lba; Expr expr;
	
	public ExprAssert(Loc loc, List1BrAssert l, Expr e) {
		lo = loc;
		lba = l;
		expr = e;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("assert ");
		visitor.visit(lba);
		WhyElement.add(";");
		visitor.visit(expr);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("assert ");
		visitor.svisit(lba);
		WhyCode.add(";");
		visitor.svisit(expr);
	}
}
