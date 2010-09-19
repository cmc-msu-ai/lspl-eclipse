package ru.lspl.analyzer.rcp.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import ru.lspl.patterns.Pattern;

public class PatternStorage {

	private final Shell shell;

	public PatternStorage( Shell shell ) {
		this.shell = shell;
	}

	public void load( Document document, String fileName ) throws FileNotFoundException {
		Scanner scanner = new Scanner( new File( fileName ) );

		try {
			int i = 0;
			while ( scanner.hasNextLine() ) {
				String line = scanner.nextLine();

				if ( line.startsWith( "//" ) || line.startsWith( "#" ) )
					continue;

				try {
					document.buildPattern( line );
				} catch ( Throwable ex ) {
					MessageBox mb = new MessageBox( shell, SWT.OK | SWT.ICON_ERROR );
					mb.setText( "Ошибка компиляции шаблона" );
					mb.setMessage( "Ошибка в шаблоне на строке " + i + ":\n\n" + ex.getMessage() );
					mb.open();
				}

				++i;
			}
		} finally {
			scanner.close();
		}
	}

	public void save( Document document, String fileName ) throws IOException {
		FileWriter fw = new FileWriter( fileName );

		try {
			for ( Pattern pattern : document.getPatternsArray() ) {
				fw.append( "\n\n" );
				fw.append( pattern.name );
				fw.append( " = " );
				fw.append( pattern.getSource() );
			}
		} finally {
			fw.close();
		}
	}

}
