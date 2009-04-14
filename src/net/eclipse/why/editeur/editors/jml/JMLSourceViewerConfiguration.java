package net.eclipse.why.editeur.editors.jml;

import net.eclipse.why.editeur.editors.MLTextAttributeProvider;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public class JMLSourceViewerConfiguration extends TextSourceViewerConfiguration {
	
	private JMLScanner scanner;
	private JScanner jscan;
	private MLTextAttributeProvider provider;
	
	/** 
	 * Classe utilisée lorsque l'on ne désire pas affiner la coloration syntaxique
	 * (ie. toute la partition de la même couleur). Utilisée pour les partitions
	 * de commentaires.
	 */
	static class SingleTokenScanner extends BufferedRuleBasedScanner {
		/**
		 * Constructeur. Définit le token renvoyé.
		 * @param attribute Attribut du Token.
		 */
		public SingleTokenScanner(TextAttribute attribute) {
			setDefaultReturnToken(new Token(attribute));
		}
	}
	

	
	/**
	 * Renvoie l'identifiant du partitionnement utilisé.
	 * @param sourceViewer
	 * @return L'identifiant du type du partitionnement.
	 * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredDocumentPartitioning(org.eclipse.jface.text.source.ISourceViewer)
	 *
	public String getConfiguredDocumentPartitioning(ISourceViewer sourceViewer) {
		return "__pos_why_partitioning";
	}/**/
	
	
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
				IDocument.DEFAULT_CONTENT_TYPE,
				JMLPartitionScanner.JML_STRING,
				JMLPartitionScanner.JML_COMMENT,
				JMLPartitionScanner.JML_INTRA_COMMENT,
				JMLPartitionScanner.JML_START_TAG1,
				JMLPartitionScanner.JML_START_TAG2
		};
	}
	
	
	protected MLTextAttributeProvider getProvider() {
		if(provider == null) {
			provider = new MLTextAttributeProvider();
		}
		return provider;
	}

	protected JMLScanner getJMLScanner() {
		if (scanner == null) {
			scanner = new JMLScanner(getProvider());
		}
		return scanner;
	}
	
	protected JScanner getJScanner() {
		if (jscan == null) {
			jscan = new JScanner(getProvider());
		}
		return jscan;
	}
	

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		
		PresentationReconciler reconciler = new PresentationReconciler();
		//reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

		// crée le damager/repairer pour le code
		//DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getJMLScanner());
		//reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		//reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
		//crée le damager/repairer pour les commentaires
		//DefaultDamagerRepairer dr= new DefaultDamagerRepairer( new SingleTokenScanner(provider.getAttribute(JMLTextAttributeProvider.JML_COMMENT_ATTRIBUTE)) );
		//reconciler.setDamager(dr, JMLPartitionScanner.JML_COMMENT);
		//reconciler.setRepairer(dr, JMLPartitionScanner.JML_COMMENT);
		
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getJScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
		dr = new DefaultDamagerRepairer(getJMLScanner());
        reconciler.setDamager(dr, JMLPartitionScanner.JML_START_TAG1);
        reconciler.setRepairer(dr, JMLPartitionScanner.JML_START_TAG1);
        
        dr = new DefaultDamagerRepairer(getJMLScanner());
        reconciler.setDamager(dr, JMLPartitionScanner.JML_START_TAG2);
        reconciler.setRepairer(dr, JMLPartitionScanner.JML_START_TAG2);
        
        dr= new DefaultDamagerRepairer( new SingleTokenScanner(provider.getAttribute(MLTextAttributeProvider.ML_COMMENT_ATTRIBUTE)) );
		reconciler.setDamager(dr, JMLPartitionScanner.JML_COMMENT);
		reconciler.setRepairer(dr, JMLPartitionScanner.JML_COMMENT);
        
		return reconciler;
	}
}
