package ru.lspl.ui.model.listeners;

import ru.lspl.ui.model.ILsplDocument;

public interface IAnalysisListener {

	void analysisRequired( ILsplDocument doc );

	void analysisStarted( ILsplDocument doc );

	void analysisCompleted( ILsplDocument doc );

}
