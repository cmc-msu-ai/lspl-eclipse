package ru.lspl.analyzer.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;

public abstract class AbstractActivePageHandler extends AbstractHandler {

	@Override
	public Object execute( ExecutionEvent event ) throws ExecutionException {
		return execute( event, HandlerUtil.getActiveWorkbenchWindow( event ).getActivePage() );
	}

	protected abstract Object execute( ExecutionEvent event, IWorkbenchPage activePage ) throws ExecutionException;

}
