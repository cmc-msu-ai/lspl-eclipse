package ru.lspl.ui.providers.content;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;

import ru.lspl.patterns.Alternative;
import ru.lspl.patterns.Pattern;

public class PatternsContentProvider extends SimpleContentProvider implements ITreeContentProvider {

	private static final Object[] EMPTY_ARRAY = new Object[0];

	public Object[] getChildren( Object obj ) {
		if ( obj instanceof Collection<?> )
			return ((Collection<?>) obj).toArray();

		if ( obj instanceof Pattern )
			return ((Pattern) obj).getAlternatives().toArray();

		return EMPTY_ARRAY;
	}

	public Object getParent( Object obj ) {
		if ( obj instanceof Alternative )
			return ((Alternative) obj).pattern;

		return null;
	}

	public boolean hasChildren( Object obj ) {
		return obj instanceof Pattern || obj instanceof Collection<?>;
	}

	@Override
	public Object[] getElements( Object obj ) {
		return getChildren( obj );
	}

}
