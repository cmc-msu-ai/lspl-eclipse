package ru.lspl.analyzer.rcp.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.text.DocumentEvent;

import ru.lspl.analyzer.rcp.model.extractors.PlainTextExtractor;
import ru.lspl.analyzer.rcp.model.extractors.TextExtractor;
import ru.lspl.patterns.Pattern;
import ru.lspl.patterns.PatternBuilder;
import ru.lspl.patterns.PatternBuildingException;
import ru.lspl.text.Text;
import ru.lspl.text.TextConfig;

/**
 * @author alno
 */
public class Document extends org.eclipse.jface.text.Document {

	private static final Text EMPTY_TEXT = Text.create( "" );

	public boolean autoAnalyze = false;

	private boolean analysisNeeded = false;

	/**
	 * Конфиг парсера текста
	 */
	private TextConfig textConfig = new TextConfig();

	/**
	 * Имя сохраненного файла
	 */
	private String fileName = null;

	/**
	 * Проанализированный текст
	 */
	private Text analyzedText = EMPTY_TEXT;

	/**
	 * Построитель шаблонов, используемый при анализе
	 */
	private PatternBuilder patternBuilder = PatternBuilder.create();

	/**
	 * Шаблоны, используемые при анализе
	 */
	private Pattern[] patterns = null;

	/**
	 * Подписчики событий
	 */
	private Collection<IAnalysisListener> listeners = new ArrayList<IAnalysisListener>();

	public Document() {
		super( "" );
	}

	public Text getAnalyzedText() {
		return analyzedText;
	}

	public TextConfig getTextConfig() {
		return textConfig;
	}

	public void setTextConfig( TextConfig textConfig ) {
		this.textConfig = textConfig;

		analysisNeeded();
	}

	public boolean isAnalysisNeeded() {
		return analysisNeeded;
	}

	public void analyze() {
		analyzedText = Text.create( get(), textConfig );

		for ( Pattern pattern : getPatternsArray() )
			analyzedText.getMatches( pattern ); // Обработать текст шаблоном

		analysisNeeded = false;

		for ( IAnalysisListener listener : listeners )
			listener.analysisComplete( this );

		fireAnalysisNeedChanged();
	}

	public Pattern[] getPatternsArray() {
		if ( patterns != null ) // Если шаблоны уже определены
			return patterns; // Возвращаем их

		return (patterns = patternBuilder.getDefinedPatternsArray());
	}

	public List<Pattern> getPatternList() {
		return patternBuilder.definedPatterns;
	}

	public void buildPattern( String source ) throws PatternBuildingException {
		patternBuilder.build( source );
		patterns = null;

		analysisNeeded();
	}

	public void clearPatterns() {
		patternBuilder = PatternBuilder.create();
		patterns = null;

		analysisNeeded();
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

	public void addAnalysisListener( IAnalysisListener listener ) {
		listeners.add( listener );
	}

	public void removeAnalysisListener( IAnalysisListener listener ) {
		listeners.remove( listener );
	}

	public void clear() {
		set( "" );
	}

	protected TextExtractor selectTextExtractor( String fileName ) {
		return new PlainTextExtractor();
	}

	@Override
	protected void fireDocumentChanged( DocumentEvent event ) {
		super.fireDocumentChanged( event );

		analysisNeeded();
	}

	protected void analysisNeeded() {
		if ( autoAnalyze ) { // Если стоит флаг автоанализа, анализируем текст
			analyze();
		} else if ( !analysisNeeded ) {
			analysisNeeded = true;
			fireAnalysisNeedChanged();
		}
	}

	protected void fireAnalysisNeedChanged() {
		for ( IAnalysisListener listener : listeners )
			listener.analisysNeedChanged( this ); // Извещаем подписчиков об анализе документа
	}

}
