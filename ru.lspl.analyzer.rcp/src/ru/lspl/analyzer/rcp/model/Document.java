package ru.lspl.analyzer.rcp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.swt.widgets.Display;

import ru.lspl.patterns.Pattern;
import ru.lspl.text.Match;
import ru.lspl.text.MatchGroup;
import ru.lspl.text.Node;
import ru.lspl.text.Text;
import ru.lspl.text.Transition;
import ru.lspl.text.Word;

/**
 * @author alno
 */
public class Document extends FileDocument {

	private final ReentrantLock updateLock = new ReentrantLock();

	private boolean configChanged = false;
	private boolean textChanged = false;
	private boolean patternsChanged = false;

	private Text analyzedText = null;
	private Set<Pattern> analyzedPatterns = new HashSet<Pattern>();
	private DocumentConfig config = new DocumentConfig();

	private final Collection<IAnalysisListener> analysisListeners = new ArrayList<IAnalysisListener>();

	private final PatternSet patternSet = new PatternSet( this, updateLock );

	public DocumentConfig getConfig() {
		return config;
	}

	public void setConfig( DocumentConfig config ) {
		this.config = config;
		this.configChanged = true;

		fireAnalysisRequired();
	}

	public boolean isTextChanged() {
		return textChanged;
	}

	public boolean isPatternsChanged() {
		return patternsChanged;
	}

	public boolean isAnalysisRequired() {
		return configChanged || textChanged || patternsChanged;
	}

	public boolean isAutoAnalysisAllowed() {
		return (configChanged && config.analyzeOnConfigChange) || (textChanged && config.analyzeOnTextChange) || (patternsChanged && config.analyzeOnPatternsChange);
	}

	public PatternSet getPatternSet() {
		return patternSet;
	}

	public List<Match> getMatches( Iterable<Pattern> patterns ) {
		if ( analyzedText == null )
			return null;

		List<Match> matches = new ArrayList<Match>();

		for ( Pattern p : patterns ) {
			List<Match> pm = getMatches( p );

			if ( pm != null )
				matches.addAll( pm );
		}

		return matches;
	}

	public List<Match> getMatches( Pattern pattern ) {
		if ( analyzedText != null && analyzedPatterns.contains( pattern ) )
			return analyzedText.getMatches( pattern );

		return null;
	}

	public List<MatchGroup> getMatchGroups( Iterable<Pattern> patterns ) {
		if ( analyzedText == null )
			return null;

		List<MatchGroup> matchGroups = new ArrayList<MatchGroup>();

		for ( Pattern p : patterns ) {
			List<MatchGroup> pm = getMatchGroups( p );

			if ( pm != null )
				matchGroups.addAll( pm );
		}

		return matchGroups;
	}

	public List<MatchGroup> getMatchGroups( Pattern pattern ) {
		if ( analyzedText != null && analyzedPatterns.contains( pattern ) )
			return analyzedText.getMatchGroups( pattern );

		return Collections.emptyList();
	}

	public List<Match> findMatchesContainingPosition( int offset ) {
		if ( analyzedText != null )
			return analyzedText.findMatchesContainingPosition( offset );

		return Collections.emptyList();
	}

	public List<Word> findWordsContainingPosition( int offset ) {
		if ( analyzedText != null )
			return analyzedText.findWordsContainingPosition( offset );

		return Collections.emptyList();
	}

	public List<Transition> findTransitionsContainingPosition( int offset ) {
		if ( analyzedText != null )
			return analyzedText.findTransitionsContainingPosition( offset );

		return Collections.emptyList();
	}

	public List<Node> getNodes() {
		return analyzedText == null ? null : analyzedText.getNodes();
	}

	public Job createAnalyzeJob() {
		return new Job( "Analysing document" ) {

			@Override
			public IStatus run( IProgressMonitor monitor ) {
				analyze( monitor );

				return Status.OK_STATUS;
			}

		};
	}

	protected void analyze( IProgressMonitor monitor ) {
		if ( !updateLock.tryLock() )
			return;

		boolean analyzeText = textChanged || configChanged;
		String analyzeContent = get();
		Pattern[] analyzePatterns = patternSet.getDefinedPatternArray();

		textChanged = false;
		configChanged = false;
		patternsChanged = false;

		Display.getDefault().asyncExec( new Runnable() {

			@Override
			public void run() {
				fireAnalysisStarted();
			}

		} );

		try {
			monitor.beginTask( "Анализ документа...", 1 );

			if ( analyzeText )
				analyzedText = Text.create( analyzeContent, config );

			for ( Pattern pattern : analyzePatterns )
				analyzedText.getMatches( pattern ); // Find paterns in text

			for ( Pattern pattern : analyzePatterns )
				analyzedPatterns.add( pattern ); // Mark patterns as analyzed

			monitor.worked( 1 );
		} finally {
			updateLock.unlock();
		}

		Display.getDefault().asyncExec( new Runnable() {

			@Override
			public void run() {
				fireAnalysisCompleted();
			}

		} );
	}

	public void addAnalysisListener( IAnalysisListener listener ) {
		analysisListeners.add( listener );
	}

	public void removeAnalysisListener( IAnalysisListener listener ) {
		analysisListeners.remove( listener );
	}

	@Override
	protected void fireDocumentChanged( DocumentEvent event ) {
		super.fireDocumentChanged( event );

		textChanged = true;

		fireAnalysisRequired();
	}

	protected void fireAnalysisRequired() {
		if ( isAutoAnalysisAllowed() ) {
			createAnalyzeJob().schedule();
		} else {
			for ( IAnalysisListener listener : analysisListeners )
				listener.analysisRequired( Document.this ); // Извещаем подписчиков об анализе документа
		}
	}

	protected void fireAnalysisStarted() {
		for ( IAnalysisListener listener : analysisListeners )
			listener.analysisStarted( Document.this );
	}

	protected void fireAnalysisCompleted() {
		for ( IAnalysisListener listener : analysisListeners )
			listener.analysisCompleted( Document.this );
	}

	protected void firePatternsChanged() {
		patternsChanged = true;

		fireAnalysisRequired();
	}

}
