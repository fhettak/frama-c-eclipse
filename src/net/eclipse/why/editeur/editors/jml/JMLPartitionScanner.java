package net.eclipse.why.editeur.editors.jml;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

public class JMLPartitionScanner extends RuleBasedPartitionScanner implements IJMLPartitions {
	

	public JMLPartitionScanner() {
		
		super();

		IToken jmlComment = new Token(JML_COMMENT);
		IToken startTag1 = new Token(JML_START_TAG1);
		IToken startTag2 = new Token(JML_START_TAG2);
		
		IPredicateRule[] rules = new IPredicateRule[] {
				//new MultiAnnotationLinedZoneTagRule(startTag1),
				new MultiLineRule("/*@", "*/", startTag1),
				new SingleLineRule("//@", "\n", startTag2),
				new MultiLineRule("/*", "*/", jmlComment),
				new SingleLineRule("//", "\n", jmlComment),
		};
		
		setPredicateRules(rules);
	}
}
