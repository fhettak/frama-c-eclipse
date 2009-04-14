package net.eclipse.why.editeur.editors.why;

import org.eclipse.jface.text.rules.*;

public class WHYPartitionScanner extends RuleBasedPartitionScanner {
	
	//public final static String WHY_DEFAULT = "__why_default";
	//public final static String XML_TAG = "__xml_tag";
	//public final static String PROOF_FEATURE = "__proof_feature";
	public final static String WHY_COMMENT = "__why_comment";
	
	
	public final static String[] WHY_PARTITION_TYPES =
		new String[] {
			WHY_COMMENT,
		};

	public WHYPartitionScanner() {
		
		super();

		IToken whyComment = new Token(WHY_COMMENT);
		//IToken tag = new Token(XML_TAG);

		IPredicateRule[] rules = new IPredicateRule[] {
				new MultiLineRule("(*", "*)", whyComment),
				//new MultiLineRule("\"", "\"", Token.UNDEFINED, '\0', false)
				//new SingleLineRule("'", "'", Token.UNDEFINED, '\0', false)
		};

		
		//rules[1] = new TagRule(tag);
		
		//IToken proofFeature = new Token(PROOF_FEATURE);
		//rules[2] = new MultiLineRule(";;",":",proofFeature);


		setPredicateRules(rules);
	}
}
