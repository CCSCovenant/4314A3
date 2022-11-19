import DependencyExtraction.Extractor;
import DependencyExtraction.Graph;

import java.io.File;
import java.util.regex.Pattern;

public class main {
	public static void main(String[] args){
		Extractor ext = Extractor.getInstance();
		Graph g = ext.Code2graph("./gcc-12.2.0/gcc/c");
		System.out.println(g.Nodes);

	}
}
