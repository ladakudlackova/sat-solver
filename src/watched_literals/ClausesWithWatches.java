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
	protected Clause getFirstUnitClause() {
		
		if (unitClausesList.isEmpty())
			return null;
		return unitClausesList.get(0);
	}

	@Override
	public void addClause(Object[] intClause, TseitinVariableToken[] variables) {
		
		unsatisfiedCount++;
		ClauseWithWatches c = new ClauseWithWatches(intClause, variables);
		if (c.isUnit())
			unitClausesList.add(c);
		else
			clausesList.add(c);
		
	}

	@Override
	public void updateClauses(Assignment a) {
		
		List<WatchedLiteral> literals = variablesWatchedLiterals.getOppositeLiterals(a);
		int size = literals.size();
		int lastSatisfied=0;
		for (int i=0;i<size;i++) {			
			WatchedLiteral lit = literals.get(i);
			boolean litValue = lit.getValue();
			int litVarIndex = lit.getVariable().getIndex();
			ClauseWithWatches clause = lit.getClause();
			boolean wasUnit = clause.isUnit();
			boolean wasSatisfied = clause.isSatisfied();
			clause.updateWatchedLiteral(lit);  //           !!!!!
			if (clause.failed()) {
				failed=true;
				return;
			}
			if (!(litVarIndex==lit.getVariable().getIndex())) {
				variablesWatchedLiterals.removeWatchedLiteral(litValue, litVarIndex, i);
				variablesWatchedLiterals.addWatchedLiteral(lit);
			}
			if (clause.isUnit()) {
				if (!wasUnit && !clause.isSatisfied())
					unitClausesList.add(clause);
			}
			else {
				if (wasUnit)
					unitClausesList.remove(clause);
			}
			
			if (!wasSatisfied && clause.isSatisfied()) {
				lastSatisfied++;
			}
		}
		unsatisfiedCount=unsatisfiedCount-lastSatisfied;
	}

	@Override
	public void resetValues(List<Integer> last, TseitinVariableToken[] variables) {
		failed=false;
	}

	@Override
	public void resetValue(TseitinVariableToken var) {
		
	}

}
