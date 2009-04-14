package net.eclipse.why.editeur.editors.why;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;


public class SymbolsTypeRule implements IRule {

	private IToken sToken;
	
//	private static final String[] chars = new String[] {
//		"-", ":", "=", "<", ">", "|", "&", "@"
//	};
    
	
    public SymbolsTypeRule(IToken token) {
        sToken = token;
    }

	
    public IToken evaluate(ICharacterScanner scanner) {
        
    	int c = scanner.read();
        int size = 1;
        
        if(c == ':' ||
           c == '=' ||
           c == '|' ||
           c == '&' ||
           c == '@' )
        {
            c = scanner.read();
            ++size;
            scanner.unread();
            return sToken;
            
        } else if(c == '<') // <
        {
        	c = scanner.read();
            ++size;
            if(c == '-') { // <-
            	c = scanner.read();
                ++size;
                scanner.unread();
                return sToken;
            }
            scanner.unread();
            return sToken;
            
        } else if(c == '>') // >
        {
        	c = scanner.read();
            ++size;
            scanner.unread();
            return sToken;
            
        } else if(c == '-')
        {
        	c = scanner.read();
            ++size;
            if(c == '>') { // ->
            	c = scanner.read();
                ++size;
            	scanner.unread();
            	return sToken;
            }
        }

        for( ; size > 0 ; --size) {
            scanner.unread();
        }
        return Token.UNDEFINED;
    }

}
