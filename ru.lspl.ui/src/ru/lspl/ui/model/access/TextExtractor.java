package ru.lspl.ui.model.access;

import java.io.IOException;
import java.io.InputStream;

public interface TextExtractor {

	String extractText( InputStream is ) throws IOException;

	boolean isLossless();

}
