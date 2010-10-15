package ru.lspl.ui.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;

import ru.lspl.ui.editors.rules.PatternsPartitionScanner;
import ru.lspl.ui.model.SourcePatternSet;

public class PatternsDocumentProvider extends TextFileDocumentProvider {

	protected static class PatternsFileInfo extends FileInfo {

		private SourcePatternSet patterns;

		public SourcePatternSet getPatterns() {
			if ( patterns == null )
				patterns = new SourcePatternSet( fTextFileBuffer.getDocument() );

			return patterns;
		}

		public void disposePatterns() {
			if ( patterns != null ) {
				patterns.dispose();
				patterns = null;
			}
		}

	};

	public PatternsDocumentProvider() {
	}

	public SourcePatternSet getPatterns( Object element ) {
		PatternsFileInfo info = (PatternsFileInfo) getFileInfo( element );

		return info != null ? info.getPatterns() : null;
	}

	@Override
	protected PatternsFileInfo createEmptyFileInfo() {
		return new PatternsFileInfo();
	}

	@Override
	protected FileInfo createFileInfo( Object element ) throws CoreException {
		FileInfo info = super.createFileInfo( element );

		if ( info == null )
			return null;

		IDocument document = info.fTextFileBuffer.getDocument();

		if ( document != null ) {
			IDocumentPartitioner partitioner = new FastPartitioner( new PatternsPartitionScanner(), PatternsPartitionScanner.CONTENT_TYPES );
			partitioner.connect( document );
			document.setDocumentPartitioner( partitioner );
		}

		return info;
	}

	@Override
	protected void disposeFileInfo( Object element, FileInfo info ) {
		((PatternsFileInfo) info).disposePatterns();

		super.disposeFileInfo( element, info );
	}

}
