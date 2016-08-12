package test;

import graph.Graph;
import graph.GraphSaveLoad;
import graph.Permutation;

public class TestGFA {

    public static void main(String[] args){
        Graph g = GraphSaveLoad.loadGFA("brca1.gfa");
        Permutation p = new Permutation();

        if (g!=null) {
            g.recount();
            System.out.println("Total vertices in graph: " + g.getCount());


            Permutation sorted = new Permutation(g.getCount());
            sorted.setPermutation(GraphSaveLoad.loadSortedGFA("sort_brca1.gfa"));

            System.out.println("My sorting: ");
            g.sorting();
            GraphSaveLoad.saveSorting("brca1_sorted.txt",g.getSorting());
            System.out.println("Reversed edges: " + g.getNumberOfReversingEdges());

            p.setPermutation(g.getSorting());
            g.reloadVertices();
            p.setCutwidth(g.cutwidth(p.getPermutation()));
            p.viewPermutation(true);

            System.out.println("\nSorting from file: ");
            System.out.println("Reversed edges: " + g.rightToLeft(sorted.getPermutation()));
            sorted.setCutwidth(g.cutwidth(sorted.getPermutation()));
            sorted.viewPermutation(true);

            //g.outputSorting();
            //GraphSaveLoad.saveSorting("cactus_sorted.txt",g.getSorting());
        }
    }

}
