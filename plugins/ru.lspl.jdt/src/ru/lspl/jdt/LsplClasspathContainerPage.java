package ru.lspl.jdt;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jdt.ui.wizards.NewElementWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class LsplClasspathContainerPage extends NewElementWizardPage implements IClasspathContainerPage, IClasspathContainerPageExtension {

	public static IJavaProject getPlaceholderProject() {
		String name = "####internal"; //$NON-NLS-1$
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		while ( true ) {
			IProject project = root.getProject( name );
			if ( !project.exists() ) {
				return JavaCore.create( project );
			}
			name += '1';
		}
	}

	private IClasspathEntry containerEntryResult;

	public LsplClasspathContainerPage() {
		super( "LsplContainerPage" ); //$NON-NLS-1$

		setTitle( "Lspl Container Settings" );
		setDescription( "Lspl Container Description" );

		containerEntryResult = LsplClasspathContainer.createContainerEntry();
	}

	@Override
	public void createControl( Composite parent ) {
		Composite composite = new Composite( parent, SWT.NONE );
		composite.setLayout( new FillLayout() );
		composite.setFont( parent.getFont() );

		Label label = new Label( composite, SWT.NONE );
		label.setFont( composite.getFont() );
		label.setText( "This is Lspl library config page, please press Finish" );

		setControl( composite );
	}

	@Override
	public void initialize( IJavaProject project, IClasspathEntry[] arg1 ) {
	}

	@Override
	public boolean finish() {
		try {
			IJavaProject[] javaProjects = new IJavaProject[] { getPlaceholderProject() };
			IClasspathContainer[] containers = { null };
			JavaCore.setClasspathContainer( containerEntryResult.getPath(), javaProjects, containers, null );
		} catch ( JavaModelException e ) {
			return false;
		}
		return true;
	}

	@Override
	public IClasspathEntry getSelection() {
		return containerEntryResult;
	}

	@Override
	public void setSelection( IClasspathEntry entry ) {
	}

}
