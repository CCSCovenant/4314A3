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
		addFiles(G,dir);
		handleDir(G,dir);
		return G;
	}
	public void addFiles(Graph G,File f){
		if (f.isDirectory()){
			for (File file:f.listFiles()){
				addFiles(G,file);
			}
		}else {
			if (f.getName().endsWith(".c")||f.getName().endsWith(".cc")||f.getName().endsWith(".h")||f.getName().endsWith(".cpp")||f.getName().endsWith(".hpp"))
			{
				G.addNode(f.getName());
			}

		}
	}

	public void handleDir(Graph G,File f){
		if (f.isDirectory()){
			for (File file:f.listFiles()){
				handleDir(G,file);
			}
		}else {
			if (f.getName().endsWith(".c")||f.getName().endsWith(".cc")||f.getName().endsWith(".h")||f.getName().endsWith(".cpp")||f.getName().endsWith(".hpp"))
			{
				List<String> dependedFiles = getCInclude(f);

				for (String s:dependedFiles){
					G.addEdge(f.getName(),s);
				}
			}

		}
	}
	public List<String> getCInclude(File file){
		List<String> Includes = new ArrayList<>();;

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuilder txt = new StringBuilder();
			String line = br.readLine();
			while (line!=null){
				txt.append(line+"\n");
				line = br.readLine();
			}
			String[] removed = txt.toString().replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)","").split("\n");


			for (int i=0;i<removed.length;i++){
				line = removed[i];
				line = line.replaceAll(" ", "");
				if (line.startsWith("#include")){
					line = line.replace("#include","");
					line = line.replace("","");
					line = line.replace("<","");
					line = line.replace(">","");
					line = line.replace("\"","");
					line = line.trim();
					Includes.add(line);
				}
			}
		}catch (IOException e){

		}
		return Includes;
	}
}
