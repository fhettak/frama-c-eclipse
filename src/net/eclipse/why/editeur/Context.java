package net.eclipse.why.editeur;

import java.io.File;
import java.io.FileReader;
import java.util.Vector;

import org.eclipse.swt.custom.StyleRange;

import net.eclipse.why.editeur.lexer.parser;
import net.eclipse.why.editeur.lexer.whyLexer;
import net.eclipse.why.editeur.lexer.ast.PrintVisitor;
import net.eclipse.why.editeur.views.TraceView;
import net.eclipse.why.editeur.views.TraceView.MessageType;

/**
 * Make and Store the context file (text + style ranges)
 * 
 * @author A. Oudot
 */
@SuppressWarnings("unchecked")
public class Context {

	
	private static String context = ""; //the text
	private static Vector styleRanges = new Vector(); //the style ranges
	
	
	/**
	 * Clean the text String and ranges Vector objects
	 */
	public static void clean() {
		context = "";
		styleRanges = new Vector();
	}
	
	/**
	 * Lex and Parse the context file. The text and ranges objects
	 * will be completed from outside during the parsing operation.
	 */
	public static void make() {
		
		String ctx_file = FileInfos.getRoot() + File.separator +
							"why" + File.separator + FileInfos.getName() + "_ctx.why";
		try {
			whyLexer lex = new whyLexer(new FileReader(ctx_file));
			parser p = new parser(lex);
			Object o = p.parse().value;
			PrintVisitor visitor = new PrintVisitor();
			visitor.visit(o);
		} catch (Exception e) {
			TraceView.print(MessageType.ERROR, "Context.make() : " + e);
		}
	}


	/**
	 * Get the context text
	 * 
	 * @return String
	 */
	public static String getContext() {
		return context;
	}


	/**
	 * Set the context text
	 * 
	 * @param context
	 */
	public static void setContext(String context) {
		Context.context = context;
	}


	/**
	 * Get the style ranges
	 * 
	 * @return Vector
	 */
	public static Vector<StyleRange> getStyleRanges() {
		return styleRanges;
	}


	/**
	 * Set the style ranges
	 * 
	 * @param styleRanges
	 */
	public static void setStyleRanges(Vector styleRanges) {
		Context.styleRanges = styleRanges;
	}
}
