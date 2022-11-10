package DependencyExtraction;

public class Extractor {
	static Extractor extractor = new Extractor();
	public Extractor(){

	}

	public static Extractor getInstance() {
		return extractor;
	}

	public Graph Code2graph(String fileDir){
		Graph G = new Graph();
		//TODO
		return G;
	}
	public String Graph2TA(Graph G){
		StringBuilder s = new StringBuilder();
		//TODO
		return s.toString();
	}
}
