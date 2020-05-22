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
	
	protected void addWatchedLiteral(WatchedLiteral lit) {
		
		getLiteralsByValue(lit.getVariable().getIndex(), lit.getValue())
			.add(lit);
	}
	
	protected void removeWatchedLiteral(boolean value, int varIndex, int index){
		
		getLiteralsByValue(varIndex, value).remove(index);
	}

	protected List<WatchedLiteral> getLiteralsByValue(Assignment a, Boolean value){
		
		return getLiteralsByValue(a.getVariable().getIndex(), value);
	}
	
	private List<WatchedLiteral> getLiteralsByValue(int index, Boolean value){
		
		if (value)
			return positiveLiterals[index];
		return negativeLiterals[index];
	}
	

	
	
}
