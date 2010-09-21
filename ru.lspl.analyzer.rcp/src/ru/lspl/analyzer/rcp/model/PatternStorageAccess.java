package ru.lspl.analyzer.rcp.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import ru.lspl.patterns.Pattern;

public class PatternStorageAccess {

	public void load( Document document, String fileName ) throws FileNotFoundException {
		Scanner scanner = new Scanner( new File( fileName ) );
		ArrayList<String> lines = new ArrayList<String>();

		try {
			int i = 0;
			while ( scanner.hasNextLine() ) {
				String line = scanner.nextLine();

				if ( line.startsWith( "//" ) || line.startsWith( "#" ) )
					continue;

				lines.add( line );

				++i;
			}
		} finally {
			scanner.close();
		}

		document.getPatternSet().createDefinePatternsJob( lines ).schedule();
	}

	public void save( Document document, String fileName ) throws IOException {
		FileWriter fw = new FileWriter( fileName );

		try {
			for ( Pattern pattern : document.getPatternSet().getDefinedPatternArray() ) {
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
