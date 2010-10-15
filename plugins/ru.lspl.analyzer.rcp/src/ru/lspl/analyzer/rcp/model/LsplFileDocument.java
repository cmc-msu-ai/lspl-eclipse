package ru.lspl.analyzer.rcp.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.lspl.ui.model.LsplDocument;
import ru.lspl.ui.model.access.PlainTextExtractor;
import ru.lspl.ui.model.access.TextExtractor;

/**
 * @author alno
 */
public class LsplFileDocument extends LsplDocument {

	private String fileName = null;

	public LsplFileDocument() {
		super();
	}

	public LsplFileDocument( String initialContent ) {
		super( initialContent );
	}

	public void load( String fileName ) throws IOException {
		TextExtractor extractor = selectTextExtractor( fileName );

		FileInputStream is = new FileInputStream( fileName );
		String text = extractor.extractText( is );
		is.close();

		set( text );

		this.fileName = extractor.isLossless() ? fileName : null;
	}

	public boolean hasFileName() {
		return fileName != null;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName( String fileName ) {
		this.fileName = fileName;
	}

	public void save( String fileName ) throws IOException {
		FileOutputStream fo = new FileOutputStream( fileName );
		fo.write( get().getBytes() );
		fo.close();

		this.fileName = fileName;
	}

	protected TextExtractor selectTextExtractor( String fileName ) {
		return new PlainTextExtractor();
	}

}
