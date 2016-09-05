package test;

import graph.Graph;
import graph.Permutation;


public class TestRightOrder {

    public static void main(String[] args){
        Graph g = new Graph(9);
        Permutation p = new Permutation();

        g.addEdge(2,3);
        g.addEdge(6,4);
        g.addEdge(8,9,10);
        g.addEdge(5,7,10);
        g.addEdge(7,8,10);
        g.addEdge(1,2);
        g.addEdge(1,5,10);
        g.addEdge(3,4);
        g.addEdge(5,6);

        g.info();

        System.out.println("\nSorting with number of reversing edges: ");
        g.sorting();
        g.viewSorting();

        p.setPermutation(g.getSorting());
        g.reloadVertices();
        p.setCutwidth(g.cutwidth(p.getPermutation()));
        System.out.println("Reversed edges: "+g.rightToLeft(p.getPermutation()));
        p.viewPermutation(true);
    }
}
