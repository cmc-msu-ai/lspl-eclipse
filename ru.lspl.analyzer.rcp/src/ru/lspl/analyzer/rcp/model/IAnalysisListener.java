package ru.lspl.analyzer.rcp.model;

public interface IAnalysisListener {

	void analisysNeedChanged( Document doc );

	void analysisComplete( Document doc );

}
