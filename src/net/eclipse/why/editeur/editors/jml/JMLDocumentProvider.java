package net.eclipse.why.editeur.editors.jml;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class JMLDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		
		IDocument document = super.createDocument(element);
		if (document != null) {
			String[] types = new String[] {
					IJMLPartitions.JML_START_TAG1,
					IJMLPartitions.JML_START_TAG2,
					IJMLPartitions.JML_COMMENT,
					IJMLPartitions.JML_INTRA_COMMENT,
					IJMLPartitions.JML_STRING,
					IDocument.DEFAULT_CONTENT_TYPE
			};
			IDocumentPartitioner partitioner = new FastPartitioner (
					new JMLPartitionScanner(),types
			);

			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
}
