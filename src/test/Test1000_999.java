package test;

import graph.*;
import java.util.Date;
import java.util.Random;

public class Test1000_999 {

	public static void main(String[] args){
        Date date = new Date();
        Random random = new Random(date.getTime());
        long startTime = System.currentTimeMillis();
		long currTime;
		int n = 13000000;//up to 18M works. at 20M fails to add 11M edges

		System.out.println("Starting the construction of the graph...");

		Graph g = new Graph(n,n);

		System.out.println("Time elapsed: "+(System.currentTimeMillis()-startTime));
		System.out.println("Adding edges...");

		int nextmillion = 0;
		for (int i=0; i<n-1; i++){
            g.addEdge(i,i+1);
            if ((i-nextmillion)>1000000){
				System.out.print(".");
				nextmillion = i;
			}
		}
		g.addEdge(n-1,0);
		System.out.println();

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
		
		g.sorting(true,true,true,false);
		g.viewSorting(true);
		
		System.out.println();
		System.out.println("Time to sort(sec): "+(System.currentTimeMillis()-currTime)/1000);
		System.out.println(g.getNumberOfReversingEdges());
		//g.outputSorting();
	}
}
