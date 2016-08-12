package graph;

import java.util.ArrayList;

public class Node{
	int id;
	//String name;
	ArrayList<Integer> edgeKeys;	//keys of all adjanced edges
	int indegree;			//total weight of all in-edges
	int outdegree;			//total weight of all out-edges
	private boolean wasSorted = false;	//If it was sorted by graph

	public Node(){
		edgeKeys = new ArrayList<Integer> (2);
	}

	public Node(int id) {
		this(id,null);
	}

	public Node(int id, String name){
		//this.name = name;
		this.id = id;
		edgeKeys = new ArrayList<Integer> (2);
	}

	public void setId(int id){
		this.id = id;
	}

	public boolean isSorted(){
		return wasSorted;
	}

	public void setSorted(){
		wasSorted = true;
	}

	public void setSorted(boolean wasSorted){
		this.wasSorted = wasSorted;
	}

	public String toString(){
		Edge edge;
		String stringInEdges = " IN: ";
		String stringOutEdges = " OUT: ";
		for (int key: edgeKeys){
			edge = Edges.getEdge(key);
			if (edge!=null){
				if (edge.in == id) {
					stringInEdges = stringInEdges + edge.out +
						 	"(" + edge.weight + "), ";
				} else {
					stringOutEdges = stringOutEdges + edge.in +
						 	"(" + edge.weight + "), ";
				}
			}
		}
		return "V "+id+stringInEdges+stringOutEdges
			+" inWeight: "+indegree+" outweight: "+outdegree;
	}

	public void ClearEdges(){
		edgeKeys.clear();
		indegree = 0;
		outdegree = 0;
	}

	public int getInEdgesCount(){
		int result = 0;
		for (int key: edgeKeys){
			if (Edges.getEdge(key).isIn(id)) {
				result++;
			}
		}
		return result;
	}

	public void addEdge(int key){
		Edge edge = Edges.getEdge(key);
		if (edge!=null){
			edgeKeys.add(key);
			if (edge.isIn(id)) {
				indegree += edge.weight;
			} else {
				outdegree += edge.weight;
			}
		}
	}

	public void addEdge(int key, int weight){
		edgeKeys.add(key);
		if (weight>0) {
			indegree += weight;
		} else {
			outdegree -= weight;	//add |weight|
		}
	}

	public void deleteEdge(int key){
		Integer keyToRemove = key;
		edgeKeys.remove(keyToRemove);
		Edge edge = Edges.getEdge(key);
		//Need to think about other end
		if (edge!=null){
			if (edge.in == id) {
				indegree -= edge.weight;
			} else {
				outdegree -= edge.weight;
			}
		}//Need to think about loops (edge between id and id)
	}
}