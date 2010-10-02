package ru.lspl.jdt;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;

import ru.lspl.core.LsplCore;

public class LsplClasspathContainer implements IClasspathContainer {

	public static final String CONTAINER_ID = "ru.lspl.jdt.LSPL_CONTAINER";

	private static final IAccessRule[] NO_ACCESS_RULES = new IAccessRule[0];

	public static IClasspathEntry createLibraryEntry() {
		return JavaCore.newLibraryEntry( LsplCore.getLibraryPath(), null, null, NO_ACCESS_RULES, new IClasspathAttribute[] { createNativeLibraryAttribute() }, false );
	}

	public static IClasspathEntry createContainerEntry() {
		return JavaCore.newContainerEntry( new Path( CONTAINER_ID ) );
	}

	public static IPath getContainerPath() {
		return new Path( CONTAINER_ID );
	}

	private static IClasspathAttribute createNativeLibraryAttribute() {
		return new IClasspathAttribute() {

			@Override
			public String getValue() {
				return LsplCore.getNativeLibraryPath().toString();
			}

			@Override
			public String getName() {
				return "org.eclipse.jdt.launching.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY";
			}

		};
	}

	private final IPath path;

	public LsplClasspathContainer( IPath path ) {
		this.path = path;
	}

	@Override
	public IClasspathEntry[] getClasspathEntries() {
		return new IClasspathEntry[] { createLibraryEntry() };
	}

	@Override
	public String getDescription() {
		return "Lspl library";
	}

	@Override
	public int getKind() {
		return K_APPLICATION;
	}

	@Override
	public IPath getPath() {
		return path;
	}

}
