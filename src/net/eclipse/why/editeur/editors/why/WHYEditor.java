package net.eclipse.why.editeur.editors.why;

import net.eclipse.why.editeur.editors.GeneralEditor;


public class WHYEditor extends GeneralEditor {

	//private ColorManager colorManager;

	public WHYEditor() {
		super();
		//colorManager = new ColorManager();
		//setSourceViewerConfiguration(new WHYConfiguration());
		setDocumentProvider(new WHYDocumentProvider());
	}
	
	public void dispose() {
		//colorManager.dispose();
		super.dispose();
	}
}
