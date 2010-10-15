package ru.lspl.ui.viewers;

import java.util.Set;

import ru.lspl.patterns.Pattern;

public interface IPatternsViewerListener {

	void patternSelect( Pattern pattern );

	void patternDoubleClick( Pattern pattern );

	void patternChecked( Pattern pattern, boolean checked, Set<Pattern> checkedPatterns );

}
