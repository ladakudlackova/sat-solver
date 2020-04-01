package tseitin_to_dimacs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Scanner;

public class IOUtils {
	
	private static final String INVALID_INPUT_FILENAME	= "Invalid input file name.";
	private static final String INVALID_OUTPUT_FILENAME	= "Invalid output file name.";
	
	public static Reader getReader(String inputFileName, Boolean valid) {
		Reader r=null;
		try {
			r = new FileReader(inputFileName);
		} catch (FileNotFoundException e) {
			valid=false;
			System.out.println(INVALID_INPUT_FILENAME);
		}
		return r;
	}
	
	public static FileOutputStream createOutputFile(String outputFileName, Boolean valid) {
		FileOutputStream fos=null;
		try {
			fos = new FileOutputStream(new File(outputFileName));
		} catch (FileNotFoundException e) {
			valid=false;
			System.out.println(INVALID_OUTPUT_FILENAME);
		}
		return fos;
	}
	
	public static void print(OutputStream os, String cnf, Boolean valid) {
		try {
			os.write(cnf.getBytes());
			os.close();
		} catch (IOException e) {
			valid=false;
			System.out.println(INVALID_OUTPUT_FILENAME);
		}
	}
}
