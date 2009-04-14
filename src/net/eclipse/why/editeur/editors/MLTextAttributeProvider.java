package net.eclipse.why.editeur.editors;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class MLTextAttributeProvider {

	public static final String ML_OPCODE_ATTRIBUTE = "__pos_ml_opcode_attribute";
	public static final String ML_OPCODE2_ATTRIBUTE = "__pos_ml_opcode2_attribute";
	public static final String ML_ZONE_ATTRIBUTE = "__pos_ml_zone_attribute";
	public static final String ML_DEFAULT_ATTRIBUTE = "__pos_ml_default_attribute";
	public static final String ML_COMMENT_ATTRIBUTE = "__pos_ml_comment_attribute";
	public static final String ML_INTRA_COMMENT_ATTRIBUTE = "__pos_ml_intra_comment_attribute";
	public static final String ML_STRING_ATTRIBUTE = "__pos_ml_string_attribute";
	public static final String C_KEYWORDS_ATTRIBUTE = "__pos_c_keywords_attribute";
	public static final String JAVA_KEYWORDS_ATTRIBUTE = "__pos_java_keywords_attribute";
	
	
	private Map<String,TextAttribute> fAttributes = new HashMap<String,TextAttribute>();

	public MLTextAttributeProvider() {
		
		// Les opcode série I, en bleu foncé et en gras
		fAttributes.put(ML_OPCODE_ATTRIBUTE, new TextAttribute(
				new Color(Display.getCurrent(), new RGB(25, 25, 112)),
				null,
				SWT.BOLD
		));
		
		// Les opcodes série II, en gras aussi mais plus clair cette fois!
		fAttributes.put(ML_OPCODE2_ATTRIBUTE, new TextAttribute(
				new Color(Display.getCurrent(), new RGB(/*160, 11, 113*/0, 0, 255)),
				null,
				SWT.BOLD
		));
		
		// La zone d'annotation en bleu ciel
		fAttributes.put(ML_ZONE_ATTRIBUTE, new TextAttribute(
				new Color(Display.getCurrent(), new RGB(65, 105, 225))
		));
		
		// les comments en golden italique
		fAttributes.put(ML_COMMENT_ATTRIBUTE, new TextAttribute(
				new Color(Display.getCurrent(), new RGB(/*218, 165, 32*/ 237, 180, 16)),
				null,
				SWT.ITALIC
		));
		
		// les comments en orange italique
		fAttributes.put(ML_INTRA_COMMENT_ATTRIBUTE, new TextAttribute(
				new Color(Display.getCurrent(), new RGB(255, 69, 0)),
				null,
				SWT.ITALIC
		));
		
		// les string de couleur verte ... c'est la nouvelle mode pour la plage cet été!!!
		fAttributes.put(ML_STRING_ATTRIBUTE, new TextAttribute(
				new Color(Display.getCurrent(), new RGB(0, 150, 0))
		));
		
		//Les mots clés C, rouge violet foncé gras
		fAttributes.put(C_KEYWORDS_ATTRIBUTE, new TextAttribute(
				new Color(Display.getCurrent(), new RGB(/*120, 0, 30*/127, 0, 85)),
				null,
				SWT.BOLD
		));
		
		//Les mots clés JAVA, idem
		fAttributes.put(JAVA_KEYWORDS_ATTRIBUTE, new TextAttribute(
				new Color(Display.getCurrent(), new RGB(127, 0, 85)),
				null,
				SWT.BOLD
		));
		
		//Et par défaut en noir
		fAttributes.put(ML_DEFAULT_ATTRIBUTE, new TextAttribute(
				new Color(Display.getCurrent(),	new RGB(0, 0, 0))
		));
		
		
		
	}
	
	public TextAttribute getAttribute(String type) {
		TextAttribute attr = (TextAttribute)fAttributes.get(type);
		if(attr == null) {
			attr = (TextAttribute) fAttributes.get(ML_DEFAULT_ATTRIBUTE);
		}
		return attr;
	}
	
}
