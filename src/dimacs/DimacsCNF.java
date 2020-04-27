package dimacs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import simple_nnf_tree.SimpleNNFVariableToken;
import tseitin.Assignment;
import tseitin.TseitinVariableToken;



public class DimacsCNF {

	public static final String COMMENT = "c ";
	public static final String HEADER = "p cnf ";
	private static final String NNF_VARIABLES = "Original NNF variables:\n";
	private static final String ROOT_VARIABLE = "Root variable: ";
	private static final String EQ = "~";
	private static final String NL = System.lineSeparator();
	private static final int COMMENTS_LINE = 5;

	private Clauses clauses= new Clauses();
	private Collection<SimpleNNFVariableToken> nnfVars;
	private TseitinVariableToken[] variables;
	private int varsCount;
	int rootVar;

	public DimacsCNF(Collection<Integer[]> intClauses, int varsCount) {
		
		this.varsCount = varsCount;
		variables=new TseitinVariableToken[varsCount+1];
		for (int index = 1; index<=varsCount; index++) 
			variables[index]=new TseitinVariableToken(index);
		for (Integer[] intClause : intClauses) 
			clauses.addClause(new Clause(intClause, variables));	
	}

	public DimacsCNF(Collection<Assignment[]> aClauses, Collection<SimpleNNFVariableToken> nnfVariables,
			ArrayList<TseitinVariableToken> tseitinVars) {

		tseitinVars.add(0, null);
		variables = tseitinVars.toArray(new TseitinVariableToken[tseitinVars.size()]);
		for (Assignment[] aClause : aClauses) 
			clauses.addClause(new Clause(aClause, variables));	
		this.nnfVars = nnfVariables;
		this.varsCount = tseitinVars.size();
		
		rootVar = nnfVars.size() + 1;
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		appendComments(sb, nnfVars);
		appendHeader(sb);
		appendClauses(sb);
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

	private void appendHeader(StringBuilder sb) {
		
		sb.append(HEADER);
		sb.append(varsCount);
		sb.append(" ");
		sb.append(clauses.getUnsatisfiedCount());
		sb.append(NL);
	}

	private void appendClauses(StringBuilder sb) {//, ArrayList<ArrayList<Assignment>> clauses) {
		//ArrayList<List<Assignment>> clausesFromRoot = new ArrayList<List<Assignment>>(clauses);
		//List<Assignment> rootClause = clausesFromRoot.remove(clausesFromRoot.size() - 1);
		//clausesFromRoot.add(0, rootClause);
		
		//for (List<Assignment> clause : clausesFromRoot)
		//	appendClause(sb, clause);
		Iterator<Clause> allClauses = clauses.getClauses();
		while (allClauses.hasNext()) 
			appendClause(sb, allClauses.next());
		
	}

	private void appendClause(StringBuilder sb, Clause clause) {
		for (TseitinVariableToken var : clause.getPosLiterals()) {
			sb.append(var.getIndex());
			sb.append(" ");
		}
		for (TseitinVariableToken var : clause.getNegLiterals()) {
			sb.append("-");
			sb.append(var.getIndex());
			sb.append(" ");
		}
		sb.append("0");
		sb.append(NL);
	}

	public Clauses getClauses() {
		return clauses;
	}

	public TseitinVariableToken[] getVariables() {
		return variables;
	}
	
	public Collection<SimpleNNFVariableToken> getNNFVars() {
		return nnfVars;
	}
}
