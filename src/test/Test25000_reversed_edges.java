package test;

import graph.*;

public class Test25000_reversed_edges {

	public static void main(String[] args){
		long startTime = System.currentTimeMillis();
		long currTime;
		int n = 400000;//

		System.out.println("Starting the construction of the graph...");

		Graph g = new Graph(n);

		System.out.println("Time elapsed: "+(System.currentTimeMillis()-startTime));
		System.out.println("Adding edges...");

		boolean check = true;
		for (int i=0; i<n-1; i++){
			g.addEdge(i,i+1);
			if (i<(n>>4)-1) {
				if (check) {
					g.addEdge(n-1-i,i);
					check = false;
				} else {
					g.addEdge(i,n-1-i);
					check = true;
				}
			}
			if (i!=n-1) {g.addEdge(n-1,i);}
		}
		
		System.out.println("Vertices in graph: "+n);

		/*
		System.out.println("Start evolution");	
	
		Permutation p = new Permutation(n);	

		Evolution evolution = new Evolution(p,300);
		Permutation permutation1 = new Permutation();
		
		permutation1 = evolution.evolute(g,300,100);

		System.out.println("RESULT ");
		permutation1.viewPermutation();
		*/

		//g.info();
		System.out.println("Time to construct the graph: "+(System.currentTimeMillis()-startTime)/1000);
		currTime = System.currentTimeMillis();
		
		g.sorting();
		
		System.out.println("Time to sort(sec): "+(System.currentTimeMillis()-currTime)/1000);
		System.out.println(g.getNumberOfReversingEdges());
		//g.outputSorting();
	}
}
