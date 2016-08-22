package test;

import graph.Graph;
import graph.GraphSaveLoad;
import graph.Permutation;

import java.util.ArrayList;

public class TestCactus {

    public static void main(String[] args){
        Graph g = GraphSaveLoad.loadGFA("cactus.gfa","ref");
        //g.info();
        Permutation p = new Permutation();

        if (g!=null) {
            System.out.println("Total vertices in graph: " + g.getCount());

            System.out.println("My sorting: ");
            g.sorting(true,true,true,true);
            g.viewSorting(true,true);

            p.setPermutation(g.getSorting());
            g.reloadVertices();
            p.setCutwidth(g.cutwidth(p.getPermutation()));
            p.viewPermutation(true);

/*
            System.out.println("\nSorting from file: ");
            Permutation sorted = new Permutation(g.getCount());
            sorted.setPermutation(GraphSaveLoad.loadSortedGFA("cactus.gfa"));
            System.out.println(sorted.length());
            System.out.println("Reversed edges: " + g.rightToLeft(sorted.getPermutation()));
            sorted.setCutwidth(g.cutwidth(sorted.getPermutation()));
            sorted.viewPermutation(true);
*/

            ArrayList<Integer> bubble = g.findBubble(g.getSorting());
            System.out.println("Here it is the bubbles: ");
            System.out.println(bubble.size());

            //System.out.println(bubble);

            //GraphSaveLoad.saveSorting("bubbles.txt",bubble);

            /*
            int bubbleNumber = 101;
            ArrayList<Integer> subBubble = new ArrayList<>(1000);
            for (Integer next: bubble){
                if (next!=-1){
                    subBubble.add(next);
                } else {
                    if (subBubble.size()>10) {
                        Graph subGraph = g.getSubGraph(subBubble);
                        //GraphSaveLoad.saveGFA("bubble_" + bubbleNumber + ".gfa", subGraph);
                        bubbleNumber++;
                    }
                    subBubble.clear();
                }
            }
            */

            /*
            int bubbleNumber = 0;
            ArrayList<Integer> subBubble = new ArrayList<>(1000);
            for (Integer next: bubble){
                if (next!=-1){
                    subBubble.add(next);
                    bubbleNumber++;
                } else {
                    if (subBubble.size()>10) {
                        Graph subGraph = g.getSubGraph(subBubble);
                        bubbleNumber++;
                        System.out.println(bubbleNumber+": "+subBubble.size()+" : ");
                    }
                    subBubble.clear();
                }
            }
            System.out.println(bubbleNumber);
            */

            //GraphSaveLoad.saveSorting("cactus_sorted5.txt",g.getSorting());
        }
    }

}
