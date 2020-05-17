package dpll;

import tseitin.Assignment;
import tseitin.TseitinVariableToken;

public abstract class Clause {

	protected abstract void addLiteral(Object literal, TseitinVariableToken[] variables);
	
	protected abstract Assignment getUnitAssignment();

}
