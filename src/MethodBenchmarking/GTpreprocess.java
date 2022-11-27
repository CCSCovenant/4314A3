package MethodBenchmarking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class GTpreprocess {
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("LocalTA/understand-srcML.local.diff.ta");
		ArrayList<String> result = preprocessDiff(file);
		writeToFile(result,"GT/temp");
	}

	public static ArrayList<String> preprocessDiff(File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(file);
		ArrayList<String> set = new ArrayList();
		while (scanner.hasNextLine()){
			String line = scanner.nextLine().trim();
			if (line.endsWith(".h"))
			set.add(line);
		}
		return set;
	}
	public static void writeToFile(ArrayList<String> set, String path) {
		try {
			FileWriter myWriter = new FileWriter(path);

			for (String temp : set) {
				myWriter.write(temp + "\n");
			}
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}
