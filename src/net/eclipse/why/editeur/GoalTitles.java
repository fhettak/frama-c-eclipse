package net.eclipse.why.editeur;

/**
 * This class is holding the titles for POs
 * 
 * @author A. Oudot
 */
public class GoalTitles {
	
	/**
	 * Match board between kinds, complementary texts and goal titles
	 * 
	 * @see why/intf/stat.ml
	 */
	private static final String[][] tab = {
			/*  0 */ {"Pre",			"PointerDeref",		"pointer dereferencing"},
			/*  1 */ {"Pre",			"IndexBounds",		"index bounds"},
			/*  2 */ {"Pre",			"ArithOverflow",	"arithmetic overflow"},
			/*  3 */ {"Pre",			"AllocSize",		"allocation size nonnegative"},
			/*  4 */ {"Pre",			"UserCall",			"precondition for user call"},
			/*  5 */ {"Pre",			null,				"precondition"},
			/*  6 */ {"Pre",			" ", 				"unclassified precondition"},
			/*  7 */ {"Other",			" ",				"unclassified obligation"},
			/*  8 */ {"Absurd",			null,				"unreachable code"},
			/*  9 */ {"Assert",			null,				"assertion"},
			/* 10 */ {"Post",			null,				"postcondition"},
			/* 11 */ {"WfRel",			null,				"well-foundedness of relation"},
			/* 12 */ {"VarDecr",		null,				"variant decrease"},
			/* 13 */ {"LoopInvInit", 	" ",				"initialization"},
			/* 14 */ {"LoopInvPreserv",	" ",				"preservation"},
			/* 15 */ {"Lemma",	        null,				"lemma"}
	};
	
	
	/**
	 * Giving a kind of PO associated with a text, this function returns
	 * a title automatically generated, or null in inconsistent cases.
	 * 
	 * @param kind the kind of the PO
	 * @param text the PO's text
	 * @return the generated title
	 */
	public static String getTitle(String kind, String text) {
		
		String title = null;
		
		if(kind == null) return null;
		
		for(int u=0; u<tab.length; u++) {
			
			if(tab[u][0].equals(kind)) {
				
				if(tab[u][1] != null && tab[u][1].equals(" ")) {
					if(u == 6 || u == 7) {
						if(text != null && !text.trim().equals("")) {
							title = tab[u][2] + " `" + text + "'";
						} else {
							title = tab[u][2];
						}
					}
					if(u == 13 || u == 14) {
						if(text == null) title = tab[u][2] + " of loop invariant";
						else title = tab[u][2] + " of generated loop inv.: " + text;
					}
					break;
				} else if(tab[u][1] == null) {
					if(text == null) {
						title = tab[u][2];
						break;
					}
				} else {
					if(text != null && text.equals(tab[u][1])) {
						title = tab[u][2];
						break;
					}
				}
			}
		}
		
		return title;
	}
}
