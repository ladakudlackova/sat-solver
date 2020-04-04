package test;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import tseitin_to_dimacs.Translation;

@FixMethodOrder
public class TranslationTest {
	
	private static final File DATA_INPUT_FOLDER = Paths.get("src", "test","data", "input").toFile();
	private static final String DATA_OUTPUT_PATH = Paths.get("src", "test","data", "output").toString();
	private static final String SAT_EXT = ".sat";
	private static final String DIMACS_EXT = ".cnf";
	private static final String L_R = ".leftToRight"; 
	private static final String AND = "and";

    @Test
    @Order(1)
    public void translateAllInputData() {
    	
    	for (final File fileEntry : DATA_INPUT_FOLDER.listFiles()) {
    		try {
    			if (fileEntry.isFile()) {
                	String inputFileName = fileEntry.getName();
                	String outputFileName = Paths.get(
                			DATA_OUTPUT_PATH, inputFileName.replace(SAT_EXT, L_R+DIMACS_EXT)).toString();
                	Translation.formula2cnf(false, fileEntry.getPath(), outputFileName);
                	outputFileName = Paths.get(
                			DATA_OUTPUT_PATH, inputFileName.replace(SAT_EXT, DIMACS_EXT)).toString();
                	Translation.formula2cnf(true, fileEntry.getPath(), outputFileName);
                }
    		}
    		catch (Exception ex) {
    			fail(ex.getMessage());
    		}            
        }
    }
    
    
    @Test
    @Order(2)
    public void checkTranslation() throws IOException {
    	try {
			File andFile = Paths.get(DATA_OUTPUT_PATH, AND+L_R+DIMACS_EXT).toFile();
			String output = getOutput(andFile);
			String expected = 	 "3 0"+System.lineSeparator()
								+"-3 1 0"+System.lineSeparator()
								+"-3 2 0"+System.lineSeparator();
			assertEquals(expected, output.toString());
			andFile = Paths.get(DATA_OUTPUT_PATH, AND+DIMACS_EXT).toFile();
			output = getOutput(andFile);
			expected = expected+"3 -1 -2 0"+System.lineSeparator();
			assertEquals(expected, output.toString());
		}
		catch (Exception ex) {
			fail(ex.getMessage());
		}          	
    }
    
    private String getOutput(File outputFile) throws IOException {
    	BufferedReader reader = new BufferedReader(new FileReader(outputFile)); 
		String line; 
		while ((line = reader.readLine()) != null) 
		    if (!(line.startsWith("c")||line.startsWith("p")))
		    	break;
		StringBuilder output = new StringBuilder(line);
		output.append(System.lineSeparator());
		while ((line = reader.readLine()) != null) {
			output.append(line);
			output.append(System.lineSeparator());
		}
		reader.close();
		return output.toString();
    }
}