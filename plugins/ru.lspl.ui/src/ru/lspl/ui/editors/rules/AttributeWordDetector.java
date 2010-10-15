package ru.lspl.ui.editors.rules;

import org.eclipse.jface.text.rules.IWordDetector;

public class AttributeWordDetector implements IWordDetector {

	public boolean isWordPart( char c ) {
		return Character.isLowerCase( c );
	}

	public boolean isWordStart( char c ) {
		return Character.isLowerCase( c );
	}

}
