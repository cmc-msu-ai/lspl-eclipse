package ru.lspl.ui.editors;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.ui.editors.text.TextEditor;

public class PatternsSourceEditor extends TextEditor {

	public PatternsSourceEditor() {
		setSourceViewerConfiguration( new PatternsSourceConfiguration( this ) );
	}

	public FontData getFontData() {
		String name = getConfigurationElement() != null ? getConfigurationElement().getAttribute( "symbolicFontName" ) : null;

		if ( name != null )
			return JFaceResources.getFont( name ).getFontData()[0];

		if ( getPreferenceStore().contains( JFaceResources.TEXT_FONT ) && !getPreferenceStore().isDefault( JFaceResources.TEXT_FONT ) )
			return PreferenceConverter.getFontData( getPreferenceStore(), JFaceResources.TEXT_FONT );

		return JFaceResources.getTextFont().getFontData()[0];
	}
}
