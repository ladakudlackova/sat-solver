package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;

public class IOUtils {
	
	private static final String INVALID_INPUT_FILENAME	= "Invalid input file name.";
	private static final String INVALID_OUTPUT_FILENAME	= "Invalid output file name.";
	private static final String CANNOT_WRITE = "Cannot write to output.";
	
	public static FileReader createReader(String inputFileName, Boolean valid) {
		FileReader r=null;
		try {
			r = new FileReader(inputFileName);
		} catch (FileNotFoundException e) {
			reportError(INVALID_INPUT_FILENAME, valid);
		}
		return r;
	}	
	
	public static FileOutputStream createOutputFile(String outputFileName, Boolean valid) {
		FileOutputStream fos=null;
		try {
			fos = new FileOutputStream(new File(outputFileName));
		} catch (FileNotFoundException e) {
			reportError(INVALID_OUTPUT_FILENAME, valid);
		}
		return fos;
	}
	
	public static void print(OutputStream os, String cnf, Boolean valid) {
		try {
			os.write(cnf.getBytes());
			os.close();
		} catch (IOException e) {
			reportError(CANNOT_WRITE, valid);
		}
	}
	
	public static void closeReader(Reader r, Boolean valid) {
		try {
			r.close();
		} catch (IOException e) {
			reportError(CANNOT_WRITE, valid);
		}
	}
	
	public static void reportError(String message, Boolean valid) {
		valid=false;
		System.out.println(message);
	}
}
