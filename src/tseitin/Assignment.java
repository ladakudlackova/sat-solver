package tseitin;

public class Assignment {
	
	private static final String NEG = "-";
	
	private TseitinVariableToken var;
	private Boolean value;
	
	public Assignment(TseitinVariableToken var, Boolean value) {
		this.var=var;
		this.value=value;
	}
	
	public Assignment(Object literal, TseitinVariableToken[] variables) {
		if (literal instanceof Assignment) {
			Assignment a = (Assignment)literal;
			var=a.getVariable();
			value=a.getValue();
		}
		else if (literal instanceof Integer){
			Integer a = ((Integer)literal);
			value = a>0;
			var=variables[Math.abs(a)];
		}
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
			return var.getIndex()+" ";
		return NEG+var.getIndex()+" ";
	}
}