package utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class sampleFile {
	static ArrayList<String> fileList = new ArrayList<>();
	public static void main(String[] args){
		handleDir(new File("gcc-12.2.0"));
		ArrayList<String> sampling = getRandomElement(fileList,40);
		writeToFile(sampling,"ground_truth/targetFiles");

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

	public static ArrayList<String>
	getRandomElement(ArrayList<String> list, int totalItems)
	{
		Random rand = new Random();

		ArrayList<String> newList = new ArrayList<>();
		int i =0;
		while (i<totalItems){

			int randomIndex = rand.nextInt(list.size());
			String item = list.get(randomIndex);
			if (item.contains("testsuite")||item.endsWith(".h")||item.endsWith(".hpp")){
			}else {
				newList.add(item);
				list.remove(randomIndex);
				i++;
			}
		}
		return newList;
	}
	public static void handleDir( File f){
		if (f.isDirectory()){
			for (File file:f.listFiles()){
				handleDir(file);
			}
		}else {
			if (f.getName().endsWith(".c")||f.getName().endsWith(".cc")||f.getName().endsWith(".h")||f.getName().endsWith(".cpp")||f.getName().endsWith(".hpp"))
			{
				fileList.add(f.getPath());
			}

		}
	}
}
