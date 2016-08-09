package graph;

import java.io.*;
import java.util.Scanner;
import graph.Graph.*;

public class GraphSaveLoad{
	public static Graph loadGraph(String filename, Graph g){
		try(FileReader fr = new FileReader(System.getProperty("user.dir")
				+"/src/graph/"+filename);
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
		} catch (FileNotFoundException e){
			System.out.println(e);
		} catch (IOException e){
			System.out.println(e);
		}		
		return g;
	}

	public static boolean saveGraph(String filename, Graph g, boolean isFirstFile){
		String lineSeparator = System.getProperty("line.separator");
		try(BufferedWriter bw = new BufferedWriter(
				new FileWriter(System.getProperty("user.dir")
				+"/src/graph/"+filename));
			PrintWriter pw = new PrintWriter(bw)){
			
			if (isFirstFile) {
				pw.println(g.count);
			} else {
				pw.println(-1);
			}
			String s="";
			for (Vertex v: g.vertices){
				for (int i: v.outEdges) {
					s = v.id + " " + i + " -1";
					pw.println(s);
				}
				pw.flush();
			}
			return true;
		} catch (FileNotFoundException e){
			System.out.println(e);
			return false;
		} catch (IOException e)	{
			System.out.println(e);
			return false;
		} 		
	}

}
