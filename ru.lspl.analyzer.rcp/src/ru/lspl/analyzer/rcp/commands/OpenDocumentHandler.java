package ru.lspl.analyzer.rcp.commands;

import java.io.IOException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import ru.lspl.analyzer.rcp.editors.DocumentEditor;
import ru.lspl.analyzer.rcp.editors.DocumentEditorInput;
import ru.lspl.analyzer.rcp.model.Document;

public class OpenDocumentHandler extends AbstractActivePageHandler {

	@Override
	protected Object execute( ExecutionEvent event, IWorkbenchPage page ) throws ExecutionException {
		FileDialog openFileDialog = new FileDialog( page.getWorkbenchWindow().getShell(), SWT.OPEN );

		openFileDialog.setText( "Открыть файл" );

		String fileName = openFileDialog.open();

		if ( fileName == null )
			return null;

		Document document = new Document();
		try {
			document.load( fileName );
		} catch ( IOException e1 ) {
			e1.printStackTrace();
		}

		DocumentEditorInput input = new DocumentEditorInput( document );

		try {
			page.openEditor( input, DocumentEditor.ID );
		} catch ( PartInitException e ) {
			e.printStackTrace();
		}

		return null;
	}

}
