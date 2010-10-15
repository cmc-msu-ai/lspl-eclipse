package ru.lspl.ui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.AnnotationModel;

import ru.lspl.patterns.Pattern;
import ru.lspl.text.Match;
import ru.lspl.ui.model.annotations.AnnotationWithPosition;
import ru.lspl.ui.model.annotations.MatchRange;
import ru.lspl.ui.model.annotations.MatchRangeAnnotation;
import ru.lspl.ui.model.annotations.MatchRangeBuilder;
import ru.lspl.ui.model.listeners.IAnalysisListener;

public class LsplDocumentAnnotationModel extends AnnotationModel implements IAnalysisListener {

	private final MatchRangeBuilder matchRangeBuilder = new MatchRangeBuilder();

	private ILsplDocument document;

	private Set<Pattern> selectedPatterns = Collections.emptySet();

	private Collection<AnnotationWithPosition> annotations = new ArrayList<AnnotationWithPosition>();

	private int lastHoverOffset = -1;

	@Override
	protected void connected() {
		document = (ILsplDocument) fDocument;
		document.addAnalysisListener( this );
	}

	@Override
	protected void disconnected() {
		document.removeAnalysisListener( this );
		document = null;

		selectedPatterns = Collections.emptySet();
		annotations.clear();
	}

	public void analysisRequired( ILsplDocument doc ) {
	}

	public void analysisStarted( ILsplDocument doc ) {
	}

	public void analysisCompleted( ILsplDocument doc ) {
		// Building selected annotations
		annotations.clear();
		addMatchRangeAnnotations( document.getMatches( selectedPatterns ) );

		// Displaying annotations
		showAllAnnotations();

		fireModelChanged();
	}

	public Set<Pattern> getSelectedPatterns() {
		return selectedPatterns;
	}

	public void setSelectedPatterns( Set<Pattern> selectedPatterns ) {
		this.selectedPatterns = selectedPatterns;

		analysisCompleted( document );
	}

	private void addMatchRangeAnnotations( Collection<Match> matches ) {
		for ( MatchRange m : matchRangeBuilder.buildMatchRanges( matches, MatchRangeAnnotation.MAX_DEPTH ) )
			annotations.add( new AnnotationWithPosition( new MatchRangeAnnotation( m.depth ), new Position( m.start, m.end - m.start ) ) );
	}

	public void showHoveredAnnotations( int offset ) {
		if ( offset == lastHoverOffset )
			return;

		removeAllAnnotations( false );
		for ( MatchRange m : matchRangeBuilder.buildMatchRanges( document.findMatchesContainingPosition( offset ), MatchRangeAnnotation.MAX_DEPTH ) ) {
			try {
				addAnnotation( new MatchRangeAnnotation( m.depth ), new Position( m.start, m.end - m.start ), false );
			} catch ( BadLocationException e ) {
			}
		}
		fireModelChanged();

		lastHoverOffset = offset;
	}

	public void showAllAnnotations() {
		removeAllAnnotations( false );
		for ( AnnotationWithPosition ap : annotations ) {
			try {
				addAnnotation( ap.annotation, ap.position, false );
			} catch ( BadLocationException e ) {
			}
		}
		fireModelChanged();

		lastHoverOffset = -1;
	}

}
