package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class LExprForall extends LExpr implements Visitable {

	Loc lo; List1IdentSep lis; PType pt; Triggers trig; LExpr lexpr;
	
	public LExprForall(Loc loc, List1IdentSep l, PType p, Triggers t, LExpr e) {
		lo = loc;
		lis = l;
		pt = p;
		trig = t;
		lexpr = e;
	}

	public void accept(ReflectiveVisitor visitor) {
		
		if(!Pointer.isInArrow()) {
			WhyElement.setForallMode();
			WhyElement.unindent();
			WhyElement.newLine();
		}
		
		WhyElement.add("forall ");
		visitor.visit(lis);
		WhyElement.add(" : ");
		visitor.visit(pt);
		visitor.visit(trig);
		WhyElement.newLine();
		

		if(!Pointer.isInArrow()) {
			if(!(lexpr instanceof LExprForall)) {
				
				while(lexpr instanceof LExprPar || lexpr instanceof LExprIdStr) {
					if(lexpr instanceof LExprPar) lexpr = ((LExprPar)lexpr).lexpr;
					else if(lexpr instanceof LExprIdStr) lexpr = ((LExprIdStr)lexpr).lexpr;
				}
				if(!(lexpr instanceof LExprForall)) {
					WhyElement.setHMode();
					WhyElement.indent();
				}
//				boolean quit = false;
//				if(lexpr instanceof LExprPar) {
//					if(((LExprPar)lexpr).lexpr instanceof LExprForall) {
//						quit = true;
//						lexpr = ((LExprPar)lexpr).lexpr;
//					}
//				}
//				if(!quit) {
//					WhyElement.setHMode();
//					WhyElement.indent();
//				}
			}
		}
		
		visitor.visit(lexpr);

	}

	public void saccept(ReflectiveVisitor visitor) {
		WhyCode.add("forall ");
		visitor.svisit(lis);
		WhyCode.add(":");
		visitor.svisit(pt);
		visitor.svisit(trig);
		WhyCode.add(".");
		WhyCode.newLine();
		visitor.svisit(lexpr);
	}

}
