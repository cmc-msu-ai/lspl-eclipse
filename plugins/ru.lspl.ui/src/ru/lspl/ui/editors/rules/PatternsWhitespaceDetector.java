package ru.lspl.ui.editors.rules;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class PatternsWhitespaceDetector implements IWhitespaceDetector {

	@Override
	public boolean isWhitespace( char c ) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
}
