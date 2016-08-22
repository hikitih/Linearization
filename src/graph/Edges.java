package graph;

import java.util.HashMap;

class Edges{
	static HashMap<Integer,Edge> edges;

	Edges(){
		edges = new HashMap<Integer,Edge>();
	}

	Edges(int count){
		edges = new HashMap<Integer,Edge>(count*4/3);
	}

	public static void addEdge(int key, int out, int in){
		addEdge(key,out,in,1);
	}

	public static void addEdge(int key, int out, int in, int weight){
		Edge edge = new Edge(out,in,weight);
		edges.put(key,edge);
	}

	static void addWeight(int key, int weight){
		Edge edge = edges.get(key);
		edge.weight = edge.weight + weight;
		edges.put(key,edge);
	}

	public static Edge deleteEdge(int key){
		return edges.remove(key);
	}

	static Edge getEdge(int key){
		return edges.get(key);
	}
}
