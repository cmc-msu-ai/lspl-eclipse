package ru.lspl.analyzer.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import ru.lspl.analyzer.rcp.editors.DocumentEditorInput;

public class AnalyzeDocumentHandler extends AbstractHandler {

	@Override
	public Object execute( ExecutionEvent event ) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow( event );
		IWorkbenchPage page = window.getActivePage();

		IEditorPart editor = page.getActiveEditor();

		if ( editor == null || !(editor.getEditorInput() instanceof DocumentEditorInput) )
			return null;

		DocumentEditorInput input = (DocumentEditorInput) editor.getEditorInput();

		input.getDocument().analyze(); // TODO Add progress

		return null;
	}

}
