package ru.lspl.ui.documents;

import java.util.List;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;

import ru.lspl.patterns.Pattern;
import ru.lspl.ui.model.LsplPatternSet;
import ru.lspl.ui.model.access.PatternStorageAccess;

class LsplSourcePatternSet extends LsplPatternSet implements IDocumentListener {

	private IDocument document;

	private PatternStorageAccess storageAccess = new PatternStorageAccess();

	private boolean dirty = true;

	public LsplSourcePatternSet( IDocument document ) {
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

	@Override
	protected List<Pattern> getDelegatePatternList() {
		if ( dirty )
			update();

		return super.getDelegatePatternList();
	}

	private void update() {
		clearPatterns();

		dirty = false;
		storageAccess.loadFromString( this, document.get() );
	}

}
