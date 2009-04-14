package net.eclipse.why.editeur.editors.cml;

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

public class CMLSourceViewerConfiguration extends TextSourceViewerConfiguration {
	
	private CMLScanner scanner;
	private CScanner cscan;
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
			CMLPartitionScanner.CML_STRING,
			CMLPartitionScanner.CML_COMMENT,
			CMLPartitionScanner.CML_INTRA_COMMENT,
			CMLPartitionScanner.CML_START_TAG1,
			CMLPartitionScanner.CML_START_TAG2
		};
	}
	
	
	protected MLTextAttributeProvider getProvider() {
		if(provider == null) {
			provider = new MLTextAttributeProvider();
		}
		return provider;
	}

	protected CMLScanner getCMLScanner() {
		if (scanner == null) {
			scanner = new CMLScanner(getProvider());
		}
		return scanner;
	}
	
	protected CScanner getCScanner() {
		if (cscan == null) {
			cscan = new CScanner(getProvider());
		}
		return cscan;
	}
	
	
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getCScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
		dr = new DefaultDamagerRepairer(getCMLScanner());
        reconciler.setDamager(dr, CMLPartitionScanner.CML_START_TAG1);
        reconciler.setRepairer(dr, CMLPartitionScanner.CML_START_TAG1);
        
        dr = new DefaultDamagerRepairer(getCMLScanner());
        reconciler.setDamager(dr, CMLPartitionScanner.CML_START_TAG2);
        reconciler.setRepairer(dr, CMLPartitionScanner.CML_START_TAG2);
        
        dr= new DefaultDamagerRepairer( new SingleTokenScanner(provider.getAttribute(MLTextAttributeProvider.ML_COMMENT_ATTRIBUTE)) );
		reconciler.setDamager(dr, CMLPartitionScanner.CML_COMMENT);
		reconciler.setRepairer(dr, CMLPartitionScanner.CML_COMMENT);
        
		return reconciler;
	}
	
//	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
//
//        ContentAssistant assistant = new ContentAssistant();
//
//        assistant.setContentAssistProcessor(new TagContentAssistProcessor(getCMLScanner()),
//                CMLPartitionScanner.CML_START_TAG1);
//        assistant.enableAutoActivation(true);
//        assistant.setAutoActivationDelay(500);
//        assistant.setProposalPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
//        assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
//        return assistant;
//
//    }
}
