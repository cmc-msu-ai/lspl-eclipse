package ru.lspl.ui.viewers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import ru.lspl.patterns.Alternative;
import ru.lspl.patterns.Pattern;

public class PatternsTreeViewer extends CheckboxTreeViewer {

	private final Collection<IPatternsViewerListener> patternViewListeners = new ArrayList<IPatternsViewerListener>();

	private Pattern selectedPattern = null;

	private final Set<Pattern> checkedPatterns = new HashSet<Pattern>();

	public PatternsTreeViewer( Composite parent, int style ) {
		super( parent, style );

		initialize();
	}

	public PatternsTreeViewer( Composite parent ) {
		super( parent );

		initialize();
	}

	public PatternsTreeViewer( Tree tree ) {
		super( tree );

		initialize();
	}

	public void removePatternListener( IPatternsViewerListener patternListener ) {
		patternViewListeners.remove( patternListener );
	}

	public void addPatternListener( IPatternsViewerListener patternListener ) {
		patternViewListeners.add( patternListener );
	}

	private void initialize() {
		addTreeListener( new ITreeViewerListener() {

			public void treeCollapsed( TreeExpansionEvent ev ) {
			}

			public void treeExpanded( TreeExpansionEvent ev ) {
				Object element = ev.getElement();

				if ( element instanceof Pattern ) {
					setSubtreeChecked( element, getChecked( element ) );
				}
			}

		} );

		addCheckStateListener( new ICheckStateListener() {

			public void checkStateChanged( CheckStateChangedEvent ev ) {
				Object element = ev.getElement();
				Object pattern;

				if ( element instanceof Pattern ) {
					pattern = element;
				} else if ( element instanceof Alternative ) {
					pattern = ((ITreeContentProvider) getContentProvider()).getParent( element );
				} else {
					throw new RuntimeException( "Unknown selection" );
				}

				setSubtreeChecked( pattern, ev.getChecked() );
				patternChecked( (Pattern) pattern, ev.getChecked() );
			}

			private void patternChecked( Pattern pattern, boolean checked ) {
				if ( checked )
					checkedPatterns.add( pattern );
				else
					checkedPatterns.remove( pattern );

				for ( IPatternsViewerListener listener : patternViewListeners )
					listener.patternChecked( pattern, checked, checkedPatterns );
			}

		} );

		addDoubleClickListener( new IDoubleClickListener() {

			public void doubleClick( DoubleClickEvent ev ) {
				for ( Object obj : ((IStructuredSelection) ev.getSelection()).toArray() ) {
					if ( obj instanceof Pattern ) {
						for ( IPatternsViewerListener listener : patternViewListeners )
							listener.patternDoubleClick( (Pattern) obj );
					}
				}
			}
		} );

		addSelectionChangedListener( new ISelectionChangedListener() {

			public void selectionChanged( SelectionChangedEvent ev ) {
				for ( Object obj : ((IStructuredSelection) ev.getSelection()).toArray() ) {
					if ( obj instanceof Pattern ) {
						Pattern pattern = (Pattern) obj;

						selectedPattern = pattern; // Обновляем выбранный шаблон

						for ( IPatternsViewerListener listener : patternViewListeners )
							listener.patternSelect( pattern );
					} else if ( obj instanceof Alternative ) {
						Pattern pattern = ((Alternative) obj).pattern;

						selectedPattern = pattern; // Обновляем выбранный шаблон

						for ( IPatternsViewerListener listener : patternViewListeners )
							listener.patternSelect( pattern );
					}
				}
			}
		} );

		Tree patternsTree = getTree();
		patternsTree.setHeaderVisible( true );
		patternsTree.setLinesVisible( true );

		TreeColumn tcPattern = new TreeColumn( patternsTree, SWT.LEFT );
		tcPattern.setText( "Шаблон" );
		tcPattern.setWidth( 180 );

		TreeColumn tcMatchGroups = new TreeColumn( patternsTree, SWT.LEFT );
		tcMatchGroups.setText( "Отрезков" );
		tcMatchGroups.setWidth( 70 );

		TreeColumn tcMatches = new TreeColumn( patternsTree, SWT.LEFT );
		tcMatches.setText( "Сопоставлений" );
		tcMatches.setWidth( 70 );

		TreeColumn tcMatchVariants = new TreeColumn( patternsTree, SWT.LEFT );
		tcMatchVariants.setText( "Вариантов" );
		tcMatchVariants.setWidth( 70 );
	}

	public Pattern getSelectedPattern() {
		return selectedPattern;
	}

	public Set<Pattern> getCheckedPatterns() {
		return checkedPatterns;
	}

}
