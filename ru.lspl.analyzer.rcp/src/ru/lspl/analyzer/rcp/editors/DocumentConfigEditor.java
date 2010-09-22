package ru.lspl.analyzer.rcp.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import ru.lspl.analyzer.rcp.model.Document;
import ru.lspl.analyzer.rcp.model.DocumentConfig;

public class DocumentConfigEditor extends EditorPart {

	private Document document;
	private DocumentConfig config;

	private Button analyzePunctuationCheck;
	private Button analyzeSpacesCheck;
	private Button analyzeOnTextChangeCheck;
	private Button analyzeOnConfigChangeCheck;
	private Button analyzeOnPatternsChangeCheck;

	@Override
	public void init( IEditorSite site, IEditorInput input ) throws PartInitException {
		document = ((DocumentEditorInput) input).getDocument();
		config = document.getConfig();

		setSite( site );
		setInput( input );
	}

	@Override
	public void doSave( IProgressMonitor monitor ) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl( Composite parent ) {
		parent.setLayout( new RowLayout( SWT.VERTICAL ) );

		analyzePunctuationCheck = new Button( parent, SWT.CHECK );
		analyzePunctuationCheck.setText( "Analyze text punctuation" );
		analyzePunctuationCheck.setSelection( config.analyzePunctuation );
		analyzePunctuationCheck.addSelectionListener( new SelectionAdapter() {

			@Override
			public void widgetSelected( SelectionEvent e ) {
				config.analyzePunctuation = analyzePunctuationCheck.getSelection();
				document.setConfig( config );
			}

		} );

		analyzeSpacesCheck = new Button( parent, SWT.CHECK );
		analyzeSpacesCheck.setText( "Analyze text spaces" );
		analyzeSpacesCheck.setSelection( config.analyzeSpaces );
		analyzeSpacesCheck.addSelectionListener( new SelectionAdapter() {

			@Override
			public void widgetSelected( SelectionEvent e ) {
				config.analyzeSpaces = analyzeSpacesCheck.getSelection();
				document.setConfig( config );
			}

		} );

		analyzeOnTextChangeCheck = new Button( parent, SWT.CHECK );
		analyzeOnTextChangeCheck.setText( "Analyze on text changes" );
		analyzeOnTextChangeCheck.setSelection( config.analyzeOnTextChange );
		analyzeOnTextChangeCheck.addSelectionListener( new SelectionAdapter() {

			@Override
			public void widgetSelected( SelectionEvent e ) {
				config.analyzeOnTextChange = analyzeOnTextChangeCheck.getSelection();
				document.setConfig( config );
			}

		} );

		analyzeOnConfigChangeCheck = new Button( parent, SWT.CHECK );
		analyzeOnConfigChangeCheck.setText( "Analyze on config changes" );
		analyzeOnConfigChangeCheck.setSelection( config.analyzeOnConfigChange );
		analyzeOnConfigChangeCheck.addSelectionListener( new SelectionAdapter() {

			@Override
			public void widgetSelected( SelectionEvent e ) {
				config.analyzeOnConfigChange = analyzeOnConfigChangeCheck.getSelection();
				document.setConfig( config );
			}

		} );

		analyzeOnPatternsChangeCheck = new Button( parent, SWT.CHECK );
		analyzeOnPatternsChangeCheck.setText( "Analyze on patterns changes" );
		analyzeOnPatternsChangeCheck.setSelection( config.analyzeOnPatternsChange );
		analyzeOnPatternsChangeCheck.addSelectionListener( new SelectionAdapter() {

			@Override
			public void widgetSelected( SelectionEvent e ) {
				config.analyzeOnPatternsChange = analyzeOnPatternsChangeCheck.getSelection();
				document.setConfig( config );
			}

		} );

	}

	@Override
	public void setFocus() {
	}

}
