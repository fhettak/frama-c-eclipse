package net.eclipse.why.editeur.editors;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextEditor;

public class GeneralEditor extends TextEditor {
	
	public GeneralEditor() {
		super();
	}
	
	public void dispose() {
		super.dispose();
	}
	
	public ISourceViewer getSViewer() {
		return getSourceViewer();
	}
}
