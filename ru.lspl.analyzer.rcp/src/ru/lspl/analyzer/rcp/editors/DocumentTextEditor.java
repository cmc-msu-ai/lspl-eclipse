package ru.lspl.analyzer.rcp.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextEditor;

import ru.lspl.analyzer.rcp.Activator;
import ru.lspl.analyzer.rcp.preferences.PreferenceConstants;
import ru.lspl.ui.editors.DocumentTextEditorConfiguration;
import ru.lspl.ui.model.LsplDocumentAnnotationModel;
import ru.lspl.ui.support.LsplDocumentSupport;

public class DocumentTextEditor extends TextEditor {

	private LsplDocumentSupport support = new LsplDocumentSupport() {

		@Override
		protected LsplDocumentAnnotationModel getAnnotationModel() {
			DocumentEditorInput editorInput = (DocumentEditorInput) getEditorInput();
			return (LsplDocumentAnnotationModel) editorInput.getDocumentProvider().getAnnotationModel( editorInput );
		}

	};

	public DocumentTextEditor() {
		setSourceViewerConfiguration( new DocumentTextEditorConfiguration( getPreferenceStore() ) );
	}

	@Override
	protected void setDocumentProvider( IEditorInput input ) {
		setDocumentProvider( ((DocumentEditorInput) input).getDocumentProvider() );
	}

	@Override
	protected void performSaveAs( IProgressMonitor progressMonitor ) {
		FileDialog saveFileDialog = new FileDialog( getSite().getShell(), SWT.SAVE );

		String fileName = saveFileDialog.open();

		if ( fileName != null ) {
			((DocumentEditorInput) getEditorInput()).getDocument().setFileName( fileName );
			performSave( true, progressMonitor );
		}
	}

	@Override
	protected ISourceViewer createSourceViewer( Composite parent, IVerticalRuler ruler, int styles ) {
		SourceViewer sv = (SourceViewer) super.createSourceViewer( parent, ruler, styles | SWT.WRAP );

		support.setAnnotationHighlightEvent( Activator.getDefault().getPreferenceStore().getString( PreferenceConstants.ANNOTATION_HIGHLIGHT_EVENT ) );
		support.install( sv );

		return sv;
	}

}
