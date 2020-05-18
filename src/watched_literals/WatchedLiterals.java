package watched_literals;

import java.util.ArrayList;
import java.util.List;

import tseitin.Assignment;

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
			addWatchedLiteral(c.addWatchedLiteral(0));
			addWatchedLiteral(c.addWatchedLiteral(1));
		}
	}
	
	protected List<WatchedLiteral> getOppositeLiterals(Assignment a){
		
		if (a.getValue())
			return negativeLiterals[a.getVariable().getIndex()];
		return positiveLiterals[a.getVariable().getIndex()];
	}
	
	protected void addWatchedLiteral(WatchedLiteral lit) {
		
		int varIndex = lit.getVariable().getIndex();
		if (lit.getValue())
			positiveLiterals[varIndex].add(lit);
		else
			negativeLiterals[varIndex].add(lit);
	}
	
	protected void removeWatchedLiteral(boolean value, int varIndex, int index){
		
		List<WatchedLiteral> literals;
		if (value)
			literals = positiveLiterals[varIndex];
		else
			literals = negativeLiterals[varIndex];
		literals.remove(index);
	}
	
}
