package ru.lspl.ui.editors.rules;

import org.eclipse.jface.text.rules.IWordDetector;

public class AttributeWordDetector implements IWordDetector {

	@Override
	public boolean isWordPart( char c ) {
		return Character.isLowerCase( c );
	}

	@Override
	public boolean isWordStart( char c ) {
		return Character.isLowerCase( c );
	}

}
