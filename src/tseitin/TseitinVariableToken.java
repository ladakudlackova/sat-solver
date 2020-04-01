package tseitin;

import tseitin_to_dimacs.FormulaToken;

public class TseitinVariableToken extends FormulaToken {
	
	private static int count = 0;
	
	private boolean value = true;
	
	public TseitinVariableToken() {
		count++;
		token=count;
	}

	@Override
	public String getToken() {
		if (value)
			return token.toString();
		return "-"+token.toString();
	}	
	
	protected static void reset() {
		count=0;
	}
}