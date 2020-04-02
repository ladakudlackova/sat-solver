package tseitin_to_dimacs;

import java.util.ArrayList;
import java.util.Collection;

import simple_nnf_tree.SimpleNNFVariableToken;
import tseitin.Assignment;

public class DimacsCNF {
	
	private static final String NNF_VARIABLES = "Original NNF variables:\n";
	private static final String ROOT_VARIABLE = "Root variable: ";
	private static final String COMMENT = "c ";
	private static final String HEADER 	= "p cnf ";
	private static final String EQ 		= "~";
	private static final String NL 		= System.lineSeparator();
	
	private static final int COMMENTS_LINE = 5;
	
	
	public static String getDimacsCNF(Collection<Assignment[]> clauses, Collection<SimpleNNFVariableToken> nnfVars, int vars) {
		StringBuilder sb = new StringBuilder();
		appendComments(sb, nnfVars);
		appendHeader(sb, vars, clauses.size());
		appendClauses(sb, clauses);
		return sb.toString();
	}
	
	private static void appendComments(StringBuilder sb,Collection<SimpleNNFVariableToken> nnfVars) {	
		sb.append(COMMENT);
		sb.append(ROOT_VARIABLE);
		int rootVar = nnfVars.size()+1;
		sb.append(rootVar);
		sb.append(NL);
		sb.append(COMMENT);
		sb.append(NNF_VARIABLES);
		appendNNFVars(sb, nnfVars);		
	}
	
	private static void appendNNFVars(StringBuilder sb, Collection<SimpleNNFVariableToken> nnfVars) {   
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
	
	private static void appendHeader(StringBuilder sb, int nbvar, int nbclauses) {   
																//p cnf nbvar nbclauses
		sb.append(HEADER);
		sb.append(nbvar);
		sb.append(" ");
		sb.append(nbclauses);
		sb.append(NL);
	}

	private static void appendClauses(StringBuilder sb, Collection<Assignment[]> clauses) {
		ArrayList<Assignment[]> clausesFromRoot = new ArrayList<Assignment[]>(clauses);
		Assignment[] rootClause = clausesFromRoot.remove(clausesFromRoot.size()-1);
		clausesFromRoot.add(0,rootClause);
		for (Assignment[] clause: clausesFromRoot) 
			appendClause(sb, clause);
	}
	
	private static void appendClause(StringBuilder sb, Assignment[] clause) {
		for (Assignment a:clause) {
			sb.append(a.toString());
			sb.append(" ");
		}
		sb.append("0");
		sb.append(NL);
	}
}
