package ru.lspl.analyzer.rcp.commands;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

import ru.lspl.analyzer.rcp.editors.DocumentEditorInput;
import ru.lspl.analyzer.rcp.model.Document;
import ru.lspl.analyzer.rcp.model.PatternStorageAccess;

public class ExportPatternsHandler extends AbstractActivePageHandler {

	private final PatternStorageAccess patternStorage = new PatternStorageAccess();

	@Override
	protected Object execute( ExecutionEvent event, IWorkbenchPage page ) throws ExecutionException {
		IEditorPart editor = page.getActiveEditor();

		if ( editor == null || !(editor.getEditorInput() instanceof DocumentEditorInput) )
			return null;

		Document document = ((DocumentEditorInput) editor.getEditorInput()).getDocument();

		FileDialog saveFileDialog = new FileDialog( page.getWorkbenchWindow().getShell(), SWT.OPEN );

		saveFileDialog.setText( "Сохранить файл" );

		String fileName = saveFileDialog.open();

		if ( fileName == null )
			return null;

		try {
			patternStorage.save( document, fileName );
		} catch ( FileNotFoundException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
