package watched_literals;

import java.util.ArrayList;
import java.util.List;

import clauses_base.ClauseBase;
import clauses_base.ClausesBase;
import tseitin.Assignment;
import tseitin.TseitinVariableToken;

public class ClausesWithWatches extends ClausesBase{

	private List<ClauseWithWatches> clausesList = new ArrayList<ClauseWithWatches>();
	private List<ClauseWithWatches> unitClausesList = new ArrayList<ClauseWithWatches>();
	private WatchedLiterals variablesWatchedLiterals;
	
	@Override
	public void init(TseitinVariableToken[] variables) {
		
		variablesWatchedLiterals = new WatchedLiterals(variables.length, clausesList);
	}

	@Override
	public void addClause(Object[] intClause, TseitinVariableToken[] variables) {
		
		ClauseWithWatches c = new ClauseWithWatches(intClause, variables);
		if (intClause.length==1)     
			unitClausesList.add(c);
		else
			clausesList.add(c);
		
	}
	
	@Override
	public ClauseBase getFirstUnitClause() {
		
		while (!unitClausesList.isEmpty()) {
			ClauseWithWatches firstUnit = unitClausesList.remove(0);
			if (firstUnit.isUnit())
				return firstUnit;
		}
		return null;
	}
	
	public List<Assignment> getAndRemoveUnitAssignment() {
		
		List<Assignment> result=new ArrayList<Assignment>();
		for (ClauseWithWatches c:unitClausesList)
			result.add(c.getFirstLiteral());
		unitClausesList=new ArrayList<ClauseWithWatches>();
		return result;
		
	}

	@Override
	public void updateClauses(Assignment a) {
		
		List<WatchedLiteral> literals = variablesWatchedLiterals.getLiteralsByValue(a, !a.getValue());
		int size = literals.size(); 
		for (int i=0;i<size;i++) {			
			WatchedLiteral lit = literals.get(i);	
			boolean litValue = lit.getValue();
			int litIndex = lit.getIndex();
			int litVarIndex = lit.getVariable().getIndex();
			ClauseWithWatches clause = lit.getClause();
			lit=clause.updateWatchedLiteral(litIndex);  
			if (clause.failed()) { 
				failed=true;		
				clause.setFailed(false);
				break;
			}
			if (!(litIndex==lit.getIndex())) {
				variablesWatchedLiterals.removeWatchedLiteral(litValue, litVarIndex, i); 
				variablesWatchedLiterals.addWatchedLiteral(lit);
				size--;
				i--;
			}
			if (clause.isNewUnit()) 
				unitClausesList.add(clause);
		}
	}

	@Override	
	public void resetValues(List<Integer> last, TseitinVariableToken[] variables) {
		
		failed=false;
		for (Integer varIndex:last) 
			resetValue(variables[varIndex]);
			
	}

	@Override
	public void resetValue(TseitinVariableToken var) {   
		
		var.setValue(null);
	}

	@Override
	public boolean allSatisfied() {
		
		for (ClauseWithWatches c:clausesList)
			if (!c.isSatisfied()) 
				return false;
		return true;
	}
	
}
