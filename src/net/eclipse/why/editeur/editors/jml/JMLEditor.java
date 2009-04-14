package net.eclipse.why.editeur.editors.jml;

import net.eclipse.why.editeur.editors.GeneralEditor;

public class JMLEditor extends GeneralEditor {

	public JMLEditor() {
		super();
		setSourceViewerConfiguration(new JMLSourceViewerConfiguration());
		setDocumentProvider(new JMLDocumentProvider());
	}
	
	public void dispose() {
		super.dispose();
	}

/*
	protected IJavaElement getCorrespondingElement(IJavaElement element) {
		if (getEditorInput() instanceof IClassFileEditorInput) {
			IClassFileEditorInput input= (IClassFileEditorInput) getEditorInput();
			IJavaElement parent= element.getAncestor(IJavaElement.CLASS_FILE);
			if (input.getClassFile().equals(parent))
				return element;
		}
		return null;
	}

	protected IJavaElement getElementAt(int offset) {
		if (getEditorInput() instanceof IClassFileEditorInput) {
			try {
				IClassFileEditorInput input= (IClassFileEditorInput) getEditorInput();
				return input.getClassFile().getElementAt(offset);
			} catch (JavaModelException x) {
			}
		}
		return null;
	}
*/
}
