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
		SourceViewer sv = (SourceViewer) super.createSourceViewer( parent, ruler, styles );

		AnnotationPainter annotationPainter = createAnnotationPainter( sv );

		sv.addPainter( annotationPainter );
		sv.addTextPresentationListener( annotationPainter );

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
		RGB base = new RGB( 80, 80, 0 );
		Display display = Display.getDefault();

		for ( int i = 1; i <= MatchRangeAnnotation.MAX_DEPTH; ++i ) {
			annotationPainter.addHighlightAnnotationType( "ru.lspl.analyzer.match" + i );
			annotationPainter.setAnnotationTypeColor( "ru.lspl.analyzer.match" + i, calcColor( base, display, i ) );
		}
	}

	private Color calcColor( RGB base, Display display, int i ) {
		int r = base.red + (MatchRangeAnnotation.MAX_DEPTH - i) * (255 - base.red) / MatchRangeAnnotation.MAX_DEPTH;
		int g = base.green + (MatchRangeAnnotation.MAX_DEPTH - i) * (255 - base.green) / MatchRangeAnnotation.MAX_DEPTH;
		int b = base.blue + (MatchRangeAnnotation.MAX_DEPTH - i) * (255 - base.blue) / MatchRangeAnnotation.MAX_DEPTH;

		return new Color( display, new RGB( r, g, b ) );
	}

}
