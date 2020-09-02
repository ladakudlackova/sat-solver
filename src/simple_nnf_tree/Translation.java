package simple_nnf_tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import dimacs.DimacsCNF;
import dpll.Clause;
import dpll.Clauses;
import tseitin.TseitinVariableToken;
import simple_nnf_tree.SimpleNNFOperatorToken.operators;

public class Translation {	
	
	public static String translate2PhiAndNotPsi(DimacsCNF phi, DimacsCNF psi) {  // nnf ~ (phi & !psi)
		
		
		
		StringBuilder nnf = new StringBuilder(appendLeftPar(operators.AND));
		List<Clause> clausesPhiList = new ArrayList<Clause>(((Clauses)phi.getClauses()).getAllClauses());
		List<Clause> clausesNotPsiList = new ArrayList<Clause>();
		for (Clause c :((Clauses)psi.getClauses()).getAllClauses()){
			clausesNotPsiList.add(c.negateLiterals());
		}
	
		
		nnf.append(translateClauses(clausesPhiList, operators.AND, operators.OR));
		nnf.append(translateClauses(clausesNotPsiList, operators.OR, operators.AND));
		return nnf.toString();
	}
	
public static String translate2NotPhi(DimacsCNF phi) {  // nnf ~ !phi)
		
		
		
	//	List<Clause> clausesPhiList = new ArrayList<Clause>(((Clauses)phi.getClauses()).getAllClauses());
		List<Clause> clausesNotPhiList = new ArrayList<Clause>();
		for (Clause c :((Clauses)phi.getClauses()).getAllClauses()){
			clausesNotPhiList.add(c.negateLiterals());
		}
	
		
		//nnf.append(translateClauses(clausesPhiList, operators.AND, operators.OR));
		StringBuilder nnf = new StringBuilder(translateClauses(clausesNotPhiList, operators.OR, operators.AND));
		return nnf.toString();
	}
	
	private static String translateClauses(List<Clause> clauses, 
			SimpleNNFOperatorToken.operators firstOperator, operators secondOperator) {

		StringBuilder nnf = new StringBuilder();
		//System.out.println();
		//System.out.print(clauses. size());
		if (clauses.size()>1) {
			nnf.append(appendLeftPar(firstOperator));
			Clause clause = clauses.get(0);
			clauses = clauses.subList(1, clauses.size());
			//System.out.println(clauses. size());
			nnf.append(translateClause(clause,secondOperator));
			nnf.append(translateClauses(clauses, firstOperator, secondOperator)+operators.RIGHT_PAR.value.getToken());
		}
		else if (clauses.size()==1)
			nnf.append(translateClause(clauses.get(0),secondOperator));
		return nnf.toString();
	}
	
	private static String translateLiterals(ArrayList<TseitinVariableToken> literalsList,
			boolean value,
			SimpleNNFOperatorToken.operators operator) {
		
		StringBuilder nnf = new StringBuilder();
		if (literalsList.size()>1) {
			nnf.append(appendLeftPar(operator));
			TseitinVariableToken var = literalsList.remove(0);
			nnf.append(translateLiteral(var, value));
			nnf.append(translateLiterals(literalsList, value, operator)+operators.RIGHT_PAR.value.getToken());
		}
		else if (literalsList.size()==1)
			nnf.append(translateLiteral(literalsList.get(0), value));
		return nnf.toString();
	}
	
	private static String translateClause(Clause clause, 
			SimpleNNFOperatorToken.operators operator) {

		StringBuilder nnf = new StringBuilder();//appendLeftPar(operator));
		if (clause.getPosLiterals().size()==1 && clause.getNegLiterals().size()>0)
			nnf.append(operator.value.getToken()+" ");
		nnf.append(translateLiterals(new ArrayList<TseitinVariableToken>(clause.getPosLiterals()), true, operator));
		
		nnf.append(translateLiterals(new ArrayList<TseitinVariableToken>(clause.getNegLiterals()), false, operator));
		return nnf.toString();
	}
	
	
	
	private static String translateLiteral(TseitinVariableToken literal,
			boolean value) {
		if (!value)
			return appendLeftPar(operators.NOT)+literal.getToken()+")";
		return literal.getToken()+" ";
	}
	
	private static String appendLeftPar(SimpleNNFOperatorToken.operators operator) {
		return operators.LEFT_PAR.value.getToken()+operator.value.getToken()+" ";
	}
}
