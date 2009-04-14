package net.eclipse.why.editeur.editors.jessie;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

public class JCPartitionScanner extends RuleBasedPartitionScanner {
	
	public final static String JC_COMMENT = "__jc_comment";
	
	
	public final static String[] JC_PARTITION_TYPES =
		new String[] {
			JC_COMMENT,
		};

	public JCPartitionScanner() {
		
		super();

		IToken jcComment = new Token(JC_COMMENT);
		
		IPredicateRule[] rules = new IPredicateRule[] {
				new MultiLineRule("/*", "*/", jcComment),
				new SingleLineRule("//", "\n", jcComment)
				//new MultiLineRule("\"", "\"", Token.UNDEFINED, '\0', false)
				//new SingleLineRule("'", "'", Token.UNDEFINED, '\0', false)
		};

		
		setPredicateRules(rules);
	}

}
