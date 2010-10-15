package ru.lspl.ui.providers.content;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class SimpleContentProvider implements IStructuredContentProvider {

	public Object[] getElements( Object input ) {
		return (Object[]) input;
	}

	public void dispose() {
	}

	public void inputChanged( Viewer arg0, Object arg1, Object arg2 ) {
	}

}
