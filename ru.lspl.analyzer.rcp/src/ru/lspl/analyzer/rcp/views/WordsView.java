package ru.lspl.analyzer.rcp.views;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.DrillDownAdapter;

import ru.lspl.analyzer.rcp.editors.DocumentEditor;
import ru.lspl.analyzer.rcp.editors.DocumentEditorInput;
import ru.lspl.analyzer.rcp.model.LsplFileDocument;
import ru.lspl.text.Node;
import ru.lspl.text.TextRange;
import ru.lspl.text.Transition;
import ru.lspl.text.Word;
import ru.lspl.text.attributes.SpeechPart;
import ru.lspl.ui.model.ILsplDocument;
import ru.lspl.ui.model.listeners.IAnalysisListener;
import ru.lspl.ui.providers.content.SimpleContentProvider;
import ru.lspl.ui.providers.content.TextWordsContentProvider;
import ru.lspl.ui.providers.labels.SpeechPartLabelProvider;
import ru.lspl.ui.providers.labels.TextWordsLabelProvider;
import ru.lspl.ui.viewers.IWordsViewListener;

public class WordsView extends AbstractDocumentViewPart {

	public static final String ID = "ru.lspl.analyzer.rcp.views.WordsView";

	private TreeViewer wordsViewer;
	private DrillDownAdapter drillDownAdapter;

	private ComboViewer speechPartViewer;

	private final ArrayList<IWordsViewListener> wordSelectionListeners = new ArrayList<IWordsViewListener>();

	private final IAnalysisListener analysisListener = new IAnalysisListener() {

		@Override
		public void analysisRequired( ILsplDocument doc ) {
			wordsViewer.refresh();
		}

		@Override
		public void analysisStarted( ILsplDocument doc ) {
		}

		@Override
		public void analysisCompleted( ILsplDocument doc ) {
			wordsViewer.refresh();
		}
	};

	public void addWordSelectionListener( IWordsViewListener listener ) {
		wordSelectionListeners.add( listener );
	}

	public void removeWordSelectionListener( IWordsViewListener listener ) {
		wordSelectionListeners.remove( listener );
	}

	@Override
	public void connect( IEditorPart editor, DocumentEditorInput input ) {
		super.connect( editor, input );

		LsplFileDocument document = getDocument();

		document.addAnalysisListener( analysisListener );

		wordsViewer.setInput( document );
	}

	@Override
	public void disconnect() {
		getDocument().removeAnalysisListener( analysisListener );

		if ( !wordsViewer.getControl().isDisposed() ) {
			wordsViewer.setInput( null );
		}

		super.disconnect();
	}

	@Override
	public void createPartControl( Composite parent ) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		parent.setLayout( gridLayout );

		createSpeechPartViewer( parent );
		createWordsViewer( parent );

		IActionBars bars = getViewSite().getActionBars();

		fillLocalPullDown( bars.getMenuManager() );
		fillLocalToolBar( bars.getToolBarManager() );

		connectToEditors(); // Wait for editor activation
	}

	private void createSpeechPartViewer( Composite parent ) {
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.CENTER;

		speechPartViewer = new ComboViewer( parent, SWT.NONE );
		speechPartViewer.getCombo().setLayoutData( gd );
		speechPartViewer.setContentProvider( new SimpleContentProvider() );
		speechPartViewer.setLabelProvider( new SpeechPartLabelProvider() );
		speechPartViewer.setInput( SpeechPart.values() );
		speechPartViewer.setSelection( new StructuredSelection( SpeechPart.ANY ) );
		speechPartViewer.addSelectionChangedListener( new ISelectionChangedListener() {

			@Override
			public void selectionChanged( SelectionChangedEvent ev ) {
				IStructuredSelection sel = (IStructuredSelection) ev.getSelection();

				wordsViewer.setContentProvider( new TextWordsContentProvider( (SpeechPart) sel.getFirstElement() ) );
			}
		} );
	}

	private void createWordsViewer( Composite parent ) {
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = GridData.FILL;

		wordsViewer = new TreeViewer( parent, SWT.BORDER );
		wordsViewer.getTree().setLayoutData( gd );
		wordsViewer.setContentProvider( new TextWordsContentProvider( SpeechPart.ANY ) );
		wordsViewer.setLabelProvider( new TextWordsLabelProvider() );
		wordsViewer.addSelectionChangedListener( new ISelectionChangedListener() {

			@Override
			public void selectionChanged( SelectionChangedEvent ev ) {
				Iterator<?> iter = ((IStructuredSelection) ev.getSelection()).iterator();

				while ( iter.hasNext() ) {
					Object o = iter.next();

					if ( o instanceof Word )
						for ( IWordsViewListener listener : wordSelectionListeners )
							listener.wordSelect( (Word) o );

					if ( o instanceof Node )
						for ( IWordsViewListener listener : wordSelectionListeners )
							listener.nodeSelect( (Node) o );
				}
			}
		} );
		wordsViewer.addDoubleClickListener( new IDoubleClickListener() {

			@Override
			public void doubleClick( DoubleClickEvent ev ) {
				Iterator<?> iter = ((IStructuredSelection) ev.getSelection()).iterator();

				while ( iter.hasNext() ) {
					Object o = iter.next();

					if ( o instanceof Word ) {
						selectTextRangeInEditor( (Word) o );

						for ( IWordsViewListener listener : wordSelectionListeners )
							listener.wordDoubleClick( (Word) o );
					} else if ( o instanceof Node ) {
						for ( Transition t : ((Node) o).transitions )
							if ( t instanceof Word )
								selectTextRangeInEditor( t );

						for ( IWordsViewListener listener : wordSelectionListeners )
							listener.nodeDoubleClick( (Node) o );
					}
				}
			}

			protected void selectTextRangeInEditor( TextRange range ) {
				IEditorPart editor = getEditor();

				if ( editor instanceof DocumentEditor ) {
					((DocumentEditor) editor).selectRange( range );
				} else {
					System.out.println( editor );
				}
			}

		} );

		drillDownAdapter = new DrillDownAdapter( wordsViewer );

		Tree wordsTree = wordsViewer.getTree();
		wordsTree.setLayoutData( gd );
		wordsTree.setHeaderVisible( true );
		wordsTree.setLinesVisible( true );

		/*
		 * Создаем колонки
		 */
		TreeColumn tc1 = new TreeColumn( wordsTree, SWT.LEFT );
		tc1.setText( "Слово" );
		tc1.setWidth( 250 );
		TreeColumn tc2 = new TreeColumn( wordsTree, SWT.LEFT );
		tc2.setText( "Контекст" );
		tc2.setWidth( 300 );
		TreeColumn tc3 = new TreeColumn( wordsTree, SWT.LEFT );
		tc3.setText( "Параметры" );
		tc3.setWidth( 200 );
	}

	private void fillLocalPullDown( IMenuManager manager ) {
	}

	private void fillLocalToolBar( IToolBarManager manager ) {
		drillDownAdapter.addNavigationActions( manager );
	}

}