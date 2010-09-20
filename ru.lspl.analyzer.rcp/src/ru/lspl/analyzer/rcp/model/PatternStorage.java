package ru.lspl.analyzer.rcp.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import ru.lspl.patterns.Pattern;

public class PatternStorage {

	public void load( Document document, String fileName ) throws FileNotFoundException {
		IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
		Scanner scanner = new Scanner( new File( fileName ) );

		try {
			int i = 0;
			while ( scanner.hasNextLine() ) {
				String line = scanner.nextLine();

				if ( line.startsWith( "//" ) || line.startsWith( "#" ) )
					continue;

				try {
					progressService.run( false, false, document.createDefinePatternJob( line ) );
				} catch ( InvocationTargetException e ) {
					e.printStackTrace();
				} catch ( InterruptedException e ) {
					e.printStackTrace();
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
			for ( Pattern pattern : document.getDefinedPatternArray() ) {
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
