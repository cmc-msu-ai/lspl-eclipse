package ru.lspl.analyzer.rcp.views;

import java.util.Set;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.part.DrillDownAdapter;

import ru.lspl.analyzer.rcp.editors.DocumentEditor;
import ru.lspl.analyzer.rcp.editors.DocumentEditorInput;
import ru.lspl.analyzer.rcp.model.LsplFileDocument;
import ru.lspl.patterns.Pattern;
import ru.lspl.text.Match;
import ru.lspl.text.MatchGroup;
import ru.lspl.text.TextRange;
import ru.lspl.ui.model.ILsplDocument;
import ru.lspl.ui.model.listeners.IAnalysisListener;
import ru.lspl.ui.providers.content.TextMatchesContentProvider;
import ru.lspl.ui.providers.labels.TextMatchesLabelProvider;
import ru.lspl.ui.viewers.IPatternsViewerListener;
import ru.lspl.ui.viewers.MatchesTreeViewer;
import ru.lspl.ui.viewers.MatchesViewAdapter;

public class MatchesView extends AbstractDocumentViewPart {

	public static final String ID = "ru.lspl.analyzer.rcp.views.MatchesView";

	private MatchesTreeViewer matchesViewer;
	private DrillDownAdapter drillDownAdapter;

	private PatternsView patternsView;

	private final TextMatchesContentProvider matchesContentProvider = new TextMatchesContentProvider();

	private final IAnalysisListener documentListener = new IAnalysisListener() {

		@Override
		public void analysisRequired( ILsplDocument doc ) {
			matchesViewer.refresh();
		}

		@Override
		public void analysisStarted( ILsplDocument doc ) {
		}

		@Override
		public void analysisCompleted( ILsplDocument doc ) {
			matchesViewer.refresh();
		}

	};

	private final IPatternsViewerListener patternsViewListener = new IPatternsViewerListener() {

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

	public void selectPattern( Pattern pattern ) {
		matchesViewer.setInput( pattern );
	}

	@Override
	public void connect( IEditorPart editor, DocumentEditorInput input ) {
		super.connect( editor, input );

		LsplFileDocument document = getDocument();

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
		matchesViewer = new MatchesTreeViewer( parent, SWT.BORDER );
		matchesViewer.setContentProvider( matchesContentProvider );
		matchesViewer.setLabelProvider( new TextMatchesLabelProvider() );
		matchesViewer.addMatchSelectionListener( new MatchesViewAdapter() {

			@Override
			public void matchGroupDoubleClick( MatchGroup group ) {
				selectRange( group );
			}

			@Override
			public void matchDoubleClick( Match match ) {
				selectRange( match );
			}

			private void selectRange( TextRange range ) {
				IEditorPart editor = getEditor();

				if ( editor instanceof DocumentEditor ) {
					((DocumentEditor) editor).selectRange( range );
				} else {
					System.out.println( editor );
				}
			}

		} );

		drillDownAdapter = new DrillDownAdapter( matchesViewer );
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
