package graph;

import java.io.*;
import java.util.*;
import java.util.Date;
import java.util.Random;

import graph.Graph.*;

public class GraphSaveLoad{
    private static Date date = new Date();
    private static Random random = new Random(date.getTime());

    public static int refCost = 15;

    public static Graph loadGraph(String filename){
		Graph g = null;
		try(FileReader fr = new FileReader(System.getProperty("user.dir")
				+"/src/test/"+filename);
			Scanner scan = new Scanner(fr)){

			int count = -1;
			int v1 = -1;
			int v2 = -1;
			int counter = 0;

			while(scan.hasNext()){
				if (scan.hasNextInt()) {
					if (counter==0){
						count = scan.nextInt();
						if (count>0) {
							g = new Graph(count);
							//System.out.println("Creating new graph with "+count+" vertices");
						}
					} else {
						if (v1==-1) {
							v1 = scan.nextInt();
						} else {
							v2 = scan.nextInt();
							if (v2==-1) {
								v1 = -1;
							} else {
								assert g != null;
								g.addEdge(v1,v2);
								//System.out.println("Adding edge from "+v1+" to "+v2);
								v1 = v2;
							}							
						}
					}
				} else {
				}
				counter++;
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		return g;
	}

	public static Graph loadGFA(String filename) {
		return loadGFA(filename, "");
	}

	public static Graph loadGFA(String filename, String refName){
		Graph g = null;
		try(FileReader fr = new FileReader(System.getProperty("user.dir")
				+"/src/test/"+filename);
			Scanner scan = new Scanner(fr)){

			class PathNode implements Comparable<PathNode>{
				int num = -1;
				int id;
				boolean forward;

				@Override
				public int compareTo(PathNode o) {
					return num - o.num;
				}
			}

			class Path{
				TreeSet<PathNode> nodes = new TreeSet<>();
			}

			HashMap<String,Path> paths = new HashMap<>();

			int v1;
			int v2;
			char c1;
			char c2;
			int counter = 0;
			while(scan.hasNextLine()){
				String s = scan.nextLine();
				Scanner scan2 = new Scanner(s).useDelimiter("\\t");
				if (counter==0){
					g = new Graph();
				} else {
					if (s.startsWith("S")){
						v1 = Integer.parseInt(scan2.findInLine("\\d+"));
						g.addVertex(v1);
					}
					if (s.startsWith("L")) {
						v1 = Integer.parseInt(scan2.findInLine("\\d+"));
						c1 = scan2.findInLine("[+-]").charAt(0);
						v2 = Integer.parseInt(scan2.findInLine("\\d+"));
						c2 = scan2.findInLine("[+-]").charAt(0);
						if (c1==c2) {
							if (c1=='+') {
								g.addEdge(v1, v2, 1);
								//System.out.println("Adding edge from "+v1+" to "+v2);
							} else {
								g.addEdge(v2, v1, 1);
								//System.out.println("Adding edge from "+v2+" to "+v1);
							}
						}
						//g.addEdge(v1,v2);
						//System.out.println("Adding edge from "+v1+" to "+v2);
					}
					if (s.startsWith("P")) {
						int id = Integer.parseInt(scan2.findInLine("\\d+"));
						String name = scan2.next();
						int num = Integer.parseInt(scan2.findInLine("\\d+"));
						char c = scan2.findInLine("[+-]").charAt(0);
						PathNode pathNode = new PathNode();
						Path path;
						if (paths.containsKey(name)){
							path = paths.get(name);
						} else {
							path = new Path();
						}
						pathNode.id = id;
						pathNode.num = num;
						pathNode.forward = ( c=='+' );
						path.nodes.add(pathNode);
						paths.put(name,path);
					}
				}
				counter++;
				scan2.close();
			}

			//Dealing with paths

			if (g!=null) {
				int weight;
				for (String name : paths.keySet()) {
					Path path = paths.get(name);
					weight = name.equals(refName) ? refCost : 1;

					PathNode previousNode = new PathNode();
					for (PathNode nextNode : path.nodes) {
						if (nextNode.num - previousNode.num == 1) {
							if (nextNode.forward && previousNode.forward) {
								g.addWeightToEdge(previousNode.id, nextNode.id, weight);
							} else if (!nextNode.forward && !previousNode.forward){
								g.addWeightToEdge(nextNode.id, previousNode.id, weight);
							}
						}
						previousNode = nextNode;
					}
				}
			}


		}  catch (IOException e){
			e.printStackTrace();
		}
		if (g!=null) { g.recount(); }
		return g;
	}

	public static ArrayList<Integer> loadSortedGFA(String filename){
		ArrayList<Integer> sorted = new ArrayList<>();
		try(FileReader fr = new FileReader(System.getProperty("user.dir")
				+"/src/test/"+filename);
			Scanner scan = new Scanner(fr)){

			int v;
			while(scan.hasNextLine()){
				String s = scan.nextLine();
				Scanner scan2 = new Scanner(s).useDelimiter("\\t");
				if (s.startsWith("S")) {
					v = Integer.parseInt(scan2.findInLine("\\d+"));
					sorted.add(v);
				}
			}
		}  catch (IOException e){
			e.printStackTrace();
		}
		return sorted;
	}

    public static ArrayList<Integer> loadSorting(String filename){
        ArrayList<Integer> sorted = new ArrayList<>();
        try(FileReader fr = new FileReader(System.getProperty("user.dir")
                +"/src/test/"+filename);
            Scanner scan = new Scanner(fr)){

            int next;
            while(scan.hasNextInt()){
                next = scan.nextInt();
                sorted.add(next);
            }
        }  catch (IOException e){
            e.printStackTrace();
        }
        return sorted;
    }

    public static boolean saveGFA(String filename, Graph g) {
        return saveGFA(filename,g,null);
    }

    public static boolean saveGFA(String filename, Graph g, ArrayList<Integer> reference){
        return saveGFA(filename, g, reference, null);
    }

	public static boolean saveGFA(String filename, Graph g, ArrayList<Integer> reference, HashSet<ArrayList<Integer>> paths){
        try(BufferedWriter bw = new BufferedWriter(
                new FileWriter(System.getProperty("user.dir")
                        +"/src/test/"+filename));
            PrintWriter pw = new PrintWriter(bw)){
            Edge edge;
            pw.print("H\tVN:Z:1.0");
            for (Node vertex: g.getVertices().values()){
                StringBuffer s = new StringBuffer(1000);
                s.append("\nS\t");
                s.append(vertex.id);
                int nextRandom = random.nextInt(4);
                switch (nextRandom){
                    case 0:
                        s.append("\tA");
                        break;
                    case 1:
                        s.append("\tC");
                        break;
                    case 2:
                        s.append("\tT");
                        break;
                    case 3:
                        s.append("\tG");
                        break;
                }
                for (Integer key: vertex.edgeKeys){
                    edge = Edges.getEdge(key);
                    if (!edge.isIn(vertex.id)) {
                        s.append("\nL\t");
                        s.append(vertex.id);
                        s.append("\t+\t");
                        s.append(edge.getOtherEnd(vertex.id));
                        s.append("\t+\t0M");
                    }
                }
                pw.print(s);
            }
            if (reference!=null){
                int counter = 1;
                for (Integer step: reference){
                    StringBuffer s = new StringBuffer(1000);
                    s.append("\nP\t");
                    s.append(step);
                    s.append("\tref\t");
                    s.append(counter);
                    s.append("\t+\t1M");
                    pw.print(s);
                    counter++;
                }
            }
            if (paths!=null){
                int pathNumber = 1;
                for (ArrayList<Integer> path: paths) {
                    int counter = 1;
                    for (Integer step : path) {
                        StringBuffer s = new StringBuffer(1000);
                        s.append("\nP\t");
                        s.append(step);
                        s.append("\tpath");
                        s.append(pathNumber);
                        s.append("\t");
                        s.append(counter);
                        s.append("\t+\t1M");
                        pw.print(s);
                        counter++;
                    }
                    pathNumber++;
                    pw.flush();
                }
            }
            pw.flush();
            pw.close();
            return true;
        }  catch (IOException e)	{
            e.printStackTrace();
            return false;
        }
    }

	public static Graph loadWeightedGraph(String filename){
		Graph g = null;
		try(FileReader fr = new FileReader(System.getProperty("user.dir")
				+"/src/test/"+filename);
			Scanner scan = new Scanner(fr)){
			
			int v1 = -1;
			int v2 = -1;
			int weight = -1;
			int counter = 0;
			while(scan.hasNextLine()){
				String s = scan.nextLine();
				Scanner scan2 = new Scanner(s);
				if (counter==0){
					int count = scan2.nextInt();
					if (count>0) {
						g = new Graph(count);
						//System.out.println("Creating new graph with "+count+" vertices");
					}
				} else {
					v1 = scan2.hasNextInt() ? scan2.nextInt() : -2;
					v2 = scan2.hasNextInt() ? scan2.nextInt() : -2;
					weight = scan2.hasNextInt() ? scan2.nextInt() : -2;
					if ( v1>-1 && v2>-1 ){
						assert g != null;
						g.addEdge(v1,v2,weight);
					}
					//System.out.println("Adding edge from "+v1+" to "+v2);
				}
				counter++;
			}
		}  catch (IOException e){
			e.printStackTrace();
		}		
		return g;
	}

	public static boolean saveGraph(String filename, Graph g, boolean isFirstFile){
		String lineSeparator = System.getProperty("line.separator");
		try(BufferedWriter bw = new BufferedWriter(
				new FileWriter(System.getProperty("user.dir")
				+"/src/test/"+filename));
			PrintWriter pw = new PrintWriter(bw)){
			
			if (isFirstFile) {
				pw.println(g.count);
			} else {
				pw.println(-1);
			}
			String s;
			for (Edge edge: Edges.edges.values()){
				s = edge.out + " " + edge.in + " " + edge.weight + " -1";
				pw.println(s);
			}
			return true;
		}  catch (IOException e)	{
			e.printStackTrace();
			return false;
		} 		
	}

	public static boolean saveSorting (String filename, ArrayList<Integer> sorting){
		try(BufferedWriter bw = new BufferedWriter(
				new FileWriter(System.getProperty("user.dir")
						+"/src/test/"+filename));
			PrintWriter pw = new PrintWriter(bw)){

			String s="";
			int inLine = 0;
			int lines = 0;
			for (int vertex_number: sorting){
				s = s + vertex_number + "\t";
				inLine++;
				if (inLine>1000){
					pw.println(s);
					s = "";
					inLine = 0;
					lines++;
				}
				if (lines % 10 == 0){
					pw.flush();
				}
			}
			pw.println(s);
			pw.flush();
			return true;
		}  catch (IOException e)	{
			e.printStackTrace();
			return false;
		}
	}
}
