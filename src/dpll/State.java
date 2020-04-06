package dpll;

import java.util.ArrayList;
import java.util.List;

import tseitin.Assignment;

public class State {

	protected ArrayList<List<Assignment>> unitClauses;
	protected Boolean[] assignment;
	protected ArrayList<ArrayList<Assignment>> clauses;
	
	protected State(
			ArrayList<List<Assignment>> unitClauses,
			Boolean[] assignment,
			ArrayList<ArrayList<Assignment>> clauses) {
		
		this.unitClauses = new ArrayList<List<Assignment>>(unitClauses);
		this.assignment =assignment;
		this.clauses = new ArrayList<ArrayList<Assignment>>(clauses);
	}
	
}
