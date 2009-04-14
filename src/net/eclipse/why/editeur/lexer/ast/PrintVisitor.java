package net.eclipse.why.editeur.lexer.ast;

import java.lang.reflect.Method;

import net.eclipse.why.editeur.WhyCode;
import net.eclipse.why.editeur.WhyElement;
import net.eclipse.why.editeur.actions.TraceDisplay;
import net.eclipse.why.editeur.actions.TraceDisplay.MessageType;

public class PrintVisitor implements ReflectiveVisitor {

	private boolean print = false;
	
	public void visitAssert(Assert x) {
	    if(print) TraceDisplay.print(MessageType.WARNING, "Assert");
	}

	public void visitBinder(Binder x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "Binder");
	}

	public void visitBrAssert(BrAssert x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "BrAssert");
	}

	public void visitCondition(Condition x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "Condition");
	}

	public void visitDAxiom(DAxiom x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "DAxiom");
	}

	public void visitDExcept(DExcept x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "DExcept");
	}

	public void visitDExceptOf(DExceptOf x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "DExceptOf");
	}

	public void visitDFunc(DFunc x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "DFunc");
	}

	public void visitDGoal(DGoal x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "DGoal");
	}

	public void visitDLetEq2(DLetEq2 x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "DLetEq2");
	}

	public void visitDLetEq(DLetEq x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "DLetEq");
	}

	public void visitDLetRec(DLetRec x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "DLetRec");
	}

	public void visitDLogic(DLogic x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "DLogic");
	}

	public void visitDParam(DParam x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "DParam");
	}

	public void visitDPred(DPred x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "DPred");
	}

	public void visitDType2(DType2 x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "DType2");
	}

	public void visitDType3(DType3 x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "DType3");
	}

	public void visitDType(DType x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "DType");
	}

	public void visitEffects(Effects x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "Effects");
	}

	public void visitExprAbsurd(ExprAbsurd x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "ExprAbsurd");
	}

	public void visitExprAmpAmp(ExprAmpAmp x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "ExprAmpAmp");
	}

	public void visitExprAssert(ExprAssert x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "ExprAssert");
	}

	public void visitExprBang(ExprBang x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "ExprBang");
	}

	public void visitExprBarBar(ExprBarBar x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "ExprBarBar");
	}

	public void visitExprBegin(ExprBegin x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "ExprBegin");
	}

	public void visitExprBoolean(ExprBoolean x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "ExprBoolean");
	}

	public void visitExprColon(ExprColon x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "ExprColon");
	}

	public void visitExprExprPar(ExprExprPar x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "ExprExprPar");
	}

	public void visitExprExprSQ(ExprExprSQ x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "ExprExprSQ");
	}

	public void visitExprFloat(ExprFloat x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "ExprFloat");
	}

	public void visitExprFun(ExprFun x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "ExprFun");
	}

	public void visitExprIdentifier(ExprIdentifier x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "ExprIdentifier");
	}

	public void visitExprIdent(ExprIdent x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "ExprIdent");
	}

	public void visitExprIdExprExpr(ExprIdExprExpr x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "ExprIdExprExpr");
	}

	public void visitExprIdExpr(ExprIdExpr x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "ExprIdExpr");
	}

	public void visitExprIfThenElse(ExprIfThenElse x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "ExprIfThenElse");
	}

	public void visitExprInt(ExprInt x) {
		if(print) TraceDisplay.print(MessageType.WARNING, "ExprInt");
	}

	public void visitExprLess(ExprLess x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprLess");
	}

	public void visitExprLetEq(ExprLetEq x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprLetEq");
	}

	public void visitExprLetIn(ExprLetIn x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprLetIn");
	}

	public void visitExprLetRecIn(ExprLetRecIn x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprLetRecIn");
	}

	public void visitExprLetRec(ExprLetRec x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprLetRec");
	}

	public void visitExprLetRefIn(ExprLetRefIn x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprLetRefIn");
	}

	public void visitExprMinus(ExprMinus x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprMinus");
	}

	public void visitExprNot(ExprNot x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprNot");
	}

	public void visitExprPCond2(ExprPCond2 x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprPCond2");
	}

	public void visitExprPCond(ExprPCond x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprPCond");
	}

	public void visitExprPercent(ExprPercent x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprPercent");
	}

	public void visitExprPlus(ExprPlus x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprPlus");
	}

	public void visitExprRaise2(ExprRaise2 x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprRaise2");
	}

	public void visitExprRaise(ExprRaise x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprRaise");
	}

	public void visitExprRel(ExprRel x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprRel");
	}

	public void visitExprSExpr(ExprSExpr x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprSExpr");
	}

	public void visitExprSlash(ExprSlash x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprSlash");
	}

	public void visitExprTimes(ExprTimes x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprTimes");
	}

	public void visitExprTry(ExprTry x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprTry");
	}

	public void visitExprTypeSQ(ExprTypeSQ x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprTypeSQ");
	}

	public void visitExprVoid(ExprVoid x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprVoid");
	}

	public void visitExprWhile(ExprWhile x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ExprWhile");
	}

	public void visitExternal(External x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "External");
	}

	public void visitHandler(Handler x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "Handler");
	}

	public void visitIdent(Ident x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "Ident");
	}

	public void visitInvVar(InvVar x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "InvVar");
	}

	public void visitLExprAnd(LExprAnd x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprAnd");
	}

	public void visitLExprArrow(LExprArrow x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprArrow");
	}

	public void visitLExprBool(LExprBool x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprBool");
	}

	public void visitLExprExists(LExprExists x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprExists");
	}

	public void visitLExprFloat(LExprFloat x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprFloat");
	}

	public void visitLExprForall(LExprForall x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprForall");
	}

	public void visitLExprFpi(LExprFpi x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprFpi");
	}

	public void visitLExprIdentExpr(LExprIdentExpr x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprIdentExpr");
	}

	public void visitLExprIdentList(LExprIdentList x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprIdentList");
	}

	public void visitLExprIdStr(LExprIdStr x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprIdStr");
	}

	public void visitLExprIf(LExprIf x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprIf");
	}

	public void visitLExprInteger(LExprInteger x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprInteger");
	}

	public void visitLExprLess(LExprLess x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprLess");
	}

	public void visitLExprLRArrow(LExprLRArrow x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprLRArrow");
	}

	public void visitLExprMinus(LExprMinus x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprMinus");
	}

	public void visitLExprNot(LExprNot x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprNot");
	}

	public void visitLExprOr(LExprOr x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprOr");
	}

	public void visitLExprPar(LExprPar x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprPar");
	}

	public void visitLExprPercent(LExprPercent x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprPercent");
	}

	public void visitLExprPlus(LExprPlus x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprPlus");
	}

	public void visitLExprQIdent(LExprQIdent x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprQIdent");
	}

	public void visitLExprRel(LExprRel x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprRel");
	}

	public void visitLExprSlash(LExprSlash x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprSlash");
	}

	public void visitLExprTimes(LExprTimes x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprTimes");
	}

	public void visitLExprVoid(LExprVoid x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LExprVoid");
	}

	public void visitList0BrAssert(List0BrAssert x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "List0BrAssert");
	}

	public void visitList1BrAssert(List1BrAssert x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "List1BrAssert");
	}

	public void visitList1Decl(List1Decl x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "List1Decl");
	}

	public void visitList1IdentSep(List1IdentSep x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "List1IdentSep");
	}

	public void visitList1LExpr(List1LExpr x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "List1LExpr");
	}

	public void visitList1PType(List1PType x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "List1PType");
	}

	public void visitList1TypeVar(List1TypeVar x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "List1TypeVar");
	}

	public void visitListBinder(ListBinder x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ListBinder");
	}

	public void visitListCondition(ListCondition x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ListCondition");
	}

	public void visitListHandler(ListHandler x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ListHandler");
	}

	public void visitListLBinder(ListLBinder x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ListLBinder");
	}

	public void visitListSimpleExpr(ListSimpleExpr x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ListSimpleExpr");
	}

	public void visitListTrigger(ListTrigger x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "ListTrigger");
	}

	public void visitLoc(Loc x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "Loc");
	}

	public void visitLogicBinder1(LogicBinder1 x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LogicBinder1");
	}

	public void visitLogicBinder2(LogicBinder2 x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LogicBinder2");
	}

	public void visitLogicType1(LogicType1 x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LogicType1");
	}

	public void visitLogicType2(LogicType2 x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LogicType2");
	}

	public void visitLogicType3(LogicType3 x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LogicType3");
	}

	public void visitLogicType4(LogicType4 x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "LogicType4");
	}

	public void visitOptAssert(OptAssert x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "OptAssert");
	}

	public void visitOptCast(OptCast x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "OptCast");
	}

	public void visitOptInv(OptInv x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "OptInv");
	}

	public void visitOptRaises(OptRaises x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "OptRaises");
	}

	public void visitOptReads(OptReads x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "OptReads");
	}

	public void visitOptVar(OptVar x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "OptVar");
	}

	public void visitOptWrites(OptWrites x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "OptWrites");
	}

	public void visitPostCond(PostCond x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "PostCond");
	}

	public void visitPTypeBool(PTypeBool x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "PTypeBool");
	}

	public void visitPTypeId(PTypeId x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "PTypeId");
	}

	public void visitPTypeInt(PTypeInt x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "PTypeInt");
	}

	public void visitPTypePrim(PTypePrim x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "PTypePrim");
	}

	public void visitPTypePrims(PTypePrims x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "PTypePrims");
	}

	public void visitPTypeReal(PTypeReal x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "PTypeReal");
	}

	public void visitPTypeUnit(PTypeUnit x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "PTypeUnit");
	}

	public void visitPTypeVar(PTypeVar x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "PTypeVar");
	}

	public void visitQIdentAt(QIdentAt x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "QIdentAt");
	}

	public void visitQIdentId(QIdentId x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "QIdentId");
	}

	public void visitRecfun(Recfun x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "Recfun");
	}

	public void visitRelation(Relation x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "Relation");
	}

	public void visitResult(Result x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "Result");
	}

	public void visitSTypeArr(STypeArr x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "STypeArr");
	}

	public void visitSTypePar(STypePar x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "STypePar");
	}

	public void visitSTypePType(STypePType x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "STypePType");
	}

	public void visitSTypeRef(STypeRef x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "STypeRef");
	}

	public void visitTriggers(Triggers x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "Triggers");
	}

	public void visitTypeC(TypeC x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "TypeC");
	}

	public void visitTypeVar(TypeVar x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "TypeVar");
	}

	public void visitTypeV(TypeV x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "TypeV");
	}

	public void visitVariant(Variant x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "Variant");
	}

	public void visitWhyFile(WhyFile x) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "WhyFile");
	}

	public void visitObject(Object o) {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "Object : " + o.toString());
		WhyElement.add(o.toString());
	}
	
	public void svisitObject(Object o) {
		WhyCode.add(o.toString());
	}
	
	public void visitNull() {
		if(print) TraceDisplay.print(MessageType.MESSAGE, "NULL");
	}
	
	public void visit(Object o) {
		try {
			if(o==null) {
				visitNull();
				return;
			}
			Method method = getMethod(o.getClass());
			method.invoke(this, new Object[] {o});
			if(o instanceof Visitable) {
				callAccept((Visitable)o);
			}
		} catch (Exception e) {
			TraceDisplay.print(MessageType.ERROR, "PrintVisitor.visit() : " + e);
		}
	}
	
	public void svisit(Object o) {
		try {
			if(o==null) {
				visitNull();
				return;
			}
			Method method = getSMethod(o.getClass());
			method.invoke(this, new Object[] {o});
			if(o instanceof Visitable) {
				callSAccept((Visitable)o);
			}
		} catch (Exception e) {
			TraceDisplay.print(MessageType.ERROR, "PrintVisitor.svisit() : " + e);
		}
	}
	
	
	
	
	
	protected Method getMethod(Class c) {
		Class newc = c;
		Method m = null;
		// Try the superclasses
		while (m == null && newc != Object.class) {
			String method = newc.getName();
			method = "visit" + method.substring(method.lastIndexOf('.') + 1);
			try {
				m = getClass().getMethod(method, new Class[] {newc});
			} catch (NoSuchMethodException e) {
				newc = newc.getSuperclass();
			}
		}
		// Try the interfaces.  If necessary, you
		// can sort them first to define 'visitable' interface wins
		// in case an object implements more than one.
		if (newc == Object.class) {
			Class[] interfaces = c.getInterfaces();
			for (int i = 0; i < interfaces.length; i++) {
				String method = interfaces[i].getName();
				method = "visit" + method.substring(method.lastIndexOf('.') + 1);
				try {
					m = getClass().getMethod(method, new Class[] {interfaces[i]});
				} catch (NoSuchMethodException e) {}
			}
		}
		if (m == null) {
			try {
				m = this.getClass().getMethod("visitObject", new Class[] {Object.class});
			} catch (Exception e) {
				// Can't happen
				TraceDisplay.print(MessageType.ERROR, "PrintVisitor.getMethod() : " + e);
			}
		}
		return m;
	}
	
	protected Method getSMethod(Class c) {
		Class newc = c;
		Method m = null;
		// Try the superclasses
		while (m == null && newc != Object.class) {
			String method = newc.getName();
			method = "visit" + method.substring(method.lastIndexOf('.') + 1);
			try {
				m = getClass().getMethod(method, new Class[] {newc});
			} catch (NoSuchMethodException e) {
				newc = newc.getSuperclass();
			}
		}
		// Try the interfaces.  If necessary, you
		// can sort them first to define 'visitable' interface wins
		// in case an object implements more than one.
		if (newc == Object.class) {
			Class[] interfaces = c.getInterfaces();
			for (int i = 0; i < interfaces.length; i++) {
				String method = interfaces[i].getName();
				method = "visit" + method.substring(method.lastIndexOf('.') + 1);
				try {
					m = getClass().getMethod(method, new Class[] {interfaces[i]});
				} catch (NoSuchMethodException e) {}
			}
		}
		if (m == null) {
			try {
				m = this.getClass().getMethod("svisitObject", new Class[] {Object.class});
			} catch (Exception e) {
				// Can't happen
				TraceDisplay.print(MessageType.ERROR, "PrintVisitor.getSMethod() : " + e);
			}
		}
		return m;
	}

	
	
	
	
	public void callAccept(Visitable visitable) {
		visitable.accept(this);
	}
	
	public void callSAccept(Visitable visitable) {
		visitable.saccept(this);
	}

}
