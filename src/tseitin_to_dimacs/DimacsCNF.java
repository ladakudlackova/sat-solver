package tseitin_to_dimacs;

import java.util.Collection;

import simple_nnf_tree.SimpleNNFVariableToken;
import tseitin.Assignment;

public class DimacsCNF {
	
	private static final String COMMENT = "c ";
	private static final String HEADER 	= "p cnf ";
	private static final String EQ 		= "~";
	private static final String DEL 	= ", ";
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
				int count=0;
																//c comments
		for (SimpleNNFVariableToken nnfVar : nnfVars) {
			if (count%COMMENTS_LINE==0)
				sb.append(COMMENT);
			sb.append(nnfVar.getToken());
			sb.append(EQ);
			sb.append(nnfVar.getTseitinVar().getToken());
			sb.append(DEL);	
			if (count%COMMENTS_LINE==COMMENTS_LINE-1)
				sb.append(NL);
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
		for (Assignment[] clause: clauses) {
			{
				for (Assignment a:clause) {
					sb.append(a.toString());
					sb.append(" ");
				}
				sb.append("0");
				sb.append(NL);
			}
			
		}
	}
}
