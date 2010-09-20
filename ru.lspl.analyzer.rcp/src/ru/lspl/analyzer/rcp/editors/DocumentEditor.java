package ru.lspl.analyzer.rcp.editors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.services.IEvaluationService;

import ru.lspl.analyzer.rcp.model.Document;
import ru.lspl.analyzer.rcp.model.IAnalysisListener;

public class DocumentEditor extends MultiPageEditorPart implements IGotoMarker {

	public static final String ID = "ru.lspl.analyzer.rcp.editors.DocumentEditor";

	/** The text editor */
	private DocumentTextEditor editor;

	/**
	 * Creates page of the multi-page editor, which contains a text editor.
	 */
	private void createTextEditorPage() {
		try {
			editor = new DocumentTextEditor();
			int index = addPage( editor, getEditorInput() );
			setPartName( editor.getTitle() );
			setPageText( index, "Текст" );
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
			public void analisysNeedChanged( Document doc ) {
				((IEvaluationService) site.getService( IEvaluationService.class )).requestEvaluation( "ru.lspl.analyzer.rcp.analysisNeeded" );
			}

			@Override
			public void analysisComplete( Document doc ) {
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
		IEditorPart editor = getEditor( 0 );

		editor.doSave( monitor );

		setPartName( editor.getEditorInput().getName() );
		setInput( editor.getEditorInput() );
	}

	@Override
	public void doSaveAs() {
		IEditorPart editor = getEditor( 0 );

		editor.doSaveAs();

		setPartName( editor.getEditorInput().getName() );
		setInput( editor.getEditorInput() );
	}

	@Override
	protected void pageChange( int newPageIndex ) {
		super.pageChange( newPageIndex );
	}

}
