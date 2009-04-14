package net.eclipse.why.editeur.editors.jessie;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class JCTextAttributeProvider {

	public static final String OPCODE_ATTRIBUTE = "__pos_jc_opcode_attribute";
	public static final String OPCODE2_ATTRIBUTE = "__pos_jc_opcode2_attribute";
	public static final String OPCODE3_ATTRIBUTE = "__pos_jc_opcode3_attribute";
	public static final String OPCODE4_ATTRIBUTE = "__pos_jc_opcode4_attribute";
	public static final String OPCODE5_ATTRIBUTE = "__pos_jc_opcode5_attribute";
	public static final String SYMBOLS_ATTRIBUTE = "__pos_jc_symbols_attribute";
	public static final String COMMENT_ATTRIBUTE = "__pos_jc_comment_attribute";
	public static final String DEFAULT_ATTRIBUTE = "__pos_jc_default_attribute";
	public static final String DECIMAL_NUMBER_ATTRIBUTE = "__pos_jc_decimal_number_attribute";
	public static final String FLOAT_NUMBER_ATTRIBUTE = "__pos_jc_float_number_attribute";
	public static final String STRING_ATTRIBUTE = "__pos_jc_string_attribute";
	
	
	private Map<String,TextAttribute> fAttributes = new HashMap<String,TextAttribute>();

	public JCTextAttributeProvider() {
		
		// Les opcode en gras
		fAttributes.put(OPCODE_ATTRIBUTE,
				new TextAttribute(new Color(Display.getCurrent(), 
									new RGB(180, 30, 70)), null, SWT.BOLD));
		
		fAttributes.put(OPCODE2_ATTRIBUTE,
				new TextAttribute(new Color(Display.getCurrent(), 
									new RGB(100, 100, 100)), null, SWT.ITALIC));
		
		fAttributes.put(OPCODE3_ATTRIBUTE,
				new TextAttribute(new Color(Display.getCurrent(), 
									new RGB(80, 0, 130)), null, SWT.BOLD));
		
		fAttributes.put(OPCODE4_ATTRIBUTE,
				new TextAttribute(new Color(Display.getCurrent(), 
									new RGB(128, 128, 0)), null, SWT.BOLD));
		
		fAttributes.put(OPCODE5_ATTRIBUTE,
				new TextAttribute(new Color(Display.getCurrent(), 
									new RGB(0, 0, 140)), null, SWT.BOLD));
		
		// Les symboles
		fAttributes.put(SYMBOLS_ATTRIBUTE,
				new TextAttribute(new Color(Display.getCurrent(), 
									new RGB(190, 125, 0)), null, SWT.BOLD));
		
		// Les commentaires en orange
		fAttributes.put(COMMENT_ATTRIBUTE,
				new TextAttribute(new Color(Display.getCurrent(), 
									new RGB(255, 125, 0)), null, SWT.ITALIC));
		// Par défaut en noir
		fAttributes.put(DEFAULT_ATTRIBUTE,
				new TextAttribute(new Color(Display.getCurrent(), 
									new RGB(0, 0, 0))));
		
		// Nombres décimaux en noir
		fAttributes.put(DECIMAL_NUMBER_ATTRIBUTE,
				new TextAttribute(new Color(Display.getCurrent(),
									new RGB(0, 0, 0)), null, SWT.BOLD));
		
		// Nombres flottants en noir également
		fAttributes.put(FLOAT_NUMBER_ATTRIBUTE,
				new TextAttribute(new Color(Display.getCurrent(),
									new RGB(0, 0, 0)), null, SWT.BOLD));
		
		// Chaines en bleu
		fAttributes.put(STRING_ATTRIBUTE, 
				new TextAttribute(new Color(Display.getCurrent(),
									new RGB(0, 0, 255)), null, SWT.ITALIC));
		
	}
	
	public TextAttribute getAttribute(String type) {
		TextAttribute attr = (TextAttribute)fAttributes.get(type);
		if(attr == null) {
			attr = (TextAttribute) fAttributes.get(DEFAULT_ATTRIBUTE);
		}
		return attr;
	}
	
}
