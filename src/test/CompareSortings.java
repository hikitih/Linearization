package test;


import graph.Graph;
import graph.GraphSaveLoad;
import graph.Permutation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class CompareSortings {
    static Graph g;

    public static StringBuilder show(String filename, String sortvg, String sortEades, String sortHaussler) {
        g = GraphSaveLoad.loadGFA(filename, "ref");
        if (g != null) {
            StringBuilder result = new StringBuilder(1000);
            result.append(filename.substring(31));

            Permutation sorted2 = new Permutation(g.getCount());
            sorted2.setPermutation(GraphSaveLoad.loadSorting(sortvg));
            sorted2.setCutwidth(g.cutWidthNew(sorted2.getPermutation()));
            result.append("\tvg.....\tReversing edges:\t");
            result.append(g.getRightToLeft());
            result.append("\tCut width:\t");
            result.append(String.format("%2.3f",sorted2.getCutwidthAverage()));

            Permutation sorted1 = new Permutation(g.getCount());
            sorted1.setPermutation(GraphSaveLoad.loadSorting(sortEades));
            sorted1.setCutwidth(g.cutWidthNew(sorted1.getPermutation()));
            result.append("\tEades\tReversing edges\t");
            result.append(g.getRightToLeft());
            result.append("\tCut width\t");
            result.append(String.format("%2.3f",sorted1.getCutwidthAverage()));

            /*
            Permutation sorted3 = new Permutation(g.getCount());
            sorted3.setPermutation(GraphSaveLoad.loadSorting(sortHaussler));
            sorted3.setCutwidth(g.cutWidthNew(sorted3.getPermutation()));
            result.append("\tHaussler\tReversing edges\t");
            result.append(g.getRightToLeft());
            result.append("\tCut width\t");
            result.append(String.format("%2.3f",sorted3.getCutwidthAverage()));
            */

            Permutation mySorting = new Permutation(g.getCount());
            g.sorting();
            g.reloadVertices();
            result.append("\tReally good sorting\tReversing edges:\t");
            result.append(g.getNumberOfReversingEdges());
            result.append("\tCut width:\t");
            mySorting.setPermutation(g.getSorting());
            mySorting.setCutwidth(g.cutWidthNew(mySorting.getPermutation()));
            result.append(String.format("%2.3f",mySorting.getCutwidthAverage()));

            result.append("\n");

            return result;
        }
        return null;
    }

    public static void main(String[] args) {

        File folder = new File("./src/test/wwpaths/good_data");
        File[] files = folder.listFiles();

        HashSet<String> fileSet = new HashSet<>();
        for (File file: files){
            fileSet.add(file.getName());
        }
        int counter = 0;
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(System.getProperty("user.dir")
                        +"/src/test/wwpaths/good_data/vg_vs_eades_comparison.txt"))){
            for (File file: files){
                String sortvg = file.getName();
                if (sortvg.contains(".gfa.vgsort.txt")){                            //vg sort
                    String gfaname = sortvg.substring(0,sortvg.length()-11);        //.gfa
                    String sortEades = gfaname+".fsort.txt";                        //Eades
                    String sortHaussler = gfaname+".sort.txt";                      //Haussler
                    if (fileSet.contains(gfaname) /*&& fileSet.contains(sortname2)*/){
                        sortEades = "../../"+file.getParent()+"/"+sortEades;
                        gfaname = "../../"+file.getParent()+"/"+gfaname;
                        sortvg = "../../"+file.getParent()+"/"+sortvg;
                        sortHaussler = "../../"+file.getParent()+"/"+sortHaussler;
                        StringBuilder result = show(gfaname,sortvg,sortEades,sortHaussler);
                        if (result!=null) {
                            bw.write(result.toString());
                            bw.flush();
                        }
                        System.out.println("Graph "+counter+": "+gfaname.substring(30));
                        counter++;
                    }
                }
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Total graphs sorted: "+counter);
    }
}
