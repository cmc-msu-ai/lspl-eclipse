package ru.lspl.core;

import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ru.lspl.LsplObject;

public class LsplCore implements BundleActivator {

	public static String PLUGIN_ID = "ru.lspl.core";

	private static final ILsplConfig config;

	static {
		try {
			config = (ILsplConfig) Class.forName( "ru.lspl.core.LsplConfig" ).newInstance();
		} catch ( Exception e ) {
			throw new RuntimeException( "Error loading LsplConfig", e );
		}
	}

	public static ILsplConfig getConfig() {
		return config;
	}

	public static IPath getLibraryPath() {
		try {
			return new Path( FileLocator.toFileURL( Platform.getBundle( PLUGIN_ID ).getEntry( "lspl.jar" ) ).getPath() );
		} catch ( IOException e ) {
			return null;
		}
	}

	public static IPath getNativeLibraryPath() {
		return new Path( config.getNativeLibraryPath() );
	}

	@Override
	public void start( BundleContext context ) throws Exception {
		LsplObject.setRml( LsplCore.getConfig().getDataPath() );
	}

	@Override
	public void stop( BundleContext context ) throws Exception {
	}

}
