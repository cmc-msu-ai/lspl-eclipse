package ru.lspl.ui.model;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;

import ru.lspl.ui.model.access.PatternStorageAccess;

public class SourcePatternSet extends LsplPatternSet implements IDocumentListener {

	private IDocument document;

	private PatternStorageAccess storageAccess = new PatternStorageAccess();

	private boolean dirty = true;

	public SourcePatternSet( IDocument document ) {
		this.document = document;
		this.document.addDocumentListener( this );
	}

	public void dispose() {
		this.document.removeDocumentListener( this );
		this.document = null;
	}

	@Override
	public void documentAboutToBeChanged( DocumentEvent event ) {
	}

	@Override
	public void documentChanged( DocumentEvent event ) {
		dirty = true;
	}

	public void update() {
		if ( !dirty )
			return;

		clearPatterns();

		dirty = false;
		storageAccess.loadFromString( this, document.get() );
	}

}
