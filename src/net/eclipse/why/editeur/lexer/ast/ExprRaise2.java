package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class ExprRaise2 extends Expr implements Visitable {

	Loc lo; Ident i; Expr expr; OptCast opt;
	
	public ExprRaise2(Loc loc, Ident id, Expr e, OptCast o) {
		lo = loc;
		i = id;
		expr = e;
		opt = o;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("raise (");
		visitor.visit(i);
		visitor.visit(expr);
		WhyElement.add(") ");
		visitor.visit(opt);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("raise (");
		visitor.svisit(i);
		visitor.svisit(expr);
		WhyCode.add(") ");
		visitor.svisit(opt);
	}

}
