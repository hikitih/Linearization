package test;

import graph.*;
import java.util.ArrayList;

public class Test20_29 {

	public static void main(String[] args){
		Graph g = new Graph(20,29);
	
		g.addEdge(0,1);
		g.addEdge(0,2);
		g.addEdge(1,3);
		g.addEdge(2,4);
		g.addEdge(3,5);
		g.addEdge(4,5);
		g.addEdge(5,6);
		g.addEdge(6,8);
		g.addEdge(8,7);
		g.addEdge(8,10);
		g.addEdge(10,9);
		g.addEdge(9,7);
		g.addEdge(7,5);
		g.addEdge(5,11);
		g.addEdge(10,11);
		g.addEdge(11,13);
		g.addEdge(12,11);
		g.addEdge(13,12);
		g.addEdge(11,14);
		g.addEdge(14,15);
		g.addEdge(14,16);
		g.addEdge(15,17);
		g.addEdge(16,17);
		g.addEdge(10,16);
		g.addEdge(17,10);
		g.addEdge(13,18);
		g.addEdge(17,18);
		g.addEdge(18,19);
		g.addEdge(4,19);
		
		Evolution evolution = new Evolution(g,500);
		Permutation permutation = new Permutation();
		
		permutation = evolution.evolute(g,500,100);

		System.out.println("RESULT ");
		permutation.viewPermutation();
		int RtoL = g.rightToLeft(permutation.getPermutation());
		System.out.println("<--- "+RtoL);
		int LtoR = g.leftToRight(permutation.getPermutation());
		System.out.println("---> "+LtoR);

		ArrayList<Integer> permutation_sorted = new ArrayList<>();
		permutation_sorted.add(0);
		permutation_sorted.add(2);
		permutation_sorted.add(4);
		permutation_sorted.add(1);
		permutation_sorted.add(3);
		permutation_sorted.add(10);
		permutation_sorted.add(9);
		permutation_sorted.add(5);
		permutation_sorted.add(6);
		permutation_sorted.add(8);
		permutation_sorted.add(7);
		permutation_sorted.add(11);
		permutation_sorted.add(13);
		permutation_sorted.add(12);
		permutation_sorted.add(14);
		permutation_sorted.add(15);
		permutation_sorted.add(16);
		permutation_sorted.add(17);
		permutation_sorted.add(18);
		permutation_sorted.add(19);
		Permutation sorted = new Permutation();
		sorted.setPermutation(permutation_sorted);
		sorted.setCutwidth(g.cutwidth(sorted.getPermutation()));
		sorted.viewPermutation();		

		g.info();
		g.sorting();
		g.outputSorting();
	}
}
