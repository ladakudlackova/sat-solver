package dpll;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import tseitin.Assignment;
import tseitin.TseitinVariableToken;
import tseitin_to_dimacs.DimacsCNF;
import tseitin_to_dimacs.Translation;
import utils.DimacsFileUtils;

//	TODO: 	
//			total time;
//			organize project 

public class Dpll {

	private static final String DIMACS_EXT = ".cnf";
	private static final String SMT_LIB = ".sat";

	private ArrayList<ArrayList<Assignment>>[] variableClausesEdges;
	private Boolean[] assignment;
	private List<TseitinVariableToken> variables;
	private boolean unsat = false;
	private int derivationCount = 0;
	private int decisionCount = 0;

	//
	public static void main(String[] args) {

		solve(args[0]);
	}

	public static Boolean[] solve(String inputFileName) {

		DimacsCNF dimacsCNF = createDimacsCNF(inputFileName);
		if (dimacsCNF != null) {
			ArrayList<ArrayList<Assignment>> clauses = dimacsCNF.getClauses();
			ArrayList<ArrayList<Assignment>>[] variableClausesEdges = dimacsCNF.createVariableClausesEdges();

			Dpll dpll = new Dpll(dimacsCNF.getVariables(), variableClausesEdges, clauses);
			dpll.solve(clauses);
			return dpll.assignment;
		}
		return null;
	}

	private Dpll(ArrayList<TseitinVariableToken> variables, ArrayList<ArrayList<Assignment>>[] variableClausesEdges,
			ArrayList<ArrayList<Assignment>> clauses) {

		this.variableClausesEdges = variableClausesEdges;
		this.variables = variables;
		assignment=new Boolean[variables.size()+1];
	}

	private static DimacsCNF createDimacsCNF(String inputFileName) {
		if (inputFileName.endsWith(DIMACS_EXT))
			return DimacsFileUtils.processDimacsFile(inputFileName);
		if (inputFileName.endsWith(SMT_LIB)) {
			return Translation.formula2dimacsCNF(false, inputFileName);
		}
		return null;
	}

	

	private void solve(ArrayList<ArrayList<Assignment>> clauses0) {

		unsat = false;
		ArrayList<List<Assignment>> unitClauses = new ArrayList<List<Assignment>>();
		ArrayList<ArrayList<Assignment>> clauses = new ArrayList<ArrayList<Assignment>>(clauses0);
		for (List<Assignment> clause : clauses)
			if (clause.size() == 1)
				unitClauses.add(clause);
		List<Integer> last = unitPropagation(unitClauses, clauses);
		if (clauses.isEmpty())
			return;
		if (unsat) {
			resetAssignment(last);
			return;
		}
		TseitinVariableToken var = null;
		for (TseitinVariableToken v : variables) // !!
			if (assignment[v.getIndex()] == null) {
				var = v;
				assignment[var.getIndex()] = true;
				break;
			}
		if (var == null) {
			throw new RuntimeException(); // never
		}

		assignment[var.getIndex()] = true;
		last.add(var.getIndex());
		solve(clauses);
		if (!unsat) 
			return;
		
		assignment[var.getIndex()] = false;
		solve(clauses0);
		if (!unsat) 
			return;
		resetAssignment(last);	
	}

	private List<Integer> unitPropagation(ArrayList<List<Assignment>> unitClauses,
			ArrayList<ArrayList<Assignment>> clauses) {

		List<Integer> last = new ArrayList<Integer>();
		while (true) {
			if (unitClauses.isEmpty())
				return last;
			List<Assignment> unitClause = unitClauses.remove(0);
			clauses.remove(unitClause);
			Assignment unit = unitClause.get(0);
			TseitinVariableToken unitVar = unit.getVariable();
			boolean unitValue = unit.getValue();
			assignment[unitVar.getIndex()] = unitValue;
			last.add(unitVar.getIndex());
			infer(unit, unitClauses, clauses);
		}
	}

	private void resetAssignment(List<Integer> last) {
		for (Integer i : last)
			assignment[i] = null;
	}

	private void infer(Assignment unit, ArrayList<List<Assignment>> unitClauses,
			ArrayList<ArrayList<Assignment>> clauses) {

		for (List<Assignment> clause : variableClausesEdges[unit.getVariable().getIndex()]) {
			for (int i = 0; i < clause.size(); i++) {
				Assignment a = clause.get(i);
				if (a.getVariable().equals(unit.getVariable())) {
					if (a.getValue() == unit.getValue())
						clauses.remove(clause);
					else {
						if (clause.size() == 2) {
							clause.remove(i);
							unitClauses.add(clause);

						} else if (clause.size() == 1) {
							unsat = true;
							return; // UNSAT
						}
					}
				}
			}
		}

	}

}
