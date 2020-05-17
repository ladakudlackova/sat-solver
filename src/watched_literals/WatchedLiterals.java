package watched_literals;

import java.util.ArrayList;
import java.util.List;

import tseitin.TseitinVariableToken;

public class WatchedLiterals {

	private List<WatchedLiteral>[] positiveLiterals;
	private List<WatchedLiteral>[] negativeLiterals;
	
	protected WatchedLiterals(int variablesCount, List<ClauseWithWatches> clausesList) {
		
		init(variablesCount);
		addWatchedLiterals(clausesList);
	}
	
	@SuppressWarnings("unchecked")
	private void init(int variablesCount) {
		
		positiveLiterals = new ArrayList[variablesCount];
		negativeLiterals = new ArrayList[variablesCount];
		for (int i=1;i<variablesCount;i++) {
			positiveLiterals[i] = new ArrayList<WatchedLiteral>();
			negativeLiterals[i] = new ArrayList<WatchedLiteral>();
		}
	}
	
	private void addWatchedLiterals(List<ClauseWithWatches> clausesList) {
		
		for (ClauseWithWatches c : clausesList) {
			addWatchedLiteral(c.getWatchedLiteral(0));
			addWatchedLiteral(c.getWatchedLiteral(1));
		}
	}
	
	private void addWatchedLiteral(WatchedLiteral lit) {
		
		int varIndex = lit.getVariable().getIndex();
		if (lit.getValue())
			positiveLiterals[varIndex].add(lit);
		else
			negativeLiterals[varIndex].add(lit);
	}
}
