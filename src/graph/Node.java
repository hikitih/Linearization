package graph;

import java.util.ArrayList;

class Node{
	int id;
	ArrayList<Integer> edgeKeys;	//keys of all adjanced edges
	int indegree;			//total weight of all in-edges
	int outdegree;			//total weight of all out-edges
	private boolean wasSorted = false;	//If it was sorted by graph

	Node(){
		edgeKeys = new ArrayList<Integer> (2);
	}

	Node(int id){
		this.id = id;
		edgeKeys = new ArrayList<Integer> (2);
	}

	public void setId(int id){
		this.id = id;
	}

	boolean isSorted(){
		return wasSorted;
	}

	void setSorted(){
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
			+" inWeight: "+indegree+" outWeight: "+outdegree;
	}

	void ClearEdges(){
		edgeKeys.clear();
		indegree = 0;
		outdegree = 0;
	}

	int getInEdgesCount(){
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

	//make weight negative when adding out-edge
	public void addEdge(int key, int weight){
		edgeKeys.add(key);
		if (weight>0) {
			indegree += weight;
		} else {
			outdegree -= weight;	//add |weight|
		}
	}

	void changeWeight(int weight){
		if (weight>0) {
			indegree += weight;
		} else {
			outdegree -= weight;	//add |weight|
		}
	}

	void deleteEdge(int key){
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