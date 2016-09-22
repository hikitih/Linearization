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

        int backboneLength = 30;
        int number = 1; //100
        ArrayList<Integer> path = new ArrayList<>(backboneLength);
        for (int i = 1; i <= backboneLength; i++) {
            path.add(i);
        }
        graphFactory.setParameters(backboneLength,
                0, 4, 30, 30,   //MOBILE_ELEMENT
                0, 4, 30, 100, //LARGE DELETION
                0, 10, 20,      //INVERSION
                2, 3, 10,     //DUPLICATION
                3, 1, 2, 5,   //INSERTION
                3, 1, 2, 5,   //SHORT DELETION
                5, 2);        //SNP

        //g.info();
        int feedbackArcs1 = 0;
        double cutwidth1 = 0d;
        int feedbackArcs2 = 0;
        double cutwidth2 = 0d;
        for (int i=0; i<number; i++) {
            graphFactory.makeGraph(false);
            Graph g = graphFactory.getGraph();
            System.out.println("Vertices in graph: " + g.getCount());
            g.sorting();

            feedbackArcs1 += g.getNumberOfReversingEdges();


            g.viewSorting(true, false);
            g.reloadVertices();
            Permutation p = new Permutation();
            p.setPermutation(g.getSorting());
            p.setCutwidth(g.cutWidthNew(p.getPermutation()));
            p.viewPermutation(true);

            cutwidth1 += p.getCutwidthAverage();

            System.out.println("Saving...");
            System.out.println("Paths: " + graphFactory.getPaths().size());
            //GraphSaveLoad.saveGFA("new test data/with paths/for_stas/biograph" + i + graphFactory.getParametersString() + ".gfa", g, path, graphFactory.getPaths());
            GraphSaveLoad.saveGFA("new test data/with paths/for_stas/with_paths_2" + graphFactory.getParametersString() + ".gfa", g, path, graphFactory.getPaths());
            GraphSaveLoad.saveGFA("new test data/with paths/for_stas/reference_only_2" + graphFactory.getParametersString() + ".gfa", g, path/*, graphFactory.getPaths()*/);

            Graph loaded = GraphSaveLoad.loadGFA("new test data/with paths/for_stas/with_paths_2" + graphFactory.getParametersString() + ".gfa", "ref");

            loaded.sorting();
            feedbackArcs2 += loaded.getNumberOfReversingEdges();


            loaded.viewSorting(true, false);
            loaded.reloadVertices();
            p.setPermutation(loaded.getSorting());
            p.setCutwidth(loaded.cutWidthNew(p.getPermutation()));
            p.viewPermutation(true);

            cutwidth2 += p.getCutwidthAverage();


            loaded = GraphSaveLoad.loadGFA("new test data/with paths/for_stas/reference_only_2" + graphFactory.getParametersString() + ".gfa", "ref");
            loaded.sorting();
            loaded.viewSorting(true, false);
            loaded.reloadVertices();
            p.setPermutation(loaded.getSorting());
            p.setCutwidth(loaded.cutWidthNew(p.getPermutation()));
            p.viewPermutation(true);
            cutwidth1 = p.getCutwidthAverage();
            feedbackArcs1 = loaded.getNumberOfReversingEdges();


            //loaded.info();
        }

        System.out.println(feedbackArcs1/number + " vs " + feedbackArcs2/number);
        System.out.println(cutwidth1/number + " vs " + cutwidth2/number);

        //GraphSaveLoad.saveGFA("fortest"+graphFactory.getParametersString()+".gfa",g,path);

        //GraphSaveLoad.saveGFA("human_divided_by_thousand.gfa",g,path);
        //System.out.println(graphFactory.getPaths().size());

        //GraphSaveLoad.saveSorting("biograph_sorted.txt",g.getSorting());

    }
}
