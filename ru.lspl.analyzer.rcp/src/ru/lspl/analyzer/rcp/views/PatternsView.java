package ru.lspl.analyzer.rcp.views;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
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
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.DrillDownAdapter;

import ru.lspl.analyzer.rcp.editors.DocumentEditorInput;
import ru.lspl.analyzer.rcp.model.Document;
import ru.lspl.analyzer.rcp.model.IAnalysisListener;
import ru.lspl.analyzer.rcp.model.IPatternListener;
import ru.lspl.analyzer.rcp.providers.DefinedPatternsContentProvider;
import ru.lspl.analyzer.rcp.providers.PatternLabelProvider;
import ru.lspl.patterns.Alternative;
import ru.lspl.patterns.Pattern;

public class PatternsView extends AbstractDocumentViewPart implements IAnalysisListener, IPatternListener {

	public static final String ID = "ru.lspl.analyzer.rcp.views.PatternsView";

	private CheckboxTreeViewer patternsViewer;
	private DrillDownAdapter drillDownAdapter;

	private Text definePatternSource;
	private Button definePatternButton;

	private Pattern selectedPattern = null;

	private final Set<Pattern> checkedPatterns = new HashSet<Pattern>();

	private final PatternLabelProvider patternLabelProvider = new PatternLabelProvider();
	private final DefinedPatternsContentProvider patternContentProvider = new DefinedPatternsContentProvider();

	private final Collection<IPatternsViewListener> patternViewListeners = new LinkedList<IPatternsViewListener>();

	public void refresh() {
		patternsViewer.refresh();
	}

	public void removePatternListener( IPatternsViewListener patternListener ) {
		patternViewListeners.remove( patternListener );
	}

	public void addPatternListener( IPatternsViewListener patternListener ) {
		patternViewListeners.add( patternListener );
	}

	public Pattern getSelectedPattern() {
		return selectedPattern;
	}

	@Override
	public void connect( IEditorPart editor, DocumentEditorInput input ) {
		super.connect( editor, input );

		Document document = getDocument();

		patternLabelProvider.setDocument( document );
		patternsViewer.setInput( document );

		patternsViewer.getControl().getParent().setEnabled( true );

		document.addAnalysisListener( this );
		document.getPatternSet().addPatternListener( this );
	}

	@Override
	public void disconnect() {
		getDocument().removeAnalysisListener( this );
		getDocument().getPatternSet().removePatternListener( this );

		if ( !patternsViewer.getControl().isDisposed() ) {
			patternsViewer.getControl().getParent().setEnabled( false );

			patternLabelProvider.setDocument( null );
			patternsViewer.setInput( null );
		}

		super.disconnect();
	}

	public Set<Pattern> getCheckedPatterns() {
		return checkedPatterns;
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
		patternsViewer = new CheckboxTreeViewer( composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL );
		patternsViewer.setLabelProvider( patternLabelProvider );
		patternsViewer.setContentProvider( patternContentProvider );
		patternsViewer.setSorter( new ViewerSorter() );
		patternsViewer.addDoubleClickListener( new IDoubleClickListener() {

			@Override
			public void doubleClick( DoubleClickEvent ev ) {
				for ( Object obj : ((IStructuredSelection) ev.getSelection()).toArray() ) {
					if ( obj instanceof Pattern ) {
						Pattern pattern = (Pattern) obj;

						try {
							((MatchesView) getSite().getPage().showView( MatchesView.ID )).selectPattern( (Pattern) obj );
						} catch ( PartInitException e ) {
							e.printStackTrace();
						}

						for ( IPatternsViewListener listener : patternViewListeners )
							listener.patternDoubleClick( pattern );
					}
				}
			}
		} );

		patternsViewer.addSelectionChangedListener( new ISelectionChangedListener() {

			@Override
			public void selectionChanged( SelectionChangedEvent ev ) {
				for ( Object obj : ((IStructuredSelection) ev.getSelection()).toArray() ) {
					if ( obj instanceof Pattern ) {
						Pattern pattern = (Pattern) obj;

						selectedPattern = pattern; // Обновляем выбранный шаблон

						for ( IPatternsViewListener listener : patternViewListeners )
							listener.patternSelect( pattern );
					} else if ( obj instanceof Alternative ) {
						Pattern pattern = ((Alternative) obj).pattern;

						selectedPattern = pattern; // Обновляем выбранный шаблон

						for ( IPatternsViewListener listener : patternViewListeners )
							listener.patternSelect( pattern );
					}
				}
			}
		} );

		patternsViewer.addTreeListener( new ITreeViewerListener() {

			@Override
			public void treeCollapsed( TreeExpansionEvent ev ) {
			}

			@Override
			public void treeExpanded( TreeExpansionEvent ev ) {
				Object element = ev.getElement();

				if ( element instanceof Pattern ) {
					patternsViewer.setSubtreeChecked( element, patternsViewer.getChecked( element ) );
				}
			}

		} );

		patternsViewer.addCheckStateListener( new ICheckStateListener() {

			@Override
			public void checkStateChanged( CheckStateChangedEvent ev ) {
				Object element = ev.getElement();

				if ( element instanceof Pattern ) {
					patternsViewer.setSubtreeChecked( element, ev.getChecked() );

					patternChecked( (Pattern) element, ev.getChecked() );
				} else if ( element instanceof Alternative ) {
					Object parent = ((ITreeContentProvider) patternsViewer.getContentProvider()).getParent( element );
					patternsViewer.setSubtreeChecked( parent, ev.getChecked() );

					patternChecked( (Pattern) parent, ev.getChecked() );
				}
			}

			private void patternChecked( Pattern pattern, boolean checked ) {
				if ( checked )
					checkedPatterns.add( pattern );
				else
					checkedPatterns.remove( pattern );

				if ( isConnected() )
					getDocumentAnnotationModel().setSelectedPatterns( checkedPatterns );

				for ( IPatternsViewListener listener : patternViewListeners )
					listener.patternChecked( pattern, checked, checkedPatterns );
			}

		} );

		drillDownAdapter = new DrillDownAdapter( patternsViewer );

		GridData gridData = new GridData();
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;

		Tree patternsTree = patternsViewer.getTree();
		patternsTree.setLayoutData( gridData );
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

				getDocument().getPatternSet().createDefinePatternJob( definePatternSource.getText() ).schedule();
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

	@Override
	public void analysisRequired( Document doc ) {
		patternsViewer.refresh();
	}

	@Override
	public void analysisStarted( Document doc ) {
	}

	@Override
	public void analysisCompleted( Document doc ) {
		patternsViewer.refresh();
	}

	@Override
	public void patternsUpdated( Document document ) {
		patternsViewer.refresh();
	}

}