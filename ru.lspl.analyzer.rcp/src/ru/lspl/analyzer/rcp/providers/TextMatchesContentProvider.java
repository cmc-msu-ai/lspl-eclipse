package ru.lspl.analyzer.rcp.providers;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;

import ru.lspl.analyzer.rcp.model.Document;
import ru.lspl.analyzer.rcp.utils.RangeMatchGroup;
import ru.lspl.analyzer.rcp.utils.RangeMatchGroupper;
import ru.lspl.patterns.Pattern;
import ru.lspl.text.Match;

/**
 * @author alno
 */
public class TextMatchesContentProvider extends SimpleContentProvider implements ITreeContentProvider {

	private static final Object[] EMPTY_ARRAY = new Object[0];

	private static final RangeMatchGroupper groupper = new RangeMatchGroupper();

	private Document document;

	@Override
	public Object[] getChildren( Object obj ) {
		if ( document == null || obj == null )
			return EMPTY_ARRAY;

		if ( obj instanceof RangeMatchGroup )
			return ((RangeMatchGroup) obj).matches.toArray();

		if ( obj instanceof Pattern ) {
			List<Match> matches = document.getMatches( (Pattern) obj );

			if ( matches != null )
				return groupper.groupMatches( matches ).toArray();
		}

		if ( obj instanceof Document )
			return document.getPatternSet().getDefinedPatternArray();

		return EMPTY_ARRAY;
	}

	@Override
	public Object getParent( Object obj ) {
		return null;
	}

	@Override
	public boolean hasChildren( Object obj ) {
		if ( document == null || obj == null )
			return false;

		if ( obj instanceof RangeMatchGroup )
			return ((RangeMatchGroup) obj).matches.size() > 1;

		if ( obj instanceof Pattern ) {
			List<Match> matches = document.getMatches( (Pattern) obj );

			return matches != null && matches.size() > 0;
		}

		if ( obj instanceof Document )
			return document.getPatternSet().getDefinedPatternList().size() > 0;

		return false;
	}

	@Override
	public Object[] getElements( Object input ) {
		return getChildren( input );
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument( Document doc ) {
		document = doc;
	}

}
