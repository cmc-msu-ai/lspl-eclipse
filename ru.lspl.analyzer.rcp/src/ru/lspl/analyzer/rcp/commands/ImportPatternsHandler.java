package ru.lspl.analyzer.rcp.commands;

import java.io.FileNotFoundException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

import ru.lspl.analyzer.rcp.editors.DocumentEditorInput;
import ru.lspl.analyzer.rcp.model.LsplFileDocument;
import ru.lspl.ui.model.access.PatternStorageAccess;

public class ImportPatternsHandler extends AbstractActivePageHandler {

	private final PatternStorageAccess patternStorage = new PatternStorageAccess();

	@Override
	protected Object execute( ExecutionEvent event, IWorkbenchPage page ) throws ExecutionException {
		IEditorPart editor = page.getActiveEditor();

		if ( editor == null || !(editor.getEditorInput() instanceof DocumentEditorInput) )
			return null;

		LsplFileDocument document = ((DocumentEditorInput) editor.getEditorInput()).getDocument();

		FileDialog openFileDialog = new FileDialog( page.getWorkbenchWindow().getShell(), SWT.OPEN );

		openFileDialog.setText( "Открыть файл" );

		String fileName = openFileDialog.open();

		if ( fileName == null )
			return null;

		try {
			patternStorage.loadFromFile( document.getPatterns(), fileName );
		} catch ( FileNotFoundException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
