package ru.lspl.ui.model.access;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import ru.lspl.patterns.Pattern;
import ru.lspl.ui.model.LsplPatternSet;

public class PatternStorageAccess {

	public void loadFromFile( LsplPatternSet patterns, String fileName ) throws FileNotFoundException {
		load( patterns, new Scanner( new File( fileName ) ) );
	}

	public void loadFromString( LsplPatternSet patterns, String content ) {
		load( patterns, new Scanner( content ) );
	}

	public void save( LsplPatternSet patterns, String fileName ) throws IOException {
		FileWriter fw = new FileWriter( fileName );

		try {
			for ( Pattern pattern : patterns ) {
				fw.append( "\n\n" );
				fw.append( pattern.name );
				fw.append( " = " );
				fw.append( pattern.getSource() );
			}
		} finally {
			fw.close();
		}
	}

	protected void load( LsplPatternSet patterns, Scanner scanner ) {
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

		patterns.createDefinePatternsJob( lines ).schedule();
	}

}
