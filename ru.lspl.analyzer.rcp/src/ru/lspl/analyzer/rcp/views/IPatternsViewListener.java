package ru.lspl.analyzer.rcp.views;

import java.util.Set;

import ru.lspl.patterns.Pattern;

public interface IPatternsViewListener {

	void patternSelect( Pattern pattern );

	void patternDoubleClick( Pattern pattern );

	void patternChecked( Pattern pattern, boolean checked, Set<Pattern> checkedPatterns );

}
