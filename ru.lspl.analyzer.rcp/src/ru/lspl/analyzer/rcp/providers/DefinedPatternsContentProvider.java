package ru.lspl.analyzer.rcp.providers;

import org.eclipse.jface.viewers.ITreeContentProvider;

import ru.lspl.analyzer.rcp.model.Document;
import ru.lspl.patterns.Alternative;
import ru.lspl.patterns.Pattern;

public class DefinedPatternsContentProvider extends SimpleContentProvider implements ITreeContentProvider {

	private static final Object[] EMPTY_ARRAY = new Object[0];

	@Override
	public Object[] getChildren( Object obj ) {
		if ( obj instanceof Document )
			return ((Document) obj).getDefinedPatternList().toArray();

		if ( obj instanceof Pattern )
			return ((Pattern) obj).getAlternatives().toArray();

		return EMPTY_ARRAY;
	}

	@Override
	public Object getParent( Object obj ) {
		if ( obj instanceof Alternative )
			return ((Alternative) obj).pattern;

		return null;
	}

	@Override
	public boolean hasChildren( Object obj ) {
		return obj instanceof Pattern || obj instanceof Document;
	}

	@Override
	public Object[] getElements( Object obj ) {
		return getChildren( obj );
	}

}
