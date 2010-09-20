package ru.lspl.analyzer.rcp.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.part.DrillDownAdapter;

import ru.lspl.analyzer.rcp.editors.DocumentEditorInput;
import ru.lspl.analyzer.rcp.model.Document;
import ru.lspl.analyzer.rcp.model.IAnalysisListener;
import ru.lspl.analyzer.rcp.providers.TextMatchesContentProvider;
import ru.lspl.analyzer.rcp.providers.TextMatchesLabelProvider;
import ru.lspl.patterns.Pattern;
import ru.lspl.text.Match;
import ru.lspl.text.Text;

public class MatchesView extends AbstractDocumentViewPart {

	private TreeViewer matchesViewer;
	private DrillDownAdapter drillDownAdapter;

	private PatternsView patternsView;

	private final TextMatchesContentProvider matchesContentProvider = new TextMatchesContentProvider();

	private final Collection<IMatchesViewListener> matchesViewListeners = new ArrayList<IMatchesViewListener>();

	private final IAnalysisListener documentListener = new IAnalysisListener() {

		@Override
		public void analisysNeedChanged( Document doc ) {
			matchesViewer.refresh();
		}

		@Override
		public void analysisComplete( Document doc ) {
			matchesViewer.refresh();
		}

	};

	private final IPatternsViewListener patternsViewListener = new IPatternsViewListener() {

		@Override
		public void patternSelect( Pattern pattern ) {
			matchesViewer.setInput( pattern );
		}

		@Override
		public void patternDoubleClick( Pattern pattern ) {
			// TODO Auto-generated method stub

		}

		@Override
		public void patternChecked( Pattern pattern, boolean checked, Set<Pattern> checkedPatterns ) {
			// TODO Auto-generated method stub

		}
	};

	public void addMatchSelectionListener( IMatchesViewListener listener ) {
		matchesViewListeners.add( listener );
	}

	public void removeMatchSelectionListener( IMatchesViewListener listener ) {
		matchesViewListeners.remove( listener );
	}

	@Override
	public void connect( IEditorPart editor, DocumentEditorInput input ) {
		super.connect( editor, input );

		Document document = getDocument();

		matchesContentProvider.setDocument( document );
		matchesViewer.setInput( document );

		matchesViewer.getControl().getParent().setEnabled( true );

		connectPatternsView();

		document.addAnalysisListener( documentListener );
	}

	@Override
	public void disconnect() {
		getDocument().removeAnalysisListener( documentListener );

		disconnectPatternsView();

		if ( !matchesViewer.getControl().isDisposed() ) {
			matchesViewer.getControl().getParent().setEnabled( false );

			matchesContentProvider.setDocument( null );
			matchesViewer.setInput( null );
		}

		super.disconnect();
	}

	protected void connectPatternsView() {
		IViewReference ref = getViewReference( PatternsView.ID );

		if ( ref == null )
			return;

		patternsView = ((PatternsView) ref.getView( true ));

		if ( patternsView != null ) {
			patternsView.addPatternListener( patternsViewListener );

			matchesViewer.setInput( patternsView.getSelectedPattern() );
		}
	}

	protected void disconnectPatternsView() {
		if ( patternsView != null )
			patternsView.removePatternListener( patternsViewListener );

		patternsView = null;
	}

	@Override
	public void createPartControl( Composite parent ) {
		parent.setLayout( new FillLayout() );

		createMatchesViewer( parent );

		IActionBars bars = getViewSite().getActionBars();

		fillLocalPullDown( bars.getMenuManager() );
		fillLocalToolBar( bars.getToolBarManager() );

		connectToEditors(); // Wait for editor activation
	}

	private void createMatchesViewer( Composite parent ) {
		matchesViewer = new TreeViewer( parent, SWT.BORDER );
		matchesViewer.setContentProvider( matchesContentProvider );
		matchesViewer.setLabelProvider( new TextMatchesLabelProvider() );
		matchesViewer.addDoubleClickListener( new IDoubleClickListener() {

			@Override
			public void doubleClick( DoubleClickEvent ev ) {
				Iterator<?> iter = ((IStructuredSelection) ev.getSelection()).iterator();

				while ( iter.hasNext() ) {
					Object obj = iter.next();

					if ( obj instanceof Text ) {
						for ( IMatchesViewListener listener : matchesViewListeners )
							listener.textDoubleClick( (Text) obj );
					}
					if ( obj instanceof Pattern ) {
						for ( IMatchesViewListener listener : matchesViewListeners )
							listener.patternDoubleClick( (Pattern) obj );
					} else if ( obj instanceof Match ) {
						for ( IMatchesViewListener listener : matchesViewListeners )
							listener.matchDoubleClick( (Match) obj );
					}
				}
			}
		} );

		drillDownAdapter = new DrillDownAdapter( matchesViewer );

		Tree matchesTree = matchesViewer.getTree();
		matchesTree.setHeaderVisible( true );
		matchesTree.setLinesVisible( true );

		/*
		 * Создаем колонки
		 */
		TreeColumn tc1 = new TreeColumn( matchesTree, SWT.LEFT );
		tc1.setText( "Текст" );
		tc1.setWidth( 300 );
		TreeColumn tc2 = new TreeColumn( matchesTree, SWT.LEFT );
		tc2.setText( "Параметры" );
		tc2.setWidth( 200 );
	}

	private void fillLocalPullDown( IMenuManager manager ) {
		//manager.add( action1 );
		//manager.add( new Separator() );
		//manager.add( action2 );
	}

	private void fillLocalToolBar( IToolBarManager manager ) {
		//manager.add( action1 );
		//manager.add( action2 );
		//manager.add( new Separator() );
		drillDownAdapter.addNavigationActions( manager );
	}
}
