package net.eclipse.why.editeur.editors.cml;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class CMLDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		
		IDocument document = super.createDocument(element);
		if (document != null) {
			String[] types = new String[] {
					ICMLPartitions.CML_START_TAG1,
					ICMLPartitions.CML_START_TAG2,
					ICMLPartitions.CML_COMMENT,
					ICMLPartitions.CML_INTRA_COMMENT,
					ICMLPartitions.CML_STRING,
					IDocument.DEFAULT_CONTENT_TYPE
			};
			IDocumentPartitioner partitioner = new FastPartitioner (
					new CMLPartitionScanner(),types
			);

			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
}
