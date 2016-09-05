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
            //System.out.println(filename);
            //System.out.println("Total vertices in graph: " + g.getCount());
            //System.out.println("My sorting: ");
            performance = g.sorting(false,true,true,false);
            //g.viewSorting(true, false);
            g.reloadVertices();
            result.append(filename.substring(31));
            result.append("\n");
            result.append("Eades.\t");
            result.append("\tReversing edges:\t");
            result.append(g.getNumberOfReversingEdges());
            result.append("\tCut width:\t");

            mySorting.setPermutation(g.getSorting());
            mySorting.setCutwidth(g.cutWidthNew(mySorting.getPermutation()));
            //mySorting.viewPermutation(true);
            result.append(String.format("%2.3f",mySorting.getCutwidthAverage()));

            //System.out.println("\nSorting from file: ");
            Permutation sorted = new Permutation(g.getCount());
            sorted.setPermutation(GraphSaveLoad.loadSorting(sortname));
            sorted.setCutwidth(g.cutWidthNew(sorted.getPermutation()));
            //System.out.println("Vertices in graph: " + sorted.length());
            //System.out.println("Reversed edges: " + g.getRightToLeft());
            //System.out.println("Weight: " + g.getRightToLeftWeight());
            //sorted.viewPermutation(true);
            result.append("\nHaussler.\t");
            result.append("\tReversing edges:\t");
            result.append(g.getRightToLeft());
            result.append("\tCut width:\t");
            result.append(String.format("%2.3f",sorted.getCutwidthAverage()));
            result.append("\n=================\n");
            return result;
            //System.out.println("\n=====================================\n");
        }
        return null;
    }

    public static void main(String[] args) {

        File folder = new File("./src/test/new test data");
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
                        +"/src/test/new test data/results.txt"));
             BufferedWriter timeWriter = new BufferedWriter(
                     new FileWriter(System.getProperty("user.dir")
                             +"/src/test/new test data/performance.txt"))){
            for (File file: files){
                String s1 = file.getName();
                if (s1.contains(".gfa")){
                    String s2 = s1 + ".sort.txt";
                    if (fileSet.contains(s2)){
                        s1 = "../../"+file.getParent()+"/"+s1;
                        s2 = "../../"+file.getParent()+"/"+s2;
                        //System.out.println(s1+" = and = "+s2);
                        if (show(s1,s2)!=null) {
                            bw.write(show(s1, s2).toString());
                            bw.flush();
                            String time = ""+performance/60000+"m"+(performance % 60000)/1000+"."+(performance % 1000);
                            timeWriter.write(s1.substring(31)+"\t"+time+"\n");
                            timeWriter.flush();
                        }
                        System.out.println("Graph "+counter+": "+s1);
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
