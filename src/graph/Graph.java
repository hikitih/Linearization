package graph;

import java.util.*;

/**
 * Graph designed to work with sorting. <br>
 *
 * Edges are static HashMap of (id, edge) <br>
 * vertices are HashMap of (id, Node) <br> <br>
 *
 * Main methods are: <br>
 * sorting = sort graph and write result to s1+s2 ArrayLists of Integer <br>
 * info = show full info about vertices and edges <br>
 * cutWidthNew = count cut width of permutation <br>
 *
 */
public class Graph {
	HashMap<Integer, Node> vertices;    //All vertices of the graph
    private ArrayList<HashSet<Integer>> verticesByDegree;   //put all vertices with same degree difference
                                                            // in one HashSet with corresponding index in ArrayList
	int count;                              //number of vertices
    //private int nextEdgeKey = 0;
	private ArrayList<Integer> sources;     //array of sources (assumed to be small)
	private ArrayList<Integer> sinks;       //array of sinks (assumed to be small)
	private ArrayList<Integer> s1, s2;      //result sorting (s2=sinks, s1=rest)
	private int left;                       //number of unsorted vertices (left=0 used as stop-point in main procedure)
	private int numberOfReversingEdges = 0; //
	private int weightOfReversingEdges = 0; //
    private int numberOfFindMaxVertex = 0;  //
	private int lefttoright = 0;            //number of rightward edges
	private int righttoleft = 0;            //number of leftward edges
    private int rightToLeftWeight = 0;
	private ArrayList<Integer> reversedEdges;//list of all reversed edges found by sorting for further output by

    private HashMap<Integer,Integer> edgeCapacity;  //for max-flow algorithm

	public Graph() {
		this(1,0,false,true);
	}

	public Graph(int numberOfVertices){
	    this(numberOfVertices,0,false,true);
    }

	public Graph(int numberOfVertices,boolean newEdges) {
		this(numberOfVertices,0, false,newEdges);
	}

	public Graph(int numberOfVertices, int numberOfEdges) {
		this(numberOfVertices, numberOfEdges, false,true);
	}

    /**
     * Creates a graph with numbers of vertices and edges specified in params. This numbers would be enlarged
     * automatically then new vertices and edges will be added.
     *
     * @param numberOfVertices number of vertices in graph
     * @param numberOfEdges number of edges in graph
     * @param verticesConsecutive if vertices should have (true) consecutive numbers from 1 to numberOfVertices
     * @param newEdges if we should (true) create new edges and start their enumeration from 1 or (false) use existing edges
     */
	public Graph(int numberOfVertices, int numberOfEdges, boolean verticesConsecutive, boolean newEdges) {
		if (numberOfVertices > 0) {
			vertices = new HashMap<Integer, Node>(numberOfVertices);
			if (verticesConsecutive) {
				for (int i = 1; i <= numberOfVertices; i++) {
					Node node = new Node(i);
					vertices.put(i, node);
				}
			}

			if (newEdges) {
                if (numberOfEdges > 0) {
                    new Edges(numberOfEdges);
                } else {
                    new Edges();
                }
            }

			count = numberOfVertices;
			sources = new ArrayList<Integer>();
			sinks = new ArrayList<Integer>();
			s1 = new ArrayList<Integer>();
			s1.ensureCapacity(count);
			s2 = new ArrayList<Integer>();
			s2.ensureCapacity(count / 3);
			left = count;

			reversedEdges = new ArrayList<>(200);
            verticesByDegree = new ArrayList<>();
		}
	}

	public void addVertex(int id){
	    Node node = new Node(id);
        if (!vertices.containsKey(id)) {
            vertices.put(id, node);
        }
    }

    public boolean addEdge(int key, Edge edge){
        if (!vertices.containsKey(edge.out)) {
            Node node = new Node(edge.out);
            vertices.put(edge.out, node);
        }
        if (!vertices.containsKey(edge.in)) {
            Node node = new Node(edge.in);
            vertices.put(edge.in, node);
        }
        vertices.get(edge.out).addEdge(key, false, edge.weight);
        vertices.get(edge.in).addEdge(key, true, edge.weight);
        return true;
    }

