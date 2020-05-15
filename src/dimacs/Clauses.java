package dimacs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import tseitin.Assignment;
import tseitin.TseitinVariableToken;

// TODO: unit clauses - satisfied

public class Clauses {

	private HashSet<Clause> clausesSet = new HashSet<Clause>();
	private HashSet<Clause> unitClausesSet = new HashSet<Clause>();
	private ArrayList<Clause>[] variableClausesEdges;
	private boolean failed = false;	
	private int unsatisfiedCount = 0;
	
	public void addClause(Clause c) {
		clausesSet.add(c);
	    unsatisfiedCount++;
	}
	
	
	public Iterator<Clause> getClauses() {
		return clausesSet.iterator();
	}
	
	public ArrayList<Clause> getUnitClauses() {
		return new ArrayList<Clause>(unitClausesSet);
	}
	
	public void setUnitClauses(ArrayList<Clause> unitClauses) {
		unitClausesSet = new HashSet<Clause>(unitClauses);
	}
	
	public void setValue(Assignment a) {
		TseitinVariableToken var=a.getVariable();
		boolean value=a.getValue();
		
		for (Clause clause : variableClausesEdges[var.getIndex()]) {
			boolean wasSatisfied = clause.isSatisfied();
			clause.setValue(var, value);
			if (clause.getUnassignedCount()==0)
				unitClausesSet.remove(clause);
			if (!wasSatisfied && clause.isSatisfied()) 
				unsatisfiedCount--;
			else if (clause.failed()) {
				failed=true;
				continue;
			}
			if (clause.getUnassignedCount()==1 && !clause.isSatisfied())
				unitClausesSet.add(clause);
		}
		var.setValue(value);
	}
	
	public void resetValues(List<Integer> last, TseitinVariableToken[] variables) {
		
		for (Integer i : last) {
			TseitinVariableToken var=variables[i];
			resetValue(var);
		}
		failed=false;
	}
	
	public void resetValue(TseitinVariableToken var) {
		
		for (Clause clause:variableClausesEdges[var.getIndex()]) {
			boolean wasSatisfied = clause.isSatisfied();
			clause.setValue(var, null);
			if (wasSatisfied && !clause.isSatisfied())
				unsatisfiedCount++;
			if (clause.getUnassignedCount()==1 && !clause.isSatisfied()) 
				unitClausesSet.add(clause);
			else if (clause.getUnassignedCount()==2) 
				unitClausesSet.remove(clause);
		}
		var.setValue(null);
	}
	
	@SuppressWarnings("unchecked")
	public void createVariableClausesEdges(TseitinVariableToken[] variables) {
		
		variableClausesEdges= new ArrayList[variables.length+1];
		for (int i=1;i<variables.length;i++) {
			TseitinVariableToken var=variables[i];
			variableClausesEdges[var.getIndex()] = new ArrayList<Clause>();
		}
		for (Clause clause : clausesSet) {
			for (TseitinVariableToken var : clause.getPosLiterals()) 
				(variableClausesEdges[var.getIndex()]).add(clause);
			for (TseitinVariableToken var : clause.getNegLiterals()) 
				(variableClausesEdges[var.getIndex()]).add(clause);
		}
	}

	public int getUnsatisfiedCount() {
		return unsatisfiedCount;
	}


	public boolean failed() {
		return failed;
	}
	
	public void setFailed(boolean value) {
		failed=value;
	}
	
	public void print() {
		for (Object o: unitClausesSet.toArray()) {
			Clause c=(Clause) o;
			System.out.println(c);
		}
	}
	
}
