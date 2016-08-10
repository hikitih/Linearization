package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Graph {
	HashMap<Integer,Node> vertices;	//All vertices of the graph	
	Edges edges;			//All edges of the graph
	int nextEdgeKey = 0;		//
	int count;			//number of vertices 
	ArrayList<Integer> sources; 	//array of sources (assumed to be small)
	ArrayList<Integer> sinks; 	//array of sinks (assumed to be small)
	ArrayList<Integer> s1,s2;	//result sorting (s2=sinks, s1=rest)
	int left;			//number of unsorted vertices (to control finish)
	int numberOfReversingEdges = 0;	//
	int weightOfReversingEdges = 0; //
	int lefttoright = 0;		//number of rightward edges
	int righttoleft = 0;		//number of leftward edges
	boolean[] wasSorted;		//if vertex already in s1 or s2 (to control multi-adding)


	public Graph(int numberOfVertices){
		this(numberOfVertices,0);
	}

	public Graph(int numberOfVertices, int numberOfEdges){
		if (numberOfVertices>0){
			vertices = new HashMap<Integer,Node>(numberOfVertices);
			if (numberOfEdges>0){
				edges = new Edges(numberOfEdges);
			} else {
				edges = new Edges();
			}
			
			count = numberOfVertices;
			sources = new ArrayList<Integer>();
			sinks = new ArrayList<Integer>();
			s1 = new ArrayList<Integer>();
			s1.ensureCapacity(count);
			s2 = new ArrayList<Integer>();
			s2.ensureCapacity(count/3);
			left = count;
			wasSorted = new boolean[count];
		}
	}

	public boolean addEdge(int out, int in){
		return addEdge(out,in,1);
	}

	public boolean addEdge(int out, int in, int weight){
		if (in!=out){
			if (weight < 0) {weight = 1;}
			edges.addEdge(nextEdgeKey,out,in,weight);
			if (!vertices.containsKey(out)){
				Node node = new Node(out);
				vertices.put(out,node);
			}
			if (!vertices.containsKey(in)){
				Node node = new Node(in);
				vertices.put(in,node);
			}
			vertices.get(out).addEdge(nextEdgeKey,-weight);
			vertices.get(in).addEdge(nextEdgeKey,weight);
			nextEdgeKey++;
			return true;
		} else return false;
	}

	public int getCount(){
		return count;
	}

	public ArrayList<Integer> getPermutation(){
		ArrayList<Integer> result = new ArrayList<Integer>();
		result.ensureCapacity(count);
		for(Node vertex: vertices.values()){
			result.add(vertex.id);
		}
		return result;
	}

	public int cutwidth(ArrayList<Integer> permutation){
		if (permutation.size()!=count) {return -1;}
		else {
			ArrayList<Integer> ends = new ArrayList<>();
			ArrayList<Integer> starts = new ArrayList<>();
			int result = 0;
			int index;
			
			int end;
			Edge edge;
			
			for (Integer id: permutation){
				Node vertex = vertices.get(id);
				if (vertex==null){
					return -1;
				} else {
					ends.removeAll(Collections.singleton(id));
					for (int key: vertex.edgeKeys){
						edge = Edges.getEdge(key);
						end = edge.getOtherEnd(id);
	
						index = starts.indexOf(end);
						if (index>-1) {
							if (id!=end) starts.remove(index);
						} else if (id==end){
						} else {
							ends.add(end);
							starts.add(id);
							if (edge.isIn(id)) {
								righttoleft++;
							} else {
								lefttoright++;
							}
						}
					}
				}
				result += ends.size();
			}
			return result;
		}
	}

	public int rightToLeft(ArrayList<Integer> permutation){
		lefttoright = 0;
		righttoleft = 0;
		cutwidth(permutation);
		return righttoleft;
	}

	public int leftToRight(ArrayList<Integer> permutation){
		lefttoright = 0;
		righttoleft = 0;
		cutwidth(permutation);
		return lefttoright;
	}

	public void fillSourcesAndSinks(){
		fillSourcesAndSinks(false);
	}

	public void fillSourcesAndSinks(boolean withoutSinks){
		if (left>0){
			for (Node vertex: vertices.values()){
				if (vertex.indegree==0) {sources.add(vertex.id);}
				else if ((withoutSinks==false)&&(vertex.outdegree==0)) {
						sinks.add(vertex.id);
				}
			}
		}
	}

	public int findMaxVertex(){
		int max = -1;
		int maxid = -1;
		int maxidindegree = -1;
		for (Node vertex: vertices.values()){
			if (max < (vertex.outdegree - vertex.indegree)) {
				max = (vertex.outdegree - vertex.indegree);
				maxid = vertex.id;
				maxidindegree = vertex.indegree;
			} else if ((max == (vertex.outdegree - vertex.indegree))&&
					(vertex.indegree > maxidindegree)) {
				maxid = vertex.id;
				maxidindegree = vertex.indegree;
			}//May add connection property (was connected with deleted)
		}
		return maxid;
	}

	public int deleteVertex(int id){
		return deleteVertex(id,false);
	}

	public int deleteVertex(int id, boolean withoutSinks){
		if (id<0) {return -1;}
		else {
			/*Integer idToRemove = id;
			sources.removeAll(Collections.singleton(idToRemove));
			sinks.removeAll(Collections.singleton(idToRemove));			
			*/
			//System.out.println("ID: "+id+" Sources: "+sources);

			int lastsource = -1;
			int lastsink = -1;

			int end;
			Node vertex;

			for (int key: vertices.get(id).edgeKeys){
				Edge edge = Edges.getEdge(key);
				end = edge.getOtherEnd(id);
				vertex = vertices.get(end);
				vertex.deleteEdge(key);
				if (withoutSinks==false && vertex.outdegree == 0){
					sinks.add(end);
					lastsink = end;
				}
				if (vertex.indegree==0){
					sources.add(end);
					lastsource = end;
				}
			}

			vertices.get(id).ClearEdges();
			
			if (lastsource>-1) {
				Integer idToRemove = lastsource;
				sources.remove(idToRemove);
				return lastsource;
			} else if (lastsink>-1) {
				Integer idToRemove = lastsink;
				sinks.remove(idToRemove);
				return -10-lastsink;
			} else {
				return -1;
			}
		}
	}

	//Main procedure
	public void sorting(){
		sorting(false);
	}

	public void sorting(boolean withoutSinks){
		int next = -1;
		boolean issink = false;

		fillSourcesAndSinks(withoutSinks);

		int nextmillion = count;
		while (left>0){
			if ((nextmillion-left)>1000000){
				System.out.print(".");
				nextmillion = left;
			}

			if (next==-1) {
				if (sources.size()>0) {
					next = sources.get(sources.size()-1);
					sources.remove(sources.size()-1);
					issink = false;
				} else if (sinks.size()>0) {
					next = sinks.get(0);
					sinks.remove(0);
					issink = true;
				} else {
					next = findMaxVertex();
					Node vertex = vertices.get(next);
					if (vertex!=null){
						numberOfReversingEdges += vertex.getInEdgesCount();
						weightOfReversingEdges += vertex.indegree;
					} else {
						return;
					}
					issink = false;
				}
			}//Get next vertex to delete

			Node nextVertex = vertices.get(next);
			if ( nextVertex!=null && !nextVertex.isSorted() ){
				nextVertex.setSorted();
				left -= 1;			
				if (issink) {
					s2.add(next);
				} else {
					s1.add(next);
				}
			}

			//System.out.println(vertices.get(next));

			next = deleteVertex(next,withoutSinks);


			if (next>-1) {issink = false;}
			else if (next<-1) {
				issink = true;
				next = -10-next;
			}
			//Need check if next is sink (to correct add at next stage)
		}
		System.out.println();
	}

	public void outputSorting(){
		String s =  "";
		String ss =  "";
		for (int x: s1) {s=s+","+x;}
		for (int x: s2) {ss=","+x+ss;} 
		System.out.println(s+"---------"+ss);
		System.out.println("Number of reversing edges: "+numberOfReversingEdges);
		System.out.println("Weight of reversing edges: "+weightOfReversingEdges);
	}

	public int getNumberOfReversingEdges(){
		return numberOfReversingEdges;
	}

	public void info(){
		for(Node vertex: vertices.values()){
			System.out.println(vertex.toString());
		}				
	}

	public void shortInfo(int next){
		String s = "NEXT: ";
		s = s+next+" = = = SOURCE: ";
		for (int x: sources) s = s + x + ", ";
		s = s+"SINK: ";
		for (int x: sinks) s = s + x + ", ";
		System.out.println(s);
	}
}
