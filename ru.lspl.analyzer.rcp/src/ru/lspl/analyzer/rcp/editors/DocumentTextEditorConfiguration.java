package ru.lspl.analyzer.rcp.editors;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public class DocumentTextEditorConfiguration extends TextSourceViewerConfiguration {

	public DocumentTextEditorConfiguration() {
		super();
	}

	public DocumentTextEditorConfiguration( IPreferenceStore preferenceStore ) {
		super( preferenceStore );
	}

	@Override
	public ITextHover getTextHover( ISourceViewer sv, String contentType ) {
		return new DocumentTextHover();
	}

}
