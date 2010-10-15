package ru.lspl.ui.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import ru.lspl.ui.Activator;
import ru.lspl.ui.DisplayManager;
import ru.lspl.ui.ILsplColorConstants;
import ru.lspl.ui.editors.rules.NonRuleBasedDamagerRepairer;
import ru.lspl.ui.editors.rules.PatternsPartitionScanner;
import ru.lspl.ui.editors.rules.PatternsScanner;

public class PatternsSourceConfiguration extends SourceViewerConfiguration {

	private final PatternsSourceEditor editor;

	private final DisplayManager displayManager;

	private PatternsScanner scanner;

	public PatternsSourceConfiguration( PatternsSourceEditor editor ) {
		this.editor = editor;
		this.displayManager = Activator.getDefault().getDisplayManager();
	}

	@Override
	public String[] getConfiguredContentTypes( ISourceViewer sourceViewer ) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE, PatternsPartitionScanner.LSPL_COMMENT };
	}

	@Override
	public IPresentationReconciler getPresentationReconciler( ISourceViewer sourceViewer ) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer( getPatternsScanner() );
		reconciler.setDamager( dr, IDocument.DEFAULT_CONTENT_TYPE );
		reconciler.setRepairer( dr, IDocument.DEFAULT_CONTENT_TYPE );

		NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer( new TextAttribute( displayManager.getColor( ILsplColorConstants.COMMENT ) ) );
		reconciler.setDamager( ndr, PatternsPartitionScanner.LSPL_COMMENT );
		reconciler.setRepairer( ndr, PatternsPartitionScanner.LSPL_COMMENT );

		return reconciler;
	}

	public PatternsScanner getPatternsScanner() {
		if ( scanner == null ) {
			scanner = new PatternsScanner( editor, displayManager );
			scanner.setDefaultReturnToken( new Token( new TextAttribute( displayManager.getColor( ILsplColorConstants.DEFAULT ) ) ) );
		}

		return scanner;
	}

}
