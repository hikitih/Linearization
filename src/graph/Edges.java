package graph;

import java.util.HashMap;

/**
 * Contains static HashMap of edges and static methods to add, get and delete edges.
 */
class Edges{
	static HashMap<Integer,Edge> edges;
	private static int nextEdgeKey = 1;

	Edges(){
        if (edges!=null) {
            edges.clear();
        } else {
			edges = new HashMap<Integer, Edge>();
		}
		nextEdgeKey = 1;
	}

	Edges(int count){
		edges = new HashMap<Integer,Edge>(count*4/3);
		nextEdgeKey = 1;
	}

	public static int addEdge(int out, int in){
		return addEdge(out,in,1);
	}

	public static int addEdge(int out, int in, int weight){
		Edge edge = new Edge(out,in,weight);
		edges.put(nextEdgeKey,edge);
		//nextEdgeKey++;
		return nextEdgeKey++;
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
