package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class LExprAnd extends LExpr implements Visitable {

	Loc lo; public LExpr l; public LExpr m;
	
	public LExprAnd(Loc loc, LExpr l1, LExpr l2) {
		lo = loc;
		l = l1;
		m = l2;
	}

	public void accept(ReflectiveVisitor visitor) {
		//WhyElement.setHMode();
		visitor.visit(l);
//		if(!WhyElement.is_goal) {
//			WhyElement.add(" && ");
//			if(WhyElement.and) {
//				WhyElement.newLine();
//			}
//		} else {
//			if(!WhyElement.and) {
//				WhyElement.add(" && ");
//			} else {
//				WhyElement.newLine();
//			}
//		}
		//if(WhyElement.is_goal) WhyElement.setKeywordMode();
		WhyElement.add(" and ");
		if(WhyElement.and) {
			WhyElement.newLine();
		}
		//if(WhyElement.is_goal) WhyElement.setExprMode();
		visitor.visit(m);
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(l);
		WhyCode.add(" and ");
		visitor.svisit(m);
	}

}
