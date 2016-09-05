package test;

import graph.*;
import java.util.ArrayList;

public class Test_from_paper {

	public static void main(String[] args){
		Graph g = new Graph(13);
		Permutation p = new Permutation();	

		g.addEdge(1,2,9);
		g.addEdge(2,3,7);
		g.addEdge(3,4,8);
		g.addEdge(4,5,7);
		g.addEdge(2,6,2);
		g.addEdge(6,4,1);
		g.addEdge(3,8,2);
		g.addEdge(9,3,3);
		g.addEdge(8,7,1);
		g.addEdge(7,9,1);
		g.addEdge(8,9,2);
		g.addEdge(1,10,2);
		g.addEdge(11,0,2);
		g.addEdge(10,11,1);
		g.addEdge(10,12,1);
		g.addEdge(12,11,1);
		//g.addEdge(12,12,1);
		g.addEdge(6,0,1);
		//g.addEdge(0,4,3);
		//g.addEdge(4,0,3);


		g.info();

		ArrayList<Integer> permutation = new ArrayList<>();
		System.out.println("\nOrder from paper: ");
		permutation.add(1);
		permutation.add(10);
		permutation.add(12);
		permutation.add(11);
		permutation.add(2);
		permutation.add(6);
		permutation.add(0);
		permutation.add(8);
		permutation.add(7);
		permutation.add(9);
		permutation.add(3);
		permutation.add(4);
		permutation.add(5);
		p.setPermutation(permutation);
		p.setCutwidth(g.cutwidth(p.getPermutation()));
		p.viewPermutation();


		System.out.println("\nOrder from weighted sorting with 0-4: ");
		permutation.clear();
		permutation.add(1);
		permutation.add(10);
		permutation.add(12);
		permutation.add(11);
		permutation.add(2);
		permutation.add(6);
		permutation.add(0);
		permutation.add(8);
		permutation.add(7);
		permutation.add(9);
		permutation.add(3);
		permutation.add(4);
		permutation.add(5);
		p.setPermutation(permutation);
		p.setCutwidth(g.cutwidth(p.getPermutation()));
		p.viewPermutation();
		
		System.out.println("\nOrder from weighted sorting with 4-0: ");
		permutation.clear();
		permutation.add(1);
		permutation.add(10);
		permutation.add(12);
		permutation.add(11);
		permutation.add(2);
		permutation.add(6);
		permutation.add(8);
		permutation.add(7);
		permutation.add(9);
		permutation.add(3);
		permutation.add(4);
		permutation.add(5);
		permutation.add(0);
		p.setPermutation(permutation);
		p.setCutwidth(g.cutwidth(p.getPermutation()));
		p.viewPermutation();

		System.out.println("\nOrder from weighted sorting with 4-0 (no sinks!): ");
		permutation.clear();
		permutation.add(1);
		permutation.add(10);
		permutation.add(12);
		permutation.add(11);
		permutation.add(2);
		permutation.add(6);
		permutation.add(3);
		permutation.add(8);
		permutation.add(7);
		permutation.add(9);
		permutation.add(4);
		permutation.add(0);
		permutation.add(5);
		p.setPermutation(permutation);
		p.setCutwidth(g.cutwidth(p.getPermutation()));
		p.viewPermutation();

		System.out.println("\nOrder from weighted sorting with 4-0 and 0-4: ");
		permutation.clear();
		permutation.add(1);
		permutation.add(10);
		permutation.add(12);
		permutation.add(11);
		permutation.add(2);
		permutation.add(6);
		permutation.add(3);
		permutation.add(8);
		permutation.add(7);
		permutation.add(0);
		permutation.add(4);
		permutation.add(9);
		permutation.add(5);
		p.setPermutation(permutation);
		p.setCutwidth(g.cutwidth(p.getPermutation()));
		p.viewPermutation();

		System.out.println("\nOrder from weighted sorting with 12-12 edge, 0-4 and 4-0: ");
		permutation.clear();
		permutation.add(1);
		permutation.add(10);
		permutation.add(2);
		permutation.add(6);
		permutation.add(3);
		permutation.add(8);
		permutation.add(7);
		permutation.add(11);
		permutation.add(0);
		permutation.add(12);
		permutation.add(4);
		permutation.add(9);
		permutation.add(5);
		p.setPermutation(permutation);
		p.setCutwidth(g.cutwidth(p.getPermutation()));
		p.viewPermutation();


		System.out.println("\nOrder from UNweighted sorting with all edges: ");
		permutation.clear();
		permutation.add(1);
		permutation.add(10);
		permutation.add(2);
		permutation.add(6);
		permutation.add(3);
		permutation.add(8);
		permutation.add(7);
		permutation.add(12);
		permutation.add(11);
		permutation.add(0);
		permutation.add(4);
		permutation.add(9);
		permutation.add(5);
		p.setPermutation(permutation);
		p.setCutwidth(g.cutwidth(p.getPermutation()));
		p.viewPermutation();


		//Evolution
		
		Evolution evolution = new Evolution(g,200);
		Permutation permutation1 = new Permutation();
		
		permutation1 = evolution.evolute(g,1000,10);

		System.out.println();
		System.out.println("RESULT ");
		permutation1.viewPermutation();
		int RtoL = g.rightToLeft(permutation1.getPermutation());
		System.out.println("<--- "+RtoL);
		int LtoR = g.leftToRight(permutation1.getPermutation());
		System.out.println("---> "+LtoR);
		


		System.out.println("\nSorting with number of reversing edges: ");
		g.sorting();
		g.viewSorting();
	}
}
