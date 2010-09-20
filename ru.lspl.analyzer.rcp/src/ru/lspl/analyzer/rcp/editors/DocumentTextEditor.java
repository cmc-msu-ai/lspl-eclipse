package ru.lspl.analyzer.rcp.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextEditor;

import ru.lspl.analyzer.rcp.model.DocumentProvider;

public class DocumentTextEditor extends TextEditor {

	@Override
	protected void setDocumentProvider( IEditorInput input ) {
		setDocumentProvider( new DocumentProvider() );
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

}
