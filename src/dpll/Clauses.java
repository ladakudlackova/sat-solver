package dpll;

import java.util.List;

import tseitin.Assignment;
import tseitin.TseitinVariableToken;

public abstract class Clauses {

	protected abstract void init(TseitinVariableToken[] variables);

	protected abstract void setValue(Assignment a);
	
	protected abstract void resetValue(TseitinVariableToken var);
	
	protected abstract void resetValues(List<Integer> last, TseitinVariableToken[] variables);
	
	protected abstract boolean failed();

	protected abstract void setFailed(boolean b);

	protected abstract Clause getFirstUnitClause();

	protected abstract int getUnsatisfiedCount();

}
