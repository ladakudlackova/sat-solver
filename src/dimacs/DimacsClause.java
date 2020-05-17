package dimacs;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Stream;

import dpll.Clause;
import tseitin.Assignment;
import tseitin.TseitinVariableToken;

public class DimacsClause extends Clause{

	private HashSet<TseitinVariableToken> posLiterals = new HashSet<TseitinVariableToken>();
	private HashSet<TseitinVariableToken> negLiterals = new HashSet<TseitinVariableToken>();
	private HashSet<TseitinVariableToken> satVariables = new HashSet<TseitinVariableToken>();
	private HashSet<TseitinVariableToken> failedVariables = new HashSet<TseitinVariableToken>();
	private int unassignedCount = 0;
	private int count;
	
	public DimacsClause(Object[] clause, TseitinVariableToken[] variables) {
		for (Object literal : clause) {
			addLiteral(literal, variables);
		}
		count = clause.length;
		unassignedCount = count;
	}
	
	protected void setValue(TseitinVariableToken var, Boolean value) {
		
		if (value==null) 
			setNullValue(var);
		
		else
			setNotNullValue(var, value);
	    
	}	
	
	private void setNullValue(TseitinVariableToken var) {
		
		if (var.getValue()!=null) {
			unassignedCount++;
			satVariables.remove(var);
			failedVariables.remove(var);			
		}
	}	
	
	private void setNotNullValue(TseitinVariableToken var, boolean value) {
		
		Boolean literalValue = posLiterals.contains(var);
		if (var.getValue()==null)
			unassignedCount--;
		if (literalValue==value)
			satVariables.add(var);
		else if (unassignedCount==0 && isSatisfied() == false) 
			failedVariables.add(var);
	}	
	
	protected void addLiteral(Object literal, TseitinVariableToken[] variables) {
		
		Assignment a = new Assignment(literal, variables);
		TseitinVariableToken var= a.getVariable();
		Boolean value=a.getValue();
		if (value)
			posLiterals.add(var);
		else
			negLiterals.add(var);
	}
	
	public HashSet<TseitinVariableToken> getPosLiterals() {
		return posLiterals;
	}
	
	public HashSet<TseitinVariableToken> getNegLiterals() {
		return negLiterals;
	}

	public int getUnassignedCount() {
		return unassignedCount;
	}

	public Assignment getUnitAssignment() {
		boolean value=true;
		Stream<TseitinVariableToken> stream=posLiterals.stream();
		Optional<TseitinVariableToken> unassignedVarOpt 
			= stream.filter(var->(var.getValue()==null)).findFirst();
		if (unassignedVarOpt.equals(Optional.empty())) {
			stream=negLiterals.stream();
			unassignedVarOpt = stream.filter(var->(var.getValue()==null)).findFirst();
			value=false;
		}
		return new Assignment(unassignedVarOpt.get(),value);
		
	}
	
	public boolean isSatisfied() {
		return !satVariables.isEmpty();
	}

	public boolean failed() {
		return !failedVariables.isEmpty();
	}
	
	// only for debugging
	@Override
	public String toString() {
		String result = "";
		for (TseitinVariableToken v:posLiterals) {
			result=result+" +"+v.toString();
		}
		for (TseitinVariableToken v:negLiterals) {
			result=result+" -"+v.toString();
		}
		return result;
	}
	
}
