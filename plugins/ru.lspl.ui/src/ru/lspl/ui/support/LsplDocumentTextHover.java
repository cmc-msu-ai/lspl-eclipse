package ru.lspl.ui.support;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.EditorsUI;

import ru.lspl.text.Match;
import ru.lspl.text.Transition;
import ru.lspl.text.Word;
import ru.lspl.text.attributes.AttributeKey;
import ru.lspl.ui.model.ILsplDocument;

public class LsplDocumentTextHover implements ITextHover, ITextHoverExtension {

	public String getHoverInfo( ITextViewer textViewer, IRegion hoverRegion ) {
		ILsplDocument document = (ILsplDocument) textViewer.getDocument();
		List<Transition> transitions = document.findTransitionsContainingPosition( hoverRegion.getOffset() );

		StringBuilder b = new StringBuilder();

		appendMatchesInfo( b, transitions );
		appendWordsInfo( b, transitions );

		return b.toString();
	}

	private void appendWordsInfo( StringBuilder b, List<Transition> transitions ) {
		boolean found = false;

		for ( Transition t : transitions ) {
			if ( t instanceof Word ) {
				if ( !found ) {
					found = true;
					b.append( "<h3>Слова:</h3><ul>" );
				}
				Word w = (Word) t;
				b.append( "<li><b>" );
				b.append( w.base );
				b.append( "</b> - " );
				b.append( w.speechPart.title );
				b.append( " - " );
				appendAttributes( b, w.getAttributes() );
				b.append( "</li>" );
			}
		}

		if ( found ) {
			b.append( "</ul><br/><br/>" );
		}
	}

	private void appendMatchesInfo( StringBuilder b, List<Transition> transitions ) {
		boolean found = false;

		for ( Transition t : transitions ) {
			if ( t instanceof Match ) {
				if ( !found ) {
					found = true;
					b.append( "<h3>Сопоставления:</h3><ul>" );
				}
				Match m = (Match) t;
				b.append( "<li><b>" );
				b.append( m.pattern.name );
				b.append( "</b> - \"" );
				b.append( m.getContent() );
				b.append( "\" - " );
				appendAttributes( b, m.getAttributes() );
				b.append( "</li>" );
			}
		}

		if ( found ) {
			b.append( "</ul><br/><br/>" );
		}
	}

	private void appendAttributes( StringBuilder builder, Map<Integer, Object> attributes ) {
		boolean first = true;

		for ( Map.Entry<Integer, Object> entry : attributes.entrySet() ) {
			if ( !first )
				builder.append( ", " );
			else
				first = false;

			builder.append( AttributeKey.valueOf( entry.getKey() ).getTitle() );
			builder.append( ": " );
			builder.append( entry.getValue().toString() );
		}
	}

	public IRegion getHoverRegion( ITextViewer textViewer, int offset ) {
		return new Region( offset, 0 );
	}

	public IInformationControlCreator getHoverControlCreator() {
		return new IInformationControlCreator() {

			public IInformationControl createInformationControl( Shell parent ) {
				DefaultInformationControl control = new DefaultInformationControl( parent, EditorsUI.getTooltipAffordanceString() );

				control.setBackgroundColor( new Color( Display.getDefault(), 236, 236, 178 ) );

				return control;
			}
		};
	}

}
