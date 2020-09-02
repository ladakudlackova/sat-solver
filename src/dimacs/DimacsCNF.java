package dimacs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import clauses_base.ClausesBase;
import dpll.Clause;
import dpll.Clauses;
import simple_nnf_tree.SimpleNNFVariableToken;
import tseitin.Assignment;
import tseitin.TseitinVariableToken;
import watched_literals.ClausesWithWatches;



public class DimacsCNF {

	public static final String COMMENT = "c ";
	public static final String HEADER = "p cnf ";
	private static final String NNF_VARIABLES = "Original NNF variables:";
	private static final String ROOT_VARIABLE = "Root variable: ";
	private static final String EQ = "~";
	private static final String NL = System.lineSeparator();
	private static final int COMMENTS_LINE = 5;

	private ClausesBase clauses;
	private Collection<SimpleNNFVariableToken> nnfVars;
	private TseitinVariableToken[] variables;
	private int varsCount;
	private int clausesCount;
	int rootVar;

	public DimacsCNF(Collection<Integer[]> intClauses, int varsCount,
			boolean withWatchedLiterals) {
		
		initClauses(withWatchedLiterals);
		this.varsCount = varsCount;
		clausesCount = intClauses.size();
		variables=new TseitinVariableToken[varsCount+1];
		for (int index = 1; index<=varsCount; index++) 
			variables[index]=new TseitinVariableToken(index);
		for (Integer[] intClause : intClauses) {
			clauses.addClause(intClause, variables);
			//for (int a:intClause)
			//	System.out.print(a+"X");
			//System.out.println();
		}
		
	}

	public DimacsCNF(Collection<Assignment[]> aClauses, Collection<SimpleNNFVariableToken> nnfVariables,
			ArrayList<TseitinVariableToken> tseitinVars, boolean watchedLiterals) {

		initClauses(watchedLiterals);
		tseitinVars.add(0, null);
		variables = tseitinVars.toArray(new TseitinVariableToken[tseitinVars.size()]);
		this.nnfVars = nnfVariables;
		rootVar = nnfVars.size() + 1;
		
		for (Assignment[] aClause : aClauses) {
			clauses.addClause(aClause, variables);
			//for (Assignment a:aClause)
			//	System.out.print(a+" ");
			//System.out.println();
		}
		this.varsCount = tseitinVars.size();
		clausesCount = aClauses.size();
		
	}

	private void initClauses(boolean watchedLiterals) {
		
		if (watchedLiterals)
			clauses=new ClausesWithWatches();
		else
			clauses=new Clauses();
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		appendComments(sb, nnfVars);
		appendHeader(sb);
		appendDimacsClauses(sb);
		return sb.toString();
	}

	private void appendComments(StringBuilder sb, Collection<SimpleNNFVariableToken> nnfVars) {
		sb.append(COMMENT);
		sb.append(ROOT_VARIABLE);
		sb.append(rootVar);
		sb.append(NL);
		sb.append(COMMENT);
		sb.append(NNF_VARIABLES);
		sb.append(System.lineSeparator());
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
		sb.append(clausesCount);
		sb.append(NL);
	}

	private void appendDimacsClauses(StringBuilder sb) {
		Iterator<Clause> allClauses = ((Clauses)clauses).getAllClausesIterator();
		while (allClauses.hasNext()) 
			appendDimacsClause(sb, (Clause)allClauses.next());
		
	}

	private void appendDimacsClause(StringBuilder sb, Clause clause) {
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

	public ClausesBase getClauses() {
		return clauses;
	}
	
	public int getClausesCount() {
		return clausesCount;
	}

	public TseitinVariableToken[] getVariables() {
		return variables;
	}
	
	public Collection<SimpleNNFVariableToken> getNNFVars() {
		return nnfVars;
	}
}
