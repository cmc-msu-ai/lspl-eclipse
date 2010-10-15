package ru.lspl.analyzer.rcp;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout( IPageLayout layout ) {
		layout.setEditorAreaVisible( true );
		//layout.addView( WordsView.ID, IPageLayout.BOTTOM, 1.0f, layout.getEditorArea() );
		//layout.addView( PatternsView.ID, IPageLayout.RIGHT, 1.0f, layout.getEditorArea() );
	}

}
