package ru.lspl.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class DisplayManager {

	protected Map<RGB, Color> colorTable = new HashMap<RGB, Color>( 10 );
	protected Map<FontData, Font> fontTable = new HashMap<FontData, Font>( 10 );

	public void dispose() {
		for ( Color c : colorTable.values() )
			c.dispose();
	}

	public Color getColor( RGB rgb ) {
		Color color = colorTable.get( rgb );

		if ( color == null ) {
			color = new Color( Display.getCurrent(), rgb );
			colorTable.put( rgb, color );
		}

		return color;
	}

	public Font getFont( FontData data ) {
		Font font = fontTable.get( data );

		if ( font == null ) {
			font = new Font( Display.getCurrent(), data );
			fontTable.put( data, font );
		}

		return font;
	}
}
