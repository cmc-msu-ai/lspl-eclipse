package ru.lspl.analyzer.rcp.views;

import ru.lspl.text.Node;
import ru.lspl.text.Word;

public interface IWordsViewListener {

	void wordSelect( Word word );

	void wordDoubleClick( Word word );

	void nodeSelect( Node node );

	void nodeDoubleClick( Node node );
}
