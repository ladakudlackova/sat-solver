package tseitin;

public class Assignment {
	
	private static final String NEG = "-";
	
	private TseitinVariableToken var;
	private boolean value;
	
	public Assignment(TseitinVariableToken var, boolean value) {
		this.var=var;
		this.value=value;
	}
	
	@Override
	public String toString() {
		if (value)
			return var.getToken();
		return NEG+var.getToken();
	}
}