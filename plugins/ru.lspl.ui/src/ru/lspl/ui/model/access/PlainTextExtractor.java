package ru.lspl.ui.model.access;

import java.io.IOException;
import java.io.InputStream;

public class PlainTextExtractor implements TextExtractor {

	public String extractText( InputStream is ) throws IOException {
		byte[] data = new byte[is.available()];
		is.read( data );

		return new String( data );
	}

	public boolean isLossless() {
		return true;
	}

}
