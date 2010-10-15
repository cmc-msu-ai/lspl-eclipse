package ru.lspl.ui.model;

import java.util.Collection;

import ru.lspl.patterns.Pattern;
import ru.lspl.ui.model.listeners.IPatternListener;

public interface ILsplPatternSet extends Collection<Pattern> {

	void addPatternListener( IPatternListener listener );

	void removePatternListener( IPatternListener listener );

}
