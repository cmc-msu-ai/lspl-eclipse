package ru.lspl.core;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import ru.lspl.LsplObject;

public class LsplConfig {

	public static String FRAGMENT_ID = "ru.lspl.core.linux.x86_64";

	public static void configure() {
		Bundle bundle = Platform.getBundle( FRAGMENT_ID );

		try {
			LsplObject.setRml( FileLocator.getBundleFile( bundle ).getAbsolutePath() + File.separatorChar + "data" );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

}
