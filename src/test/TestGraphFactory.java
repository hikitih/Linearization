package test;

import graph.Graph;
import graph.GraphFactory;

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

        //graphFactory.setParameters(20,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,6,10);

        graphFactory.makeGraph(true);
        Graph g = graphFactory.getGraph();
        g.info();
        g.sorting();
        g.viewSorting();

        /*
        graphFactory.makeGraph();
        g = graphFactory.getGraph();
        g.info();
        g.sorting();
        g.viewSorting();
        */
    }
}
