package ru.lspl.analyzer.rcp.views;

import ru.lspl.patterns.Pattern;
import ru.lspl.text.Match;
import ru.lspl.text.Text;

public interface IMatchesViewListener {

	void patternSelect( Pattern pattern );

	void patternDoubleClick( Pattern pattern );

	void matchSelect( Match match );

	void matchDoubleClick( Match match );

	void textSelect( Text obj );

	void textDoubleClick( Text obj );
}
