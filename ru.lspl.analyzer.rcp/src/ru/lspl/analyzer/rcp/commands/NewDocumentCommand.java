package ru.lspl.analyzer.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import ru.lspl.analyzer.rcp.editors.DocumentEditor;
import ru.lspl.analyzer.rcp.editors.DocumentEditorInput;
import ru.lspl.analyzer.rcp.model.Document;

public class NewDocumentCommand extends AbstractHandler {

	@Override
	public Object execute( ExecutionEvent event ) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow( event );
		IWorkbenchPage page = window.getActivePage();

		Document doc = new Document();
		DocumentEditorInput input = new DocumentEditorInput( doc );

		try {
			page.openEditor( input, DocumentEditor.ID );
		} catch ( PartInitException e ) {
			e.printStackTrace();
		}

		return null;
	}

}
