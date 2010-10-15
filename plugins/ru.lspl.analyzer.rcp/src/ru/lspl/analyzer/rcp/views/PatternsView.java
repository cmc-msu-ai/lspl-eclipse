package ru.lspl.analyzer.rcp.views;

import java.util.Set;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.DrillDownAdapter;

import ru.lspl.analyzer.rcp.editors.DocumentEditorInput;
import ru.lspl.analyzer.rcp.model.LsplFileDocument;
import ru.lspl.patterns.Pattern;
import ru.lspl.ui.model.ILsplDocument;
import ru.lspl.ui.model.ILsplPatternSet;
import ru.lspl.ui.model.LsplPatternSet;
import ru.lspl.ui.model.listeners.IAnalysisListener;
import ru.lspl.ui.model.listeners.IPatternListener;
import ru.lspl.ui.providers.content.PatternsContentProvider;
import ru.lspl.ui.providers.labels.PatternLabelProvider;
import ru.lspl.ui.viewers.IPatternsViewerListener;
import ru.lspl.ui.viewers.PatternsTreeViewer;

public class PatternsView extends AbstractDocumentViewPart implements IAnalysisListener, IPatternListener {

	public static final String ID = "ru.lspl.analyzer.rcp.views.PatternsView";

	private PatternsTreeViewer patternsViewer;
	private DrillDownAdapter drillDownAdapter;

	private Text definePatternSource;
	private Button definePatternButton;

	private final PatternLabelProvider patternLabelProvider = new PatternLabelProvider();
	private final PatternsContentProvider patternContentProvider = new PatternsContentProvider();

	public void refresh() {
		patternsViewer.refresh();
	}

	public void removePatternListener( IPatternsViewerListener patternListener ) {
		patternsViewer.removePatternListener( patternListener );
	}

	public void addPatternListener( IPatternsViewerListener patternListener ) {
		patternsViewer.addPatternListener( patternListener );
	}

	public Pattern getSelectedPattern() {
		return patternsViewer.getSelectedPattern();
	}

	@Override
	public void connect( IEditorPart editor, DocumentEditorInput input ) {
		super.connect( editor, input );

		LsplFileDocument document = getDocument();

		patternLabelProvider.setDocument( document );
		patternsViewer.setInput( document.getPatterns() );

		patternsViewer.getControl().getParent().setEnabled( true );

		document.addAnalysisListener( this );
		document.getPatterns().addPatternListener( this );
	}

	@Override
	public void disconnect() {
		getDocument().removeAnalysisListener( this );
		getDocument().getPatterns().removePatternListener( this );

		if ( !patternsViewer.getControl().isDisposed() ) {
			patternsViewer.getControl().getParent().setEnabled( false );

			patternLabelProvider.setDocument( null );
			patternsViewer.setInput( null );
		}

		super.disconnect();
	}

	public Set<Pattern> getCheckedPatterns() {
		return patternsViewer.getCheckedPatterns();
	}

	@Override
	public void createPartControl( Composite parent ) {
		createPatternsViewer( parent );
		createDefinitionControls( parent );

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;

		parent.setLayout( gridLayout );
		parent.setEnabled( false );

		IActionBars bars = getViewSite().getActionBars();

		fillLocalPullDown( bars.getMenuManager() );
		fillLocalToolBar( bars.getToolBarManager() );

		connectToEditors(); // Wait for editor activation
	}

	private void createPatternsViewer( Composite composite ) {
		patternsViewer = new PatternsTreeViewer( composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL );
		patternsViewer.setLabelProvider( patternLabelProvider );
		patternsViewer.setContentProvider( patternContentProvider );
		patternsViewer.setSorter( new ViewerSorter() );
		patternsViewer.addPatternListener( new IPatternsViewerListener() {

			public void patternSelect( Pattern pattern ) {
				// TODO Auto-generated method stub

			}

			public void patternDoubleClick( Pattern pattern ) {
				try {
					((MatchesView) getSite().getPage().showView( MatchesView.ID )).selectPattern( pattern );
				} catch ( PartInitException e ) {
					e.printStackTrace();
				}
			}

			public void patternChecked( Pattern pattern, boolean checked, Set<Pattern> checkedPatterns ) {
				if ( isConnected() )
					getDocumentAnnotationModel().setSelectedPatterns( checkedPatterns );
			}

		} );

		drillDownAdapter = new DrillDownAdapter( patternsViewer );

		GridData gridData = new GridData();
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;

		patternsViewer.getControl().setLayoutData( gridData );
	}

	private void createDefinitionControls( Composite composite ) {
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = GridData.CENTER;
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.heightHint = 50;
		gridData1.horizontalSpan = 2;
		definePatternSource = new Text( composite, SWT.BORDER | SWT.MULTI );
		definePatternSource.setLayoutData( gridData1 );

		definePatternButton = new Button( composite, SWT.NONE );
		definePatternButton.setText( "Добавить" );
		definePatternButton.addSelectionListener( new org.eclipse.swt.events.SelectionAdapter() {

			@Override
			public void widgetSelected( org.eclipse.swt.events.SelectionEvent e ) {
				if ( !isConnected() )
					return;

				((LsplPatternSet) getDocument().getPatterns()).createDefinePatternJob( definePatternSource.getText() ).schedule();
			}
		} );
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

	@Override
	public void setFocus() {
		patternsViewer.getControl().setFocus();
	}

	public void analysisRequired( ILsplDocument doc ) {
		patternsViewer.refresh();
	}

	public void analysisStarted( ILsplDocument doc ) {
	}

	public void analysisCompleted( ILsplDocument doc ) {
		patternsViewer.refresh();
	}

	public void patternsUpdated( ILsplPatternSet patterns ) {
		patternsViewer.refresh();
	}

}