package ru.lspl.ui.providers.content;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;

import ru.lspl.patterns.Pattern;
import ru.lspl.text.Match;
import ru.lspl.text.MatchGroup;
import ru.lspl.ui.model.ILsplDocument;

/**
 * @author alno
 */
public class TextMatchesContentProvider extends SimpleContentProvider implements ITreeContentProvider {

	private static final Object[] EMPTY_ARRAY = new Object[0];

	private ILsplDocument document;

	public Object[] getChildren( Object obj ) {
		if ( document == null || obj == null )
			return EMPTY_ARRAY;

		if ( obj instanceof MatchGroup )
			return ((MatchGroup) obj).matches.toArray();

		if ( obj instanceof Pattern )
			return document.getMatchGroups( (Pattern) obj ).toArray();

		if ( obj instanceof ILsplDocument )
			return document.getPatterns().toArray();

		return EMPTY_ARRAY;
	}

	public Object getParent( Object obj ) {
		return null;
	}

	public boolean hasChildren( Object obj ) {
		if ( document == null || obj == null )
			return false;

		if ( obj instanceof MatchGroup )
			return ((MatchGroup) obj).matches.size() > 1;

		if ( obj instanceof Pattern ) {
			List<Match> matches = document.getMatches( (Pattern) obj );

			return matches != null && matches.size() > 0;
		}

		if ( obj instanceof ILsplDocument )
			return document.getPatterns().size() > 0;

		return false;
	}

	@Override
	public Object[] getElements( Object input ) {
		return getChildren( input );
	}

	public ILsplDocument getDocument() {
		return document;
	}

	public void setDocument( ILsplDocument doc ) {
		document = doc;
	}

}
