package net.eclipse.why.editeur.editors.jessie;

import java.util.ArrayList;
import java.util.List;

import net.eclipse.why.editeur.editors.FloatingPointNumberRule;
import net.eclipse.why.editeur.editors.StringRule;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.NumberRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

public class JCScanner extends RuleBasedScanner {
	
	private static final String[] fgOpcodes = new String[] {
		"as", "boolean", "break", "case",
		"default", "catch", "continue", "do", /*"double"*/ "else", "end", /*"enum"*/ "exception",
		"false", "finally", /*"float"*/ "for", "free", "goto", "if", "in", "integer",
		"let", "new", "null", "of", "real", "rep", "return", "switch",
		"throw", "true", "try", "unit", "while"
	};
	
	private static final String[] fgOpcodes2 = new String[] {
		"\\at", "\\bottom", "\\forall", "\\exists", "\\mutable", "\\nothing", "\\offset_max",
		"\\offset_min", "\\old", "\\result", "\\typeeq", "\\typeof"
	};
	
	private static final String[] fgOpcodes3 = new String[] {
		"assigns", "assumes", "behavior", "ensures", "invariant",
		"reads", "requires", "throws", "variant"
	};
	
	private static final String[] fgOpcodes4 = new String[] {
		"and", "axiom", "lemma", "logic", "match", "tag", "type", "with"
	};
	
	private static final String[] fgOpcodes5 = new String[] {
		"assert", "pack", "unpack"
	};
	

	/**
	 * Détecteur de "mots".
	 */
	static class JCWordDetector implements IWordDetector {
		/**
		 * Indique si le caractère passé en paramètre est valide à
		 * l'intérieur d'un mot.
		 * @param c Caractère à analyser.
		 * @return 	<code>true</code> si le caractére est valide.
		 * 			<code>false</code> sinon.
		 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
		 */
		public boolean isWordPart(char c) {
		    return (Character.isLetterOrDigit(c) ||
		            c == '_');
		}
		
		/**
		 * Indique si le caractère passé en paramètre est valide au
		 * début d'un mot.
		 * @param c Caractère à analyser.
		 * @return 	<code>true</code> si le caractère est valide.
		 * 			<code>false</code> sinon.
		 * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
		 */
		public boolean isWordStart(char c) {
			return (Character.isLetter(c) ||
			        c == '_') ||
			        c == '\\';
		}
	}
	
	
	/**
	 * Constructeur. Crée les règles utilisées pour la coloration
	 * syntaxique.
	 * @param provider Classe fournissant les attributs de texte.
	 */
	public JCScanner(JCTextAttributeProvider provider) {
		List<IRule> rules = new ArrayList<IRule>();
		
		IToken opcode 		= new Token(provider.getAttribute(JCTextAttributeProvider.OPCODE_ATTRIBUTE));
		IToken opcode2 		= new Token(provider.getAttribute(JCTextAttributeProvider.OPCODE2_ATTRIBUTE));
		IToken opcode3 		= new Token(provider.getAttribute(JCTextAttributeProvider.OPCODE3_ATTRIBUTE));
		IToken opcode4 		= new Token(provider.getAttribute(JCTextAttributeProvider.OPCODE4_ATTRIBUTE));
		IToken opcode5 		= new Token(provider.getAttribute(JCTextAttributeProvider.OPCODE5_ATTRIBUTE));
		IToken undefined 	= new Token(provider.getAttribute(JCTextAttributeProvider.DEFAULT_ATTRIBUTE));
		IToken decNumber 	= new Token(provider.getAttribute(JCTextAttributeProvider.DECIMAL_NUMBER_ATTRIBUTE));
		IToken floatNumber 	= new Token(provider.getAttribute(JCTextAttributeProvider.FLOAT_NUMBER_ATTRIBUTE));
		IToken string 		= new Token(provider.getAttribute(JCTextAttributeProvider.STRING_ATTRIBUTE));
		

		//rules.add(new MultiLineRule("\"", "\"", string, '\0', true));
		rules.add(new WhitespaceRule(new IWhitespaceDetector() {
            public boolean isWhitespace(char c) {
                return Character.isWhitespace(c);
            }
		}));
		
		
		WordRule wr = new WordRule(new JCWordDetector(), undefined);
		
		for(int i = 0 ; i < fgOpcodes.length ; ++i) {
			wr.addWord(fgOpcodes[i], opcode);
			//wr.addWord(fgOpcodes[i].toUpperCase(), opcode);
		}
		
		for(int j = 0 ; j < fgOpcodes2.length ; ++j) {
			wr.addWord(fgOpcodes2[j], opcode2);
			//wr.addWord(fgOpcodes2[j].toUpperCase(), opcode2);
		}
		
		for(int k = 0 ; k < fgOpcodes3.length ; ++k) {
			wr.addWord(fgOpcodes3[k], opcode3);
			//wr.addWord(fgOpcodes3[k].toUpperCase(), opcode3);
		}
		
		for(int l = 0 ; l < fgOpcodes4.length ; ++l) {
			wr.addWord(fgOpcodes4[l], opcode4);
			//wr.addWord(fgOpcodes4[l].toUpperCase(), opcode4);
		}
		
		for(int m = 0 ; m < fgOpcodes5.length ; ++m) {
			wr.addWord(fgOpcodes5[m], opcode5);
			//wr.addWord(fgOpcodes5[m].toUpperCase(), opcode5);
		}
		
		rules.add(wr);
		
		rules.add(new NumberRule(decNumber));
		rules.add(new FloatingPointNumberRule(floatNumber));
		rules.add(new StringRule(string));
		
		
		IRule[] param = new IRule[rules.size()];
		rules.toArray(param);
		setRules(param);
	}
}
