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

        int backboneLength = 200;
        ArrayList<Integer> path = new ArrayList<>(backboneLength);
        for (int i=1; i<=backboneLength; i++){
            path.add(i);
        }
        graphFactory.setParameters(backboneLength,
                0,1,5,8,//MOBILE_ELEMENT
                0,0,0,  //LARGE DELETION
                2,10,20,  //INVERSION
                15,3,8,  //DUPLICATION
                15,1,4,  //INSERTION
                17,1,4,  //SHORT DELETION
                40);     //SNP

        graphFactory.makeGraph(true);
        Graph g = graphFactory.getGraph();
        g.info();
        g.sorting();
        g.viewSorting(false,true);
        g.reloadVertices();
        Permutation p = new Permutation();
        p.setPermutation(g.getSorting());
        p.setCutwidth(g.cutWidthNew(p.getPermutation()));
        p.viewPermutation(true);

        //GraphSaveLoad.saveGFA("biograph.gfa",g,path);

        //GraphSaveLoad.saveSorting("biograph_sorted.txt",g.getSorting());

    }
}
