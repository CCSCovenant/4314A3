import DependencyExtraction.Extractor;
import DependencyExtraction.Graph;
import DependencyExtraction.Graph2TAConverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

public class main {
	public static void main(String[] args){
		Extractor ext = Extractor.getInstance();
		Graph g = ext.Code2graph("./gcc-12.2.0/");
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("include.raw.ta"));
			out.write(Graph2TAConverter.getInstance().Graph2TA(g));
			out.close();
		} catch (IOException e) {
		}

	}
}
