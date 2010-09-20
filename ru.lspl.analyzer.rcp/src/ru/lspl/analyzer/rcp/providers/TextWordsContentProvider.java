package ru.lspl.analyzer.rcp.providers;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ITreeContentProvider;

import ru.lspl.analyzer.rcp.model.Document;
import ru.lspl.text.Node;
import ru.lspl.text.Transition;
import ru.lspl.text.Word;
import ru.lspl.text.attributes.SpeechPart;

/**
 * @author alno
 */
public class TextWordsContentProvider extends SimpleContentProvider implements ITreeContentProvider {

	private static final Object[] EMPTY_ARRAY = new Object[0];

	private final SpeechPart speechPart;

	public TextWordsContentProvider( SpeechPart sp ) {
		speechPart = sp;
	}

	@Override
	public Object[] getChildren( Object obj ) {
		if ( obj instanceof Node ) {
			ArrayList<Object> words = new ArrayList<Object>();

			for ( Transition t : ((Node) obj).transitions )
				if ( t instanceof Word && (speechPart == SpeechPart.ANY || ((Word) t).speechPart == speechPart) )
					words.add( t );

			return words.toArray();
		} else if ( obj instanceof Document && ((Document) obj).getNodes() != null ) {
			ArrayList<Object> nodes = new ArrayList<Object>();

			for ( Node n : ((Document) obj).getNodes() )
				if ( hasChildren( n ) )
					nodes.add( n );

			return nodes.toArray();
		} else {
			return EMPTY_ARRAY;
		}
	}

	@Override
	public Object getParent( Object obj ) {
		if ( obj instanceof Transition )
			return ((Transition) obj).start;

		return null;
	}

	@Override
	public boolean hasChildren( Object obj ) {
		if ( obj instanceof Document )
			return true;

		if ( obj instanceof Node )
			for ( Transition t : ((Node) obj).transitions )
				if ( t instanceof Word && (speechPart == SpeechPart.ANY || ((Word) t).speechPart == speechPart) )
					return true;

		return false;
	}

	@Override
	public Object[] getElements( Object input ) {
		return getChildren( input );
	}

}
