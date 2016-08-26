package test;

import graph.Evolution;
import graph.Graph;
import graph.GraphSaveLoad;
import graph.Permutation;

public class TestGFA {

    public static void main(String[] args){
        Graph g = GraphSaveLoad.loadGFA("random_graph_20_63.gfa", "ref");
        Permutation mySorting = new Permutation();

        if (g!=null) {
            System.out.println("Total vertices in graph: " + g.getCount());
            //g.info();
            System.out.println("My sorting: ");
            g.sorting();
            //GraphSaveLoad.saveSorting("random_graph_20_63_sorted.txt",g.getSorting());
            g.viewSorting();

            mySorting.setPermutation(g.getSorting());
            g.reloadVertices();
            mySorting.setCutwidth(g.cutWidthNew(mySorting.getPermutation()));
            mySorting.viewPermutation(true);


            System.out.println("\nSorting from file: ");
            Permutation sorted = new Permutation(g.getCount());
            sorted.setPermutation(GraphSaveLoad.loadSortedGFA("random_graph_20_63.gfa"));
            sorted.setCutwidth(g.cutWidthNew(sorted.getPermutation()));
            System.out.println("Reversed edges: " + g.getRightToLeft());
            System.out.println("Weight: " + g.getRightToLeftWeight());
            sorted.viewPermutation(true);

            //GraphSaveLoad.saveGFA("brca1_try_to_save.gfa",g);

            //g.outputSorting();
        }
    }

}
