package ru.lspl.analyzer.rcp.model;

public interface IAnalysisListener {

	void analysisRequired( Document doc );

	void analysisStarted( Document doc );

	void analysisCompleted( Document doc );

}
