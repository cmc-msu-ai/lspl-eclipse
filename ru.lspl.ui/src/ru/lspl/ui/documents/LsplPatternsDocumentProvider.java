package ru.lspl.ui.documents;

import org.eclipse.ui.editors.text.TextFileDocumentProvider;

public class LsplPatternsDocumentProvider extends TextFileDocumentProvider {

	protected static class PatternsFileInfo extends FileInfo {

		private LsplSourcePatternSet patterns;

		public LsplSourcePatternSet getPatterns() {
			if ( patterns == null )
				patterns = new LsplSourcePatternSet( fTextFileBuffer.getDocument() );

			return patterns;
		}

		public void disposePatterns() {
			if ( patterns != null ) {
				patterns.dispose();
				patterns = null;
			}
		}

	};

	public LsplPatternsDocumentProvider() {
	}

	public LsplSourcePatternSet getPatterns( Object element ) {
		PatternsFileInfo info = (PatternsFileInfo) getFileInfo( element );

		return info != null ? info.getPatterns() : null;
	}

	@Override
	protected PatternsFileInfo createEmptyFileInfo() {
		return new PatternsFileInfo();
	}

	@Override
	protected void disposeFileInfo( Object element, FileInfo info ) {
		((PatternsFileInfo) info).disposePatterns();

		super.disposeFileInfo( element, info );
	}

}
