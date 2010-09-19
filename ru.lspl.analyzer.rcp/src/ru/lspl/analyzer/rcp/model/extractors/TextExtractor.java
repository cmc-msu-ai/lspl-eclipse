package ru.lspl.analyzer.rcp.model.extractors;

import java.io.IOException;
import java.io.InputStream;

public interface TextExtractor {

	String extractText( InputStream is ) throws IOException;

	boolean isLossless();

}
