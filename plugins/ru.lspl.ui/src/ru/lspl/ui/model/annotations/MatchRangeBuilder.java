package ru.lspl.ui.model.annotations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.lspl.text.Match;

public class MatchRangeBuilder {

	private static final Comparator<Match> startOffsetComparator = new Comparator<Match>() {

		@Override
		public int compare( Match o1, Match o2 ) {
			return o1.start.endOffset - o2.start.endOffset;
		}

	};

	public List<MatchRange> buildMatchRanges( Collection<Match> inputMatches, int maxDepth ) {
		if ( inputMatches == null )
			return null;

		List<Match> matches = new ArrayList<Match>( inputMatches );
		List<MatchRange> ranges = new ArrayList<MatchRange>();

		Collections.sort( matches, startOffsetComparator );

		for ( Match m : matches ) {
			int start = m.start.endOffset;
			int end = m.end.startOffset;

			if ( ranges.isEmpty() || end > ranges.get( ranges.size() - 1 ).end ) {
				MatchRange r = new MatchRange();
				r.start = start;
				r.end = end;
				r.depth = 1;

				ranges.add( r );
			} else {
				appendRange( ranges, start, end );
			}
		}

		return ranges;
	}

	private void appendRange( List<MatchRange> ranges, int start, int end ) {
		int pos = start;
		int index = findRange( ranges, start );

		while ( index < ranges.size() && pos < end ) {
			MatchRange range = ranges.get( index );

			if ( range.start < pos ) { // |---*---|
				if ( range.end <= end ) { // |---*===|
					MatchRange nr = new MatchRange();
					nr.depth = range.depth + 1;
					nr.start = pos;
					nr.end = range.end;

					range.end = pos;

					ranges.add( index + 1, nr );

					pos = nr.end;
					index = index + 2;
				} else { // |---*==*--|
					MatchRange nr = new MatchRange();
					nr.depth = range.depth + 1;
					nr.start = pos;
					nr.end = end;

					MatchRange ar = new MatchRange();
					ar.depth = range.depth;
					ar.start = end;
					ar.end = range.end;

					range.end = pos;

					ranges.add( index + 1, nr );
					ranges.add( index + 2, ar );

					break;
				}
			} else if ( range.start > pos ) { // *  |-----|
				if ( range.start <= end ) { // *===|-----|
					MatchRange nr = new MatchRange();
					nr.start = pos;
					nr.end = range.start;
					nr.depth = 1;

					ranges.add( index, nr );

					pos = nr.end;
					index = index + 1;
				} else { // *===* |----|
					MatchRange nr = new MatchRange();
					nr.start = pos;
					nr.end = end;
					nr.depth = 1;

					ranges.add( index, nr );

					break;
				}
			} else { // *-----|
				if ( range.end < end ) { // *====|---
					range.depth += 1;

					pos = range.end;
					index = index + 1;
				} else { // *===*--|
					MatchRange nr = new MatchRange();
					nr.start = range.start;
					nr.end = end;
					nr.depth = range.depth + 1;

					range.start = end;

					ranges.add( index, nr );

					break;
				}
			}
		}
	}

	private int findRange( List<MatchRange> ranges, int pos ) {
		int i = 0;
		for ( MatchRange r : ranges ) {
			if ( r.containsWoEnd( pos ) )
				return i;

			++i;
		}

		return -1;
	}
}
