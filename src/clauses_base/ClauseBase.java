package clauses_base;

import tseitin.Assignment;
import tseitin.TseitinVariableToken;

public abstract class ClauseBase {

	protected abstract void addLiteral(Object literal, TseitinVariableToken[] variables);
	
	public abstract Assignment getUnitAssignment();

	public abstract boolean isSatisfied();
	
	public abstract boolean failed();
}
