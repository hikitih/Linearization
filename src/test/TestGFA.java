package test;

import graph.Graph;
import graph.GraphSaveLoad;
import graph.Permutation;

public class TestGFA {
    static Graph g;
    static Permutation mySorting = new Permutation();

    public static void show(String filename, String sortname) {
        g = GraphSaveLoad.loadGFA(filename, "ref");
        if (g != null) {
            System.out.println(filename);
            System.out.println("Total vertices in graph: " + g.getCount());
            System.out.println("My sorting: ");
            g.sorting();
            g.viewSorting(true, false);
            g.reloadVertices();

            mySorting.setPermutation(g.getSorting());
            mySorting.setCutwidth(g.cutWidthNew(mySorting.getPermutation()));
            mySorting.viewPermutation(true);

            System.out.println("\nSorting from file: ");
            Permutation sorted = new Permutation(g.getCount());
            sorted.setPermutation(GraphSaveLoad.loadSorting(sortname));
            sorted.setCutwidth(g.cutWidthNew(sorted.getPermutation()));
            System.out.println("Vertices in graph: " + sorted.length());
            System.out.println("Reversed edges: " + g.getRightToLeft());
            System.out.println("Weight: " + g.getRightToLeftWeight());
            sorted.viewPermutation(true);
            System.out.println("\n=====================================\n");

        }
    }

    public static void main(String[] args) {

        show("biographs/biograph_200_ME_0_LD_10_DU_0_InDel_32_SNP_50.gfa","biographs/biograph_200_ME_0_LD_10_DU_0_InDel_32_SNP_50.gfa.sort.txt");
        g.info();
        g.viewSorting(false,true);

    }

}
