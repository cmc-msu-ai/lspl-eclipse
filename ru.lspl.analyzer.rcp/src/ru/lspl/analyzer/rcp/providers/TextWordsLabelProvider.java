package ru.lspl.analyzer.rcp.providers;

import ru.lspl.text.Node;
import ru.lspl.text.Transition;
import ru.lspl.text.Word;

public class TextWordsLabelProvider extends BaseTransitionsLabelProvider {

	@Override
	protected String getMainColumnText( Object obj ) {
		if ( obj instanceof Word )
			return ((Word) obj).speechPart + ": " + ((Word) obj).base;

		if ( obj instanceof Node )
			for ( Transition t : ((Node) obj).getTransitions() )
				if ( t instanceof Word )
					return ((Word) t).form;

		return "";
	}

	@Override
	protected String getContextColumnText( Object obj ) {
		if ( obj instanceof Node ) {
			for ( Transition t : ((Node) obj).getTransitions() )
				if ( t instanceof Word )
					return extractTextFragment( t.getText(), t.getStartOffset() - 10, t.getEndOffset() + 10 );
		}

		return super.getContextColumnText( obj );
	}

}
