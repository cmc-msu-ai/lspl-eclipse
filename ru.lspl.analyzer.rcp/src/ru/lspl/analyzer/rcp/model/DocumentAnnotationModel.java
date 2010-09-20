package ru.lspl.analyzer.rcp.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.AnnotationModel;

import ru.lspl.analyzer.rcp.model.annotations.MatchRange;
import ru.lspl.analyzer.rcp.model.annotations.MatchRangeAnnotation;
import ru.lspl.analyzer.rcp.model.annotations.MatchRangeBuilder;
import ru.lspl.patterns.Pattern;
import ru.lspl.text.Match;

public class DocumentAnnotationModel extends AnnotationModel implements IAnalysisListener {

	private final MatchRangeBuilder matchRangeBuilder = new MatchRangeBuilder();

	private Document document;

	private Set<Pattern> selectedPatterns = Collections.emptySet();

	@Override
	protected void connected() {
		document = (Document) fDocument;
		document.addAnalysisListener( this );

		//	analysisComplete( document );
	}

	@Override
	protected void disconnected() {
		document.removeAnalysisListener( this );
		document = null;
	}

	@Override
	public void analisysNeedChanged( Document doc ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void analysisComplete( Document doc ) {
		removeAllAnnotations();
		addMatchRangeAnnotations( document.getAnalyzedText().getMatches( this.selectedPatterns ) );

		fireModelChanged();
	}

	public Set<Pattern> getSelectedPatterns() {
		return selectedPatterns;
	}

	public void setSelectedPatterns( Set<Pattern> selectedPatterns ) {
		this.selectedPatterns = selectedPatterns;

		analysisComplete( document );
	}

	private void addMatchRangeAnnotations( Collection<Match> matches ) {
		for ( MatchRange m : matchRangeBuilder.buildMatchRanges( matches, MatchRangeAnnotation.MAX_DEPTH ) ) {
			MatchRangeAnnotation ann = new MatchRangeAnnotation( m.depth );
			Position pos = new Position( m.start, m.end - m.start );

			try {
				addAnnotation( ann, pos, false );
			} catch ( BadLocationException e ) {
			}
		}
	}

}
