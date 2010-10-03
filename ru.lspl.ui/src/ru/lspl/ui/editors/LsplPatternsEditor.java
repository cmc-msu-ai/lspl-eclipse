package ru.lspl.ui.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;

public class LsplPatternsEditor extends MultiPageEditorPart {

	public static final String ID = "ru.lspl.ui.editors.LsplPatternsEditor";

	private final LsplPatternsSourceEditor patternsEditor = new LsplPatternsSourceEditor();
	private final LsplPatternsPreview patternsPreview = new LsplPatternsPreview();

	@Override
	protected void createPages() {
		try {
			setPageText( addPage( patternsEditor, getEditorInput() ), "Source" );
			setPageText( addPage( patternsPreview, getEditorInput() ), "Preview" );
		} catch ( PartInitException e ) {
			ErrorDialog.openError( getSite().getShell(), "Error creating nested text editor", null, e.getStatus() );
		}

		setPartName( patternsEditor.getTitle() );
	}

	@Override
	public void doSave( IProgressMonitor monitor ) {
		patternsEditor.doSave( monitor );

		setPartName( patternsEditor.getEditorInput().getName() );
		setInput( patternsEditor.getEditorInput() );
	}

	@Override
	public void doSaveAs() {
		patternsEditor.doSaveAs();

		setPartName( patternsEditor.getEditorInput().getName() );
		setInput( patternsEditor.getEditorInput() );
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	protected void pageChange( int newPageIndex ) {
		super.pageChange( newPageIndex );

		if ( newPageIndex == 1 )
			patternsPreview.refresh();
	}

}
