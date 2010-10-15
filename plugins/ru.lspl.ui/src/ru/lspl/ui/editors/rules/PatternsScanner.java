package ru.lspl.ui.editors.rules;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.NumberRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

import ru.lspl.text.attributes.AttributeKey;
import ru.lspl.text.attributes.IndexedAttribute;
import ru.lspl.text.attributes.SpeechPart;
import ru.lspl.ui.DisplayManager;
import ru.lspl.ui.ILsplColorConstants;
import ru.lspl.ui.ILsplFontConstants;
import ru.lspl.ui.editors.PatternsSourceEditor;

public class PatternsScanner extends RuleBasedScanner {

	private final FontData fontData;

	private final DisplayManager manager;

	public PatternsScanner( PatternsSourceEditor editor, DisplayManager manager ) {
		this.manager = manager;
		this.fontData = editor.getFontData();

		IToken speechPartToken = createToken( ILsplColorConstants.SPEECH_PART, ILsplFontConstants.SPEECH_PART );
		IToken patternToken = createToken( ILsplColorConstants.PATTERN, ILsplFontConstants.PATTERN );
		IToken attributeToken = createToken( ILsplColorConstants.ATTRIBUTE, ILsplFontConstants.ATTRIBUTE );
		IToken numToken = createToken( ILsplColorConstants.NUMBER, ILsplFontConstants.NUMBER );

		IToken wordToken = createToken( ILsplColorConstants.WORD, ILsplFontConstants.WORD );
		IToken stringToken = createToken( ILsplColorConstants.STRING, ILsplFontConstants.STRING );

		WordRule patternRule = new WordRule( new PatternWordDetector(), patternToken );

		for ( SpeechPart sp : SpeechPart.values() ) {
			patternRule.addWord( sp.getAbbrevation(), speechPartToken );
		}

		WordRule attributeRule = new WordRule( new AttributeWordDetector(), wordToken );

		for ( int i = 0; i < AttributeKey.COUNT; ++i )
			attributeRule.addWord( AttributeKey.valueOf( i ).abbrevation, attributeToken );

		for ( int i = 0; i < 31; ++i )
			// FIXME Should be COUNT
			attributeRule.addWord( IndexedAttribute.valueOf( i ).abbrevation, attributeToken );

		IRule[] rules = new IRule[5];
		rules[0] = new SingleLineRule( "'", "'", stringToken );
		rules[1] = new SingleLineRule( "\"", "\"", stringToken );
		rules[2] = patternRule; // Patterns and speech parts
		rules[3] = attributeRule; // Attribute
		rules[4] = new NumberRule( numToken );

		setRules( rules );
	}

	protected Token createToken( RGB color, int fontStyle ) {
		return new Token(
				new TextAttribute( manager.getColor( color ), null, 0, manager.getFont( new FontData( fontData.getName(), (int) fontData.height, fontData.getStyle() | fontStyle ) ) ) );
	}
}
