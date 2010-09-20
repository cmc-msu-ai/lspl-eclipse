package ru.lspl.analyzer.rcp.providers;

import java.util.Collection;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import ru.lspl.analyzer.rcp.model.Document;
import ru.lspl.patterns.Alternative;
import ru.lspl.patterns.Pattern;

/**
 * @author alno
 */
public class PatternLabelProvider extends BaseLabelProvider implements ITableLabelProvider {

	private Document document;

	private static final int COLUMN_PATTERN = 0;
	private static final int COLUMN_PARAMS = 1;
	private static final int COLUMN_MATCHES = 2;

	@Override
	public String getColumnText( Object obj, int column ) {
		switch ( column ) {
		case COLUMN_PATTERN:
			if ( obj instanceof Pattern )
				return ((Pattern) obj).name;

			if ( obj instanceof Alternative )
				return ((Alternative) obj).getSource();

			break;
		case COLUMN_PARAMS:
			break;
		case COLUMN_MATCHES:
			if ( obj instanceof Pattern && document != null ) {// Возвращаем количество сопоставлений
				Collection<?> matches = document.getMatches( (Pattern) obj );

				if ( matches != null )
					return String.valueOf( matches.size() );
			}

			if ( obj instanceof Alternative )
				return "";

			break;
		}

		return "<Unknown>";
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
