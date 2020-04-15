package tseitin;

import java.util.ArrayList;
import java.util.Collection;

import simple_nnf_tree.DerivationTree;
import simple_nnf_tree.SimpleNNFVariableToken;

public class Encoding {

	ArrayList<TseitinVariableToken> variables = new ArrayList<TseitinVariableToken>();
	CNFDefinition cnfDef;
	
	public void encode(DerivationTree tree, CNFDefinition.implicationType implicationType) {
		initVariables(tree);
		cnfDef = new CNFDefinition(implicationType);
		tree.traverse(cnfDef);
		cnfDef.addRootClause(tree.getRoot());
	}
	
	private void initVariables(DerivationTree tree) {
		TseitinVariableToken.reset();
		addNNFVariables(tree);
		tree.traverse(new VariableNameDefinition());	
		for (int i=tree.getNNFVariables().size()+1; i<=TseitinVariableToken.getCount();i++) {
			variables.add(new TseitinVariableToken(i));
		}
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
	
	public ArrayList<TseitinVariableToken> getTseitinVariables() {
		return variables;
	}
}
