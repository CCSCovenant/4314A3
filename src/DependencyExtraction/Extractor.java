package DependencyExtraction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extractor {
	static Extractor extractor = new Extractor();
	private Extractor(){

	}

	public static Extractor getInstance() {
		return extractor;
	}

	public Graph Code2graph(String fileDir){
		Graph G = new Graph();
		File dir = new File(fileDir);
		handleDir(G,dir);
		return G;
	}
	public void handleDir(Graph G,File f){
		if (f.isDirectory()){
			for (File file:f.listFiles()){
				handleDir(G,file);
			}
		}else {
			List<String> dependedFiles = getCInclude(f);

			for (String s:dependedFiles){
				G.addEdge(f.getName(),s);
			}
		}
	}
	public List<String> getCInclude(File file){
		List<String> Includes = new ArrayList<>();
		String regex = "^#include ";
		Pattern pattern = Pattern.compile(regex);

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while (line!=null){
				if (pattern.matcher(line).lookingAt()){
					String dependName = line.substring(9);
					if (dependName.charAt(0)=='<'){

					}else {
						Includes.add(dependName);
					}
				}
				line = br.readLine();
			}
		}catch (IOException e){

		}
		return Includes;
	}
}
