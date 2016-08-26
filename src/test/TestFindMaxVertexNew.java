package test;

import graph.Graph;
import graph.GraphSaveLoad;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class TestFindMaxVertexNew {

    public static void main(String[] args) {
        Date date = new Date();
        Random random = new Random(date.getTime());
        long startTime = System.currentTimeMillis();
        long currTime;
        int k = 5;
        int p = 101;

        System.out.println("Starting the construction of the graph...");

        Graph g = new Graph(k * p);

        System.out.println("Time elapsed: " + (System.currentTimeMillis() - startTime));
        System.out.println("Adding edges...");

        ArrayList<Integer> path = new ArrayList<>();
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < k; j++) {
                for (int jj = j + 1; jj < k; jj++) {
                    if (random.nextBoolean()) {
                        g.addEdge(k * i + j + 1, k * i + jj + 1);
                    } else {
                        g.addEdge(k * i + jj + 1, k * i + j + 1);
                    }
                }
            }
            if (i < p - 1) {
                g.addEdge(k * i + 1, k * (i + 1) + 1);
            }
            path.add(k * i + 1);
        }

        int numberOfEdges = g.getEdgeCount();
        GraphSaveLoad.saveGFA("random_graph_" + k * p + "_" + numberOfEdges + ".gfa", g, path);

        System.out.println();
        System.out.println("Time to construct the graph: " + (System.currentTimeMillis() - startTime) / 1000);
        System.out.println();
        System.out.println("Vertices in graph: " + g.getCount());
        System.out.println("Edges in graph: " + g.getEdgeCount());

        //g.info();
        currTime = System.currentTimeMillis();

        g.sorting(true, true, true, false);
        g.viewSorting(true);

        System.out.println();
        System.out.println("Time to sort(sec): " + (System.currentTimeMillis() - currTime) / 1000);
        System.out.println(g.getNumberOfReversingEdges());
        //g.outputSorting();

    }
}
