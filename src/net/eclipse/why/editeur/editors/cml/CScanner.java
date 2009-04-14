package net.eclipse.why.editeur.editors.cml;

import java.util.ArrayList;
import java.util.List;

import net.eclipse.why.editeur.editors.MLTextAttributeProvider;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

public class CScanner extends RuleBasedScanner {

	private static final String[] keyWords = new String[] {
		"auto", "break", "case", "char", "const", "continue", "default", "do", "double", "else", "enum",
		"entry", "extern", "float", "for", "goto", "if", "int", "long", "register", "return", "short",
		"signed", "sizeof", "static", "struct", "switch", "typedef", "union", "unsigned", "void",
		"volatile", "while"
	};
	
	private static final String[] keyWords2 = new String[] {
		"#assert","#define", "#elif", "#else", "#endif", "#error", "#ident", "#if", "#ifdef",
		"#ifndef", "#import", "#include", "#include_next", "#line", "#pragma", "#sccs",
		"#unassert", "#undef", "#warning"
	};
	
	
	/**
	 * Détecteur de "mots".
	 */
	static class CWordDetector implements IWordDetector {
		
		public boolean isWordPart(char c) {
		    return Character.isJavaIdentifierPart(c);
		}
		
		public boolean isWordStart(char c) {
			return Character.isJavaIdentifierStart(c) ||
				   (c == '#');
		}
	}
	
	
	public CScanner(MLTextAttributeProvider provider) {
		
		IToken keyword 		= new Token(provider.getAttribute(MLTextAttributeProvider.C_KEYWORDS_ATTRIBUTE));
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
		for(int i = 0 ; i < keyWords2.length ; ++i) {
			wr.addWord(keyWords2[i], keyword);
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
