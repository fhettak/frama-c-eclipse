package net.eclipse.why.editeur.lexer.ast;

public class Recfun implements Visitable {

	Loc lo; Ident i; Object x; TypeV tv; OptVar opt; List0BrAssert list; Expr expr;
	
	public Recfun(Loc loc, Ident id, Object b, TypeV t, OptVar o, List0BrAssert l0, Expr e) {
		lo = loc;
		i = id;
		x = b;
		tv = t;
		opt = o;
		list = l0;
		expr = e;
	}

	public void accept(ReflectiveVisitor visitor) {
		visitor.visit(i);
		visitor.visit(x);
		visitor.visit(tv);
		visitor.visit(opt);
		visitor.visit(list);
		visitor.visit(expr);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(i);
		visitor.svisit(x);
		visitor.svisit(tv);
		visitor.svisit(opt);
		visitor.svisit(list);
		visitor.svisit(expr);
	}

}
