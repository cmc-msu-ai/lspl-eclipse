package ru.lspl.ui.model;

import java.util.List;

import ru.lspl.patterns.Pattern;
import ru.lspl.text.Match;
import ru.lspl.text.MatchGroup;
import ru.lspl.text.Node;
import ru.lspl.text.Transition;
import ru.lspl.text.Word;
import ru.lspl.ui.model.listeners.IAnalysisListener;

public interface ILsplDocument {

	ILsplPatternSet getPatterns();

	List<Match> getMatches( Iterable<Pattern> patterns );

	List<Match> getMatches( Pattern pattern );

	List<MatchGroup> getMatchGroups( Iterable<Pattern> patterns );

	List<MatchGroup> getMatchGroups( Pattern pattern );

	List<Node> getNodes();

	List<Transition> findTransitionsContainingPosition( int offset );

	List<Word> findWordsContainingPosition( int offset );

	List<Match> findMatchesContainingPosition( int offset );

	void addAnalysisListener( IAnalysisListener listener );

	void removeAnalysisListener( IAnalysisListener listener );

}
