package ru.lspl.analyzer.rcp.views;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import ru.lspl.analyzer.rcp.editors.DocumentEditorInput;
import ru.lspl.analyzer.rcp.model.Document;
import ru.lspl.analyzer.rcp.model.DocumentAnnotationModel;

public abstract class AbstractDocumentViewPart extends ViewPart implements IPartListener {

	private IEditorPart editor;
	private Document document;
	private DocumentAnnotationModel documentAnnotationModel;

	public Document getDocument() {
		return document;
	}

	public DocumentAnnotationModel getDocumentAnnotationModel() {
		return documentAnnotationModel;
	}

	public void connect( IEditorPart editor, DocumentEditorInput input ) {
		if ( isConnected() )
			disconnect();

		this.editor = editor;
		this.document = input.getDocument();
		this.documentAnnotationModel = (DocumentAnnotationModel) input.getDocumentProvider().getAnnotationModel( input );
	}

	public void disconnect() {
		this.editor = null;
		this.document = null;
		this.documentAnnotationModel = null;
	}

	public boolean isConnected() {
		return document != null;
	}

	@Override
	public void partActivated( IWorkbenchPart part ) {
		if ( part == null || !(part instanceof IEditorPart) )
			return;

		IEditorPart editorPart = (IEditorPart) part;

		if ( editorPart == editor ) // No need to reconnect already connected editor
			return;

		if ( isConnected() )
			disconnect();

		if ( !(editorPart.getEditorInput() instanceof DocumentEditorInput) )
			return;

		connect( editorPart, (DocumentEditorInput) editorPart.getEditorInput() );
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

	protected void connectToEditors() {
		getSite().getPage().addPartListener( this );

		partActivated( getSite().getPage().getActiveEditor() );
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void dispose() {
		getSite().getPage().removePartListener( this );

		if ( isConnected() )
			disconnect();

		super.dispose();
	}

	protected IViewReference getViewReference( String id ) {
		for ( IViewReference ref : getSite().getPage().getViewReferences() )
			if ( ref.getId().equals( id ) )
				return ref;

		return null;
	}

}
