package test;

import graph.Graph;
import graph.Permutation;

import java.util.ArrayList;

public class Test_missing_nodes {
    public static void main(String[] args) {
        Graph g = new Graph(13);

        g.addEdge(1,11,5);
        g.addEdge(11,3,5);
        g.addEdge(3,9,5);
        g.addEdge(9,18,5);
        g.addEdge(18,6,5);
        g.addEdge(6,7,5);
        g.addEdge(1,8);
        g.addEdge(8,3);
        g.addEdge(3,4);
        g.addEdge(4,18);
        g.addEdge(18,10);
        g.addEdge(10,7);
        g.addEdge(8,2);
        g.addEdge(2,4);
        g.addEdge(4,12);
        g.addEdge(12,10);
        g.addEdge(2,13);
        g.addEdge(13,12);
        g.addEdge(7,14,5);
        g.addEdge(14,15,5);
        g.addEdge(7,16);
        g.addEdge(16,15);
        g.addEdge(10,17);
        g.addEdge(17,16);
        g.addEdge(12,5);
        g.addEdge(5,17);
        g.addEdge(13,19);
        g.addEdge(19,5);

        g.info();
        g.sorting();
        g.reloadVertices();
        g.viewSorting();
        System.out.println(g.cutWidthNew(g.getSorting()));

        Permutation p = new Permutation();
        ArrayList<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(2);
        a.add(8);
        a.add(3);
        a.add(11);
        a.add(9);
        a.add(4);
        a.add(13);
        a.add(12);
        a.add(5);
        a.add(10);
        a.add(6);
        a.add(7);
        p.setPermutation(a);
        p.setCutwidth(g.cutWidthNew(p.getPermutation()));
        p.viewPermutation();

    }
}
