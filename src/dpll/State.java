package dpll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tseitin.Assignment;

public class State {
	
	protected ArrayList<ArrayList<Assignment>> clauses;
	protected ArrayList<List<Assignment>> unitClauses;
	protected ArrayList<ArrayList<Assignment>>[] variableClausesEdges;
	
	protected State(State s) {
	   this(s.clauses, s.unitClauses, s.variableClausesEdges);
	}
	
	@SuppressWarnings("unchecked")
	protected State(
			ArrayList<ArrayList<Assignment>> clauses,
			ArrayList<List<Assignment>> unitClauses,
			ArrayList<ArrayList<Assignment>>[] variableClausesEdges) {
		
		HashMap<String, ArrayList<Assignment>> clausesMap = initClausesMap(clauses);	
		this.unitClauses = new ArrayList<List<Assignment>>(unitClauses);
		int len=variableClausesEdges.length;
		this.variableClausesEdges = new ArrayList[len];
		for (int i=1;i<len;i++) {
			ArrayList<ArrayList<Assignment>> edges = new ArrayList<ArrayList<Assignment>>();
			for (ArrayList<Assignment> clause:variableClausesEdges[i]) {
				ArrayList<Assignment> copyClause = getCopy(clause, clausesMap);
				if (copyClause!=null)
					edges.add(copyClause);
			}
			this.variableClausesEdges[i]=edges;
		}
	}
	
	private HashMap<String, ArrayList<Assignment>> initClausesMap(
			ArrayList<ArrayList<Assignment>> clauses0){
		
		HashMap<String, ArrayList<Assignment>> clausesMap = new HashMap<String, ArrayList<Assignment>>();
		clauses = new ArrayList<ArrayList<Assignment>>();
		for (ArrayList<Assignment> clause:clauses0) {
			ArrayList<Assignment> clauseCopy = new ArrayList<Assignment>(clause);
			StringBuilder key = new StringBuilder();
			for (Assignment a:clause)
				key.append(a.toString());
			clausesMap.put(key.toString(), clauseCopy);
			clauses.add(clauseCopy);
		}
		return clausesMap;
	}
	
	private ArrayList<Assignment> getCopy(ArrayList<Assignment> clause,
			HashMap<String, ArrayList<Assignment>> clausesMap){
		StringBuilder key = new StringBuilder();
		for (Assignment a:clause)
			key.append(a.toString());
		Object o =clausesMap.get(key.toString());
		return clausesMap.get(key.toString());
	}
	
}
