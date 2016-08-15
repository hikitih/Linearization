package test;

import graph.Graph;
import graph.GraphSaveLoad;
import graph.Permutation;

public class TestGFA {

    public static void main(String[] args){
        Graph g = GraphSaveLoad.loadGFA("brca1.gfa");
        Permutation mySorting = new Permutation();

        if (g!=null) {
            System.out.println("Total vertices in graph: " + g.getCount());


            System.out.println("My sorting: ");
            //g.info();
            g.sorting();
            //g.viewSorting(true);
            GraphSaveLoad.saveSorting("brca1_sorted.txt",g.getSorting());
            System.out.println("Reversed edges: " + g.getNumberOfReversingEdges());
            mySorting.setPermutation(g.getSorting());
            g.reloadVertices();
            mySorting.setCutwidth(g.cutwidth(mySorting.getPermutation()));
            mySorting.viewPermutation(true);


            System.out.println("\nSorting from file: ");
            Permutation sorted = new Permutation(g.getCount());
            sorted.setPermutation(GraphSaveLoad.loadSortedGFA("sort_brca1.gfa"));
            System.out.println("Reversed edges: " + g.rightToLeft(sorted.getPermutation()));
            sorted.setCutwidth(g.cutwidth(sorted.getPermutation()));
            sorted.viewPermutation(true);

            //g.outputSorting();
        }
    }

}
