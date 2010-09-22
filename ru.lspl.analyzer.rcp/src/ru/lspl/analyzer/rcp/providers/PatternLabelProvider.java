package ru.lspl.analyzer.rcp.providers;

import java.util.Collection;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import ru.lspl.analyzer.rcp.model.Document;
import ru.lspl.patterns.Alternative;
import ru.lspl.patterns.Pattern;
import ru.lspl.text.Match;

/**
 * @author alno
 */
public class PatternLabelProvider extends BaseLabelProvider implements ITableLabelProvider {

	private Document document;

	private static final int COLUMN_PATTERN = 0;
	private static final int COLUMN_GROUPS = 1;
	private static final int COLUMN_MATCHES = 2;
	private static final int COLUMN_VARIANTS = 3;

	@Override
	public String getColumnText( Object obj, int column ) {
		switch ( column ) {
		case COLUMN_PATTERN:
			if ( obj instanceof Pattern )
				return ((Pattern) obj).name;

			if ( obj instanceof Alternative )
				return ((Alternative) obj).getSource();

			break;
		case COLUMN_GROUPS:
			if ( obj instanceof Pattern && document != null ) {// Возвращаем количество сопоставлений
				Collection<?> matches = document.getMatches( (Pattern) obj );

				return matches != null ? String.valueOf( document.getMatchGroups( (Pattern) obj ).size() ) : "?";
			}

			if ( obj instanceof Alternative )
				return "";

			break;
		case COLUMN_MATCHES:
			if ( obj instanceof Pattern && document != null ) {// Возвращаем количество сопоставлений
				Collection<?> matches = document.getMatches( (Pattern) obj );

				return matches != null ? String.valueOf( matches.size() ) : "?";
			}

			if ( obj instanceof Alternative )
				return "";

			break;
		case COLUMN_VARIANTS:
			if ( obj instanceof Pattern && document != null ) {// Возвращаем количество сопоставлений
				Collection<Match> matches = document.getMatches( (Pattern) obj );

				if ( matches != null ) {
					int variantCount = 0;

					for ( Match m : matches )
						variantCount += m.getVariantCount();

					return String.valueOf( variantCount );
				} else {
					return "?";
				}
			}

			if ( obj instanceof Alternative ) {
				Pattern p = ((Alternative) obj).pattern;
				Collection<Match> matches = document.getMatches( p );

				if ( matches != null ) {
					int variantCount = 0;

					for ( Match m : matches )
						for ( int i = 0; i < m.getVariantCount(); ++i )
							if ( m.getVariantAlternative( i ) == obj )
								variantCount++;

					return String.valueOf( variantCount );
				} else {
					return "?";
				}
			}

			break;
		}

		return "";
	}

	@Override
	public Image getColumnImage( Object arg0, int arg1 ) {
		return null;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument( Document doc ) {
		document = doc;
	}
}
