package ru.lspl.analyzer.rcp.preferences;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import ru.lspl.analyzer.rcp.Activator;

public class DocumentEditorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public DocumentEditorPreferencePage() {
		super( GRID );
		setPreferenceStore( Activator.getDefault().getPreferenceStore() );
		setDescription( "Preferences for document editor" );
	}

	@Override
	public void createFieldEditors() {
		addField( new ComboFieldEditor( PreferenceConstants.ANNOTATION_HIGHLIGHT_EVENT, "Mouse event to highlight annotations", new String[][] { { "MouseHover", "HOVER" },
				{ "MouseMove", "MOVE" } }, getFieldEditorParent() ) );
	}

	@Override
	public void init( IWorkbench workbench ) {
	}

}