package test;


import graph.Graph;
import graph.GraphSaveLoad;
import graph.Permutation;

import java.io.File;
import java.util.HashSet;


public class TestManySorts {
    static Graph g;
    static Permutation mySorting = new Permutation();



    public static void show(String filename, String sortname) {
        g = GraphSaveLoad.loadGFA(filename, "ref");
        if (g != null) {
            System.out.println(filename);
            System.out.println("Total vertices in graph: " + g.getCount());
            System.out.println("My sorting: ");
            g.sorting();
            g.viewSorting(true, false);
            g.reloadVertices();

            mySorting.setPermutation(g.getSorting());
            mySorting.setCutwidth(g.cutWidthNew(mySorting.getPermutation()));
            mySorting.viewPermutation(true);

            System.out.println("\nSorting from file: ");
            Permutation sorted = new Permutation(g.getCount());
            sorted.setPermutation(GraphSaveLoad.loadSorting(sortname));
            sorted.setCutwidth(g.cutWidthNew(sorted.getPermutation()));
            System.out.println("Vertices in graph: " + sorted.length());
            System.out.println("Reversed edges: " + g.getRightToLeft());
            System.out.println("Weight: " + g.getRightToLeftWeight());
            sorted.viewPermutation(true);
            System.out.println("\n=====================================\n");

        }
    }

    public static void main(String[] args) {

        /*
        show("biograph_200_ME_0_LD_10_DU_0_InDel_32_SNP_50.gfa", "biograph_200_ME_0_LD_10_DU_0_InDel_32_SNP_50.sort.txt");
        show("biograph_200_ME_3_LD_10_DU_10_InDel_32_SNP_40.gfa", "biograph_200_ME_3_LD_10_DU_10_InDel_32_SNP_40.sort.txt");
        show("biograph_200_ME_4_LD_10_DU_0_InDel_32_SNP_50.gfa", "biograph_200_ME_4_LD_10_DU_0_InDel_32_SNP_50.sort.txt");
        show("biograph_400_ME_2_LD_8_DU_30_InDel_32_SNP_80.gfa", "biograph_400_ME_2_LD_8_DU_30_InDel_32_SNP_80.sort.txt");
        show("biograph_400_ME_2_LD_9_DU_35_InDel_32_SNP_80.gfa", "biograph_400_ME_2_LD_9_DU_35_InDel_32_SNP_80.sort.txt");
        show("biograph_400_ME_2_LD_9_DU_36_InDel_38_SNP_70.gfa", "biograph_400_ME_2_LD_9_DU_36_InDel_38_SNP_70.sort.txt");
        show("biograph_400_ME_3_LD_10_DU_50_InDel_32_SNP_50.gfa", "biograph_400_ME_3_LD_10_DU_50_InDel_32_SNP_50.sort.txt");
        show("biograph_1000_ME_3_LD_9_DU_37_InDel_38_SNP_70.gfa", "biograph_1000_ME_3_LD_9_DU_37_InDel_38_SNP_70.sort.txt");
        show("biograph_10000_ME_3_LD_9_DU_37_InDel_38_SNP_70.gfa", "biograph_10000_ME_3_LD_9_DU_37_InDel_38_SNP_70.sort.txt");
        show("biograph_10000_ME_3_LD_9_DU_37_InDel_38_SNP_120.gfa", "biograph_10000_ME_3_LD_9_DU_37_InDel_38_SNP_120.sort.txt");
        */

        File folder = new File("./src/test/biotest");
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
        for (File file: files){
            String s1 = file.getName();
            if (s1.contains(".gfa")){
                String s2 = s1.substring(0,s1.length()-3)+"sort.txt";
                if (fileSet.contains(s2)){
                    s1 = "../../"+file.getParent()+"/"+s1;
                    s2 = "../../"+file.getParent()+"/"+s2;
                    System.out.println(s1+" = and = "+s2);
                    show(s1,s2);
                }
            }
        }


        /*
        show("biograph_200_ME_4_LD_10_DU_20_InDel_32_SNP_40.gfa","biograph_200_ME_0_LD_10_DU_0_InDel_32_SNP_50.sort.txt");
        g.info();
        g.viewSorting(false,true);
        */
    }

}
