package net.eclipse.why.editeur.editors.why;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class WHYConfiguration extends SourceViewerConfiguration {
	
	//private WHYDoubleClickStrategy doubleClickStrategy;
	//private WHYTagScanner tagScanner;
	private WHYScanner scanner;
	private WHYTextAttributeProvider provider;
	//private ColorManager colorManager;
	
	
	
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
	

	
//	public WHYConfiguration(ColorManager colorManager) {
//		this.colorManager = colorManager;
//	}
	
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
			WHYPartitionScanner.WHY_COMMENT,
			IDocument.DEFAULT_CONTENT_TYPE
		};
	}
	
//	public ITextDoubleClickStrategy getDoubleClickStrategy(
//		ISourceViewer sourceViewer,
//		String contentType) {
//		if (doubleClickStrategy == null)
//			doubleClickStrategy = new WHYDoubleClickStrategy();
//		return doubleClickStrategy;
//	}
	
	protected WHYTextAttributeProvider getProvider() {
		if(provider == null) {
			provider = new WHYTextAttributeProvider();
		}
		return provider;
	}

	protected WHYScanner getWHYScanner() {
		if (scanner == null) {
			//scanner = new WHYScanner(colorManager);
			scanner = new WHYScanner(getProvider());
//			scanner.setDefaultReturnToken(
//				new Token(
//					new TextAttribute(
//						colorManager.getColor(IWHYColorConstants.DEFAULT))));
		}
		return scanner;
	}
	
//	protected WHYTagScanner getXMLTagScanner() {
//		if (tagScanner == null) {
//			tagScanner = new WHYTagScanner(colorManager);
//			tagScanner.setDefaultReturnToken(
//				new Token(
//					new TextAttribute(
//						colorManager.getColor(IWHYColorConstants.TAG))));
//		}
//		return tagScanner;
//	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		
		PresentationReconciler reconciler = new PresentationReconciler();
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

//		DefaultDamagerRepairer dr =
//			new DefaultDamagerRepairer(getWHYTagScanner());
//		reconciler.setDamager(dr, WHYPartitionScanner.XML_TAG);
//		reconciler.setRepairer(dr, WHYPartitionScanner.XML_TAG);

		// crée le damager/repairer pour le code
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getWHYScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
		//crée le damager/repairer pour les commentaires
		dr= new DefaultDamagerRepairer( new SingleTokenScanner(provider.getAttribute(WHYTextAttributeProvider.WHY_COMMENT_ATTRIBUTE)) );
		reconciler.setDamager(dr, WHYPartitionScanner.WHY_COMMENT);
		reconciler.setRepairer(dr, WHYPartitionScanner.WHY_COMMENT);

//		NonRuleBasedDamagerRepairer ndr =
//			new NonRuleBasedDamagerRepairer(
//				new TextAttribute(
//					colorManager.getColor(IWHYColorConstants.XML_COMMENT)));
//		reconciler.setDamager(ndr, WHYPartitionScanner.WHY_COMMENT);
//		reconciler.setRepairer(ndr, WHYPartitionScanner.WHY_COMMENT);
//		
//		NonRuleBasedDamagerRepairer ndr2 = new NonRuleBasedDamagerRepairer(
//				  new TextAttribute(colorManager.getColor(IWHYColorConstants.PROOF_FEATURE)));
//				reconciler.setDamager(ndr2, WHYPartitionScanner.PROOF_FEATURE);
//				reconciler.setRepairer(ndr2, WHYPartitionScanner.PROOF_FEATURE);


		return reconciler;
	}

}