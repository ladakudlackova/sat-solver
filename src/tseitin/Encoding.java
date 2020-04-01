package tseitin;

import java.util.ArrayList;
import java.util.Collection;

import simple_nnf_tree.DerivationTree;
import simple_nnf_tree.SimpleNNFVariableToken;

public class Encoding {

	DerivationTree tree;
	ArrayList<TseitinVariableToken> variables = new ArrayList<TseitinVariableToken>();
	CNFDefinition cnfDef;
	
	public void encode(DerivationTree tree) {
		this.tree=tree;
		initVariables();
		cnfDef = new CNFDefinition();
		tree.traverse(cnfDef);
	}
	
	private void initVariables() {
		TseitinVariableToken.reset();
		addNNFVariables();
		tree.traverse(new VaribleNameDefinition());	
	}
	
	private void addNNFVariables() {
		for (SimpleNNFVariableToken nnfVar : tree.getNNFVariables()) {
			TseitinVariableToken tseitinVar = new TseitinVariableToken();
			variables.add(tseitinVar);
			nnfVar.setTseitinVar(tseitinVar);
		} 
	}

	public Collection<Assignment[]> getClauses() {
		return cnfDef.clauses;
	}
	
	public int getTseitinVarCount() {
		return variables.size();
	}
}
