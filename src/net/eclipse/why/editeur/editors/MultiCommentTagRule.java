package net.eclipse.why.editeur.editors;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.Token;

public class MultiCommentTagRule extends MultiLineRule  {

	
	private IToken eToken;
	private IToken fToken;
	private int level = 0;
    
	
    public MultiCommentTagRule(IToken token1, IToken token2) {
    	super("/*@", "*/", token1);
    	eToken = token1;
        fToken = token2;
    }

	
    
    public IToken evaluate(ICharacterScanner scanner) {
    	if(isInComment(scanner)) {
    		level = 1;
    	}
        int c = scanner.read();
        int size = 1;
        if(c == '/') {
            c = scanner.read();
            ++size;
            if(c == '*') {
                c = scanner.read();
                ++size;
                if(c == '@') {
                	level = 1;
                	return eToken;
                }
            } else if(level > 0 && c == '/') {
            	while(c != '\n' && c != -1) {
            		c = scanner.read();
            		++size;
            	}
            	scanner.unread();
            	return fToken;
            }
        } else if(level > 0 && c == '*') {
        	c = scanner.read();
        	++size;
        	if(c == '/') {
        		level = 0;
        		scanner.read();
        		return eToken;
        	}
        }
        for( ; size > 0 ; --size) {
            scanner.unread();
        }
        return Token.UNDEFINED;
    }
    
    
    private boolean isInComment(ICharacterScanner scanner) {
    	int size = 0;
    	scanner.unread();
    	int c = scanner.read();
    	boolean l = false;
    	w: while(c != -1) {
    		if(c == '/') {
    			size++;
        		c = unread(scanner);
        		if(c == '*') {
        			l = false;
        			break w;
        		}
    		} else if(c == '@') { //64
    			size++;
        		c = unread(scanner);
    			if(c == '*') { //42
    				size++;
            		c = unread(scanner);
            		if(c == '/') { //47
            			size++;
                		c = unread(scanner);
            			if(c != '/') {
            				boolean stop = false;
            				while(!stop) {
            					if(c == '\n' || c == -1) {
            						l = true;
                        			break w;
            					}
            					else if(c == '/') {
            						size++;
                            		c = unread(scanner);
                            		if(c == '/') stop = true;
            					} else {
            						size++;
                            		c = unread(scanner);
            					}
            				}
            			}
            		}
    			}
    		} else {
    			size++;
    			c = unread(scanner);
    		}
    	}
    	for( ; size > 0 ; --size) {
            scanner.read();
        }
    	return l;
    }
    
    private int unread(ICharacterScanner scanner) {
    	try {
    		scanner.unread();
    		scanner.unread();
    		return scanner.read();
    	} catch(Exception e) {
    		return -1;
    	}
    }
}