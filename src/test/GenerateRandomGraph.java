package test;

import graph.Graph;
import graph.GraphSaveLoad;
import graph.Permutation;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class GenerateRandomGraph {

    public static void main(String[] args) {
        Date date = new Date();
        Random random = new Random(date.getTime());

        int n = 20;
        int m = 80;
        int repeat = 10000;

        Permutation p = new Permutation(n);

        int num = 0;
        int edgeNum = 0;
        int cut = 0;
        for (int launch=0;launch<repeat;launch++) {
            Graph g = new Graph(n);

            for (int i = 0; i < n; i++) {
                g.addVertex(i);
            }

            for (int j = 0; j < m; j++) {
                int v1 = random.nextInt(n);
                int v2 = random.nextInt(n);

                if (!g.hasEdge(v1, v2) && !g.hasEdge(v2, v1)) {
                    g.addEdge(v1, v2);
                }
            }

            //GraphSaveLoad.saveGFA("random_graph_"+n+"_"+m+".gfa",g);

            g.sorting(true, false, true, true);
            g.reloadVertices();
            num += g.getNumberOfFindMaxVertex();
            edgeNum += g.getEdgeCount();
            cut += g.cutwidth(g.getSorting());
            //g.viewSorting();
        }
        System.out.println("\nGraph with " + n + " vertices and (in average) " + (double) edgeNum/repeat + " edges." +
                "\nAverage number of find max vertex procedure launches is " + (double) num/repeat +
                "\nAverage cutwidth is " + (double) cut/(repeat*(n-1)));
    }

}
