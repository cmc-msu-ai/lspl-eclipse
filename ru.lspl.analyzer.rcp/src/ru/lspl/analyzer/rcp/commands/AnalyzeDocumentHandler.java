package ru.lspl.analyzer.rcp.commands;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

import ru.lspl.analyzer.rcp.editors.DocumentEditorInput;
import ru.lspl.analyzer.rcp.model.Document;

public class AnalyzeDocumentHandler extends AbstractActivePageHandler {

	private static final class AnalyzeRunnable implements IRunnableWithProgress {

		private final Document document;

		private AnalyzeRunnable( Document document ) {
			this.document = document;
		}

		@Override
		public void run( IProgressMonitor monitor ) throws InvocationTargetException, InterruptedException {
			monitor.beginTask( "Анализ документа...", 1 );
			document.analyze();
			monitor.worked( 1 );
		}
	}

	@Override
	protected Object execute( ExecutionEvent event, IWorkbenchPage page ) throws ExecutionException {
		IEditorPart editor = page.getActiveEditor();

		if ( editor == null || !(editor.getEditorInput() instanceof DocumentEditorInput) )
			return null;

		try {
			IRunnableWithProgress op = new AnalyzeRunnable( ((DocumentEditorInput) editor.getEditorInput()).getDocument() );
			new ProgressMonitorDialog( page.getWorkbenchWindow().getShell() ).run( true, true, op );
		} catch ( InvocationTargetException e ) {
			// handle exception
		} catch ( InterruptedException e ) {
			// handle cancelation
		}

		return null;
	}

}
