package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class TypeC implements Visitable {

	Loc lo; TypeV tv; Opt opt; Result r; Effects e; Object o;
	
	public TypeC(Loc loc, TypeV t) {
		lo = loc;
		tv = t;
		opt = null;
		r = null;
		e = null;
		o = null;
	}

	public TypeC(Loc loc, Opt opta, Result res, Effects eff, Object optp) {
		lo = loc;
		tv = null;
		opt = opta;
		r = res;
		e = eff;
		o = optp;
	}

	public void accept(ReflectiveVisitor visitor) {
		if(tv!=null) {
			visitor.visit(tv);
		} else {
			WhyElement.add("{");
			visitor.visit(opt);
			WhyElement.add("} ");
			visitor.visit(r);
			visitor.visit(e);
			WhyElement.add(" {");
			visitor.visit(o);
			WhyElement.add("}");
		}
	}

	public void saccept(ReflectiveVisitor visitor) {
		if(tv!=null) {
			visitor.svisit(tv);
		} else {
			WhyCode.add("{");
			visitor.svisit(opt);
			WhyCode.add("} ");
			visitor.svisit(r);
			visitor.svisit(e);
			WhyCode.add(" {");
			visitor.svisit(o);
			WhyCode.add("}");
		}
	}

}
