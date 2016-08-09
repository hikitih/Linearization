package test;

import graph.*;
import java.util.ArrayList;

public class TestDegradation {

	public static void main(String[] args){
		int n=20;
		Graph g = new Graph(n);
	
		for (int i=0; i<n-1; i++){
			g.addEdge(i,i+1);
		}

		ArrayList<Integer> permutation_sorted = new ArrayList<>();
		for (int i=0;i<100;i++){
			permutation_sorted.add(i);
		}
		Permutation sorted = new Permutation();
		sorted.setPermutation(permutation_sorted);
		sorted.setCutwidth(g.cutwidth(sorted.getPermutation()));
		sorted.viewPermutation();		


		Evolution evolution = new Evolution(g,500);
		Permutation permutation = new Permutation();
		
		permutation = evolution.evolute(g,500,100);

		System.out.println("RESULT ");
		permutation.viewPermutation();
		int RtoL = g.rightToLeft(permutation.getPermutation());
		System.out.println("<--- "+RtoL);
		int LtoR = g.leftToRight(permutation.getPermutation());
		System.out.println("---> "+LtoR);		

		//g.info();
		g.sorting();
		g.outputSorting();
	}
}
