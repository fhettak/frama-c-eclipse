package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;


public class LExprPar extends LExpr implements Visitable {

	Loc lo; public LExpr lexpr; boolean p; boolean and_mode;
	
	public LExprPar(Loc loc, LExpr l) {
		lo = loc;
		lexpr = l;
		p = false;
		and_mode = true;
	}

	public void accept(ReflectiveVisitor visitor) {
		
		if(lexpr instanceof LExprOr && WhyElement.and) {
			and_mode = false;
			WhyElement.and = false;
		}
		
//		if(p || !WhyElement.and) WhyElement.add("(");
//		visitor.visit(lexpr);
//		if(p || !WhyElement.and) WhyElement.add(")");
		
		boolean par = true;
		if(lexpr instanceof LExprForall && !Pointer.isInArrow()) {
			par = false;
		}
		
		if(par) {
			WhyElement.add("(");
			WhyElement.indent_incr();
		}
		visitor.visit(lexpr);
		if(par) {
			WhyElement.indent_decr();
			WhyElement.add(")");
		}
		
		if(!and_mode) {
			WhyElement.and = true;
		}
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("(");
		visitor.svisit(lexpr);
		WhyCode.add(")");
	}

}
