package simple_nnf_tree;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

	
	public static List<Object> getTokens(Reader reader) throws IOException {
	    StreamTokenizer streamTokenizer = new StreamTokenizer(reader);
	    List<Object> tokens = new ArrayList<Object>();	 
	    int currentToken = streamTokenizer.nextToken();
	    while (currentToken != StreamTokenizer.TT_EOF) {
	        if (streamTokenizer.ttype == StreamTokenizer.TT_WORD)
	        	tokens.add(streamTokenizer.sval);
	        else 
	            tokens.add((char) currentToken);	 
	        currentToken = streamTokenizer.nextToken();
	    }
	 for (Object o:tokens)
		 System.out.println(o);
	    return tokens;
	}
}
