package ru.lspl.ui.editors;

import org.eclipse.ui.editors.text.TextEditor;

public class PatternsSourceEditor extends TextEditor {

	public PatternsSourceEditor() {
		setSourceViewerConfiguration( new PatternsSourceConfiguration( this ) );
	}
}
