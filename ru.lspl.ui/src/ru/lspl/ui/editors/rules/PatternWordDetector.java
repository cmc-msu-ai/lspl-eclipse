package ru.lspl.ui.editors.rules;

import org.eclipse.jface.text.rules.IWordDetector;

public class PatternWordDetector implements IWordDetector {

	@Override
	public boolean isWordPart( char c ) {
		return Character.isLetter( c );
	}

	@Override
	public boolean isWordStart( char c ) {
		return Character.isUpperCase( c );
	}

}
