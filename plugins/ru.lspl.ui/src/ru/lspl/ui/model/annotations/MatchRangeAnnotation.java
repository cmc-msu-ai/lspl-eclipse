package ru.lspl.ui.model.annotations;

import org.eclipse.jface.text.source.Annotation;

public class MatchRangeAnnotation extends Annotation {

	public static String BASE_TYPE = "ru.lspl.analyzer.match";
	public static int MAX_DEPTH = 8;

	public MatchRangeAnnotation( int depth ) {
		super( BASE_TYPE + Math.min( MAX_DEPTH, depth ), true, "Text" );
	}

}
