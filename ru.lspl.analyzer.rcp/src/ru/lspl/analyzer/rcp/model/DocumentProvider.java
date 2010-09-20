package ru.lspl.analyzer.rcp.model;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.texteditor.AbstractDocumentProvider;

import ru.lspl.analyzer.rcp.editors.DocumentEditorInput;

public class DocumentProvider extends AbstractDocumentProvider {

	private final Document document;

	private final DocumentEditorInput input;

	public DocumentProvider( DocumentEditorInput input, Document document ) {
		this.input = input;
		this.document = document;
	}

	@Override
	public boolean isModifiable( Object element ) {
		Assert.isTrue( input == element );

		return true;
	}

	@Override
	protected IDocument createDocument( Object element ) throws CoreException {
		Assert.isTrue( input == element );

		return document;
	}

	@Override
	protected IAnnotationModel createAnnotationModel( Object element ) throws CoreException {
		Assert.isTrue( input == element );

		return null;
	}

	@Override
	public boolean isDeleted( Object element ) {
		Assert.isTrue( input == element );

		return !document.hasFileName();
	}

	@Override
	protected void doSaveDocument( IProgressMonitor monitor, Object element, IDocument doc, boolean overwrite ) throws CoreException {
		Assert.isTrue( document == doc );

		try {
			document.save( document.getFileName() );
		} catch ( Throwable e ) {
			e.printStackTrace();

			throw new CoreException( STATUS_ERROR );
		}
	}

	@Override
	protected IRunnableContext getOperationRunner( IProgressMonitor monitor ) {
		return null;
	}

}
