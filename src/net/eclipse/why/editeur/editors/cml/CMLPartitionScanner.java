package net.eclipse.why.editeur.editors.cml;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

public class CMLPartitionScanner extends RuleBasedPartitionScanner implements ICMLPartitions {
	

	public CMLPartitionScanner() {
		
		super();

		IToken cmlComment = new Token(CML_COMMENT);
		IToken startTag1 = new Token(CML_START_TAG1);
		IToken startTag2 = new Token(CML_START_TAG2);
		
		IPredicateRule[] rules = new IPredicateRule[] {
				//new MultiAnnotationLinedZoneTagRule(startTag1),
				new MultiLineRule("/*@", "*/", startTag1),
				new SingleLineRule("//@", "\n", startTag2),
				new MultiLineRule("/*", "*/", cmlComment),
				new SingleLineRule("//", "\n", cmlComment),
		};
		
		setPredicateRules(rules);
	}
}
