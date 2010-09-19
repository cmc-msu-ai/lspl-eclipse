package ru.lspl.analyzer.rcp.editors;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextEditor;

import ru.lspl.analyzer.rcp.model.DocumentProvider;

public class DocumentTextEditor extends TextEditor {

	@Override
	protected void setDocumentProvider( IEditorInput input ) {
		setDocumentProvider( new DocumentProvider() );
	}

}
