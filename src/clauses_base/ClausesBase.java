package clauses_base;

import java.util.List;

import tseitin.Assignment;
import tseitin.TseitinVariableToken;

public abstract class ClausesBase {
	
	protected boolean failed = false;	

	public abstract void init(TseitinVariableToken[] variables);
	
	public abstract void addClause(Object[] intClause, TseitinVariableToken[] variables);
	
	public abstract ClauseBase getFirstUnitClause();	
	
	public abstract void updateClauses(Assignment a);
	
	public abstract void resetValues(List<Integer> last, TseitinVariableToken[] variables);
	
	public abstract void resetValue(TseitinVariableToken var);
	
	public abstract boolean allSatisfied();
	
	public void setValue(Assignment a) {
		
		TseitinVariableToken var=a.getVariable();
		boolean value=a.getValue();
		updateClauses(a);                  
		var.setValue(value);
	}

	public boolean failed() {
		return failed;
	}

	public void setFailed(boolean b) {
		failed = b;
	}
}

