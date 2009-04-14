package net.eclipse.why.editeur.editors.why;

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

public class WHYScanner extends RuleBasedScanner {
	
	private static final String[] fgOpcodes = new String[] {
		"absurd", "and", "array", "as", "assert", "axiom", "begin",
		"bool", "do", "done", "else", "end", "exception", "exists",
		"external", "false", "for", "forall", "fun", "function", "goal",
		"if", "in", "int", "invariant", "let", "logic", "not", "of",
		"or", "parameter", "predicate", "prop", "raise", "raises",
		"reads", "real", "rec", "ref", "returns", "then", "true",
		"try", "type", "unit", "variant", "void", "while", "with", "writes"
	};
	
	
//	public WHYScanner(ColorManager manager) {
//		IToken procInstr =
//			new Token(
//				new TextAttribute(
//					manager.getColor(IWHYColorConstants.PROC_INSTR)));
//
//		IRule[] rules = new IRule[2];
//		//Add rule for processing instructions
//		rules[0] = new SingleLineRule("/*@", "@*/", procInstr);
//		// Add generic whitespace rule.
//		rules[1] = new WhitespaceRule(new WHYWhitespaceDetector());
//
//		setRules(rules);
//	}
	
	
	
	/**
	 * Détecteur de "mots".
	 */
	static class WHYWordDetector implements IWordDetector {
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
		            //c == '?' ||
		            //c == '$' ||
		            //c == '#' ||
		            //c == '@' ||
		            //c == '~' ||
		            //c == '.' ||
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
			        //c == '.' ||
			        //c == '?' ||
			        //c == '$' ||
					c == '_');
		}
	}
	
	
	/**
	 * Constructeur. Crée les règles utilisées pour la coloration
	 * syntaxique.
	 * @param provider Classe fournissant les attributs de texte.
	 */
	public WHYScanner(WHYTextAttributeProvider provider) {
		List<IRule> rules = new ArrayList<IRule>();		// Contiendra les règles
		
		/*
		 * Création des tokens. Chaque token contient l'attribut
		 * à appliquer au texte correspondant.
		 */
		IToken opcode 		= new Token(provider.getAttribute(WHYTextAttributeProvider.WHY_OPCODE_ATTRIBUTE));
		IToken symbol 		= new Token(provider.getAttribute(WHYTextAttributeProvider.WHY_SYMBOLS_ATTRIBUTE));
		IToken undefined 	= new Token(provider.getAttribute(WHYTextAttributeProvider.WHY_DEFAULT_ATTRIBUTE));
		IToken decNumber 	= new Token(provider.getAttribute(WHYTextAttributeProvider.WHY_DECIMAL_NUMBER_ATTRIBUTE));
		IToken floatNumber 	= new Token(provider.getAttribute(WHYTextAttributeProvider.WHY_FLOAT_NUMBER_ATTRIBUTE));
		IToken string 		= new Token(provider.getAttribute(WHYTextAttributeProvider.WHY_STRING_ATTRIBUTE));
		

		
		// Régles pour les chaînes
		//rules.add(new MultiLineRule("\"", "\"", string, '\0', true));
		//rules.add(new SingleLineRule("'", "'", string, '\0', true));
		// Régle générique pour les espaces
		rules.add(new WhitespaceRule(new IWhitespaceDetector() {
		    /**
		     * Indique si le caractère passé en paramètre est un espace.
             * @param c Caractère à analyser.
             * @return <code>true</code> si le caractère doit être considéré comme un
             * espace, <code>false</code> sinon.
             * @see org.eclipse.jface.text.rules.IWhitespaceDetector#isWhitespace(char)
             */
            public boolean isWhitespace(char c) {
                return Character.isWhitespace(c);
            }
		}));
		
		
		/*
		 * Création de la règle pour les mots clés. En deux temps :
		 * 	Tout d'abord on paramètre un détecteur qui va indiquer les
		 * caractères valides pour le début et le contenu d'un mot,
		 * puis on ajoute une liste de mots en indiquant quel token doit
		 * être renvoyé pour chacun.
		 */
		// Si le mot n'est pas dans la liste, renvoie undefined
		WordRule wr = new WordRule(new WHYWordDetector(), undefined);
		
		// Ajout des opcodes
		for(int i = 0 ; i < fgOpcodes.length ; ++i) {
			wr.addWord(fgOpcodes[i], opcode);
			// Cas des majuscules
			//wr.addWord(fgOpcodes[i].toUpperCase(), opcode);
		}
		
		
		rules.add(wr);
		
		// Règle pour les nombres flottants et décimaux
		rules.add(new NumberRule(decNumber));
		rules.add(new FloatingPointNumberRule(floatNumber));
		rules.add(new SymbolsTypeRule(symbol));
		
		// Règle pour les chaînes de caractères
		rules.add(new StringRule(string));
		
		// Conversion de la List en tableau pour la passer à la méthode setRules
		IRule[] param = new IRule[rules.size()];
		rules.toArray(param);
		setRules(param);
	}
}
