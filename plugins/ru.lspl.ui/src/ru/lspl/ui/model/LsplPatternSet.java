package ru.lspl.ui.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import ru.lspl.patterns.Pattern;
import ru.lspl.patterns.PatternBuilder;
import ru.lspl.patterns.PatternBuildingException;
import ru.lspl.ui.model.listeners.IPatternListener;

public class LsplPatternSet extends AbstractList<Pattern> implements ILsplPatternSet {

	private final Lock updateLock;

	private PatternBuilder patternBuilder = PatternBuilder.create();
	private Collection<IPatternListener> patternListeners = new ArrayList<IPatternListener>();

	private Pattern[] definedPatterns = null;

	public LsplPatternSet() {
		this( new ReentrantLock() );
	}

	public LsplPatternSet( Lock updateLock ) {
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

	@Override
	public void addPatternListener( IPatternListener listener ) {
		patternListeners.add( listener );
	}

	@Override
	public void removePatternListener( IPatternListener listener ) {
		patternListeners.remove( listener );
	}

	public void clearPatterns() {
		patternBuilder = PatternBuilder.create();
		definedPatterns = null;

		firePatternsUpdated();
	}

	public Job createDefinePatternJob( final String source ) {
		return new Job( "Defining pattern" ) {

			@Override
			public IStatus run( IProgressMonitor monitor ) {
				definePatterns( Collections.singletonList( source ), monitor, true );

				return Status.OK_STATUS;
			}

		};
	}

	public Job createDefinePatternsJob( final Iterable<String> sources ) {
		return new Job( "Defining patterns" ) {

			@Override
			public IStatus run( IProgressMonitor monitor ) {
				definePatterns( sources, monitor, true );

				return Status.OK_STATUS;
			}

		};
	}

	@Override
	public ListIterator<Pattern> listIterator() {
		return getDelegatePatternList().listIterator();
	}

	@Override
	public ListIterator<Pattern> listIterator( int index ) {
		return getDelegatePatternList().listIterator( index );
	}

	@Override
	public Iterator<Pattern> iterator() {
		return getDelegatePatternList().iterator();
	}

	@Override
	public int size() {
		return getDelegatePatternList().size();
	}

	@Override
	public Pattern get( int index ) {
		return getDelegatePatternList().get( index );
	}

	protected List<Pattern> getDelegatePatternList() {
		return patternBuilder.getDefinedPatterns();
	}

	protected void definePatterns( Iterable<String> sources, IProgressMonitor monitor, boolean notifyUpdate ) {
		updateLock.lock();

		try {
			monitor.beginTask( "Определение шаблона...", 1 );

			for ( final String source : sources ) {
				try {
					patternBuilder.build( source );
				} catch ( final PatternBuildingException e ) {
					Display.getDefault().asyncExec( new Runnable() {

						@Override
						public void run() {
							MessageBox mb = new MessageBox( PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OK | SWT.ICON_ERROR );
							mb.setText( "Ошибка компиляции шаблона" );
							mb.setMessage( source + " - " + e.getMessage() );
							mb.open();
						}

					} );
				}
			}
			definedPatterns = null;

			monitor.worked( 1 );
		} finally {
			updateLock.unlock();
		}

		if ( notifyUpdate ) {
			Display.getDefault().asyncExec( new Runnable() {

				@Override
				public void run() {
					firePatternsUpdated();
				}

			} );
		}
	}

	protected void firePatternsUpdated() {
		for ( IPatternListener listener : patternListeners )
			listener.patternsUpdated( this ); // Извещаем подписчиков об анализе документа
	}

}