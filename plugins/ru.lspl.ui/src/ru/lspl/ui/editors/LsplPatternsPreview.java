package ru.lspl.ui.editors;

import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.texteditor.DocumentProviderRegistry;

import ru.lspl.patterns.Pattern;
import ru.lspl.text.Match;
import ru.lspl.text.MatchGroup;
import ru.lspl.text.TextRange;
import ru.lspl.ui.model.DocumentConfig;
import ru.lspl.ui.model.ILsplDocument;
import ru.lspl.ui.model.LsplDocument;
import ru.lspl.ui.model.LsplDocumentAnnotationModel;
import ru.lspl.ui.model.SourcePatternSet;
import ru.lspl.ui.model.listeners.AnalysisAdapter;
import ru.lspl.ui.providers.content.PatternsContentProvider;
import ru.lspl.ui.providers.content.TextMatchesContentProvider;
import ru.lspl.ui.providers.labels.PatternLabelProvider;
import ru.lspl.ui.providers.labels.TextMatchesLabelProvider;
import ru.lspl.ui.support.LsplDocumentSupport;
import ru.lspl.ui.viewers.IPatternsViewerListener;
import ru.lspl.ui.viewers.MatchesTreeViewer;
import ru.lspl.ui.viewers.MatchesViewAdapter;
import ru.lspl.ui.viewers.PatternsTreeViewer;

public class LsplPatternsPreview extends EditorPart {

	private final TextMatchesContentProvider matchesContentProvider = new TextMatchesContentProvider();
	private final PatternLabelProvider patternLabelProvider = new PatternLabelProvider();

	private final LsplDocumentSupport lsplTextViewerSupport = new LsplDocumentSupport() {

		@Override
		protected LsplDocumentAnnotationModel getAnnotationModel() {
			return textDocumentAnnotationModel;
		}

	};

	private final DocumentConfig textDocumentConfig = new DocumentConfig();

	private PatternsDocumentProvider patternsDocumentProvider;
	private SourcePatternSet patterns;

	private SourceViewer textViewer;
	private LsplDocument textDocument;
	private LsplDocumentAnnotationModel textDocumentAnnotationModel;

	private PatternsTreeViewer patternsViewer;
	private MatchesTreeViewer matchesViewer;

	public LsplPatternsPreview() {
		textDocumentConfig.analyzeOnTextChange = true;
	}

	@Override
	public void init( IEditorSite site, IEditorInput input ) throws PartInitException {
		setSite( site );

		patternsDocumentProvider = (PatternsDocumentProvider) DocumentProviderRegistry.getDefault().getDocumentProvider( input );
		patterns = patternsDocumentProvider.getPatterns( input );

		textDocument = new LsplDocument( patterns );
		textDocument.setConfig( textDocumentConfig );
		textDocument.addAnalysisListener( new AnalysisAdapter() {

			@Override
			public void analysisCompleted( ILsplDocument doc ) {
				patternsViewer.refresh();
				matchesViewer.refresh();
			}

		} );

		textDocumentAnnotationModel = new LsplDocumentAnnotationModel();
		textDocumentAnnotationModel.connect( textDocument );
	}

	@Override
	public void createPartControl( Composite parent ) {
		SashForm sfVertical = new SashForm( parent, SWT.NONE );
		sfVertical.setOrientation( SWT.VERTICAL );

		SashForm sfHorizontal = new SashForm( sfVertical, SWT.NONE );
		sfHorizontal.setOrientation( SWT.HORIZONTAL );

		createTextViewer( sfHorizontal );
		createPatternsViewer( sfHorizontal );
		createMatchesViewer( sfVertical );
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void doSave( IProgressMonitor monitor ) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	public void refresh() {
		patterns.update();
		textDocument.createAnalyzeJob().schedule();
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
				textViewer.setSelectedRange( range.getStartOffset(), range.getEndOffset() - range.getStartOffset() );
			}

		} );

		matchesContentProvider.setDocument( textDocument );
		matchesViewer.setInput( textDocument );
	}

	private void createPatternsViewer( Composite parent ) {
		patternsViewer = new PatternsTreeViewer( parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL );
		patternsViewer.setLabelProvider( patternLabelProvider );
		patternsViewer.setContentProvider( new PatternsContentProvider() );
		patternsViewer.setSorter( new ViewerSorter() );
		patternsViewer.addPatternListener( new IPatternsViewerListener() {

			public void patternSelect( Pattern pattern ) {
				matchesViewer.setInput( pattern );
			}

			public void patternDoubleClick( Pattern pattern ) {
			}

			public void patternChecked( Pattern pattern, boolean checked, Set<Pattern> checkedPatterns ) {
				textDocumentAnnotationModel.setSelectedPatterns( checkedPatterns );
			}

		} );

		patternLabelProvider.setDocument( textDocument );
		patternsViewer.setInput( patterns );
	}

	private void createTextViewer( Composite parent ) {
		textViewer = new SourceViewer( parent, new VerticalRuler( 0 ), SWT.BORDER | SWT.WRAP | SWT.V_SCROLL );
		textViewer.configure( new DocumentTextEditorConfiguration() );
		textViewer.setDocument( textDocument, textDocumentAnnotationModel );

		lsplTextViewerSupport.install( textViewer );
	}

}
