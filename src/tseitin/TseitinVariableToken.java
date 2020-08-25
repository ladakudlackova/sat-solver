package tseitin;

import tseitin_to_dimacs.FormulaToken;

public class TseitinVariableToken extends FormulaToken {
	
	private static int count = 0;
	
	private Boolean value = null;
	
	public TseitinVariableToken() {
		count = getCount() + 1;
		token=getCount();
	}

	public TseitinVariableToken(int index) {
		token=index;
	}

	
	@Override
	public String getToken() {
		if (value==null||value)
			return token.toString();
		return "-"+token.toString();
	}	
	
	public Integer getIndex() {
		return (Integer) token;
	}	
	
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof TseitinVariableToken)
			return (getToken().equals(((TseitinVariableToken)o).getToken()));
		return false;
	}	
	
	protected static void reset() {
		count=0;
	}

	public static int getCount() {
		return count;
	}

	public void setValue(Boolean value) {
		this.value=value;	
	}
	
	public Boolean getValue() {
		return value;	
	}
	
	@Override
	public String toString() {                                    // 
		return getIndex().toString();
	}
}