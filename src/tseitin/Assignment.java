package tseitin;

public class Assignment {
	
	private static final String NEG = "-";
	
	private TseitinVariableToken var;
	private Boolean value;
	
	public Assignment(TseitinVariableToken var, Boolean value) {
		this.var=var;
		this.value=value;
	}
	
	public TseitinVariableToken getVariable() {
		return var;
	}
	
	public Boolean getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		if (value)
			return var.getToken()+" ";
		return NEG+var.getToken()+" ";
	}
}