package test;


import graph.Graph;
import graph.Permutation;

public class TestWrongOrder {

    public static void main(String[] args){
        Graph g = new Graph(10);
        Permutation p = new Permutation();


        g.addEdge(10,11,5);
        g.addEdge(10,12);
        g.addEdge(11,13,5);
        g.addEdge(11,14);
        g.addEdge(12,14);
        g.addEdge(13,12);
        g.addEdge(13,15,5);
        g.addEdge(15,14);
        g.addEdge(15,16,5);
        g.addEdge(16,17,5);
        g.addEdge(17,18,5);
        g.addEdge(18,19,5);

        g.info();

        System.out.println("\nSorting with number of reversing edges: ");
        g.sorting(true,false,true,false);
        g.outputSorting();

        p.setPermutation(g.getSorting());
        g.reloadVertices();
        p.setCutwidth(g.cutwidth(p.getPermutation()));
        p.viewPermutation(true);
    }
}
