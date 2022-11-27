package DependencyExtraction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class IncludeExtraction {
	/*
	overflow
	* **/
	public static void main(String[] args){
		Extractor extractor = Extractor.getInstance();
		Graph g = new Graph();
		extractor.handleDir(g,new File("gcc-12.2.0"));
		String TA = Graph2TAConverter.graph2TAConverter.Graph2TA(g);
		try {
			FileWriter myWriter = new FileWriter("test-include.ta");
			myWriter.write(TA);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}
