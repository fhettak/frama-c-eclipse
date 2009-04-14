package net.eclipse.why.editeur.editors.jml;

import java.util.ArrayList;
import java.util.List;

import net.eclipse.why.editeur.editors.MLTextAttributeProvider;

import org.eclipse.cdt.internal.ui.text.util.CWordDetector;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

public class JScanner extends RuleBasedScanner {

	private static final String[] keyWords = new String[] {
		"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
		"continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
		"for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long",
		"native", "new", "package", "private", "protected", "public", "return", "short",
		"static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws",
		"transient", "try", "void", "volatile", "while"
	};
	
	
	/*
	 * Détecteur de "mots".
	 *
	static class JWordDetector implements IWordDetector {
		
		public boolean isWordPart(char c) {
		    //return (Character.isLetterOrDigit(c) || c == '_');
			return (Character.isLetterOrDigit(c) || c == '_')  || (c == '/');
		}
		
		public boolean isWordStart(char c) {
			//return (Character.isLetter(c) || c == '_') || c == '\\';
			return (Character.isDefined(c) && !Character.isWhitespace(c) && !(c == '"') && !(c == '\''));
		}
	}/**/
	
	
	public JScanner(MLTextAttributeProvider provider) {
		
		IToken keyword 		= new Token(provider.getAttribute(MLTextAttributeProvider.JAVA_KEYWORDS_ATTRIBUTE));
		IToken string		= new Token(provider.getAttribute(MLTextAttributeProvider.ML_STRING_ATTRIBUTE));
		IToken def			= new Token(provider.getAttribute(MLTextAttributeProvider.ML_DEFAULT_ATTRIBUTE));
		
		List<IRule> rules = new ArrayList<IRule>();
		
		rules.add(new WhitespaceRule(new IWhitespaceDetector() {
            public boolean isWhitespace(char c) {
                return Character.isWhitespace(c);
            }
		}));
		
		WordRule wr = new WordRule(new CWordDetector(), def);
		for(int i = 0 ; i < keyWords.length ; ++i) {
			wr.addWord(keyWords[i], keyword);
		}
		rules.add(wr);
		
		MultiLineRule multis = new MultiLineRule("\"", "\"", string, '\\');
		SingleLineRule sins = new SingleLineRule("'", "'", string, '\\');
		rules.add(multis);
		rules.add(sins);
		
		
		IRule[] param = new IRule[rules.size()];
		rules.toArray(param);
		setRules(param);
	}
}
