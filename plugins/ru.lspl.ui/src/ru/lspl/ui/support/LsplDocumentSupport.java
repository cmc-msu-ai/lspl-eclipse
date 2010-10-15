package ru.lspl.ui.support;

import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationPainter;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import ru.lspl.ui.model.LsplDocumentAnnotationModel;
import ru.lspl.ui.model.annotations.MatchRangeAnnotation;

public abstract class LsplDocumentSupport {

	public static final String EVENT_MOVE = "MOVE";
	public static final String EVENT_HOVER = "HOVER";

	private String annotationHighlightEvent = EVENT_MOVE;

	public String getAnnotationHighlightEvent() {
		return annotationHighlightEvent;
	}

	public void setAnnotationHighlightEvent( String annotationHighlightEvent ) {
		this.annotationHighlightEvent = annotationHighlightEvent;
	}

	public void install( final SourceViewer sourceViewer ) {
		AnnotationPainter annotationPainter = createAnnotationPainter( sourceViewer );

		sourceViewer.addPainter( annotationPainter );
		sourceViewer.addTextPresentationListener( annotationPainter );

		if ( annotationHighlightEvent.equals( EVENT_MOVE ) ) {
			sourceViewer.getTextWidget().addMouseMoveListener( new MouseMoveListener() {

				@Override
				public void mouseMove( MouseEvent e ) {
					getAnnotationModel().showHoveredAnnotations( getMouseOffset( sourceViewer, e ) );
				}

			} );
			sourceViewer.getTextWidget().addMouseTrackListener( new MouseTrackAdapter() {

				@Override
				public void mouseExit( MouseEvent e ) {
					getAnnotationModel().showAllAnnotations();
				}

			} );
		} else {
			sourceViewer.getTextWidget().addMouseTrackListener( new MouseTrackAdapter() {

				@Override
				public void mouseHover( MouseEvent e ) {
					getAnnotationModel().showHoveredAnnotations( getMouseOffset( sourceViewer, e ) );
				}

				@Override
				public void mouseExit( MouseEvent e ) {
					getAnnotationModel().showAllAnnotations();
				}

			} );
		}
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
			annotationPainter.setAnnotationTypeColor( "ru.lspl.analyzer.match" + i,
					lerpColor( baseOne, baseMax, display, MatchRangeAnnotation.MAX_DEPTH - i, MatchRangeAnnotation.MAX_DEPTH - 1 ) );
		}
	}

	protected int getMouseOffset( SourceViewer sourceViewer, MouseEvent e ) {
		try {
			return sourceViewer.getTextWidget().getOffsetAtLocation( new Point( e.x, e.y ) );
		} catch ( Throwable ex ) {
			return -1;
		}
	}

	protected abstract LsplDocumentAnnotationModel getAnnotationModel();

	private Color lerpColor( RGB baseOne, RGB baseMax, Display display, int pos, int max ) {
		int r = baseMax.red + pos * (baseOne.red - baseMax.red) / max;
		int g = baseMax.green + pos * (baseOne.green - baseMax.green) / max;
		int b = baseMax.blue + pos * (baseOne.blue - baseMax.blue) / max;

		return new Color( display, r, g, b );
	}

}
