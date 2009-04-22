package net.eclipse.why.editeur.lexer;

import java.io.FileReader;

import java_cup.runtime.Scanner;
import net.eclipse.why.editeur.Goal;
import net.eclipse.why.editeur.WhyElement;
import net.eclipse.why.editeur.lexer.ast.Pointer;
import net.eclipse.why.editeur.lexer.ast.PrintVisitor;
import net.eclipse.why.editeur.views.TraceView;
import net.eclipse.why.editeur.views.TraceView.MessageType;


/**
 * Make the strings and StyleRanges for .why files pretty printer
 * 
 * @author A. Oudot
 */
public class GoalDisplayModifier {
	
	
	/**
	 * Constructor
	 */
	public GoalDisplayModifier() {}
	
	
	/**
	 * Make strings and StyleRange for why file pretty printer
	 * 
	 * @param file the why file name
	 */
	public void whyToView(String file) {
		
		Scanner lex;
		
		//clean elements which will contain pretty why code
		WhyElement.clean();
		Goal.clean();
		
		if(file == null) {
			return;
		}
		
		try {
			//why file parsing
			lex = new whyLexer(new FileReader(file));
			parser p = new parser(lex);
			//get the abstract syntax tree
			Object o = p.parse().value;
			
			Pointer.reset(); //clean the tree pointer
			Pointer.setTree(o); //set the Tree
			PrintVisitor visitor = new PrintVisitor();
			visitor.visit(o); //visit the Tree nodes
			
			//During the "visit", the Pointer'll record the
			//last LExprArrow (x -> y) object visited to
			//separate after the first part of the goal
			//(hypothesis) and the second part(implication)
			//The WhyElement will be completed by why code
			//ready for the pretty print. It can be a goal
			//code or a context code. In this case, we've
			//parsed a goal file, thus the Goal static class
			//will be completed. The Goal.make() function
			//will separate the hypothesis (1st part) to
			//the result (2nd part).
			
			WhyElement.saveAsGoal();
			Goal.make();
			
			//Splitter.reset();
			//Splitter.setNumG(num);
			//Pointer.breakUp();
			//Splitter.setPartI(o);
			//o = Pointer.get();
			//Splitter.setPartII(o);
			
		} catch (Exception e) {
			TraceView.print(MessageType.ERROR, "GoalDisplayModifier.whyToView() : " + e);
		}
		catch (Throwable t) {
			TraceView.print(MessageType.ERROR, "GoalDisplayModifier.whyToView() : " + t);
		}   
	}
}
