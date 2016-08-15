package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Graph {
	private HashMap<Integer,Node> vertices;	//All vertices of the graph
	//private Edges edges;					//All edges of the graph
	private int nextEdgeKey = 0;			//
	int count;								//number of vertices
	private ArrayList<Integer> sources; 	//array of sources (assumed to be small)
	private ArrayList<Integer> sinks; 		//array of sinks (assumed to be small)
	private ArrayList<Integer> s1,s2;		//result sorting (s2=sinks, s1=rest)
	private int left;						//number of unsorted vertices (to control finish)
	private int numberOfReversingEdges = 0;	//
	private int weightOfReversingEdges = 0; //
	private int lefttoright = 0;			//number of rightward edges
	private int righttoleft = 0;			//number of leftward edges

	public Graph(){ this(1,0,false); }

	public Graph(int numberOfVertices){
		this(numberOfVertices,0,false);
	}

	public Graph(int numberOfVertices, int numberOfEdges){ this(numberOfVertices,numberOfEdges,false); }

	public Graph(int numberOfVertices, int numberOfEdges, boolean verticesConsecutive){
		if (numberOfVertices>0){
			vertices = new HashMap<Integer,Node>(numberOfVertices);
			if (verticesConsecutive) {
				for (int i=0;i<numberOfVertices;i++) {
					Node node = new Node(i);
					vertices.put(i, node);
				}
			}

			if (numberOfEdges>0){
				new Edges(numberOfEdges);
			} else {
				new Edges();
			}
			
			count = numberOfVertices;
			sources = new ArrayList<Integer>();
			sinks = new ArrayList<Integer>();
			s1 = new ArrayList<Integer>();
			s1.ensureCapacity(count);
			s2 = new ArrayList<Integer>();
			s2.ensureCapacity(count/3);
			left = count;
		}
	}

	public boolean addEdge(int out, int in){
		return addEdge(out,in,1,false);
	}

	public boolean addEdge(int out, int in, boolean verticesExists){
		return addEdge(out,in,1,verticesExists);
	}

	public boolean addEdge(int out, int in, int weight) { return  addEdge(out,in,weight,false);}

	public boolean addEdge(int out, int in, int weight, boolean verticesExists){
		if (in!=out){
			if (weight < 0) {weight = 1;}
			Edges.addEdge(nextEdgeKey,out,in,weight);
			if (!verticesExists) {
				if (!vertices.containsKey(out)) {
					Node node = new Node(out);
					vertices.put(out, node);
				}
				if (!vertices.containsKey(in)) {
					Node node = new Node(in);
					vertices.put(in, node);
				}
			}
			vertices.get(out).addEdge(nextEdgeKey,-weight);
			vertices.get(in).addEdge(nextEdgeKey,weight);
			nextEdgeKey++;
			return true;
		} else return false;
	}

	//slower then addEdge but no duplicate edges
	public boolean addWeightToEdge(int out, int in, int weight){
		Node vertex = vertices.get(out);
		if (vertex != null) {
			for (int key : vertex.edgeKeys) {
				Edge edge = Edges.getEdge(key);
				if (edge.getOtherEnd(out) == in) {
					Edges.changeWeight(key, weight);
					vertex.changeWeight(-weight);
					vertices.get(in).changeWeight(weight);
					return true;
				}
			}
		}
		//System.out.println("No edge "+out+","+in);
		//addEdge(out,in,weight);
		return false;
	}

	public void reloadVertices(){
		for (Integer key: Edges.edges.keySet()){
			Edge edge = Edges.edges.get(key);
			vertices.get(edge.out).addEdge(key,-edge.weight);
			vertices.get(edge.in).addEdge(key,edge.weight);
		}
	}

	public void recount(){
		count = vertices.size();
		left = count;
		s1.ensureCapacity(count);
		s2.ensureCapacity(count/3);
	}

	public int getCount(){ return count; }

	public ArrayList<Integer> getSorting(){
		ArrayList<Integer> result = new ArrayList<>();
		result.ensureCapacity(count);
		result.addAll(s1);
		Collections.reverse(s2);
		result.addAll(s2);
		Collections.reverse(s2);
		return result;
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
						} else if (id!=end){
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

	private void fillSourcesAndSinks(){
		fillSourcesAndSinks(false);
	}

	private void fillSourcesAndSinks(boolean withoutSinks){
		if (left>0){
			for (Node vertex: vertices.values()){
				if (vertex.indegree==0) {sources.add(vertex.id);}
				else if ((!withoutSinks)&&(vertex.outdegree==0)) {
						sinks.add(vertex.id);
				}
			}
		}
	}

	private int findMaxVertex(){
		int max = -1;
		int maxid = -1;
		int maxiddegree = -1;
		for (Node vertex: vertices.values()){
			if (max < (vertex.outdegree - vertex.indegree)) {
				max = (vertex.outdegree - vertex.indegree);
				maxid = vertex.id;
				maxiddegree = vertex.indegree;
			} else if ((max == (vertex.outdegree - vertex.indegree)) && (vertex.indegree > maxiddegree)) {
				maxid = vertex.id;
				maxiddegree = vertex.indegree;
			}//May add connection property (was connected with deleted)
		}
		return maxid;
	}

	private int deleteVertex(int id){
		return deleteVertex(id,true,false,true,true);
	}

	private int deleteVertex(int id, boolean sourceToSink, boolean withoutSinks, boolean takeNeighbor, boolean takeAny){
		if (id<0) {return -1;}
		else {
			/*Integer idToRemove = id;
			sources.removeAll(Collections.singleton(idToRemove));
			sinks.removeAll(Collections.singleton(idToRemove));			
			*/
			//System.out.println("ID: "+id+" Sources: "+sources);

			int lastsource = -1;
			int lastsink = -1;

			int lastsourcedegree = Integer.MAX_VALUE;
			int lastsinkdegree = Integer.MAX_VALUE;

			int end;
			Node vertex;

			for (int key: vertices.get(id).edgeKeys){
				Edge edge = Edges.getEdge(key);
				end = edge.getOtherEnd(id);
				vertex = vertices.get(end);
				vertex.deleteEdge(key);
				//If vertex has no edges left immediately put it into s1
				if (vertex.outdegree == 0 && vertex.indegree==0){
					if ( !vertex.isSorted() ){
						vertex.setSorted();
						left -= 1;
						if (sourceToSink){
							s1.add(vertex.id);
						} else {
							s2.add(vertex.id);
						}
					}
				} else {
					//if vertex is sink
					if (!withoutSinks && vertex.outdegree == 0) {
						sinks.add(end);
						if (takeAny) {
							lastsink = end;
						} else {
							if (lastsinkdegree > vertex.indegree) {
								lastsinkdegree = vertex.indegree;
								lastsink = end;
							}
						}
					}
					//if vertex is source
					if (vertex.indegree == 0) {
						sources.add(end);
						if (takeAny){
							lastsource = end;
						} else {
							if (lastsourcedegree > vertex.outdegree) {
								lastsourcedegree = vertex.outdegree;
								lastsource = end;
							}
						}
					}
				}
			}

			vertices.get(id).ClearEdges();

			/*
				If takeNeighbor then we return

				for sourceToSink:
				lastsource, then lastsink, then -1
				or
				for NOT sourceToSink:
				lastsink , then lastsource, then -1

				else we return
				-1
			*/
			if (takeNeighbor) {
				if (sourceToSink) {
					if (lastsource > -1) {
						Integer idToRemove = lastsource;
						sources.remove(idToRemove);
						return lastsource;
					} else if (lastsink > -1) {
						Integer idToRemove = lastsink;
						sinks.remove(idToRemove);
						if (sources.size()==0) {
							return -10 - lastsink;
						}
					} else {
						return -1;
					}
				} else {
					if (lastsink > -1) {
						Integer idToRemove = lastsink;
						sinks.remove(idToRemove);
						return -10 - lastsink;
					} else if (lastsource > -1) {
						Integer idToRemove = lastsource;
						sources.remove(idToRemove);
						if (sinks.size()==0) {
							return lastsource;
						}
					} else {
						return -1;
					}
				}
			}
			return -1;
		}
	}

	private int getMinSource(){
		int minid = -1;
		int mindegree = Integer.MAX_VALUE;
		int counter = 0;

		for (int key: sources) {
			Node vertex = vertices.get(key);

			if (mindegree > vertex.outdegree){
				mindegree = vertex.outdegree;
				minid = counter;
			}
			counter++;
		}
		return minid;
	}

	private int getMaxSink(){
		int maxid = -1;
		int maxdegree = -1;
		int counter = 0;

		for (int key: sinks) {
			Node vertex = vertices.get(key);
			if (maxdegree < vertex.indegree){
				maxdegree = vertex.indegree;
				maxid = counter;
			}
			counter++;
		}
		return maxid;
	}

	//Main procedure
	public void sorting(){
		sorting(true,false,true,true);
	}


	/*	sourceToSink = get source first if true, get sink first if false
		withoutSinks = true means we use only sources and s1 (sourceToSink == false means withoutSinks = false)
		takeNeighbor = choose next vertex from neighbors of previous if possible
		takeAny = take any neighbor/any source/sink when does have choice

		default settings are true,false,true,true
			we go from sources to sinks, using sinks when no sources available
			and choose next vertex randomly among neighbors of previous source or sink respectively

		improved Kahn algorithm settings are true, false, false, true
			these should also give best performance in time sense

		best natural result settings are true, false, true, false
			we go from sources to sinks, using sinks when no sources available
			and choose next vertex with lowest outcoming from source/highest incoming from sink degree

		closest to flow algorithm settings are false, false, true, false (or false, false, false, false)


		All these settings have withoutSinks == false because there is no sense not using sinks
		 	but you could stop using them with sourceToSink == true any time you want
	*/
	public void sorting(boolean sourceToSink, boolean withoutSinks, boolean takeNeighbor, boolean takeAny){
		System.out.println("Go from sources to sinks: "+sourceToSink+"\nDo not use sinks: "+withoutSinks
							+"\nTake neighbor if possible: "+takeNeighbor+"\nTake random when have choice: "+takeAny);

		int next = -1;
		boolean issink = false;
		if (!sourceToSink) { withoutSinks = false; }

		fillSourcesAndSinks(withoutSinks);

		int nextmillion = count;
		while (left>0){
			if ((nextmillion-left)>1000000){
				System.out.print(".");
				nextmillion = left;
			}

			if (next==-1) {
				if ( sources.size()>0 || sinks.size()>0 ){
					if ((sourceToSink && sources.size()>0)||(!sourceToSink && sinks.size()==0)) {
						if (takeAny) {
							next = sources.get(sources.size()-1);
							sources.remove(sources.size()-1);
						} else {
							int nextIndex = getMinSource();
							next = sources.get(nextIndex);
							sources.remove(nextIndex);
						}
						issink = false;
					} else {
						if (takeAny) {
							next = sinks.get(0);
							sinks.remove(0);
						} else {
							int nextIndex = getMaxSink();
							next = sinks.get(nextIndex);
							sinks.remove(nextIndex);
						}
						issink = true;
					}

					/*
					if (sources.size()>0) {
						next = sources.get(sources.size()-1);
						sources.remove(sources.size()-1);
						//next = sources.get(0);
						//sources.remove(0);
						issink = false;
					} else if (sinks.size()>0) {
						next = sinks.get(0);
						sinks.remove(0);
						issink = true;
					}
					*/
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
			//System.out.print("Before. ");
			//shortInfo(next);

			next = deleteVertex(next,sourceToSink,withoutSinks,takeNeighbor,takeAny);

			//System.out.print("After. ");
			//shortInfo(next);

			if (next>-1) {issink = false;}
			else if (next<-1) {
				issink = true;
				next = -10-next;
			}
			//Need check if next is sink (to correct add at next stage)
		}
		//System.out.println();
	}

	public void viewSorting(){
		viewSorting(false);
	}

	public void viewSorting(boolean printOnlyParameters){
		String s =  "";
		String ss =  "";
		for (int x: s1) {s=s+","+x;}
		for (int x: s2) {ss=","+x+ss;}
		if (!printOnlyParameters) {
			System.out.println(s + "---------" + ss);
		}
		System.out.println("Number of reversing edges: "+numberOfReversingEdges);
		System.out.println("Weight of reversing edges: "+weightOfReversingEdges);
	}

	public int getNumberOfReversingEdges(){
		return numberOfReversingEdges;
	}

	public int getWeightOfReversingEdges() { return weightOfReversingEdges; }

	public void info(){
		for(Node vertex: vertices.values()){
			System.out.println(vertex.toString());
		}				
	}

	public void vertexInfo(int id){
		System.out.println(vertices.get(id).toString());
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
