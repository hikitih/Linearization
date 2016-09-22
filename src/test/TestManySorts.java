package test;


import graph.Graph;
import graph.GraphSaveLoad;
import graph.Permutation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;


public class TestManySorts {
    static Graph g;
    static Permutation mySorting = new Permutation();
    static long performance;

    public static StringBuilder show(String filename, String sortname) {
        //GraphSaveLoad.refCost = 1500;
        g = GraphSaveLoad.loadGFA(filename, "ref");
        if (g != null) {
            StringBuilder result = new StringBuilder(1000);
            performance = g.sorting(true,true,true,true);
            g.reloadVertices();
            result.append(filename.substring(31));
            Permutation sorted = new Permutation(g.getCount());
            sorted.setPermutation(GraphSaveLoad.loadSorting(sortname));
            sorted.setCutwidth(g.cutWidthNew(sorted.getPermutation()));
            result.append("\tvg\tReversing edges:\t");
            result.append(g.getRightToLeft());
            result.append("\tCut width:\t");
            result.append(String.format("%2.3f",sorted.getCutwidthAverage()));


            result.append("\tEades\tReversing edges:\t");
            result.append(g.getNumberOfReversingEdges());
            result.append("\tCut width:\t");
            mySorting.setPermutation(g.getSorting());
            mySorting.setCutwidth(g.cutWidthNew(mySorting.getPermutation()));
            result.append(String.format("%2.3f",mySorting.getCutwidthAverage()));

            result.append("\n");
            return result;
            //System.out.println("\n=====================================\n");
        }
        return null;
    }

    public static void main(String[] args) {

        File folder = new File("./src/test/small_data/good_data");
        File[] files = folder.listFiles();
        /*
        for (File file: files) {
            System.out.println(file.getName());
        }
        */

        HashSet<String> fileSet = new HashSet<>();
        for (File file: files){
            fileSet.add(file.getName());
        }
        int counter = 0;
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(System.getProperty("user.dir")
                        +"/src/test/small_data/good_data/test_vg_sort.txt"));
             BufferedWriter timeWriter = new BufferedWriter(
                     new FileWriter(System.getProperty("user.dir")
                             +"/src/test/small_data/good_data/test_vg_performance.txt"))){
            for (File file: files){
                String s1 = file.getName();
                if (s1.contains(".gfa.vgsort.txt")){
                    String s2 = s1.substring(0,s1.length()-11);
                    if (fileSet.contains(s2)){
                        s1 = "../../"+file.getParent()+"/"+s1;
                        s2 = "../../"+file.getParent()+"/"+s2;
                        //System.out.println(s1+" = and = "+s2);
                        if (show(s2,s1)!=null) {
                            bw.write(show(s2, s1).toString());
                            bw.flush();
                            timeWriter.write(s2.substring(31)+"\t"+performance+"\n");
                            timeWriter.flush();
                        }
                        System.out.println("Graph "+counter+": "+s2.substring(31));
                        counter++;
                    }
                }
            }
            bw.close();
            timeWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Total graphs sorted: "+counter);
    }

}
