package ru.lspl.core;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class LsplConfig implements ILsplConfig {

	public static String FRAGMENT_ID = "ru.lspl.core.linux.x86_64";

	public String getDataPath() {
		Bundle bundle = Platform.getBundle( FRAGMENT_ID );

		try {
			return FileLocator.getBundleFile( bundle ).getAbsolutePath() + File.separatorChar + "data";
		} catch ( IOException e ) {
			return null;
		}
	}

	public String getNativeLibraryPath() {
		Bundle bundle = Platform.getBundle( FRAGMENT_ID );

		try {
			return FileLocator.getBundleFile( bundle ).getAbsolutePath();
		} catch ( IOException e ) {
			return null;
		}
	}

	public String getLibraryPath() {
		return LsplCore.getLibraryPath().toString();
	}

}
