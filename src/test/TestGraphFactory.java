package test;

import graph.Graph;
import graph.GraphFactory;
import graph.GraphSaveLoad;
import graph.Permutation;

import java.util.ArrayList;
import java.util.Collections;

public class TestGraphFactory {
    public static void main(String[] args) {
        GraphFactory graphFactory = new GraphFactory();

        /*
        graphFactory.makeBackbone(25);

        graphFactory.addVariation(GraphFactory.VariationType.DELETION,10);
        graphFactory.addVariation(GraphFactory.VariationType.DELETION,4);
        graphFactory.addVariation(GraphFactory.VariationType.INSERTION,3);
        graphFactory.addVariation(GraphFactory.VariationType.INSERTION,5);
        graphFactory.addVariation(GraphFactory.VariationType.DUPLICATION,6);
        graphFactory.addVariation(GraphFactory.VariationType.DUPLICATION,7);
        for (int i=0; i<10; i++) {
            graphFactory.addVariation(GraphFactory.VariationType.SNP);
        }
        */

        int backboneLength = 1000000;
        ArrayList<Integer> path = new ArrayList<>(backboneLength);
        for (int i = 1; i <= backboneLength; i++) {
            path.add(i);
        }
        graphFactory.setParameters(backboneLength,
                8, 150, 30, 30,   //MOBILE_ELEMENT
                80, 10, 100, 5000, //LARGE DELETION
                0, 10, 20,      //INVERSION
                30, 100, 10000,     //DUPLICATION
                60, 100, 2, 20,   //INSERTION
                60, 100, 2, 20,   //SHORT DELETION
                5, 1000);        //SNP

        graphFactory.makeGraph(false);
        Graph g = graphFactory.getGraph();
        //g.info();
        System.out.println("Vertices in graph: "+g.getCount());
        g.sorting();
        g.viewSorting(true, false);
        g.reloadVertices();
        Permutation p = new Permutation();
        p.setPermutation(g.getSorting());
        p.setCutwidth(g.cutWidthNew(p.getPermutation()));
        p.viewPermutation(true);

        System.out.println("Saving...");

        //GraphSaveLoad.saveGFA("human_divided_by_thousand.gfa",g,path);
        //GraphSaveLoad.saveGFA("biograph"+graphFactory.getParametersString()+".gfa",g,path);

        //GraphSaveLoad.saveSorting("biograph_sorted.txt",g.getSorting());

    }
}
