package net.eclipse.why.editeur.editors.jml;

import java.util.ArrayList;
import java.util.List;

import net.eclipse.why.editeur.editors.MLTextAttributeProvider;
import net.eclipse.why.editeur.editors.MultiCommentTagRule;
import net.eclipse.why.editeur.editors.SingleCommentTagRule;

import org.eclipse.jface.text.rules.ICharacterScanner;
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

public class JMLScanner extends RuleBasedScanner {
	
	private static final String[] fgOpcodes = new String[] {
		  "abstract", "assert", "assigns", "axiom", "behavior", "boolean", "break", "byte", "byvalue",
		  "case", "cast", "catch", "char", "class", "const", "continue", "decreases", "default", "do",
		  "double", "else", "ensures", "extends", "false", "final", "finally", "float", "for", "future",
		  "generic", "ghost", "goto", "if", "implements", "import", "inner", "instanceof", "int",
		  "integer", "interface", "invariant", "lemma", "logic", "long", "loop_invariant", "model", "native",
		  "new", "non_null", "null", "nullable", "operator", "outer", "package", "predicate", "private",
		  "protected", "public", "reads", "real", "requires", "rest", "return", "short", "signals",
		  "static", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true",
		  "try", "var", "void", "volatile", "while"
	};
	
	private static final String[] fgOpcodes2 = new String[] {
		"\\at", "\\exists", "\\forall", "\\nothing", "\\old", "\\result"
	};
	

	/**
	 * Détecteur de "mots".
	 */
	static class JMLWordDetector implements IWordDetector {
		/**
		 * Indique si le caractère passé en paramètre est valide à
		 * l'intérieur d'un mot.
		 * @param c Caractère à analyser.
		 * @return 	<code>true</code> si le caractére est valide.
		 * 			<code>false</code> sinon.
		 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
		 */
		public boolean isWordPart(char c) {
			//boolean b = Character.isLetterOrDigit(c) || (c == '_') || (c == '/');
			boolean b = Character.isLetterOrDigit(c)	||
							(c == '_');
			
			return b;
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
			//boolean b = Character.isDefined(c) && !Character.isWhitespace(c) && !(c == '"') && !(c == '\'') && !(c == '/');
			boolean b = Character.isDefined(c)		&&
						!Character.isWhitespace(c)	&&
						!(c == '"')					&&
						!(c == '\'')				&&
						!(c == '/')					&&
						!(c == '*');
			return (b);
		}
	}
	
	
	/**
	 * Constructeur. Crée les règles utilisées pour la coloration
	 * syntaxique.
	 * @param provider Classe fournissant les attributs de texte.
	 */
	public JMLScanner(MLTextAttributeProvider provider) {
		
		List<IRule> rules = new ArrayList<IRule>();		// Contiendra les règles
		
		/*
		 * Création des tokens. Chaque token contient l'attribut
		 * à appliquer au texte correspondant.
		 */
		final IToken opcode 		= new Token(provider.getAttribute(MLTextAttributeProvider.ML_OPCODE_ATTRIBUTE));
		final IToken opcode2 		= new Token(provider.getAttribute(MLTextAttributeProvider.ML_OPCODE2_ATTRIBUTE));
		final IToken zone			= new Token(provider.getAttribute(MLTextAttributeProvider.ML_ZONE_ATTRIBUTE));
		final IToken icomment		= new Token(provider.getAttribute(MLTextAttributeProvider.ML_INTRA_COMMENT_ATTRIBUTE));
		final IToken string		= new Token(provider.getAttribute(MLTextAttributeProvider.ML_STRING_ATTRIBUTE));
		

		IWhitespaceDetector wspace = new IWhitespaceDetector() {
		    public boolean isWhitespace(char c) {
                return Character.isWhitespace(c)	||
                	   (c == '(')					||
                	   (c == ')')					||
                	   (c == ',')					||
                	   (c == '[')					||
                	   (c == ']')					||
                	   (c == '{')					||
                	   (c == '}')					||
                	   (c == ';')					||
                	   (c == ':');
            }
		};
		
		WhitespaceRule srule = new WhitespaceRule(wspace) {
			public IToken evaluate(ICharacterScanner scanner) {
				int c= scanner.read();
				if (fDetector.isWhitespace((char) c)) {
					do {
						c= scanner.read();
					} while (fDetector.isWhitespace((char) c));
					scanner.unread();
					return zone;
				}

				scanner.unread();
				return Token.UNDEFINED;
			}
		};
		
		rules.add(srule);
		
		
		/*
		 * Création de la règle pour les mots clés. En deux temps :
		 * Tout d'abord on paramètre un détecteur qui va indiquer les
		 * caractères valides pour le début et le contenu d'un mot,
		 * puis on ajoute une liste de mots en indiquant quel token doit
		 * être renvoyé pour chacun.
		 */
		// Si le mot n'est pas dans la liste, renvoie zone
		WordRule wr = new WordRule(new JMLWordDetector(), zone);
		
		// Ajout des opcodes
		for(int i = 0 ; i < fgOpcodes.length ; ++i) {
			wr.addWord(fgOpcodes[i], opcode);
		}
		
		for(int i = 0 ; i < fgOpcodes2.length ; ++i) {
			wr.addWord(fgOpcodes2[i], opcode2);
		}
		
		rules.add(wr);
		
		
		MultiLineRule multis = new MultiLineRule("\"", "\"", string, '\\');
		SingleLineRule sins = new SingleLineRule("'", "'", string, '\\');
		rules.add(multis);
		rules.add(sins);
		
		MultiCommentTagRule multic = new MultiCommentTagRule(zone,icomment);
		SingleCommentTagRule sinc = new SingleCommentTagRule(zone,icomment);
		rules.add(multic);
		rules.add(sinc);
		
		// Conversion de la List en tableau pour la passer à la méthode setRules
		IRule[] param = new IRule[rules.size()];
		rules.toArray(param);
		setRules(param);
	}
}
