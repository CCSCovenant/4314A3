package MethodBenchmarking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class TApreprocess {
	public static void main(String[] args) throws FileNotFoundException {
		File file1 = new File("understand.ta");
		File file2 = new File("include.ta");
		File file3 = new File("srcML.ta");
		File file_list = new File("ground_truth/targetFiles");

		HashSet<String> list = preprocessRaw(file_list);

		ArrayList<String> understand = getSample(file1,list);
		ArrayList<String> include = getSample(file2,list);
		ArrayList<String> srcML = getSample(file3,list);



		writeToFile(understand,list,"LocalTA/understand.local.ta");
		writeToFile(include,list,"LocalTA/include.local.ta");
		writeToFile(srcML,list,"LocalTA/srcML.local.ta");
		ArrayList<String> diff13 = new ArrayList<>(understand);

	}

	public static HashSet<String>  preprocessRaw(File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(file);
		HashSet<String> set = new HashSet<>();
		while (scanner.hasNextLine()){
			String line = scanner.nextLine().trim();
			set.add(line);
		}
		return set;
	}
	public static ArrayList<String> getSample(File file,HashSet<String> rawSet) throws FileNotFoundException{
		Scanner scanner = new Scanner(file);
		ArrayList<String> set = new ArrayList<>();
		while (scanner.hasNextLine()){
			String line = scanner.nextLine().trim();
			if (line.split("\\s+")[0].equals("$INSTANCE")||line.split("\\s+")[0].equals("FACT TUPLE :")){
				continue;
			}
			String name = line.split("\\s+")[1];
			if (rawSet.contains(name)){
				set.add(line);
			}
		}
		return set;
	}
	public static void writeToFile(ArrayList<String> set, HashSet<String> instance,String path) {
		try {
			FileWriter myWriter = new FileWriter(path);
			myWriter.write("FACT TUPLE :\n");
			for (String temp : instance) {
				myWriter.write("$INSTANCE "+temp + " cFile\n");
			}
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
