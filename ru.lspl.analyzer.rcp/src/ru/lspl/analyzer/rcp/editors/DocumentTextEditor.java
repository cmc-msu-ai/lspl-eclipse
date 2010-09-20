package ru.lspl.analyzer.rcp.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationPainter;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextEditor;

import ru.lspl.analyzer.rcp.model.annotations.MatchRangeAnnotation;

public class DocumentTextEditor extends TextEditor {

	public DocumentTextEditor() {
		setSourceViewerConfiguration( new DocumentTextEditorConfiguration( getPreferenceStore() ) );
	}

	@Override
	protected void setDocumentProvider( IEditorInput input ) {
		setDocumentProvider( ((DocumentEditorInput) input).getDocumentProvider() );
	}

	@Override
	protected void performSaveAs( IProgressMonitor progressMonitor ) {
		FileDialog saveFileDialog = new FileDialog( getSite().getShell(), SWT.SAVE );

		String fileName = saveFileDialog.open();

		if ( fileName != null ) {
			((DocumentEditorInput) getEditorInput()).getDocument().setFileName( fileName );
			performSave( true, progressMonitor );
		}
	}

	@Override
	protected ISourceViewer createSourceViewer( Composite parent, IVerticalRuler ruler, int styles ) {
		final SourceViewer sv = (SourceViewer) super.createSourceViewer( parent, ruler, styles );

		AnnotationPainter annotationPainter = createAnnotationPainter( sv );

		sv.addPainter( annotationPainter );
		sv.addTextPresentationListener( annotationPainter );
		//		sv.getTextWidget().addMouseMoveListener( new MouseMoveListener() {
		//			
		//			@Override
		//			public void mouseMove(MouseEvent ev) {
		//				int offset = -1;
		//				
		//				try {
		//					offset = sv.getTextWidget().getOffsetAtLocation(new Point(ev.x,ev.y));
		//				} catch ( Throwable e ) {}
		//				
		//				DocumentEditorInput editorInput = (DocumentEditorInput) getEditorInput();				
		//				DocumentAnnotationModel annotationModel = (DocumentAnnotationModel)editorInput.getDocumentProvider().getAnnotationModel( editorInput );
		//				
		//				//annotationModel.highlightAnnotations( offset );								
		//			}
		//			
		//		});

		return sv;
	}

	protected AnnotationPainter createAnnotationPainter( SourceViewer sv ) {
		IAnnotationAccess annotationAccess = new IAnnotationAccess() {

			@Override
			public Object getType( Annotation annotation ) {
				return annotation.getType();
			}

			@Override
			public boolean isMultiLine( Annotation annotation ) {
				return true;
			}

			@Override
			public boolean isTemporary( Annotation annotation ) {
				return true;
			}

		};

		AnnotationPainter annotationPainter = new AnnotationPainter( sv, annotationAccess );

		registerMatchRangeAnnotationTypes( annotationPainter );

		return annotationPainter;
	}

	protected void registerMatchRangeAnnotationTypes( AnnotationPainter annotationPainter ) {
		RGB baseMax = new RGB( 80, 80, 0 );
		RGB baseOne = new RGB( 255, 255, 160 );
		Display display = Display.getDefault();

		for ( int i = 1; i <= MatchRangeAnnotation.MAX_DEPTH; ++i ) {
			annotationPainter.addHighlightAnnotationType( "ru.lspl.analyzer.match" + i );
			annotationPainter.setAnnotationTypeColor( "ru.lspl.analyzer.match" + i, calcColor( baseOne, baseMax, display, i ) );
		}
	}

	private Color calcColor( RGB baseOne, RGB baseMax, Display display, int i ) {
		int dist = MatchRangeAnnotation.MAX_DEPTH - i;
		int maxDist = MatchRangeAnnotation.MAX_DEPTH - 1;

		int r = baseMax.red + dist * (baseOne.red - baseMax.red) / maxDist;
		int g = baseMax.green + dist * (baseOne.green - baseMax.green) / maxDist;
		int b = baseMax.blue + dist * (baseOne.blue - baseMax.blue) / maxDist;

		return new Color( display, new RGB( r, g, b ) );
	}

}
