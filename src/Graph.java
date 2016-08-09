package graph;

import java.util.ArrayList;

public class Graph {

	public class Vertex {
		int id;
		ArrayList<Integer> inEdges;
		ArrayList<Integer> inWeights;
		ArrayList<Integer> outEdges;
		ArrayList<Integer> outWeights;
		int indegree, outdegree;
		
		public Vertex(int id){
			this.id = id;
			inEdges = new ArrayList<Integer>(1);
			outEdges = new ArrayList<Integer>(1);
			inWeights = new ArrayList<Integer>(1);
			outWeights = new ArrayList<Integer>(1);
		}
		
		public String toString(){
			String s = " IN: ";
			for (int in: inEdges){
				s = s+in+", ";
			}
			s = s+"OUT: ";
			for (int out: outEdges){
				s = s+out+", ";
			}
			int inweight = 0;
			int outweight = 0;
			for (int weight: inWeights){
				inweight +=weight;
			}
			for (int weight: outWeights){
				outweight +=weight;
			}
			return "V "+id+s+" inWeight: "+inweight+" outweight "+outweight;
		}

		public void ClearEdges(){
			inEdges.clear();
			inWeights.clear();
			indegree = 0;
			outEdges.clear();
			outWeights.clear();
			outdegree = 0;
		}
	
		public void addInEdge(int in){
			addInEdge(in,1);
		}
		public void addOutEdge(int out){
			addOutEdge(out,1);
		}
		public void addInEdge(int in, int weight){
			inEdges.add(in);
			inWeights.add(weight);
			indegree += weight;
		}
		public void addOutEdge(int out, int weight){
			outEdges.add(out);
			outWeights.add(weight);
			outdegree += weight;
		}
		public boolean deleteInEdge(int in){
			int current = inEdges.indexOf(in);
			if (current>-1) {
				inEdges.remove(current);
				indegree -= inWeights.remove(current);
				return true;
			} else {
				return false;
			}
		}
		public boolean deleteOutEdge(int out){
			int current = outEdges.indexOf(out);
			if (current>-1) {
				outEdges.remove(current);
				outdegree -= outWeights.remove(current);
				return true;
			} else {
				return false;
			}
		}

		/*
		public void addEdges(ArrayList<Integer> ins, ArrayList<Integer> outs){
			addInEdges(ins);
			addOutEdges(outs);
		}
		
		public void addInEdges(ArrayList<Integer> ins){
			if (inEdges==null) {inEdges = new ArrayList<Integer>();} 
			for(int x: ins){
				inEdges.add(x);
			}
			indegree += ins.size();
		}
		public void addOutEdges(ArrayList<Integer> outs){
			if (outEdges==null) {outEdges = new ArrayList<Integer>();} 
			for(int x: outs){
				outEdges.add(x);
			}
			outdegree += outs.size();
		}
		*/		
	}



	Vertex[] vertices;		//All vertices of the graph
	int count;			//number of vertices 
	ArrayList<Integer> sources; 	//array of sources (assumed to be small)
	ArrayList<Integer> sinks; 	//array of sinks (assumed to be small)
	ArrayList<Integer> s1,s2;	//result sorting (s2=sinks, s1=rest)
	int left;			//number of unsorted vertices (to control finish)
	int numberOfReversingEdges = 0;	//
	int weightOfReversingEdges = 0; //
	boolean withoutSinks;		//if we use s2 and sinks or not
	int lefttoright = 0;		//number of rightward edges
	int righttoleft = 0;		//number of leftward edges
	boolean[] wasSorted;		//if vertex already in s1 or s2 (to control multi-adding)
	ArrayList<Integer> degree0,
			degree1,
			degree2,
			degree_1,
			degree_2;	//vertices of degree 0,1,2+,-2,2- respectively
	int testcounter = 0;		//counter for testing reasons


	public Graph(int number_of_vertices){
		this(number_of_vertices,false);
	}

	public Graph(int number_of_vertices, Edge[] edges){
		this(number_of_vertices,edges,false);
	}

	public Graph(int number_of_vertices, Edge[] edges, boolean withoutSinks){
		this(number_of_vertices,withoutSinks);
		this.addEdges(edges);
	}

