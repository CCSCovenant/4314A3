package DependencyExtraction;

import java.util.HashMap;
import java.util.HashSet;

public class Graph {
	public HashMap<String,Node> Nodes;
	public Graph(){
		this.Nodes = new HashMap<>();
	}
	public void addEdge(String fileA,String fileB){
		if (Nodes.keySet().contains(fileA)&&Nodes.keySet().contains(fileB)){
			Node nodeA = Nodes.get(fileA);
			Node nodeB = Nodes.get(fileB);
			nodeA.addChild(nodeB);
		}

	}
	public void addNode(String fileA){
		if (!Nodes.containsKey(fileA)){
			Node N = new Node(fileA);
			Nodes.put(fileA,N);
		}
	}
}
