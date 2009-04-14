package net.eclipse.why.editeur.editors.cml;

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

public class CMLScanner extends RuleBasedScanner {

	private static final String[] fgOpcodes = new String[] {
		"if", "then", "else", "data", "invariant", "variant", "decreases", "for", "assert",
		"assume", "label", "requires", "ensures", "assigns", "loop_assigns", "reads", "logic",
		"predicate", "axiom", "int", "integer", "float", "void", "char", "signed", "unsigned",
		"short", "long", "double", "real", "struct", "enum", "union", "ghost", "set", "type"
	};
	
	private static final String[] fgOpcodes2 = new String[] {
		"\\forall", "\\exists", "\\true", "\\false", "\\old", "\\at", "\\result", "\\base_addr",
		"\\offset", "\\block_length", "\\arrlen", "\\strlen", "\\min", "\\max", "\\min_range",
		"\\max_range", "\\valid", "\\separated", "\\bound_separated", "\\full_separated",
		"\\fullseparated", "\\fresh", "\\valid_index", "\\valid_range", "\\nothing", "\\null",
		"\\abs", "\\sqrt", "\\round_error", "\\total_error", "\\exact", "\\model"
	};
	
	/*
	static class CMLWordRule extends WordRule {
		
		protected IWordDetector fDetector;
		protected IToken fDefaultToken;
		private StringBuffer fBuffer= new StringBuffer();
		
		public CMLWordRule(IWordDetector detector) {
			super(detector);
		}
		
		public CMLWordRule(IWordDetector detector, IToken defaultToken) {
			super(detector, defaultToken);
			fDetector = detector;
			fDefaultToken = defaultToken;
		}
		
		public IToken evaluate(ICharacterScanner scanner) {
			
			int c= scanner.read();
			if (fDetector.isWordStart((char) c)) {
				if (fColumn == UNDEFINED || (fColumn == scanner.getColumn() - 1)) {

					fBuffer.setLength(0);
					do {
						fBuffer.append((char) c);
						c= scanner.read();
					} while (c != ICharacterScanner.EOF && fDetector.isWordPart((char) c));
					scanner.unread();

					IToken token= (IToken) fWords.get(fBuffer.toString());
					if (token != null)
						return token;

					if (fDefaultToken.isUndefined())
						unreadBuffer(scanner);

					return fDefaultToken;
				}
			}

			scanner.unread();
			return Token.UNDEFINED;
			
		}
	}
	/**/
	
	/**
	 * Word detector for ML language
	 */
	static class CMLWordDetector implements IWordDetector {
		
		/**
		 * Indique si le caractère passé en paramètre est valide à
		 * l'intérieur d'un mot.
		 * @param c Caractère à analyser.
		 * @return 	<code>true</code> si le caractére est valide.
		 * 			<code>false</code> sinon.
		 * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
		 */
		public boolean isWordPart(char c) {
			
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
			
			boolean b = Character.isDefined(c)		&&
						!Character.isWhitespace(c)	&&
						!(c == '"')					&&
						!(c == '\'')				&&
						!(c == '/')					&&
						!(c == '*');

			return b;
		}
	}
	
	
	/**
	 * Constructeur. Crée les règles utilisées pour la coloration
	 * syntaxique.
	 * @param provider Classe fournissant les attributs de texte.
	 */
	public CMLScanner(MLTextAttributeProvider provider) {
		
		List<IRule> rules = new ArrayList<IRule>();		// Contiendra les règles
		
		/*
		 * Création des tokens. Chaque token contient l'attribut
		 * à appliquer au texte correspondant.
		 */
		final IToken opcode 		= new Token(provider.getAttribute(MLTextAttributeProvider.ML_OPCODE_ATTRIBUTE));
		final IToken opcode2 		= new Token(provider.getAttribute(MLTextAttributeProvider.ML_OPCODE2_ATTRIBUTE));
		final IToken zone			= new Token(provider.getAttribute(MLTextAttributeProvider.ML_ZONE_ATTRIBUTE));
		final IToken icomment		= new Token(provider.getAttribute(MLTextAttributeProvider.ML_INTRA_COMMENT_ATTRIBUTE));
		final IToken string			= new Token(provider.getAttribute(MLTextAttributeProvider.ML_STRING_ATTRIBUTE));
		

		
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
		WordRule wrule = new WordRule(new CMLWordDetector(), zone);
		
//		WordRule wrule = new WordRule(new CMLWordDetector(), zone) {
//			
//			/** Buffer used for pattern detection */
//			private StringBuffer fBuffer= new StringBuffer();
//			
//			/*
//			 * @see IRule#evaluate(ICharacterScanner)
//			 */
//			public IToken evaluate(ICharacterScanner scanner) {
//				int c= scanner.read();
//				if (fDetector.isWordStart((char) c)) {
//					if (fColumn == UNDEFINED || (fColumn == scanner.getColumn() - 1)) {
//
//						fBuffer.setLength(0);
//						boolean no_more_one_slash = true;
//						do {
//							if(c == '/') {
//								int d = scanner.read();
//								if(d == '/') {
//									no_more_one_slash = false;
//								}
//								scanner.unread();
//							}
//							fBuffer.append((char) c);
//							c= scanner.read();
//						} while (c != ICharacterScanner.EOF && fDetector.isWordPart((char) c));
//						scanner.unread();
//
//						IToken token= (IToken) fWords.get(fBuffer.toString());
//						if (token != null)
//							return token;
//
//						if (fDefaultToken.isUndefined())
//							unreadBuffer(scanner);
//
//						return fDefaultToken;
//					}
//				}
//
//				scanner.unread();
//				return Token.UNDEFINED;
//			}
//			
//			/**
//			 * Returns the characters in the buffer to the scanner.
//			 *
//			 * @param scanner the scanner to be used
//			 */
//			protected void unreadBuffer(ICharacterScanner scanner) {
//				for (int i= fBuffer.length() - 1; i >= 0; i--)
//					scanner.unread();
//			}
//		};
		
		// Ajout des opcodes
		for(int i = 0 ; i < fgOpcodes.length ; ++i) {
			wrule.addWord(fgOpcodes[i], opcode);
		}
		
		for(int i = 0 ; i < fgOpcodes2.length ; ++i) {
			wrule.addWord(fgOpcodes2[i], opcode2);
		}
		
		rules.add(wrule);
		
		
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
