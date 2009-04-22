package net.eclipse.why.editeur;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;


/**
 * A container for the text of goal selected in
 * the Prover View and pretty printed in the
 * PO Viewer
 * 
 * @author A. Oudot
 */
public class WhyElement {

	
	private static String IDENT = ""; //indentation
	private static String content = ""; //goal text
	private static Vector<StyleRange> styleRanges = new Vector<StyleRange>(); //styles set
	private static StyleRange style; //current style
	private static int chars = 0; //text length (used to set style ranges)
	public static boolean is_goal = false; //goal(true) or context(false) file
	public static boolean and = true;
	
	
	/**
	 * Initialization function
	 */
	public static void clean() {
		content = "";
		styleRanges = new Vector<StyleRange>();
		style = null;
		chars = 0;
		is_goal = false;
	}
	
	/**
	 * Add a string at the end of the goal text
	 * 
	 * @param s the text to add
	 */
	public static void add(String s) {
		content = content + s;
		chars += s.length();
	}
	
	
	/**
	 * Add a newline
	 */
	public static void newLine() {
		
		boolean v = true;
		String sr = content.substring(content.lastIndexOf("\n") +1);
		//the last character of the text is a newline?
		if(sr.trim().equals(""))
			v = false;
		
		//if not, add a '\n' character
		if(v) {
			content += "\n";
			chars += 1;
			add(IDENT);
		}
	}
	
	
	/**
	 * Put an indentation of 4 whitespaces for next lines added to the text
	 */
	public static void indent() {
		if(is_goal) IDENT = "    ";
	}
	
	
	/**
	 * Unset the identation
	 */
	public static void unindent() {
		IDENT = "";
	}
	
	
	/**
	 * Increments the indentation characters of 1 whitespace
	 */
	public static void indent_incr() {
		IDENT += " "; 
	}
	
	
	/**
	 * Decrements the indentation characters of 1 whitespace
	 */
	public static void indent_decr() {
		int x = IDENT.length();
		unindent();
		for(int i=1; i<x; i++) {
			indent_incr();
		}
	}
	
	
	/**
	 * Indentation length getter
	 * 
	 * @return the number of the current indentation whitespaces
	 */
	public static int getIndent() {
		return IDENT.length();
	}
	
	
	/**
	 * Add a white line
	 */
	public static void whiteLine() {
		if(content.endsWith("\n")) { //the text ends with a newline
			if(!content.endsWith("\n\n")) { //but not with a whiteline
				//add another newline
				content += "\n";
				chars += 1;
			}
		} else { //the text doesn't ends with a newline
			//add two newlines = one whiteline
			content += "\n\n";
			chars += 2;
		}
	}
	
	
	/**
	 * Set the "goal" mode for the recording text.
	 * It implies another text forms
	 */
	public static void setGoalMode() {
		is_goal = true;
		style = new StyleRange();
		style.background = new Color(null, 0, 0, 0);
		style.foreground = new Color(null, 255, 255, 255);
		style.fontStyle = SWT.BOLD;
		style.start = chars;
	}
	
	
	/**
	 * Set the "hypothesis" mode : put if necessary
	 * a white 'h:' text with a red background
	 */
	public static void setHMode() {
		recordStyleRange();
		if(is_goal) {
			style = new StyleRange();
			style.foreground = new Color(null, 255, 255, 255);
			style.background = new Color(null, 200, 0, 30);
			chars = content.length();
			style.start = chars;
			style.length = 2;
			styleRanges.add(style);
			add("h: ");
		}
		style = new StyleRange();
		style.foreground = new Color(null, 0, 0, 190);
		style.start = chars;
	}
	
	
	/**
	 * Set the "expression" mode creating the particular style ranges and text forms
	 */
	public static void setExprMode() {
		recordStyleRange();
		style = new StyleRange();
		style.foreground = new Color(null, 0, 0, 190);
		style.start = chars;
	}
	
	
	/**
	 * Set the "keyword" mode creating the particular style ranges and text forms
	 */
	public static void setKeywordMode() {
		recordStyleRange();
		style = new StyleRange();
		style.foreground = new Color(null, 0, 140, 70);
		style.fontStyle = SWT.BOLD;
		style.start = chars;
	}
	
	
	/**
	 * Set the "forall" mode creating the particular style ranges and text forms
	 */
	public static void setForallMode() {
		recordStyleRange();
		style = new StyleRange();
		style.foreground = new Color(null, 199, 21, 133);
		style.fontStyle = SWT.ITALIC;
		style.start = chars;
	}
	
	
	/**
	 * Set the "binder" mode creating the particular style ranges and text forms
	 */
	public static void setBinderMode() {
		recordStyleRange();
		style = new StyleRange();
		style.foreground = new Color(null, 0, 0, 0);
		style.fontStyle = SWT.ITALIC;
		style.start = chars;
	}
	
	
	/**
	 * Set the "logic" mode creating the particular style ranges and text forms
	 */
	public static void setLogicMode() {
		unindent();
		recordStyleRange();
		style = new StyleRange();
		style.foreground = new Color(null, 0, 0, 190);
		style.fontStyle = SWT.BOLD;
		style.start = chars;
	}
	
	
	/**
	 * Set the "axiom" mode creating the particular style ranges and text forms
	 */
	public static void setAxiomMode() {
		unindent();
		recordStyleRange();
		style = new StyleRange();
		style.foreground = new Color(null, 0, 0, 0);
		style.fontStyle = SWT.BOLD;
		style.start = chars;
	}
	
	
	/**
	 * Set the "function" mode creating the particular style ranges and text forms
	 */
	public static void setFunctionMode() {
		unindent();
		recordStyleRange();
		style = new StyleRange();
		style.foreground = new Color(null, 255, 90, 0);
		style.fontStyle = SWT.BOLD;
		style.start = chars;
	}
	
	
	/**
	 * Set the "predicate" mode creating the particular style ranges and text forms
	 */
	public static void setPredicateMode() {
		unindent();
		recordStyleRange();
		style = new StyleRange();
		style.foreground = new Color(null, 170, 0, 130);
		style.fontStyle = SWT.BOLD;
		style.start = chars;
	}
	
	
	/**
	 * Set the "type" mode creating the particular style ranges and text forms
	 */
	public static void setTypeMode() {
		unindent();
		recordStyleRange();
		style = new StyleRange();
		style.foreground = new Color(null, 0, 100, 0);
		style.fontStyle = SWT.BOLD;
		style.start = chars;
	}
	
	
	/**
	 * Set the "parameter" mode creating the particular style ranges and text forms
	 */
	public static void setParameterMode() {
		unindent();
		recordStyleRange();
		style = new StyleRange();
		style.foreground = new Color(null, 225, 200, 50);
		style.fontStyle = SWT.BOLD;
		style.start = chars;
	}
	
	
	/**
	 * Set the "trigger" mode creating the particular style ranges and text forms
	 */
	public static void setTriggerMode() {
		recordStyleRange();
		style = new StyleRange();
		style.foreground = new Color(null, 130, 130, 0);
		style.fontStyle = SWT.ITALIC;
		style.start = chars;
	}
	
	
	/**
	 * Set the "let" mode creating the particular style ranges and text forms
	 */
	public static void setLetMode() {
		unindent();
		recordStyleRange();
		style = new StyleRange();
		style.foreground = new Color(null, 90, 0, 150);
		style.fontStyle = SWT.BOLD;
		style.start = chars;
	}
	
	
	/**
	 * Set the "exception" mode creating the particular style ranges and text forms
	 */
	public static void setExceptionMode() {
		unindent();
		recordStyleRange();
		style = new StyleRange();
		style.foreground = new Color(null, 210, 0, 30);
		style.fontStyle = SWT.BOLD;
		style.start = chars;
	}
	
	
	/**
	 * Set the "normal" mode creating the particular style ranges and text forms
	 */
	public static void setTextMode() {
		recordStyleRange();
		style = new StyleRange();
		Color fgColor = new Color(null,0, 0, 0);
		style.foreground = fgColor;
		style.start = chars;
	}
	
	
	/**
	 * Record the current style range into the style ranges set :
	 * this function is used, when the text form is changing, to
	 * record the past style before creating the next style
	 */
	public static void recordStyleRange() {
		if(style != null) {
			int q = chars-style.start;
			if(q>0) {
				style.length = q;
				styleRanges.add(style);
			}
		}
	}
	
	
	/**
	 * Save the text with all style ranges as a Goal object,
	 * if the considered text comes from a goal file.
	 */
	public static void saveAsGoal() {
		Goal.setGoal(content);
		Goal.setStyleRanges(styleRanges);
	}
	
	
	/**
	 * Save the text with all style ranges as a Context object,
	 * if the considered text comes from a context file.
	 */
	public static void saveAsContext() {
		Context.setContext(content);
		Context.setStyleRanges(styleRanges);
	}
}
