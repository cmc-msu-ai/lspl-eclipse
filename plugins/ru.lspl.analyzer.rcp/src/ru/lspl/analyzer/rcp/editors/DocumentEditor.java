package ru.lspl.analyzer.rcp.editors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.services.IEvaluationService;

import ru.lspl.text.TextRange;
import ru.lspl.ui.model.ILsplDocument;
import ru.lspl.ui.model.listeners.IAnalysisListener;

public class DocumentEditor extends MultiPageEditorPart implements IGotoMarker {

	public static final String ID = "ru.lspl.analyzer.rcp.editors.DocumentEditor";

	/** The text editor */
	private DocumentTextEditor textEditor;
	private DocumentConfigEditor configEditor;

	/**
	 * Creates page of the multi-page editor, which contains a text editor.
	 */
	private void createTextEditorPage() {
		try {
			textEditor = new DocumentTextEditor();
			configEditor = new DocumentConfigEditor();

			setPageText( addPage( textEditor, getEditorInput() ), "Текст" );
			setPageText( addPage( configEditor, getEditorInput() ), "Опции" );

			setPartName( textEditor.getTitle() );
		} catch ( PartInitException e ) {
			ErrorDialog.openError( getSite().getShell(), "Error creating nested text editor", null, e.getStatus() );
		}
	}

	@Override
	protected void createPages() {
		createTextEditorPage();
	}

	@Override
	public void gotoMarker( IMarker marker ) {
		setActivePage( 0 );
		IDE.gotoMarker( getEditor( 0 ), marker );
	}

	@Override
	public void init( final IEditorSite site, final IEditorInput editorInput ) throws PartInitException {
		if ( !(editorInput instanceof DocumentEditorInput) )
			throw new PartInitException( "Invalid Input: Must be DocumentEditorInput" );

		((DocumentEditorInput) editorInput).getDocument().addAnalysisListener( new IAnalysisListener() {

			@Override
			public void analysisRequired( ILsplDocument doc ) {
				((IEvaluationService) site.getService( IEvaluationService.class )).requestEvaluation( "ru.lspl.analyzer.rcp.analysisNeeded" );
			}

			@Override
			public void analysisStarted( ILsplDocument doc ) {
				((IEvaluationService) site.getService( IEvaluationService.class )).requestEvaluation( "ru.lspl.analyzer.rcp.analysisNeeded" );
			}

			@Override
			public void analysisCompleted( ILsplDocument doc ) {
				((IEvaluationService) site.getService( IEvaluationService.class )).requestEvaluation( "ru.lspl.analyzer.rcp.analysisNeeded" );
			}

		} );

		super.init( site, editorInput );
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public void doSave( IProgressMonitor monitor ) {
		textEditor.doSave( monitor );

		setPartName( textEditor.getEditorInput().getName() );
		setInput( textEditor.getEditorInput() );
	}

	@Override
	public void doSaveAs() {
		textEditor.doSaveAs();

		setPartName( textEditor.getEditorInput().getName() );
		setInput( textEditor.getEditorInput() );
	}

	@Override
	protected void pageChange( int newPageIndex ) {
		super.pageChange( newPageIndex );
	}

	public void selectRange( TextRange range ) {
		textEditor.selectAndReveal( range.getStartOffset(), range.getEndOffset() );
	}

}
