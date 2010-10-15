package ru.lspl.ui.providers.labels;

import org.eclipse.jface.viewers.ITableLabelProvider;

import ru.lspl.patterns.Pattern;
import ru.lspl.text.Match;
import ru.lspl.text.MatchGroup;

public class TextMatchesLabelProvider extends BaseTransitionsLabelProvider implements ITableLabelProvider {

	@Override
	protected String getMainColumnText( Object obj ) {
		if ( obj instanceof Match )
			return "[" + ((Match) obj).start.endOffset + "-" + ((Match) obj).end.startOffset + "] " + ((Match) obj).getContent();

		if ( obj instanceof MatchGroup )
			return "[" + ((MatchGroup) obj).start.endOffset + "-" + ((MatchGroup) obj).end.startOffset + "] " + ((MatchGroup) obj).getContent();

		if ( obj instanceof Pattern )
			return ((Pattern) obj).name;

		return "";
	}

	@Override
	protected String getParamsColumnText( Object obj ) {
		if ( obj instanceof MatchGroup ) {
			MatchGroup group = (MatchGroup) obj;

			if ( group.matches.size() == 1 )
				return super.getParamsColumnText( group.matches.get( 0 ) );
			else
				return "";
		}

		return super.getParamsColumnText( obj );
	}

}
