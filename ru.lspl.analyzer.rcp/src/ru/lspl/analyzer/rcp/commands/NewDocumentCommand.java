package ru.lspl.analyzer.rcp.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import ru.lspl.analyzer.rcp.editors.DocumentEditor;
import ru.lspl.analyzer.rcp.editors.DocumentEditorInput;
import ru.lspl.analyzer.rcp.model.LsplFileDocument;

public class NewDocumentCommand extends AbstractActivePageHandler {

	@Override
	protected Object execute( ExecutionEvent event, IWorkbenchPage page ) throws ExecutionException {
		LsplFileDocument doc = new LsplFileDocument();
		DocumentEditorInput input = new DocumentEditorInput( doc );

		try {
			page.openEditor( input, DocumentEditor.ID );
		} catch ( PartInitException e ) {
			e.printStackTrace();
		}

		return null;
	}

}
