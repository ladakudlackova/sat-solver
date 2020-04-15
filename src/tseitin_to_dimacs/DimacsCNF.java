package tseitin_to_dimacs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import simple_nnf_tree.SimpleNNFVariableToken;
import tseitin.Assignment;
import tseitin.TseitinVariableToken;


// TODO: edges -class cast
public class DimacsCNF {

	public static final String COMMENT = "c ";
	public static final String HEADER = "p cnf ";
	private static final String NNF_VARIABLES = "Original NNF variables:\n";
	private static final String ROOT_VARIABLE = "Root variable: ";
	private static final String EQ = "~";
	private static final String NL = System.lineSeparator();
	private static final int COMMENTS_LINE = 5;

	private ArrayList<ArrayList<Assignment>> clauses;
	private Collection<SimpleNNFVariableToken> nnfVars;
	private ArrayList<TseitinVariableToken> variables;
	private int varsCount;
	int rootVar;

	public DimacsCNF(Collection<Integer[]> intClauses, int varsCount) {
		clauses = new ArrayList<ArrayList<Assignment>>();
		init(intClauses, varsCount);
		this.varsCount = varsCount;
	}

	public DimacsCNF(Collection<Assignment[]> clauses, Collection<SimpleNNFVariableToken> nnfVariables,
			ArrayList<TseitinVariableToken> tseitinVars) {

		this.clauses = new ArrayList<ArrayList<Assignment>>();
		for (Assignment[] clause : clauses) {
			ArrayList<Assignment> clauseList = new ArrayList<Assignment>( Arrays.asList(clause));
			this.clauses.add(clauseList);
		}
		this.nnfVars = nnfVariables;
		this.varsCount = tseitinVars.size();
		variables = tseitinVars;
		rootVar = nnfVars.size() + 1;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Assignment>>[] createVariableClausesEdges() {
		ArrayList<ArrayList<Assignment>>[] variableClausesEdges= new ArrayList[variables.size()+1];
		for (TseitinVariableToken var : getVariables()) {
			variableClausesEdges[var.getIndex()] = new ArrayList<ArrayList<Assignment>>();
		}
		for (ArrayList<Assignment> clause : clauses)
			for (Assignment a : clause) {
				(variableClausesEdges[a.getVariable().getIndex()]).add(clause);
			}
		return variableClausesEdges;
	}

	private void init(Collection<Integer[]> intClauses, int varsCount) {
		variables=new ArrayList<TseitinVariableToken>();
		variables.add(null);
		for (int index = 1; index<=varsCount; index++)
			variables.add(new TseitinVariableToken(index));
		for (Integer[] intClause : intClauses) {
			Assignment[] clause = new Assignment[intClause.length];
			int i = 0;
			for (Integer intAssignment : intClause) {
				if (intAssignment > 0)
					clause[i] = new Assignment(getVariables().get(intAssignment), true);
				else
					clause[i] = new Assignment(getVariables().get(-intAssignment), false);
				i++;
			}
			ArrayList<Assignment> clauseList = new ArrayList<Assignment>( Arrays.asList(clause));
			clauses.add(clauseList);			
		}
		variables.remove(0);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		appendComments(sb, nnfVars);
		appendHeader(sb, varsCount, clauses.size());
		appendClauses(sb, clauses);
		return sb.toString();
	}

	private void appendComments(StringBuilder sb, Collection<SimpleNNFVariableToken> nnfVars) {
		sb.append(COMMENT);
		sb.append(ROOT_VARIABLE);
		sb.append(rootVar);
		sb.append(NL);
		sb.append(COMMENT);
		sb.append(NNF_VARIABLES);
		appendNNFVars(sb, nnfVars);
	}

	private void appendNNFVars(StringBuilder sb, Collection<SimpleNNFVariableToken> nnfVars) {
		int count = 0;
		for (SimpleNNFVariableToken nnfVar : nnfVars) { // nnfVar~tVar
			if (count % COMMENTS_LINE == 0)
				sb.append(COMMENT);
			sb.append(nnfVar.getToken());
			sb.append(EQ);
			sb.append(nnfVar.getTseitinVar().getToken());
			if (count % COMMENTS_LINE == COMMENTS_LINE - 1)
				sb.append(NL);
			else
				sb.append("  ");
			count++;
		}
		if (count % COMMENTS_LINE > 0)
			sb.append(NL);
	}

	private void appendHeader(StringBuilder sb, int nbvar, int nbclauses) {
		
		sb.append(HEADER);
		sb.append(nbvar);
		sb.append(" ");
		sb.append(nbclauses);
		sb.append(NL);
	}

	private void appendClauses(StringBuilder sb, ArrayList<ArrayList<Assignment>> clauses) {
		ArrayList<List<Assignment>> clausesFromRoot = new ArrayList<List<Assignment>>(clauses);
		List<Assignment> rootClause = clausesFromRoot.remove(clausesFromRoot.size() - 1);
		clausesFromRoot.add(0, rootClause);
		for (List<Assignment> clause : clausesFromRoot)
			appendClause(sb, clause);
	}

	private void appendClause(StringBuilder sb, List<Assignment> clause) {
		for (Assignment a : clause) {
			sb.append(a.toString());
			sb.append(" ");
		}
		sb.append("0");
		sb.append(NL);
	}

	public ArrayList<ArrayList<Assignment>> getClauses() {
		return clauses;
	}

	public ArrayList<TseitinVariableToken> getVariables() {
		return variables;
	}
	
	public Collection<SimpleNNFVariableToken> getNNFVars() {
		return nnfVars;
	}
}