	public boolean addEdge(int out, int in) {
		return addEdge(out, in, 1, false);
	}

	public boolean addEdge(int out, int in, boolean verticesExists) {
		return addEdge(out, in, 1, verticesExists);
	}

	public boolean addEdge(int out, int in, int weight) {
		return addEdge(out, in, weight, false);
	}

	public boolean addEdge(int out, int in, int weight, boolean verticesExists) {
		if (in != out) {
			if (weight < 0) {
				weight = 1;
			}
			int edgeKey = Edges.addEdge(out, in, weight);
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
			vertices.get(out).addEdge(edgeKey, false, weight);
			vertices.get(in).addEdge(edgeKey, true, weight);
			return true;
		} else return false;
	}

	//true if succeed
	boolean addWeightToEdge(int out, int in, int weight) {
		Node vertex = vertices.get(out);
		if (vertex != null) {
			for (int key : vertex.edgeKeys) {
				Edge edge = Edges.getEdge(key);
				if (edge.getOtherEnd(out) == in && edge.isIn(in)) {
					Edges.addWeight(key, weight);
					vertex.changeWeight(false, weight);
					vertices.get(in).changeWeight(true, weight);
					return true;
				}
			}
		}
		return false;
	}

	public HashMap<Integer, Node> getVertices(){
	    return vertices;
    }

