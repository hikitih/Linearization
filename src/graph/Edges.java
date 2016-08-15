package graph;

import java.util.HashMap;

public class Edges{
	static HashMap<Integer,Edge> edges;

	public Edges(){
		edges = new HashMap<Integer,Edge>();
	}

	public Edges(int count){
		edges = new HashMap<Integer,Edge>(count);
	}

	public void addEdge(int key, int out, int in){
		addEdge(key,out,in,1);
	}

	public void addEdge(int key, int out, int in, int weight){
		Edge edge = new Edge(out,in,weight);
		edges.put(key,edge);
	}

	public void changeWeight(int key, int weight){
		Edge edge = edges.get(key);
		edge.weight = edge.weight + weight;
		edges.put(key,edge);
	}

	public static Edge deleteEdge(int key){
		return edges.remove(key);
	}

	public static Edge getEdge(int key){
		return edges.get(key);
	}
}
