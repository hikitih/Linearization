package test;

import graph.Graph;
import java.util.Date;
import java.util.Random;

public class TestFindMaxVertexNew {

    public static void main(String[] args){
        Date date = new Date();
        Random random = new Random(date.getTime());
        long startTime = System.currentTimeMillis();
        long currTime;
        int k = 50;
        int p = 10000;

        System.out.println("Starting the construction of the graph...");

        Graph g = new Graph(k*p);

        System.out.println("Time elapsed: "+(System.currentTimeMillis()-startTime));
        System.out.println("Adding edges...");

        for (int i=0; i<p; i++){
            for (int j=0; j<k; j++){
                for (int jj=j+1; jj<k; jj++){
                    if (random.nextBoolean()) {
                        g.addEdge(k * i + j, k * i + jj);
                    } else {
                        g.addEdge(k * i + jj, k * i + j);
                    }
                }
            }
        }

        System.out.println();
        System.out.println("Time to construct the graph: "+(System.currentTimeMillis()-startTime)/1000);
        System.out.println();
        System.out.println("Vertices in graph: "+g.getCount());
        System.out.println("Edges in graph: "+g.getEdgeCount());

        //g.info();
        currTime = System.currentTimeMillis();

        g.sorting(true,true,true,false);
        g.viewSorting(true);

        System.out.println();
        System.out.println("Time to sort(sec): "+(System.currentTimeMillis()-currTime)/1000);
        System.out.println(g.getNumberOfReversingEdges());
        //g.outputSorting();
    }
}
