package test;

import graph.Graph;
import graph.GraphSaveLoad;
import graph.Permutation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class TestCactus {

    public static void main(String[] args){
        Graph g = GraphSaveLoad.loadGFA("cactus.gfa","ref");
        //g.info();
        Permutation p = new Permutation();

        if (g!=null) {
            System.out.println("Total vertices in graph: " + g.getCount());

            System.out.println("My sorting: ");
            g.sorting(true,true,true,false);
            g.viewSorting(true,true);

            p.setPermutation(g.getSorting());
            g.reloadVertices();
            p.setCutwidth(g.cutWidthNew(p.getPermutation()));
            p.viewPermutation(true);

            System.out.println("\nSorting from GFA: ");
            Permutation sorted = new Permutation(g.getCount());
            sorted.setPermutation(GraphSaveLoad.loadSortedGFA("cactus.gfa"));
            sorted.setCutwidth(g.cutWidthNew(sorted.getPermutation()));
            System.out.println(sorted.length());
            System.out.println("Reversed edges: " + g.getRightToLeft());
            System.out.println("Weight: " + g.getRightToLeftWeight());
            sorted.viewPermutation(true);

            System.out.println("\nSorting from file: ");
            sorted = new Permutation(g.getCount());
            sorted.setPermutation(GraphSaveLoad.loadSorting("sort_order.txt"));

            /*
            System.out.println("\nOld: ");
            System.out.println(sorted.length());
            sorted.setCutwidth(g.cutWidthNew(sorted.getPermutation()));
            System.out.println("Reversed edges: " + g.getRightToLeft());
            System.out.println("Weight: " + g.getRightToLeftWeight());
            sorted.viewPermutation(true);
            //g.viewSorting(true,true);
            */

            System.out.println("\nNew: ");
            HashSet<Integer> sortedSet = new HashSet<>(sorted.length());
            sortedSet.addAll(sorted.getPermutation());
            System.out.println(sortedSet.size());
            Graph gg = g.getSubGraph(sortedSet);
            //g.reloadVertices();
            sorted.setCutwidth(gg.cutWidthNew(sorted.getPermutation()));
            System.out.println("Reversed edges: " + gg.getRightToLeft());
            System.out.println("Weight: " + gg.getRightToLeftWeight());
            sorted.viewPermutation(true);
            //gg.viewSorting(true,true);


            /*
            ArrayList<Integer> co = new ArrayList<>(g.getCount()+1);
            for (int i = 1; i <= g.getCount(); i++) {
                co.add(0);
            }
            for (Integer next: sorted.getPermutation()){
                co.set(next,co.get(next)+1);
            }
            for (int i = 1; i <= g.getCount() ; i++) {
                if (co.get(i)>1){
                    System.out.println(i);
                }
            }
            */


            /*
            HashSet<Integer> missedNodes = new HashSet<>();
            missedNodes.addAll(g.getSorting());
            missedNodes.removeAll(sorted.getPermutation());
            System.out.println("Missed nodes: ");
            System.out.println(missedNodes);
            */

            //GraphSaveLoad.saveSorting("my_sorted_cactus.txt",g.getSorting());
            //GraphSaveLoad.saveSorting("haussler_sorted_cactus.txt",sorted.getPermutation());

            /*
            System.out.println("\nTest sorting for different number of nodes: ");
            sorted = new Permutation(g.getCount());
            ArrayList<Integer> testSorting = new ArrayList<>();
            for (int i=110; i<119; i++){
                testSorting.add(i);
            }
            sorted.setPermutation(testSorting);
            sorted.setCutwidth(g.cutWidthNew(sorted.getPermutation()));
            System.out.println(sorted.length());
            System.out.println("Reversed edges: " + g.getRightToLeft());
            System.out.println("Weight: " + g.getRightToLeftWeight());
            sorted.viewPermutation(true);
            g.viewSorting(true,true);
            */

            /*
            Graph subg = g.getSubGraph(testSorting);
            subg.info();
            sorted.setCutwidth(subg.cutWidthNew(sorted.getPermutation()));
            System.out.println(sorted.length());
            System.out.println("Reversed edges: " + subg.getRightToLeft());
            System.out.println("Weight: " + subg.getRightToLeftWeight());
            sorted.viewPermutation(true);
            */
            /*
            ArrayList<Integer> bubble = g.findBubble(g.getSorting());
            System.out.println("Here it is the bubbles: ");
            System.out.println(bubble.size());
            */

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
