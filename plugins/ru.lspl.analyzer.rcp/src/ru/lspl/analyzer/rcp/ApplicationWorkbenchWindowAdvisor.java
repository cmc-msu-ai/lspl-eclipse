package ru.lspl.analyzer.rcp;

import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor( IWorkbenchWindowConfigurer configurer ) {
		super( configurer );
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor( IActionBarConfigurer configurer ) {
		return new ApplicationActionBarAdvisor( configurer );
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setShowCoolBar( true );
		configurer.setShowStatusLine( true );
		configurer.setShowProgressIndicator( true );
		configurer.setTitle( "Lspl Analyzer" ); //$NON-NLS-1$
	}
}
