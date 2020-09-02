package dpll;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import clauses_base.ClausesBase;
import tseitin.Assignment;
import tseitin.TseitinVariableToken;


public class Clauses extends ClausesBase{

	private HashSet<Clause> clausesSet = new HashSet<Clause>();
	private HashSet<Clause> unitClausesSet = new HashSet<Clause>();
	private ArrayList<Clause>[] variableClausesEdges;
	protected int unsatisfiedCount = 0;
	
	public Clauses(Clauses clauses) { //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
		this.clausesSet=new HashSet<Clause>( clauses.clausesSet);
		this.unitClausesSet=new HashSet<Clause>( clauses.unitClausesSet);
		this.variableClausesEdges=clauses.variableClausesEdges;
	}
	
	public Clauses() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(TseitinVariableToken[] variables) {
		
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
	
	public void addClause(Clause c) {
		
		//if (c.getUnassignedCount()==1)
	//		unitClausesSet.add(c);
	//	else
			clausesSet.add(c);
		
	    unsatisfiedCount++;
	}	
	
	@Override
	public void addClause(Object[] clause, TseitinVariableToken[] variables) {
		addClause(new Clause(clause, variables));
	}
	
	@Override
	public Clause getFirstUnitClause() {
		Iterator<Clause> iterator = unitClausesSet.iterator();
		if (iterator.hasNext())
			return iterator.next();
		return null;
	}
	
	@Override
	public void updateClauses(Assignment a){
		
		for (Clause clause : variableClausesEdges[a.getVariable().getIndex()]) {
			boolean wasSatisfied = clause.isSatisfied();
			clause.setValue(a.getVariable(), a.getValue());
			if (clause.getUnassignedCount()==0)
				unitClausesSet.remove(clause);
			if (!wasSatisfied && clause.isSatisfied()) {
				unsatisfiedCount--;
			}
			else if (clause.failed()) {
				failed=true;
				continue;
			}
			if (clause.getUnassignedCount()==1 && !clause.isSatisfied())
				unitClausesSet.add(clause);
		}
	}
	
	@Override
	public void resetValues(List<Integer> last, TseitinVariableToken[] variables) {
		
		for (Integer i : last) {
			TseitinVariableToken var=variables[i];
			resetValue(var);
		}
		failed=false;
	}
	
	@Override
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

	public Collection<Clause> getAllClauses() {
		return clausesSet;
	}
	
	public Iterator<Clause> getAllClausesIterator() {
		return clausesSet.iterator();
	}

	@Override
	public boolean allSatisfied() {
		return unsatisfiedCount==0;
	}
	

}
