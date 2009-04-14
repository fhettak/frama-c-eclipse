package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class LExprMinus extends LExpr implements Visitable {

	Loc lo; LExpr lexpr;
	
	public LExprMinus(Loc loc, LExpr l) {
		lo = loc;
		lexpr = l;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.add("-");
		if(lexpr instanceof LExprPar)
			//WhyElement.par = true;
			((LExprPar)lexpr).p = true;
//		} else {
//			WhyElement.par = false;
//		}
		visitor.visit(lexpr);
	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("-");
		visitor.svisit(lexpr);
	}

}
