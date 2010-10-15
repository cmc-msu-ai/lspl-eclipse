package ru.lspl.jdt;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class LsplClasspathContainerInitializer extends ClasspathContainerInitializer {

	private static boolean isValidLsplContainerPath( IPath path ) {
		return path != null && path.segmentCount() >= 1 && LsplClasspathContainer.CONTAINER_ID.equals( path.segment( 0 ) );
	}

	@Override
	public void initialize( IPath path, IJavaProject project ) throws CoreException {
		if ( isValidLsplContainerPath( path ) )
			JavaCore.setClasspathContainer( path, new IJavaProject[] { project }, new IClasspathContainer[] { new LsplClasspathContainer( path ) }, null );
	}

}