	public Graph(int number_of_vertices, boolean withoutSinks){
		if (number_of_vertices>0){
			vertices = new Vertex[number_of_vertices];
			int nextmillion = 0;
			for(int i = 0; i<number_of_vertices;i++){
				vertices[i] = new Vertex(i);
				if ((i-nextmillion)>1000000){
					System.out.print(".");
					nextmillion = i;
				}
			}
			System.out.println();
			count = number_of_vertices;
			sources = new ArrayList<Integer>();
			sinks = new ArrayList<Integer>();
			s1 = new ArrayList<Integer>(count);
			s2 = new ArrayList<Integer>(count);
			left = count;
			this.withoutSinks = withoutSinks;
			wasSorted = new boolean[count];
			degree0 = new ArrayList<Integer>();
			degree1 = new ArrayList<Integer>();
			degree2 = new ArrayList<Integer>();
			degree_1= new ArrayList<Integer>();
			degree_2= new ArrayList<Integer>();
		}
	}

	public void compactify(){
		for (Vertex vertex: vertices){
			vertex.inEdges.trimToSize();
			vertex.outEdges.trimToSize();
		}
	}

	public int getCount(){
		return count;
	}

	public void info(){
		for(int i = 0; i<count;i++){
			System.out.println(vertices[i].toString());
		}				
	}

	public void degreeInfo(){
		String s = "Degree 2+:";
		for (int i: degree2) {
			s = s + i + ", ";
		}
		System.out.println(s);
		s = "Degree 1:";
		for (int i: degree1) {
			s = s + i + ", ";
		}
		System.out.println(s);
		s = "Degree 0:";
		for (int i: degree0) {
			s = s + i + ", ";
		}
		System.out.println(s);
		s = "Degree -1:";
		for (int i: degree_1) {
			s = s + i + ", ";
		}
		System.out.println(s);
		s = "Degree -2-:";
		for (int i: degree_2) {
			s = s + i + ", ";
		}
		System.out.println(s);
	}

