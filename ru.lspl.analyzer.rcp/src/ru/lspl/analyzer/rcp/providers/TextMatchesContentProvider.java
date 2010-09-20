package ru.lspl.analyzer.rcp.providers;

import org.eclipse.jface.viewers.ITreeContentProvider;

import ru.lspl.analyzer.rcp.model.Document;
import ru.lspl.analyzer.rcp.utils.RangeMatchGroup;
import ru.lspl.analyzer.rcp.utils.RangeMatchGroupper;
import ru.lspl.patterns.Pattern;

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

		if ( obj instanceof Pattern )
			return groupper.groupMatches( document.getAnalyzedText().getMatches( (Pattern) obj ) ).toArray();

		if ( obj instanceof Document )
			return document.getPatternsArray();

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

		if ( obj instanceof Pattern )
			return document.getAnalyzedText().getMatchCount( (Pattern) obj ) > 0;

		if ( obj instanceof Document )
			return document.getPatternList().size() > 0;

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
