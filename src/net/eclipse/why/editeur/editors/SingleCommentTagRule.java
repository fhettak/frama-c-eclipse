package net.eclipse.why.editeur.editors;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

public class SingleCommentTagRule extends SingleLineRule  {

	
	private IToken eToken;
	private IToken fToken;
	private int level = 0;
    
	
    public SingleCommentTagRule(IToken token1, IToken token2) {
    	super("//@", "\n", token1);
        eToken = token1;
        fToken = token2;
    }
    
	
    
    public IToken evaluate(ICharacterScanner scanner) {
        int c = scanner.read();
        int size = 1;
        if(c == '/') {
            c = scanner.read();
            ++size;
            if(c == '/') {
                c = scanner.read();
                ++size;
                if(level > 0 || c != '@') {
                	while(c != '\n' && c != -1) {
                		c = scanner.read();
                        ++size;
                    }
                	scanner.unread();
                	level = 0;
                    return fToken;
                } else {
                	level ++;
                	return eToken;
                }
            }
        }
        
        if(c == '\n' || c == -1) {
        	level = 0;
        }
        
        for( ; size > 1 ; --size) {
            scanner.unread();
        }
        return Token.UNDEFINED;
    }
}