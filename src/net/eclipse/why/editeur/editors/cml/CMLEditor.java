package net.eclipse.why.editeur.editors.cml;
import net.eclipse.why.editeur.editors.GeneralEditor;

public class CMLEditor extends GeneralEditor {
	
	
	public CMLEditor() {
		super();
		setSourceViewerConfiguration(new CMLSourceViewerConfiguration());
		setDocumentProvider(new CMLDocumentProvider());
	}
}
