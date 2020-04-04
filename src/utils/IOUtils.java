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
	
	public static FileReader createReader(String inputFileName) {
		FileReader r=null;
		try {
			r = new FileReader(inputFileName);
		} catch (FileNotFoundException e) {
			reportError(INVALID_INPUT_FILENAME);
			return null;
		}
		return r;
	}	
	
	public static FileOutputStream createOutputFile(String outputFileName) {
		FileOutputStream fos=null;
		try {
			fos = new FileOutputStream(new File(outputFileName));
		} catch (FileNotFoundException e) {
			reportError(INVALID_OUTPUT_FILENAME);
		}
		return fos;
	}
	
	public static void print(OutputStream os, String cnf, Boolean valid) {
		try {
			os.write(cnf.getBytes());
			os.close();
		} catch (IOException e) {
			reportError(CANNOT_WRITE);
		}
	}
	
	public static void closeReader(Reader r) {
		try {
			r.close();
		} catch (IOException e) {
			reportError(CANNOT_WRITE);
		}
	}
	
	public static void reportError(String message) {
		System.out.println(message);
	}
}
