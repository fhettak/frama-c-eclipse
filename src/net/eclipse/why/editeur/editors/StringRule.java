package net.eclipse.why.editeur.editors;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class StringRule implements IRule  {

	
	private IToken sToken;
    
	/**
	 * Constructeur.
	 * La régle renverra token si lors de l'analyse
	 * elle trouve une chaine de caractères. 
	 * @param token Token à renvoyer en cas de succés.
	 */
    public StringRule(IToken token) {
        sToken = token;
    }

	/**
	 * Détermine si l'on est en présence d'une chaine.
	 * Appelé par WHYScanner (via le RuleBasedScanner).
	 * @param scanner Scanner permettant de lire le texte.
	 * @return Le token passé au constructeur si la chaine est une chaine binaire.
	 * <code>Token.UNDEFINED</code> sinon ce qui permet de continuer l'analyse.
	 * @see org.eclipse.jface.text.rules.IRule#evaluate(org.eclipse.jface.text.rules.ICharacterScanner)
	 */
    public IToken evaluate(ICharacterScanner scanner) {
        
    	int c = scanner.read();
        int size = 1;
        boolean interpret = true;
        
        
        if( c == '"' ) {
            do {
            	if(c == '\\') {
            		interpret = !interpret;
            	} else {
            		interpret = true;
            	}
            	c = scanner.read();
            	++size;
            } while( (!interpret || !(c == '"')) && (c != -1) );
            
            return sToken;
        }
       
        
        for( ; size > 0 ; --size) {
            scanner.unread();
        }
        return Token.UNDEFINED;
    }
}