	public int cutwidth(ArrayList<Integer> permutation){
		if (permutation.size()!=count) {return -1;}
		else {
			ArrayList<Integer> ends = new ArrayList<>();
			ArrayList<Integer> starts = new ArrayList<>();
			int result = 0;
			
			for (int id: permutation){
				while (ends.indexOf(id)>-1){
					ends.remove(ends.indexOf(id));
				}
				for (int out: vertices[id].outEdges) {
					if (starts.indexOf(out)>-1) {
						if (id!=out) starts.remove(starts.indexOf(out));
					} else if (id==out){
					} else {
						ends.add(out);
						lefttoright++;
						starts.add(id);
					}
				}
				for (int in: vertices[id].inEdges) {
					if (starts.indexOf(in)>-1) {
						if (id!=in) starts.remove(starts.indexOf(in));
					} else if (id==in){
					} else {
						ends.add(in);
						righttoleft++;
						starts.add(id);
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

	public boolean addEdge(int out, int in){
		return addEdge(out,in,1);
	}

	public boolean addEdge(int out, int in, int weight){
		if ((out>-1)&&(in>-1)&&(out<count)&&(in<count)){
			vertices[out].addOutEdge(in,weight);
			vertices[in].addInEdge(out,weight);
			return true;
		} else return false;
	}

	public void addEdges(Edge[] edges){
		for (Edge edge: edges){
			addEdge(edge.out, edge.in);
		}
	}

	public void fillSourcesAndSinks(boolean newStyle){
		if (left>0){
			for (Vertex vertex: vertices){
				if (vertex.indegree==0) {sources.add(vertex.id);}
				else if ((withoutSinks==false)&&(vertex.outdegree==0)) {
						sinks.add(vertex.id);
				}
				if (newStyle){
					degree0.ensureCapacity(count);
					degree1.ensureCapacity(count);
					degree2.ensureCapacity(count);
					degree_1.ensureCapacity(count);
					degree_2.ensureCapacity(count);

					int diff = vertex.outdegree - vertex.indegree;
					if (diff==0) {degree0.add(vertex.id);}
					else if (diff==1) {degree1.add(vertex.id);}
					else if (diff==-1) {degree_1.add(vertex.id);}
					else if (diff>1) {degree2.add(vertex.id);}
					else {degree_2.add(vertex.id);}
				}
			}
		}
		//degreeInfo();
	}

	public void recountDegreeDifference(int id,int change){
		testcounter++;
		int diff = vertices[id].outdegree - vertices[id].indegree;
		int num = -1;

		//System.out.println("***changing "+id+"(diff="+diff+") by "+change);

		//delete
		if (diff==0) {
			num = degree0.indexOf(id);
			if (num>-1) {degree0.remove(num);}
		} else if (diff==1) {
			num = degree1.indexOf(id);
			if (num>-1) {degree1.remove(num);}
		} else if (diff==-1) {
			num = degree_1.indexOf(id);
			if (num>-1) {degree_1.remove(num);}
		} else if (diff>1) {
			num = degree2.indexOf(id);
			if (num>-1) {degree2.remove(num);}
		} else {
			num = degree_2.indexOf(id);
			if (num>-1) {degree_2.remove(num);}
		}
		//add
		diff = diff + change;
		if (diff==0) {degree0.add(id);}
		else if (diff==1) {degree1.add(id);}
		else if (diff==-1) {degree_1.add(id);}
		else if (diff>1) {degree2.add(id);}
		else {degree_2.add(id);}
		//degreeInfo();
	}

	public int findMaxVertex(){
		int max = -1;
		int maxid = -1;
		int maxidindegree = -1;
		for (Vertex vertex: vertices){
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

	public int findMaxVertexNew(){
		if (degree2.isEmpty()) {
			if (degree1.isEmpty()){
				if (degree0.isEmpty()){
					return -1;
				} else {
					return degree0.get(0);
				}
			} else {
				int maxid = -1;
				int maxidindegree = -1;
				for (int id: degree1) {
					if (vertices[id].indegree > maxidindegree) {
						maxid = vertices[id].id;
						maxidindegree = vertices[id].indegree;
					}
				}
				return maxid;
			}
		} else {
			int max = -1;
			int maxid = -1;
			int maxidindegree = -1;
			for (int id: degree2) {
				if (max < (vertices[id].outdegree - vertices[id].indegree)) {
					max = (vertices[id].outdegree - vertices[id].indegree);
					maxid = vertices[id].id;
					maxidindegree = vertices[id].indegree;
				} else if ((max == (vertices[id].outdegree - vertices[id].indegree))&&
						(vertices[id].indegree > maxidindegree)) {
					maxid = vertices[id].id;
					maxidindegree = vertices[id].indegree;
				}//May add connection property (was connected with deleted)
			}
			return maxid;
		}
	}

	public int deleteVertex(int id, boolean newStyle){
		if ((id<0)||(id>count)) {return -1;}
		else {
			
			int current = sources.indexOf(vertices[id].id);
			if (current>-1) {sources.remove(current);}
			current = sinks.indexOf(vertices[id].id);
			if (current>-1) {sinks.remove(current);} 
			

			int lastsource = -1;
			int lastsink = -1;
			//int current;

			for (int out: vertices[id].inEdges){
				if ( vertices[out].deleteOutEdge(id) 
					&& withoutSinks==false 
					&& vertices[out].outdegree==0 
					&& sinks.indexOf(out)<0 ) {
						sinks.add(out);
						lastsink = out;
				}
			}//Here need recount diff if it stores in array for fast search
	
			for (int in: vertices[id].outEdges){
				if ( vertices[in].deleteInEdge(id) 
					&& vertices[in].indegree==0 
					&& sinks.indexOf(in)<0 ) {
						sources.add(in);
						lastsource = in;
				}
			}//Here need recount diff if it stores in array for fast search
			
			vertices[id].ClearEdges();
			
			if (lastsource>-1) {return lastsource;}
			else if (lastsink>-1) {return -10-lastsink;}
			else {return -1;}
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

	public void sorting(){
		sorting(false);
	}

	//Main procedure (newStyle was designed to be faster but failed)
	public void sorting(boolean newStyle){
		int next = -1;
		boolean issink = false;

		fillSourcesAndSinks(newStyle);

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
					if (newStyle) {next = findMaxVertexNew();}
					else {next = findMaxVertex();}
					numberOfReversingEdges += vertices[next].inEdges.size();
					weightOfReversingEdges += vertices[next].indegree;
					issink = false;
				}
			}//Get next vertex to delete

			if (issink) {
				if (!wasSorted[next]){
					s2.add(next);
					wasSorted[next] = true;
					left -= 1;			
				}
			} else {
				if (!wasSorted[next]){
					s1.add(next);
					wasSorted[next] = true;
					left -= 1;			
				}
			}
			/*if (issink) {
				if ((s2.indexOf(next)<0)&&(s1.indexOf(next)<0)){
					s2.add(next);
					left -= 1;			
				}
			} else {
				if ((s2.indexOf(next)<0)&&(s1.indexOf(next)<0)){
					s1.add(next);
					left -= 1;			
				}
			}*/

			//System.out.println(vertices[next] + " ||| ");
			next = deleteVertex(next, newStyle);
			if (next>-1) {issink = false;}
			else if (next<-1) {
				issink = true;
				next = -10-next;
			}
			//Need check if next is sink (to correct add at next stage)
		}
		System.out.println();
		//System.out.println("N of recounts :"+testcounter);
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

}
