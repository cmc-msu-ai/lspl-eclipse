package ru.lspl.analyzer.rcp.commands;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import ru.lspl.analyzer.rcp.editors.DocumentEditorInput;

public class AnalyzeDocumentHandler extends AbstractActivePageHandler {

	@Override
	protected Object execute( ExecutionEvent event, IWorkbenchPage page ) throws ExecutionException {
		IEditorPart editor = page.getActiveEditor();

		if ( editor == null || !(editor.getEditorInput() instanceof DocumentEditorInput) )
			return null;

		try {
			PlatformUI.getWorkbench().getProgressService().busyCursorWhile( ((DocumentEditorInput) editor.getEditorInput()).getDocument().createAnalyzeJob() );
		} catch ( InvocationTargetException e ) {
			// handle exception
		} catch ( InterruptedException e ) {
			// handle cancelation
		}

		return null;
	}

}
