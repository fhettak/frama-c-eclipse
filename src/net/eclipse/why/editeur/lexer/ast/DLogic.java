package net.eclipse.why.editeur.lexer.ast;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;

public class DLogic extends Declaration implements Visitable {

	Loc lo; External ext; List1IdentSep lis; LogicType lt;
	
	public DLogic(Loc loc, External e, List1IdentSep l1, LogicType l) {
		lo = loc;
		ext = e;
		lis = l1;
		lt = l;
	}

	public void accept(ReflectiveVisitor visitor) {
		WhyElement.setLogicMode();
		visitor.visit(ext);
		WhyElement.add("logic ");
		WhyElement.setTextMode();
		visitor.visit(lis);
		WhyElement.newLine();
		WhyElement.setTextMode();
		visitor.visit(lt);
		WhyElement.whiteLine();
	}

	public void saccept(ReflectiveVisitor visitor) {
		visitor.svisit(ext);
		WhyCode.add("logic ");
		visitor.svisit(lis);
		WhyCode.add(": ");
		visitor.svisit(lt);
		WhyCode.whiteLine();
	}

}
