package ru.lspl.ui.editors.rules;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class PatternsPartitionScanner extends RuleBasedPartitionScanner {

	public static final String LSPL_COMMENT = "__lspl_comment";
	public static final String[] CONTENT_TYPES = new String[] { LSPL_COMMENT };

	public PatternsPartitionScanner() {
		IToken comment = new Token( LSPL_COMMENT );

		IPredicateRule[] rules = new IPredicateRule[3];

		rules[0] = new MultiLineRule( "/*", "*/", comment ); // Multiline comments
		rules[1] = new EndOfLineRule( "//", comment ); // Single-line comments
		rules[2] = new EndOfLineRule( "#", comment ); // Single-line comments

		setPredicateRules( rules );
	}
}
