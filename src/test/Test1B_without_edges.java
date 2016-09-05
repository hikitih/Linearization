package test;

import graph.*;

public class Test1B_without_edges {

	public static void main(String[] args){
		long startTime = System.currentTimeMillis();
		long currTime = System.currentTimeMillis();
		int n = 15000000;//up to 18M works. at 20M fails to add 11M edges

		System.out.println("Starting the construction of the graph...");

		Graph g = new Graph(n);

		System.out.println("Time elapsed: "+(System.currentTimeMillis()-startTime));
		System.out.println("Adding edges...");
		currTime = System.currentTimeMillis();

		int nextmillion = 0;
		/*
		for (int i=0; i<n-1; i++){
			g.addEdge(i,i+1);
			if ((i-nextmillion)>1000000){
				System.out.print(".");
				nextmillion = i;
			}
		}
		g.addEdge(n-1,0);
		*/
		
		System.out.println();

		System.out.println("Vertices in graph: "+n);

		//g.info();
		System.out.println("Time to construct the graph: "+(System.currentTimeMillis()-startTime)/1000);
		currTime = System.currentTimeMillis();
		
		//g.fillSourcesAndSinks(false);

		g.sorting();
		
		System.out.println();
		System.out.println("Time to sort(sec): "+(System.currentTimeMillis()-currTime)/1000);
		System.out.println(g.getNumberOfReversingEdges());
		//g.outputSorting();
	}
}