    public boolean hasEdge(int out, int in){
        for (Edge edge: Edges.edges.values()){
            if (edge.in==in && edge.out == out) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates subgraph containing only vertexNumbers vertices.
     *
     * Creates subgraph containing only vertexNumbers vertices. The vertex is skipped in subgraph if there is no such
     * vertex number in graph.
     * @param vertexNumbers id numbers of vertices of subgraph
     * @return Graph. Edge keys and vertex numbers are the same as in original graph
     */
    public Graph getSubGraph(HashSet<Integer> vertexNumbers){
        Graph subGraph = new Graph(vertexNumbers.size(),false);
        if (vertexNumbers.size()>0) {
            for (Integer vertexNumber : vertexNumbers) {
                Node vertex = vertices.get(vertexNumber);
                subGraph.addVertex(vertexNumber);
                if (vertex!=null) {
                    for (Integer key : vertex.edgeKeys) {
                        Edge edge = Edges.getEdge(key);
                        int other = edge.getOtherEnd(vertexNumber);
                        if (edge.in==vertexNumber && edge.out==other){
                            subGraph.addEdge(key,edge);
                        }
                    }
                }
            }
        }
        return subGraph;
    }

    /*
    public Graph getSubGraph(ArrayList<Integer> vertexNumbers){
        Graph subGraph = new Graph(vertexNumbers.size(),false);
        if (vertexNumbers.size()>0) {
            for (Integer vertexNumber : vertexNumbers) {
                if (vertices.containsKey(vertexNumber)) {
                    for (Integer key : vertices.get(vertexNumber).edgeKeys) {
                        Edge edge = Edges.getEdge(key);
                        if (edge!=null && edge.isIn(vertexNumber) &&
                                vertexNumbers.contains(edge.getOtherEnd(vertexNumber))) {
                            subGraph.addEdge(edge.out, edge.in, edge.weight);
                        }
                    }
                }
            }
        }
        return subGraph;
    }
    */

    /**
     * Add back edge keys to vertices.
     *
     * Sorting delete edge keys from vertices in process. This method allows us to recover all edge keys.
     * Graph recovers all original vertices and edges after this method's end.
     */
	public void reloadVertices() {
		for (Integer key : Edges.edges.keySet()) {
			Edge edge = Edges.edges.get(key);
            if (!vertices.get(edge.out).edgeKeys.contains(key)) {
                vertices.get(edge.out).addEdge(key, false, edge.weight);
            }
            if (!vertices.get(edge.in).edgeKeys.contains(key)) {
                vertices.get(edge.in).addEdge(key, true, edge.weight);
            }
		}
	}

    /**
     * Refresh count (number of vertices in graph) and left (number of unsorted vertices).
     *
     */
	public void recount() {
		count = vertices.size();
		left = count;
		s1.ensureCapacity(count);
		s2.ensureCapacity(count / 3);
	}

	public int getCount() {
		return count;
	}

    /**
     * Number of edges in graph.
     *
     * @return number of edges in graph
     */
	public int getEdgeCount() { return Edges.edges.keySet().size(); }

	public ArrayList<Integer> getSorting() {
		ArrayList<Integer> result = new ArrayList<>();
		result.ensureCapacity(count);
		result.addAll(s1);
		Collections.reverse(s2);
		result.addAll(s2);
		Collections.reverse(s2);
		return result;
	}

	public ArrayList<Integer> getPermutation() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		result.ensureCapacity(count);
		for (Node vertex : vertices.values()) {
			result.add(vertex.id);
		}
		return result;
	}

	public ArrayList<Integer> findBubble(ArrayList<Integer> permutation){
	    TreeSet<Integer> overEdges = new TreeSet<>();
        TreeSet<Integer> insideEdges = new TreeSet<>();

        if (permutation.isEmpty()) {
            System.out.println("List is empty. No bubbles");
            return null;
        }

        int start;
        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<Integer> current = new ArrayList<>();

        for (int next: permutation){
            Node vertex = vertices.get(next);
            if (vertex != null && vertex.indegree+vertex.outdegree>0) {
                    if (current.size()<1){
                        current.add(next);
                        for (Integer edgeKey: vertex.edgeKeys){
                            if (overEdges.contains(edgeKey)){
                                overEdges.remove(edgeKey);
                            } else {
                                insideEdges.add(edgeKey);
                            }
                        }
                        //insideEdges.addAll(vertex.edgeKeys);
                    } else {
                        if (Collections.disjoint(overEdges, vertex.edgeKeys)) {
                            current.add(next);
                            for (Integer edgeKey : vertex.edgeKeys) {
                                if (insideEdges.contains(edgeKey)) {
                                    insideEdges.remove(edgeKey);
                                } else {
                                    insideEdges.add(edgeKey);
                                }
                            }
                            if (current.size()>2 && vertex.edgeKeys.containsAll(insideEdges)) {
                                if (current.size()>10 && current.size()<251000){
                                    System.out.println(current.size()+" : "+current);
                                }
                                result.add(-1);
                                result.addAll(current);
                                current.clear();
                                current.add(next);
                                overEdges.addAll(insideEdges);
                                insideEdges.clear();
                            }
                        } else {
                            //System.out.println("("+overEdges.size()+")"+vertex);
                            start = next;
                            current.clear();
                            current.add(start);
                            overEdges.addAll(insideEdges);
                            insideEdges.clear();
                            for (Integer edgeKey : vertex.edgeKeys) {
                                if (overEdges.contains(edgeKey)) {
                                    overEdges.remove(edgeKey);
                                } else {
                                    insideEdges.add(edgeKey);
                                    //System.out.print(Edges.getEdge(edgeKey));
                                }
                            }
                            //System.out.println();
                        }
                    }
            }
        }
        return result;
    }

    /**
     * Count cut width of permutation of vertices.
     *
     * Count cut width of permutation of vertices. Id missed in graph does not affect on result. Some id from graph
     * could be missed in permutation. Then their edges would not be count at result
     * @param permutation permutation of vertices of the graph
     * @return cut width
     */
    public int cutWidthNew(ArrayList<Integer> permutation){
        rightToLeftWeight = 0;
        righttoleft = 0;
        lefttoright = 0;
        reversedEdges.clear();
        ArrayList<Integer> edgeKeys = new ArrayList<>();
        int result = 0;

        for (Integer id: permutation){
            Node vertex = vertices.get(id);
            if (vertex!=null) {
                for (Integer key : vertex.edgeKeys) {
                    Edge edge = Edges.getEdge(key);
                    if (edgeKeys.contains(key)) {
                        edgeKeys.remove(key);
                    } else {
                        edgeKeys.add(key);
                        if (edge.isIn(id)) {
                            righttoleft++;
                            reversedEdges.add(key);
                            rightToLeftWeight += edge.weight;
                        } else {
                            lefttoright++;
                        }
                    }
                }
                result += edgeKeys.size();
            }
        }
        //System.out.println(edgeKeys);
        for (Integer key: edgeKeys){
            Edge edge = Edges.getEdge(key);
            Integer start = permutation.indexOf(edge.out);
            Integer end = permutation.indexOf(edge.in);
            if (start < 0){
                result -= permutation.size() - end;
                righttoleft--;
                rightToLeftWeight -= edge.weight;
                reversedEdges.remove(key);
            }
            if (end < 0){
                result -= permutation.size() - start;
            }
        }
        return result;
    }

    @Deprecated
    /**
     * Please, use cutWidthNew.
     *
     * Old version of cut width. Please, use cutWidthNew.
     *
     * @param permutation
     * @return
     */
	public int cutwidth(ArrayList<Integer> permutation) {
		/*
	    if (permutation.size() != count) {
			return -1;
		} else {
		*/
			ArrayList<Integer> ends = new ArrayList<>();
			ArrayList<Integer> starts = new ArrayList<>();
			int result = 0;
			int index;

			int end;
			Edge edge;

			for (Integer id : permutation) {
				Node vertex = vertices.get(id);
				if (vertex == null) {
					return -1;
				} else {
					ends.removeAll(Collections.singleton(id));
					for (int key : vertex.edgeKeys) {
						edge = Edges.getEdge(key);
						end = edge.getOtherEnd(id);

						index = starts.indexOf(end);
						if (index > -1) {
							if (id != end) starts.remove(index);
						} else if (id != end) {
							ends.add(end);
							starts.add(id);
							if (edge.isIn(id)) {
								righttoleft++;
                                rightToLeftWeight += edge.weight;
							} else {
								lefttoright++;
							}
						}
					}
				}
				result += ends.size();
			}

			//System.out.println(starts);
            //System.out.println(ends);
            for (int edgeNumber=0; edgeNumber<starts.size(); edgeNumber++){
			    Integer startNumber = starts.get(edgeNumber);
                Integer endNumber = -1;
                if (edgeNumber<ends.size()) {
                    endNumber = ends.get(edgeNumber);
                }
			    Node vertex = vertices.get(startNumber);
                for (int key : vertex.edgeKeys){
                    edge = Edges.getEdge(key);
                    if (edge!=null && edge.getOtherEnd(startNumber)==endNumber){
                        if (edge.isIn(startNumber)){
                            righttoleft--;
                            rightToLeftWeight -= edge.weight;
                        }
                    }
                }
            }
			return result;
		//}
	}

	public int rightToLeft(ArrayList<Integer> permutation) {
		lefttoright = 0;
		righttoleft = 0;
        rightToLeftWeight = 0;
		cutwidth(permutation);
		return righttoleft;
	}

	public int leftToRight(ArrayList<Integer> permutation) {
		lefttoright = 0;
		righttoleft = 0;
		cutwidth(permutation);
		return lefttoright;
	}

	private void fillSourcesAndSinks() {
		fillSourcesAndSinks(false);
	}

    /**
     * Find all sources and fill verticesByDegree.
     *
     * Fill sources with all vertices with zero incoming degree and also fill verticesByDegrees with vertices with
     * non-negative difference between out- and incoming weights.
     *
     * @param withoutSinks (true) do not touch sinks or (false) search for sink too
     */
	private void fillSourcesAndSinks(boolean withoutSinks) {
		if (left > 0) {
			for (Node vertex : vertices.values()) {
				if (vertex.indegree == 0) {
				    if (vertex.outdegree == 0) {
				        s1.add(vertex.id);
                        left -= 1;
                    } else {
                        sources.add(vertex.id);
                    }
				} else if ((!withoutSinks) && (vertex.outdegree == 0)) {
					sinks.add(vertex.id);
				}
				//experiments (improving performance)
                int difference = vertex.outdegree - vertex.indegree;
                addToVerticesByDegree(vertex.id,difference);
			}
		}
	}

    /**
     * Service method to simplify work with verticesByDegree.
     *
     * @param id id of vertex
     * @param difference diff between out- and incoming weights
     */
	private void addToVerticesByDegree(int id, int difference){
        if (difference >= 0) {
            if (verticesByDegree.size() <= difference){
                for (int i=verticesByDegree.size(); i<=difference; i++){
                    verticesByDegree.add(new HashSet<Integer>());
                }
            }
            verticesByDegree.get(difference).add(id);
        }
    }

    /**
     * Service method to simplify work with verticesByDegree.
     *
     * @param id id of vertex
     * @param difference diff between out- and incoming weights
     */
    private boolean removeFromVerticesByDegree(Integer id, int difference){
        if ( difference >= 0 && verticesByDegree.size() > difference &&
                verticesByDegree.get(difference).contains(id)){
            verticesByDegree.get(difference).remove(id);
            return true;
        }
        return false;
    }

    @Deprecated
	private int findMaxVertex() {
		int max = -1;
		int maxid = -1;
		int maxiddegree = -1;
		for (Node vertex : vertices.values()) {
			if (max < (vertex.outdegree - vertex.indegree)) {
				max = (vertex.outdegree - vertex.indegree);
				maxid = vertex.id;
				maxiddegree = vertex.indegree;
			} else if ((max == (vertex.outdegree - vertex.indegree)) && (vertex.indegree > maxiddegree)) {
				maxid = vertex.id;
				maxiddegree = vertex.indegree;
			}
		}
		return maxid;
	}

    /**
     * Find vertex with max difference between out- and incoming weights
     * @return id of such vertex
     */
	private int findMaxVertexNew(){
	    int k = verticesByDegree.size() - 1;
        while (verticesByDegree.get(k)!=null && verticesByDegree.get(k).isEmpty()) {
            verticesByDegree.trimToSize();
            k--;
            if (k<0){
                for (Node node: vertices.values()){
                    if (!node.isSorted()) {
                        System.out.println(node);
                    }
                }
                System.out.println("hmm");
            }
        }
        return verticesByDegree.get(k).iterator().next();
    }

	private int deleteVertex(int id) {
		return deleteVertex(id, true, false, true, true);
	}

    /**
     * Delete all edges from specified vertex and recount many things.
     *
     * Delete all edges from specified vertex. While deleting test another end of edge for becoming a source. Otherwise
     * recount it's difference, delete it from verticesByDegree and put back with new difference (if diff>=0).
     *
     * @param id id of vertex
     * @param sourceToSink same as in sorting
     * @param withoutSinks same as in sorting
     * @param takeNeighbor same as in sorting
     * @param takeAny same as in sorting
     * @return id of neighbor source or -1 if hasn't
     */
	private int deleteVertex(int id, boolean sourceToSink, boolean withoutSinks, boolean takeNeighbor, boolean takeAny) {
		if (id < 0) {
			return -1;
		} else {
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

			for (int key : vertices.get(id).edgeKeys) {
				Edge edge = Edges.getEdge(key);
				end = edge.getOtherEnd(id);
				vertex = vertices.get(end);

                //experiments (improving performance)
                removeFromVerticesByDegree(end,vertex.outdegree-vertex.indegree);
				if (vertex.deleteEdge(key)){
                    System.out.println(key);
                }

                //experiments (improving performance)
                addToVerticesByDegree(end,vertex.outdegree-vertex.indegree);

				//If vertex has no edges left immediately put it into s1
				if (vertex.outdegree == 0 && vertex.indegree == 0) {
					if (!vertex.isSorted()) {
						vertex.setSorted();
						left -= 1;
						if (sourceToSink) {
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
						if (takeAny) {
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

			//experiments (improving performance)
			vertex = vertices.get(id);
            removeFromVerticesByDegree(id,vertex.outdegree-vertex.indegree);

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
						if (sources.size() == 0) {
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
						if (sinks.size() == 0) {
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

	//Service procedures to find best source/sink to go to

    /**
     * Service method to find best source to go to.
     *
     * @return source with min outcoming degree
     */
	private int getMinSource() {
		int minid = -1;
		int mindegree = Integer.MAX_VALUE;
		int counter = 0;

		for (int key : sources) {
			Node vertex = vertices.get(key);

			if (mindegree > vertex.outdegree) {
				mindegree = vertex.outdegree;
				minid = counter;
			}
			counter++;
		}
		return minid;
	}

    /**
     * Service procedures to find best sink to go to.
     *
     * @return sink with max incoming degree
     */
	private int getMaxSink() {
		int maxid = -1;
		int maxdegree = -1;
		int counter = 0;

		for (int key : sinks) {
			Node vertex = vertices.get(key);
			if (maxdegree < vertex.indegree) {
				maxdegree = vertex.indegree;
				maxid = counter;
			}
			counter++;
		}
		return maxid;
	}

	//Main procedure

    /**
     * Main method with default parameters.
     */
	public long sorting() {
		return sorting(true, true, true, true);
	}

    /**
     * Eades, Lin, Smyth sorting of graph with some improvements.
     *
     * We sort all vertices of the graph to obtain best result in sense of number of reversing edges.
     * The result sorting is put into s1 (+ s2 if we use sinks).
     * Some improvements to original algorithm was done. Read parameters description first and next text later.
     *
     * Default settings are true,true,true,true
     * We go from sources to sinks, not using sinks and choose next vertex randomly among neighbors of previous source
     *
     * Original Eades, Lin, Smyth (improved Kahn) algorithm settings are true, false, false, true
     *
     * Closest to max-flow algorithm settings are false, false, true, false (or false, false, false, false)
     *
     * @param sourceToSink (true) get source first or (false) get sink first
     * @param withoutSinks (true) we use only sources and s1 (sourceToSink == false means withoutSinks = false)
     * @param takeNeighbor (true) choose next vertex from neighbors of previous if possible
     * @param takeAny (true )take any neighbor/any source/sink when does have choice
     * @return time taken
     */
	public long sorting(boolean sourceToSink, boolean withoutSinks, boolean takeNeighbor, boolean takeAny) {
		/*
		System.out.println("Go from sources to sinks: " + sourceToSink + "\nDo not use sinks: " + withoutSinks
				+ "\nTake neighbor if possible: " + takeNeighbor + "\nTake random when have choice: " + takeAny);
		*/
        recount();

		int next = -1;
		boolean issink = false;
		if (!sourceToSink) {
			withoutSinks = false;
		}

		fillSourcesAndSinks(withoutSinks);

        long startTime = System.currentTimeMillis();
		int nextmillion = count;
		while (left > 0) {
			if ((nextmillion - left) > 100000) {
				System.out.print(".");
				nextmillion = left;
			}

			if (next == -1) {
				if (sources.size() > 0 || sinks.size() > 0) {
					if ((sourceToSink && sources.size() > 0) || (!sourceToSink && sinks.size() == 0)) {
						if (takeAny) {
							next = sources.get(sources.size() - 1);
							sources.remove(sources.size() - 1);
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
				    //experiments (improving performance)
					next = findMaxVertexNew();
                    numberOfFindMaxVertex++;
					Node vertex = vertices.get(next);
					if (vertex != null) {
						numberOfReversingEdges += vertex.getInEdgesCount();
						weightOfReversingEdges += vertex.indegree;

						for (Integer key : vertex.edgeKeys) {
							if (Edges.getEdge(key).isIn(next)) {
								reversedEdges.add(key);
							}
						}
					} else {
						return -1;
					}
					issink = false;
				}
			}//Get next vertex to delete


			Node nextVertex = vertices.get(next);
			if (nextVertex != null && !nextVertex.isSorted()) {
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

			next = deleteVertex(next, sourceToSink, withoutSinks, takeNeighbor, takeAny);

			//System.out.print("After. ");
			//shortInfo(next);

			if (next > -1) {
				issink = false;
			} else if (next < -1) {
				issink = true;
				next = -10 - next;
			}
			//Need check if next is sink (to correct add at next stage)
		}
        //System.out.println("\nSorting take: "+(System.currentTimeMillis()-startTime)+" ms.");
        return System.currentTimeMillis()-startTime;
	}

	public void viewSorting() {
		viewSorting(false, false);
	}

	public void viewSorting(boolean printOnlyParameters) {
		viewSorting(printOnlyParameters, false);
	}

    /**
     * View sort to default out (console by default).
     *
     * Use sorting first!!! Otherwise there would be nothing to show.
     * View sort to default out (console by default).
     * Print all vertices separated with comma in sorting order. Afterwards print number and weight of reversing edges.
     * Finally print all reversing edges with their "length".
     * @param printOnlyParameters (true) do not show sorting, only metrics or (false) show all vertices by id
     * @param viewReversedEdges (true) show reversing edges or (false) not
     */
	public void viewSorting(boolean printOnlyParameters, boolean viewReversedEdges) {
		if (!printOnlyParameters) {
			String s = "";
			String ss = "";
			for (int x : s1) {
				s = s + "," + x;
			}
			for (int x : s2) {
				ss = "," + x + ss;
			}
			System.out.println(s + "---------" + ss);
		}
		System.out.println("Number of reversing edges: " + numberOfReversingEdges);
		System.out.println("Weight of reversing edges: " + weightOfReversingEdges);
        //System.out.println("(service info)Number of find max vertex procedure launching : " + numberOfFindMaxVertex);
		if (viewReversedEdges) {
			System.out.println("Reversing edges: ");
			ArrayList<Integer> ss = new ArrayList<>(count);
			ss.addAll(s1);
			ss.addAll(s2);
			for (Integer key : reversedEdges) {
				Edge edge = Edges.getEdge(key);
				System.out.print(edge);
				Integer from = edge.out;
				Integer to = edge.in;
				System.out.println("  length  " + (ss.indexOf(from) - ss.indexOf(to)));
			}
		}
	}

	public void fillCapacities(){
	    edgeCapacity = new HashMap<>();
        for (Integer edgeKey: Edges.edges.keySet()){
            Edge edge = Edges.getEdge(edgeKey);
            edgeCapacity.put(edgeKey,edge.weight);
        }
    }

	public int addFlowSourceAndSink(HashSet<Integer> backbone){
	    addVertex(count+1);
        addVertex(count+2);

        for (Integer id: backbone) {
            Node node = vertices.get(id);
            boolean isSourceEdgeAdded = false;
            int length = node.edgeKeys.size();
            for (int i=0; i< length; i++){
                Integer key = node.edgeKeys.get(i);
                Edge edge = Edges.getEdge(key);
                if (!backbone.contains(edge.getOtherEnd(id))) {
                    if (edge.isIn(id)){
                        //add edge from in-join node to sink
                            addEdge(edge.getOtherEnd(id), count + 2, edge.weight, true);
                    } else {
                        //add edge from source to backbone node
                        if (!isSourceEdgeAdded) {
                            addEdge(count+1, id, 99, true);
                            isSourceEdgeAdded = true;
                        }
                    }
                }
            }
        }

        return count;
    }

    public ArrayList<Integer> findPathFromFlowSourceToSink(int sourceId, int sinkId){
        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<Integer> resultEdges = new ArrayList<>();
        HashSet<Integer> visitedEdges = new HashSet<>();

        result.add(sourceId);
        Integer currentNode = sourceId;
        boolean getSink = false;
        while (!getSink){
            Node node = vertices.get(currentNode);
            int previousNode = currentNode;
            for (Integer key: node.edgeKeys){
                Edge edge = Edges.getEdge(key);
                if (!visitedEdges.contains(key) && !edge.isIn(currentNode) && edgeCapacity.get(key) > 0){
                    currentNode = edge.getOtherEnd(currentNode);
                    result.add(currentNode);
                    resultEdges.add(key);
                    visitedEdges.add(key);
                    if (currentNode==sinkId){
                        getSink = true;
                    }
                    break;
                }
            }
            if (previousNode==currentNode) {
                result.remove(result.size()-1);
                if (resultEdges.size()>0) {
                    resultEdges.remove(resultEdges.size() - 1);
                }
            }
            if (result.size()==0) {
                break;
            }
            currentNode = result.get(result.size()-1);
        }

        int minCapacity = Integer.MAX_VALUE;
        Integer minCapacityEdge = null;
        for (Integer edgekey: resultEdges){
            Integer capacity = edgeCapacity.get(edgekey);
            if (capacity < minCapacity) {
                minCapacity = capacity;
                minCapacityEdge = edgekey;
            }
        }
        for (Integer edgekey: resultEdges){
            Integer capacity = edgeCapacity.get(edgekey);
            edgeCapacity.put(edgekey,capacity - minCapacity);
        }

        System.out.println(minCapacity+":"+result);
        return result;
    }

    HashSet<Integer> visitedNodes = new HashSet<>();

    /**
     *
     * @param id
     * @param direction false for source, true for sink
     */
    private void fillVisitedNodes(Integer id, boolean direction){
        if (!visitedNodes.contains(id)) {
            visitedNodes.add(id);
            Node node = vertices.get(id);
            for (Integer edgeKey : node.edgeKeys) {
                Edge edge = Edges.getEdge(edgeKey);
                if (edge.isIn(id) == direction && edgeCapacity.get(edgeKey) > 0) {
                    Integer otherEnd = edge.getOtherEnd(id);
                    fillVisitedNodes(otherEnd, direction);
                }
            }
        }
    }

    public HashSet<Integer> minimumCut(int sourceId, int sinkId){
        HashSet<Integer> result = new HashSet<>();
        HashSet<Integer> sourceNodes;
        HashSet<Integer> sinkNodes;

        while (findPathFromFlowSourceToSink(sourceId, sinkId).size()>0){
        }

        fillVisitedNodes(sourceId, false);
        sourceNodes = visitedNodes;
        visitedNodes = new HashSet<>();
        fillVisitedNodes(sinkId, true);
        sinkNodes = visitedNodes;

        for (Integer edgeKey: Edges.edges.keySet()){
            Edge edge = Edges.getEdge(edgeKey);
            if (sourceNodes.contains(edge.out) && sinkNodes.contains(edge.in)){
                result.add(edgeKey);
            }
        }

        return result;
    }

    public void viewFlow(int sourceId, int sinkId){
        HashSet<Integer> hs = minimumCut(sourceId,sinkId);
        int flow = 0;
        for (Integer edgeKey: hs){
            Edge edge = Edges.getEdge(edgeKey);
            flow += edge.weight;
            System.out.println(edge);
        }
        System.out.println("Max flow = "+flow);
    }

    /*
	public HashSet<Integer> minimumCut(int sourceId, int sinkId){
	    HashSet<Integer> result = new HashSet<>();
        ArrayList<Integer> flow = new ArrayList<>(20);

        flow.add(sourceId);
        Integer currentNode = sourceId;
        while (true){
            Node node = vertices.get(currentNode);
            for (Integer key: node.edgeKeys){
                Edge edge = Edges.getEdge(key);
                if (!edge.isIn(currentNode)){

                }
            }
        }

        return result;
    }
    */

	public int getNumberOfReversingEdges() {
		return numberOfReversingEdges;
	}

	public int getWeightOfReversingEdges() { return weightOfReversingEdges; }

	public int getNumberOfFindMaxVertex() { return numberOfFindMaxVertex; }

    public int getRightToLeftWeight() {
        return rightToLeftWeight;
    }

    public int getLeftToRight(){
        return lefttoright;
    }

    public  int getRightToLeft(){
        return righttoleft;
    }

    /**
     * Full info about graph.
     *
     * Print description of all vertices line by line.
     */
    public void info(){
		for(Node vertex: vertices.values()){
			System.out.println(vertex.toString());
		}				
	}

	public void vertexInfo(int id){
	    Node vertex = vertices.get(id);
        if (vertex!=null) {
            System.out.println(vertex.toString());
        } else {
            System.out.println("\n\n");
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
