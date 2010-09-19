package ru.lspl.analyzer.rcp.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import ru.lspl.analyzer.rcp.editors.DocumentEditorInput;
import ru.lspl.analyzer.rcp.model.Document;

public class AbstractDocumentViewPart extends ViewPart implements IPartListener {

	private IEditorPart editor;
	private Document document;

	public Document getDocument() {
		return document;
	}

	public void connect( IEditorPart editor, Document document ) {
		if ( isConnected() )
			disconnect();

		this.editor = editor;
		this.document = document;
	}

	public void disconnect() {
		this.editor = null;
		this.document = null;
	}

	public boolean isConnected() {
		return document != null;
	}

	@Override
	public void partActivated( IWorkbenchPart part ) {
		if ( !(part instanceof IEditorPart) )
			return;

		IEditorPart editorPart = (IEditorPart) part;

		if ( editorPart == editor ) // No need to reconnect already connected editor
			return;

		if ( isConnected() )
			disconnect();

		if ( !(editorPart.getEditorInput() instanceof DocumentEditorInput) )
			return;

		connect( editorPart, ((DocumentEditorInput) editorPart.getEditorInput()).getDocument() );
	}

	@Override
	public void partBroughtToTop( IWorkbenchPart part ) {
	}

	@Override
	public void partClosed( IWorkbenchPart part ) {
		if ( part == editor && isConnected() )
			disconnect();
	}

	@Override
	public void partDeactivated( IWorkbenchPart part ) {
	}

	@Override
	public void partOpened( IWorkbenchPart part ) {
	}

	@Override
	public void createPartControl( Composite parent ) {
		getSite().getPage().addPartListener( this );
	}

	@Override
	public void setFocus() {
	}

}
