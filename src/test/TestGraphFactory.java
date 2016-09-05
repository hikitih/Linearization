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

        int backboneLength = 256000;
        int number = 1;
        ArrayList<Integer> path = new ArrayList<>(backboneLength);
        for (int i = 1; i <= backboneLength; i++) {
            path.add(i);
        }
        graphFactory.setParameters(backboneLength,
                64, 6, 30, 30,   //MOBILE_ELEMENT
                64, 4, 45, 500, //LARGE DELETION
                0, 10, 20,      //INVERSION
                96, 3, 1000,     //DUPLICATION
                96, 10, 2, 20,   //INSERTION
                96, 10, 2, 20,   //SHORT DELETION
                96, 100);        //SNP

        graphFactory.makeGraph(false);
        Graph g = graphFactory.getGraph();
        //g.info();
        System.out.println("Vertices in graph: "+g.getCount());
        g.sorting();
        //g.viewSorting(true, false);
        g.reloadVertices();
        Permutation p = new Permutation();
        p.setPermutation(g.getSorting());
        p.setCutwidth(g.cutWidthNew(p.getPermutation()));
        p.viewPermutation(true);

        System.out.println("Saving...");
        //GraphSaveLoad.saveGFA("fortest"+graphFactory.getParametersString()+".gfa",g,path);

        //GraphSaveLoad.saveGFA("human_divided_by_thousand.gfa",g,path);
        GraphSaveLoad.saveGFA("new test data/big graphs/biograph"+number+graphFactory.getParametersString()+".gfa",g,path/*,graphFactory.getPaths()*/);
        //System.out.println(graphFactory.getPaths().size());

        //GraphSaveLoad.saveSorting("biograph_sorted.txt",g.getSorting());

    }
}
