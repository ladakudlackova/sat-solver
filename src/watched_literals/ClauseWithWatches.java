package watched_literals;

import java.util.ArrayList;
import java.util.List;

import clauses_base.ClauseBase;
import tseitin.Assignment;
import tseitin.TseitinVariableToken;

public class ClauseWithWatches extends ClauseBase{

	private List<Assignment> literals = new ArrayList<Assignment>();
	private WatchedLiteral[] watchedLiterals= new WatchedLiteral[2];
	private boolean failed = false;
	
	public ClauseWithWatches(Object[] clause, TseitinVariableToken[] variables) {
		
		for (Object literal : clause) 
			addLiteral(literal, variables);
	}
	
	@Override
	protected void addLiteral(Object literal, TseitinVariableToken[] variables) {           
		
		Assignment a = new Assignment(literal, variables);
		literals.add(a);
	}  

	@Override
	public Assignment getUnitAssignment() {

		for (int i=0;i<watchedLiterals.length;i++) 
			if (watchedLiterals[i].getVariableValue()==null)
				return watchedLiterals[i].getLiteral();
		return null;
	}
	
	public Assignment getFirstLiteral() {
		return literals.get(0);
	}
	
	public WatchedLiteral addWatchedLiteral(int index) {         
		
		if (0<=index && index<watchedLiterals.length) {
			WatchedLiteral lit = new WatchedLiteral(literals.get(index), this, index);
			watchedLiterals[index]=lit;
			return lit;
		}
		return null;
	}
	
	protected WatchedLiteral updateWatchedLiteral(int index) {
		
		WatchedLiteral watchedLit;		
		WatchedLiteral otherWatchedLit;			
		if (index==watchedLiterals[0].getIndex()) {
			watchedLit=watchedLiterals[0];
			otherWatchedLit=watchedLiterals[1];
		}
		else {
			watchedLit=watchedLiterals[1];
			otherWatchedLit=watchedLiterals[0];
		}
		return updateWatchedLiteral(watchedLit, otherWatchedLit);
	}
	
	private WatchedLiteral updateWatchedLiteral(
							WatchedLiteral watchedLit,
							WatchedLiteral otherWatchedLit){
		
		if (isUnit() && !otherWatchedLit.isSatisfied()) {
			failed=true;
			return watchedLit;
		}
	    WatchedLiteral freeLit = getFreeLiteral(watchedLit.getIndex(), otherWatchedLit.getIndex());
	    if (freeLit!=null) 
	    	watchedLit.update(freeLit);
		return watchedLit;
		
	}
	
	private WatchedLiteral getFreeLiteral(int watchedIndex, int otherIndex) {
		
		if (literals.size()<=2)
			return null;
		int i=watchedIndex+1;
		while (i!=watchedIndex) {
			if (i==literals.size())
				i=0;
			if (i!=otherIndex) {
				Assignment a = literals.get(i);
				Boolean value = a.getVariable().getValue();
				if (value==null || value.equals(a.getValue())) 
					return new WatchedLiteral(a, this, i);
			}
			i++;
		}
		return null;
	}
	
	protected boolean isUnit() {
		
	
		if (isSatisfied())
			return false;
		if (watchedLiterals[0].getVariableValue()==null) {
				if (watchedLiterals[1].getVariableValue()!=null) 
					return true;
		}
		else if (watchedLiterals[1].getVariableValue()==null)
			return true;
		return false;
	}

	protected boolean isNewUnit() {	
		
		if (isSatisfied())
			return false;
		if (watchedLiterals[0].getVariableValue()==null 
				&& watchedLiterals[1].getVariableValue()==null) {
				return true;
		}
		return false;
	}

	
	@Override
	public boolean isSatisfied() {
		
		
		
		return (watchedLiterals[0].isSatisfied()||watchedLiterals[1].isSatisfied());
	/*	for (Assignment a:literals)
			if (a.getValue()==a.getVariable().getValue()) {
				return true;
			}
		return false;*/
	}

	@Override
	public boolean failed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed=failed;
	}
	
}
