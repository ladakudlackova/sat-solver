package tseitin;

import java.util.ArrayList;
import java.util.Collection;

import simple_nnf_tree.DerivationTree;
import simple_nnf_tree.SimpleNNFVariableToken;

public class Encoding {

	ArrayList<TseitinVariableToken> variables = new ArrayList<TseitinVariableToken>();
	CNFDefinition cnfDef;
	
	public void encode(DerivationTree tree) {
		initVariables(tree);
		cnfDef = new CNFDefinition();
		tree.traverse(cnfDef);
		cnfDef.addRootClause(tree.getRoot());
	}
	
	private void initVariables(DerivationTree tree) {
		TseitinVariableToken.reset();
		addNNFVariables(tree);
		tree.traverse(new VaribleNameDefinition());	
	}
	
	private void addNNFVariables(DerivationTree tree) {
		for (SimpleNNFVariableToken nnfVar : tree.getNNFVariables()) {
			TseitinVariableToken tseitinVar = new TseitinVariableToken();
			variables.add(tseitinVar);
			nnfVar.setTseitinVar(tseitinVar);
		} 
	}

	public Collection<Assignment[]> getClauses() {
		return cnfDef.getClauses();
	}
	
	public int getTseitinVarCount() {
		return TseitinVariableToken.getCount();
	}
}
