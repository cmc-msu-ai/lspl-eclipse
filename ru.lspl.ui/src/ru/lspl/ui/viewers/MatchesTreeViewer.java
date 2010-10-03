package ru.lspl.ui.viewers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import ru.lspl.patterns.Pattern;
import ru.lspl.text.Match;
import ru.lspl.text.MatchGroup;
import ru.lspl.text.Text;

public class MatchesTreeViewer extends TreeViewer {

	private final Collection<IMatchesViewListener> matchesViewListeners = new ArrayList<IMatchesViewListener>();

	public MatchesTreeViewer( Composite parent, int style ) {
		super( parent, style );

		initialize();
	}

	public MatchesTreeViewer( Composite parent ) {
		super( parent );

		initialize();
	}

	public MatchesTreeViewer( Tree tree ) {
		super( tree );

		initialize();
	}

	public void addMatchSelectionListener( IMatchesViewListener listener ) {
		matchesViewListeners.add( listener );
	}

	public void removeMatchSelectionListener( IMatchesViewListener listener ) {
		matchesViewListeners.remove( listener );
	}

	private void initialize() {
		addDoubleClickListener( new IDoubleClickListener() {

			@Override
			public void doubleClick( DoubleClickEvent ev ) {
				Iterator<?> iter = ((IStructuredSelection) ev.getSelection()).iterator();

				while ( iter.hasNext() ) {
					Object obj = iter.next();

					if ( obj instanceof Text ) {
						for ( IMatchesViewListener listener : matchesViewListeners )
							listener.textDoubleClick( (Text) obj );
					} else if ( obj instanceof Pattern ) {
						for ( IMatchesViewListener listener : matchesViewListeners )
							listener.patternDoubleClick( (Pattern) obj );
					} else if ( obj instanceof Match ) {
						for ( IMatchesViewListener listener : matchesViewListeners )
							listener.matchDoubleClick( (Match) obj );
					} else if ( obj instanceof MatchGroup ) {
						for ( IMatchesViewListener listener : matchesViewListeners )
							listener.matchGroupDoubleClick( (MatchGroup) obj );
					}
				}
			}
		} );

		Tree matchesTree = getTree();
		matchesTree.setHeaderVisible( true );
		matchesTree.setLinesVisible( true );

		/*
		 * Создаем колонки
		 */
		TreeColumn tc1 = new TreeColumn( matchesTree, SWT.LEFT );
		tc1.setText( "Текст сопоставления" );
		tc1.setWidth( 300 );
		TreeColumn tc2 = new TreeColumn( matchesTree, SWT.LEFT );
		tc2.setText( "Контекст" );
		tc2.setWidth( 300 );
		TreeColumn tc3 = new TreeColumn( matchesTree, SWT.LEFT );
		tc3.setText( "Параметры" );
		tc3.setWidth( 200 );
	}

}
