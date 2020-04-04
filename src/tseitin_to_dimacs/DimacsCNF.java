package tseitin_to_dimacs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import simple_nnf_tree.SimpleNNFVariableToken;
import tseitin.Assignment;
import tseitin.TseitinVariableToken;

public class DimacsCNF {
	
	public static final String COMMENT = "c ";
	public static final String HEADER 	= "p cnf ";
	private static final String NNF_VARIABLES = "Original NNF variables:\n";
	private static final String ROOT_VARIABLE = "Root variable: ";
	private static final String EQ 		= "~";
	private static final String NL 		= System.lineSeparator();	
	private static final int COMMENTS_LINE = 5;
	
	private Collection<Assignment[]> clauses;
	private Collection<SimpleNNFVariableToken> nnfVars;
	private ArrayList<TseitinVariableToken> variables;
	private int varsCount;
	int rootVar;
	
	protected DimacsCNF(Collection<Assignment[]> clauses, 
			Collection<SimpleNNFVariableToken> nnfVars, ArrayList<TseitinVariableToken> tseitinVars) {
		this.clauses = clauses;
		this.nnfVars = nnfVars;
		this.varsCount = tseitinVars.size();
		variables = tseitinVars;
		rootVar = nnfVars.size()+1;
	}
	
	public DimacsCNF(Collection<Integer[]> intClauses, int varsCount) {
		clauses =  new ArrayList<Assignment[]>();
		for (Integer[] intClause : intClauses) {
			Assignment[] clause = new Assignment[intClause.length];
			int i = 0;
			for (Integer intAssignment : intClause) {
				if (intAssignment>0)
					clause[i]= new Assignment(variables.get(intAssignment), true);
				else 
					clause[i]= new Assignment(variables.get(-intAssignment), false);
			}
		}
			
		this.varsCount = varsCount;
		rootVar = nnfVars.size()+1;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		appendComments(sb, nnfVars);
		appendHeader(sb, varsCount, clauses.size());
		appendClauses(sb, clauses);
		return sb.toString();
	}
	
	private void appendComments(StringBuilder sb,Collection<SimpleNNFVariableToken> nnfVars) {	
		sb.append(COMMENT);
		sb.append(ROOT_VARIABLE);
		sb.append(rootVar);
		sb.append(NL);
		sb.append(COMMENT);
		sb.append(NNF_VARIABLES);
		appendNNFVars(sb, nnfVars);		
	}
	
	private void appendNNFVars(StringBuilder sb, Collection<SimpleNNFVariableToken> nnfVars) {   
		int count=0;
		for (SimpleNNFVariableToken nnfVar : nnfVars) {			//nnfVar~tVar
			if (count%COMMENTS_LINE==0)							
				sb.append(COMMENT);
			sb.append(nnfVar.getToken());						
			sb.append(EQ);
			sb.append(nnfVar.getTseitinVar().getToken());	
			if (count%COMMENTS_LINE==COMMENTS_LINE-1)
				sb.append(NL);
			else 
				sb.append("  ");
			count++;
		}
		if (count%COMMENTS_LINE>0)
			sb.append(NL);
	}
	
	private void appendHeader(StringBuilder sb, int nbvar, int nbclauses) {   
																//p cnf nbvar nbclauses
		sb.append(HEADER);
		sb.append(nbvar);
		sb.append(" ");
		sb.append(nbclauses);
		sb.append(NL);
	}

	private void appendClauses(StringBuilder sb, Collection<Assignment[]> clauses) {
		ArrayList<Assignment[]> clausesFromRoot = new ArrayList<Assignment[]>(clauses);
		Assignment[] rootClause = clausesFromRoot.remove(clausesFromRoot.size()-1);
		clausesFromRoot.add(0,rootClause);
		for (Assignment[] clause: clausesFromRoot) 
			appendClause(sb, clause);
	}
	
	private void appendClause(StringBuilder sb, Assignment[] clause) {
		for (Assignment a:clause) {
			sb.append(a.toString());
			sb.append(" ");
		}
		sb.append("0");
		sb.append(NL);
	}
}
