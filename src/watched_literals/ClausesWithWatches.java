package watched_literals;

import java.util.ArrayList;
import java.util.List;

import dpll.Clause;
import dpll.Clauses;
import tseitin.Assignment;
import tseitin.TseitinVariableToken;

public class ClausesWithWatches extends Clauses{

	private List<ClauseWithWatches> clausesList = new ArrayList<ClauseWithWatches>();
	private List<ClauseWithWatches> unitClausesList = new ArrayList<ClauseWithWatches>();
	private WatchedLiterals variablesWatchedLiterals;
	
	
	@Override
	protected void init(TseitinVariableToken[] variables) {
		
		variablesWatchedLiterals = new WatchedLiterals(variables.length, clausesList);
	}

	@Override
	protected void setValue(Assignment a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void resetValue(TseitinVariableToken var) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetValues(List<Integer> last, TseitinVariableToken[] variables) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean failed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void setFailed(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Clause getFirstUnitClause() {
		
		if (unitClausesList.isEmpty())
			return null;
		return unitClausesList.get(0);
	}

	@Override
	public int getUnsatisfiedCount() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void addClause(Object[] intClause, TseitinVariableToken[] variables) {
		
		ClauseWithWatches c = new ClauseWithWatches(intClause, variables);
		if (c.isUnit)
			unitClausesList.add(c);
		else
			clausesList.add(c);
		
	}

}
