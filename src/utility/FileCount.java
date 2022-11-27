package utility;

import DependencyExtraction.Graph;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FileCount {
	static ArrayList<String> fileList = new ArrayList<>();
	public static void main(String[] args){
		handleDir(new File("gcc-12.2.0"));
		List<String> sampling = getRandomElement(fileList,100);
		StringBuilder out = new StringBuilder();
		for (String s:sampling){
			out.append(s+"\n");
		}
		System.out.println(out);


	}
	public static List<String>
	getRandomElement(List<String> list, int totalItems)
	{
		Random rand = new Random();

		List<String> newList = new ArrayList<>();
		for (int i = 0; i < totalItems; i++) {

			int randomIndex = rand.nextInt(list.size());

			newList.add(list.get(randomIndex));

			list.remove(randomIndex);
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
