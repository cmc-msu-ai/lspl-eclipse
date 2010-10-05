package ru.lspl.ui.editors.rules;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.NumberRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

import ru.lspl.text.attributes.SpeechPart;
import ru.lspl.ui.DisplayManager;
import ru.lspl.ui.ILsplColorConstants;

public class PatternsScanner extends RuleBasedScanner {

	public PatternsScanner( DisplayManager manager ) {
		IToken speechPartToken = new Token( new TextAttribute( manager.getColor( ILsplColorConstants.SPEECH_PART ) ) );
		IToken patternToken = new Token( new TextAttribute( manager.getColor( ILsplColorConstants.PATTERN ) ) );
		IToken attributeToken = new Token( new TextAttribute( manager.getColor( ILsplColorConstants.ATTRIBUTE ) ) );

		IToken numToken = new Token( new TextAttribute( manager.getColor( ILsplColorConstants.NUMBER ) ) );

		IToken stringToken = new Token( new TextAttribute( manager.getColor( ILsplColorConstants.STRING ) ) );

		WordRule patternRule = new WordRule( new PatternWordDetector(), patternToken );

		for ( SpeechPart sp : SpeechPart.values() ) {
			patternRule.addWord( sp.getAbbrevation(), speechPartToken );
		}

		WordRule attributeRule = new WordRule( new AttributeWordDetector(), attributeToken );

		IRule[] rules = new IRule[5];
		rules[0] = new SingleLineRule( "'", "'", stringToken );
		rules[1] = new SingleLineRule( "\"", "\"", stringToken );
		rules[2] = patternRule; // Patterns and speech parts
		rules[3] = attributeRule; // Attribute
		rules[4] = new NumberRule( numToken );

		setRules( rules );
	}
}
