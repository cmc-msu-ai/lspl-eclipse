package ru.lspl.analyzer.rcp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.text.DocumentEvent;

import ru.lspl.patterns.Pattern;
import ru.lspl.patterns.PatternBuilder;
import ru.lspl.patterns.PatternBuildingException;
import ru.lspl.text.Text;
import ru.lspl.text.TextConfig;

/**
 * @author alno
 */
public class Document extends FileDocument {

	private static final Text EMPTY_TEXT = Text.create( "" );

	public boolean autoAnalyze = false;

	private boolean analysisNeeded = false;

	/** Конфиг парсера текста */
	private TextConfig textConfig = new TextConfig();

	/** Проанализированный текст */
	private Text analyzedText = EMPTY_TEXT;

	/** Построитель шаблонов, используемый при анализе */
	private PatternBuilder patternBuilder = PatternBuilder.create();

	/** Шаблоны, используемые при анализе */
	private Pattern[] patterns = null;

	private final Collection<IAnalysisListener> listeners = new ArrayList<IAnalysisListener>();

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

	public void addAnalysisListener( IAnalysisListener listener ) {
		listeners.add( listener );
	}

	public void removeAnalysisListener( IAnalysisListener listener ) {
		listeners.remove( listener );
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
