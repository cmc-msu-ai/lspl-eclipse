package ru.lspl.ui.model.annotations;

public class MatchRange {
	public int start;
	public int end;
	public int depth;

	public boolean containsWoEnd( int pos ) {
		return start <= pos && pos < end;
	}
}