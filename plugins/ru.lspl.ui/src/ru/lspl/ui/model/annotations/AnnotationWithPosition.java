package ru.lspl.ui.model.annotations;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;

public class AnnotationWithPosition {

	public final Annotation annotation;
	public final Position position;

	public AnnotationWithPosition( Annotation annotation, Position position ) {
		this.annotation = annotation;
		this.position = position;
	}

}
