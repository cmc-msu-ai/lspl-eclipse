package ru.lspl.analyzer.rcp.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import ru.lspl.analyzer.rcp.model.DocumentProvider;
import ru.lspl.analyzer.rcp.model.LsplFileDocument;

public class DocumentEditorInput implements IEditorInput {

	private final LsplFileDocument document;

	private final DocumentProvider documentProvider;

	public DocumentEditorInput( LsplFileDocument document ) {
		this.document = document;
		this.documentProvider = new DocumentProvider( this, document );
	}

	@SuppressWarnings( "rawtypes" )
	public Object getAdapter( Class adapter ) {
		return null;
	}

	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		return document.getFileName() == null ? "Новый документ" : document.getFileName();
	}

	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		// TODO Auto-generated method stub
		return "Документ";
	}

	@Override
	public int hashCode() {
		return document.hashCode();
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass() != obj.getClass() )
			return false;

		return document.equals( ((DocumentEditorInput) obj).document );
	}

	public LsplFileDocument getDocument() {
		return document;
	}

	public DocumentProvider getDocumentProvider() {
		return documentProvider;
	}

}
