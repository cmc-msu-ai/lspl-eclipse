package ru.lspl.core;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	@Override
	public void start( BundleContext context ) throws Exception {
		Class.forName( "ru.lspl.core.LsplConfig" ).getMethod( "configure" ).invoke( null );
	}

	@Override
	public void stop( BundleContext context ) throws Exception {
	}

}
