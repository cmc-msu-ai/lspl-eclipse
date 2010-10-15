package ru.lspl.analyzer.rcp.testers;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

import ru.lspl.analyzer.rcp.editors.DocumentEditorInput;
import ru.lspl.analyzer.rcp.model.LsplFileDocument;

public class DocumentStateTester extends PropertyTester {

	private static final String ANALYISIS_NEEDED = "analysisNeeded";

	public boolean test( Object receiver, String property, Object[] args, Object expectedValue ) {
		if ( !(receiver instanceof IEditorPart) )
			return false;

		IEditorInput input = ((IEditorPart) receiver).getEditorInput();

		if ( !(input instanceof DocumentEditorInput) )
			return false;

		LsplFileDocument document = ((DocumentEditorInput) input).getDocument();

		if ( property.equals( ANALYISIS_NEEDED ) )
			return expectedValue.equals( document.isAnalysisRequired() );

		return false;
	}

}
