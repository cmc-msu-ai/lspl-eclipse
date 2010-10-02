package ru.lspl.ui.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;

public class LsplPatternsEditor extends MultiPageEditorPart {

	public static final String ID = "ru.lspl.ui.editors.LsplPatternsEditor";

	private final LsplPatternsSourceEditor sourceEditor = new LsplPatternsSourceEditor();

	@Override
	protected void createPages() {
		try {
			setPageText( addPage( sourceEditor, getEditorInput() ), "Source" );
		} catch ( PartInitException e ) {
			ErrorDialog.openError( getSite().getShell(), "Error creating nested text editor", null, e.getStatus() );
		}

		setPartName( sourceEditor.getTitle() );
	}

	@Override
	public void doSave( IProgressMonitor monitor ) {
		sourceEditor.doSave( monitor );

		setPartName( sourceEditor.getEditorInput().getName() );
		setInput( sourceEditor.getEditorInput() );
	}

	@Override
	public void doSaveAs() {
		sourceEditor.doSaveAs();

		setPartName( sourceEditor.getEditorInput().getName() );
		setInput( sourceEditor.getEditorInput() );
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

}
