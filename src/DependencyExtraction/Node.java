package DependencyExtraction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Node {
	String fileName;
	HashSet<Node> children;
	public Node(String fileName){
		this.fileName = fileName;
		this.children = new HashSet<>();
	}

	public HashSet<Node> getChildren() {
		return children;
	}
	public String getFileName() {
		return fileName;
	}
	public boolean addChild(Node node){
		return children.add(node);
	}
	public boolean removeChild(Node node){
		return children.remove(node);
	}
}
