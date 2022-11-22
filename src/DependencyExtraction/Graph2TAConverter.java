package DependencyExtraction;

public class Graph2TAConverter {
	static Graph2TAConverter graph2TAConverter = new Graph2TAConverter();
	private Graph2TAConverter(){

	}

	public static Graph2TAConverter getInstance() {
		return graph2TAConverter;
	}
	public String Graph2TA(Graph G){
		StringBuilder s = new StringBuilder();
		s.append("FACT TUPLE\n");
		for (String file:G.Nodes.keySet()){
			s.append("$INSTANCE "+file+" cFile\n");
		}
		for (String file:G.Nodes.keySet()){
			Node N = G.Nodes.get(file);
			for (Node C:N.children){
				s.append("cLinks "+N.fileName+" "+C.fileName+"\n");
			}
		}
		return s.toString();
	}
}
