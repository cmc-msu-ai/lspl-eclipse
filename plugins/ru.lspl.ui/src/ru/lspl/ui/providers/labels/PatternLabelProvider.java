package ru.lspl.ui.providers.labels;

import java.util.Collection;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import ru.lspl.patterns.Alternative;
import ru.lspl.patterns.Pattern;
import ru.lspl.text.Match;
import ru.lspl.ui.model.ILsplDocument;

/**
 * @author alno
 */
public class PatternLabelProvider extends BaseLabelProvider implements ITableLabelProvider {

	private ILsplDocument document;

	private static final int COLUMN_PATTERN = 0;
	private static final int COLUMN_GROUPS = 1;
	private static final int COLUMN_MATCHES = 2;
	private static final int COLUMN_VARIANTS = 3;

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
						variantCount += m.getVariants().size();

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
						for ( int i = 0; i < m.getVariants().size(); ++i )
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

	public Image getColumnImage( Object arg0, int arg1 ) {
		return null;
	}

	public ILsplDocument getDocument() {
		return document;
	}

	public void setDocument( ILsplDocument doc ) {
		document = doc;
	}
}
