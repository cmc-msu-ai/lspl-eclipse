package ru.lspl.analyzer.rcp.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Lock;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import ru.lspl.patterns.Pattern;
import ru.lspl.patterns.PatternBuilder;
import ru.lspl.patterns.PatternBuildingException;

public class PatternSet {

	private final Document document;
	private final Lock updateLock;

	private PatternBuilder patternBuilder = PatternBuilder.create();
	private Collection<IPatternListener> patternListeners = new ArrayList<IPatternListener>();

	private Pattern[] definedPatterns = null;

	public PatternSet( Document document, Lock updateLock ) {
		this.document = document;
		this.updateLock = updateLock;
	}

	public Pattern[] getDefinedPatternArray() {
		if ( definedPatterns != null ) // Если шаблоны уже определены
			return definedPatterns; // Возвращаем их

		return (definedPatterns = patternBuilder.getDefinedPatternsArray());
	}

	public List<Pattern> getDefinedPatternList() {
		return patternBuilder.definedPatterns;
	}

	public void addPatternListener( IPatternListener listener ) {
		patternListeners.add( listener );
	}

	public void removePatternListener( IPatternListener listener ) {
		patternListeners.remove( listener );
	}

	public void clearPatterns() {
		patternBuilder = PatternBuilder.create();
		definedPatterns = null;

		firePatternsUpdated();
		document.analysisNeeded();
	}

	public IRunnableWithProgress createDefinePatternJob( final String source ) {
		return new IRunnableWithProgress() {

			@Override
			public void run( IProgressMonitor monitor ) throws InvocationTargetException, InterruptedException {
				try {
					buildPattern( source, monitor );
				} catch ( PatternBuildingException e ) {
					MessageBox mb = new MessageBox( PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OK | SWT.ICON_ERROR );
					mb.setText( "Ошибка компиляции шаблона" );
					mb.setMessage( e.getMessage() );
					mb.open();
				}
			}

		};
	}

	protected void buildPattern( String source, IProgressMonitor monitor ) throws PatternBuildingException {
		updateLock.lock();

		try {
			monitor.beginTask( "Определение шаблона...", 1 );

			patternBuilder.build( source );
			definedPatterns = null;

			monitor.worked( 1 );

			Display.getDefault().asyncExec( new Runnable() {

				@Override
				public void run() {
					firePatternsUpdated();
					document.analysisNeeded();
				}

			} );
		} finally {
			updateLock.unlock();
		}
	}

	protected void firePatternsUpdated() {
		for ( IPatternListener listener : patternListeners )
			listener.patternsUpdated( document ); // Извещаем подписчиков об анализе документа
	}

}