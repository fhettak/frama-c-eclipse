package net.eclipse.why.editeur.editors.why;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class WHYTextAttributeProvider {

	public static final String WHY_OPCODE_ATTRIBUTE = "__pos_why_opcode_attribute";
	public static final String WHY_SYMBOLS_ATTRIBUTE = "__pos_why_symbols_attribute";
	public static final String WHY_COMMENT_ATTRIBUTE = "__pos_why_comment_attribute";
	public static final String WHY_DEFAULT_ATTRIBUTE = "__pos_why_default_attribute";
	public static final String WHY_DECIMAL_NUMBER_ATTRIBUTE = "__pos_why_decimal_number_attribute";
	public static final String WHY_FLOAT_NUMBER_ATTRIBUTE = "__pos_why_float_number_attribute";
	public static final String WHY_STRING_ATTRIBUTE = "__pos_why_string_attribute";
	
	
	private Map<String,TextAttribute> fAttributes = new HashMap<String,TextAttribute>();

	public WHYTextAttributeProvider() {
		
		// Les opcode en gras
		fAttributes.put(WHY_OPCODE_ATTRIBUTE,
				new TextAttribute(new Color(Display.getCurrent(), 
									new RGB(0, 145, 120)), null, SWT.BOLD));
		
		// Les symboles en bleu
		fAttributes.put(WHY_SYMBOLS_ATTRIBUTE,
				new TextAttribute(new Color(Display.getCurrent(), 
									new RGB(0, 0, 255)), null, SWT.BOLD));
		
		// Les commentaires en rouge
		fAttributes.put(WHY_COMMENT_ATTRIBUTE,
				new TextAttribute(new Color(Display.getCurrent(), 
									new RGB(180, 0, 70)), null, SWT.ITALIC));
		// Par défaut en noir
		fAttributes.put(WHY_DEFAULT_ATTRIBUTE,
				new TextAttribute(new Color(Display.getCurrent(), 
									new RGB(0, 0, 0))));
		
		// Nombres décimaux en noir
		fAttributes.put(WHY_DECIMAL_NUMBER_ATTRIBUTE,
				new TextAttribute(new Color(Display.getCurrent(),
									new RGB(0, 0, 0)), null, SWT.BOLD));
		
		// Nombres flottants en noir également
		fAttributes.put(WHY_FLOAT_NUMBER_ATTRIBUTE,
				new TextAttribute(new Color(Display.getCurrent(),
									new RGB(0, 0, 0)), null, SWT.BOLD));
		
		// Chaines
		fAttributes.put(WHY_STRING_ATTRIBUTE, 
				new TextAttribute(new Color(Display.getCurrent(),
									new RGB(190, 125, 0)), null, SWT.ITALIC));
		
	}
	
	public TextAttribute getAttribute(String type) {
		TextAttribute attr = (TextAttribute)fAttributes.get(type);
		if(attr == null) {
			attr = (TextAttribute) fAttributes.get(WHY_DEFAULT_ATTRIBUTE);
		}
		return attr;
	}
	
}
