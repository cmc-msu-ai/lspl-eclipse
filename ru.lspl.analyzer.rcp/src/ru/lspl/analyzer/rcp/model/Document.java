package ru.lspl.analyzer.rcp.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import ru.lspl.patterns.Pattern;
import ru.lspl.text.Match;
import ru.lspl.text.Node;
import ru.lspl.text.Text;
import ru.lspl.text.TextConfig;

/**
 * @author alno
 */
public class Document extends FileDocument {

	public boolean autoAnalyze = false;

	private boolean analysisNeeded = false;

	private TextConfig textConfig = new TextConfig();

	private ReentrantLock updateLock = new ReentrantLock();

	private Text analyzedText = null;
	private Set<Pattern> analyzedPatterns = new HashSet<Pattern>();

	private final Collection<IAnalysisListener> analysisListeners = new ArrayList<IAnalysisListener>();

	private final PatternSet patternSet = new PatternSet( this, updateLock );

	public TextConfig getTextConfig() {
		return textConfig;
	}

	public void setTextConfig( TextConfig textConfig ) {
		this.textConfig = textConfig;

		analysisNeeded();
	}

	public boolean isAnalysisNeeded() {
		return analysisNeeded;
	}

	public PatternSet getPatternSet() {
		return patternSet;
	}

	public List<Match> getMatches( Iterable<Pattern> patterns ) {
		if ( analyzedText == null )
			return null;

		List<Match> matches = new ArrayList<Match>();

		for ( Pattern p : patterns )
			matches.addAll( getMatches( p ) );

		return matches;
	}

	public List<Match> getMatches( Pattern pattern ) {
		if ( analyzedText != null && analyzedPatterns.contains( pattern ) )
			return analyzedText.getMatches( pattern );

		return null;
	}

	public List<Node> getNodes() {
		return analyzedText == null ? null : analyzedText.getNodes();
	}

	public IRunnableWithProgress createAnalyzeJob() {
		return new IRunnableWithProgress() {

			@Override
			public void run( IProgressMonitor monitor ) throws InvocationTargetException, InterruptedException {
				analyze( monitor );
			}

		};
	}

	public void addAnalysisListener( IAnalysisListener listener ) {
		analysisListeners.add( listener );
	}

	public void removeAnalysisListener( IAnalysisListener listener ) {
		analysisListeners.remove( listener );
	}

	protected void analyze( IProgressMonitor monitor ) {
		if ( !updateLock.tryLock() )
			return;

		try {
			monitor.beginTask( "Анализ документа...", 1 );

			analyzedText = Text.create( get(), textConfig );

			for ( Pattern pattern : patternSet.getDefinedPatternArray() )
				analyzedText.getMatches( pattern ); // Обработать текст шаблоном

			analyzedPatterns.addAll( patternSet.getDefinedPatternList() );
			analysisNeeded = false;

			monitor.worked( 1 );

			Display.getDefault().asyncExec( new Runnable() {

				@Override
				public void run() {
					fireAnalysisComplete();
				}

			} );
		} finally {
			updateLock.unlock();
		}
	}

	protected void analysisNeeded() {
		if ( autoAnalyze ) { // Если стоит флаг автоанализа, анализируем текст			
			try {
				PlatformUI.getWorkbench().getProgressService().busyCursorWhile( createAnalyzeJob() );
			} catch ( InvocationTargetException e ) {
				e.printStackTrace();
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		} else if ( !analysisNeeded ) {
			analysisNeeded = true;
			fireAnalysisNeedChanged();
		}
	}

	@Override
	protected void fireDocumentChanged( DocumentEvent event ) {
		super.fireDocumentChanged( event );

		analysisNeeded();
	}

	protected void fireAnalysisComplete() {
		for ( IAnalysisListener listener : analysisListeners )
			listener.analysisComplete( Document.this );

		for ( IAnalysisListener listener : analysisListeners )
			listener.analisysNeedChanged( Document.this ); // Извещаем подписчиков об анализе документа
	}

	protected void fireAnalysisNeedChanged() {
		for ( IAnalysisListener listener : analysisListeners )
			listener.analisysNeedChanged( Document.this ); // Извещаем подписчиков об анализе документа
	}

}
