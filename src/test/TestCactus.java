package test;


import graph.Graph;
import graph.GraphSaveLoad;
import graph.Permutation;

public class TestCactus {

    public static void main(String[] args){
        Graph g = GraphSaveLoad.loadGFA("cactus.gfa","ref");
        //g.info();
        g.vertexInfo(97764);
        //g.vertexInfo(248981);
        Permutation p = new Permutation();

        if (g!=null) {
            System.out.println("Total vertices in graph: " + g.getCount());

            System.out.println("My sorting: ");
            g.sorting(true,true,true,false);
            System.out.println("Reversed edges: " + g.getNumberOfReversingEdges());

            p.setPermutation(g.getSorting());
            g.reloadVertices();
            p.setCutwidth(g.cutwidth(p.getPermutation()));
            p.viewPermutation(true);

            //g.outputSorting();
            //GraphSaveLoad.saveSorting("cactus_sorted.txt",g.getSorting());
        }
    }

}
