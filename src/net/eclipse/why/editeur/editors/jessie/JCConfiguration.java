package net.eclipse.why.editeur.editors.jessie;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class JCConfiguration extends SourceViewerConfiguration {
	
	private JCScanner scanner;
	private JCTextAttributeProvider provider;
	
	
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
	

	
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			JCPartitionScanner.JC_COMMENT,
			IDocument.DEFAULT_CONTENT_TYPE
		};
	}
	
	protected JCTextAttributeProvider getProvider() {
		if(provider == null) {
			provider = new JCTextAttributeProvider();
		}
		return provider;
	}

	protected JCScanner getJCScanner() {
		if (scanner == null) {
			scanner = new JCScanner(getProvider());
		}
		return scanner;
	}
	

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		
		PresentationReconciler reconciler = new PresentationReconciler();
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

		// crée le damager/repairer pour le code
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getJCScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
		//crée le damager/repairer pour les commantaires
		dr= new DefaultDamagerRepairer( new SingleTokenScanner(provider.getAttribute(JCTextAttributeProvider.COMMENT_ATTRIBUTE)) );
		reconciler.setDamager(dr, JCPartitionScanner.JC_COMMENT);
		reconciler.setRepairer(dr, JCPartitionScanner.JC_COMMENT);

		return reconciler;
	}
}
