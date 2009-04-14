package net.eclipse.why.editeur.editors.jessie;

import net.eclipse.why.editeur.editors.GeneralEditor;

public class JCEditor extends GeneralEditor {

	public JCEditor() {
		super();
		setSourceViewerConfiguration(new JCConfiguration());
		setDocumentProvider(new JCDocumentProvider());
	}
	
	public void dispose() {
		super.dispose();
	}
	
}
