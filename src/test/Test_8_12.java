package test;

import graph.Graph;
import graph.Permutation;

import java.util.ArrayList;

public class Test_8_12 {
    public static void main(String[] args) {
        Graph g = new Graph(8);

        g.addEdge(1,2);
        g.addEdge(2,3);
        g.addEdge(3,4);
        g.addEdge(2,5);
        g.addEdge(4,1);
        g.addEdge(5,4);
        g.addEdge(3,6);
        g.addEdge(6,1);
        g.addEdge(5,7);
        g.addEdge(7,1);
        g.addEdge(7,8);
        g.addEdge(8,2);

        g.info();
        g.sorting();
        g.reloadVertices();
        g.viewSorting(false,false);

        Permutation p = new Permutation(g.getCount());
        p.setPermutation(g.getSorting());
        p.setCutwidth(g.cutWidthNew(p.getPermutation()));
        p.viewPermutation();

        /*
        System.out.println("Alternative by Haussler's algorithm.");
        Permutation p2 = new Permutation(g.getCount());
        ArrayList<Integer> a2 = new ArrayList<>(8);
        a2.add(1);
        a2.add(7);
        a2.add(8);
        a2.add(2);
        a2.add(3);
        a2.add(6);
        a2.add(5);
        a2.add(4);
        p2.setPermutation(a2);
        p2.setCutwidth(g.cutWidthNew(p2.getPermutation()));
        p2.viewPermutation();
        */
    }
}
