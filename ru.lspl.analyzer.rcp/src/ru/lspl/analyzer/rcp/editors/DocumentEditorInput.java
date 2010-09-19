package ru.lspl.analyzer.rcp.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import ru.lspl.analyzer.rcp.model.Document;

public class DocumentEditorInput implements IEditorInput {

	private final Document document;

	public DocumentEditorInput( Document document ) {
		this.document = document;
	}

	@Override
	@SuppressWarnings( "rawtypes" )
	public Object getAdapter( Class adapter ) {
		return null;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return document.getSavedFileName() == null ? "Новый документ" : document.getSavedFileName();
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
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

	public Document getDocument() {
		return document;
	}

}
