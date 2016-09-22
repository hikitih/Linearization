package test;

import java.io.*;
import java.util.*;

public class AnalyzeResults {
    public static void main(String[] args) {
        class Metrics{
            double reversingEdges;
            double averageCutWidth;

            Metrics(int reversingEdges, double averageCutWidth) {
                this.averageCutWidth = averageCutWidth;
                this.reversingEdges = reversingEdges;
            }
        }

        class TotalMetrics{
            Metrics average;
            Metrics minimum;
            Metrics maximum;
            int count;

            TotalMetrics(int count, int reversingEdges, double averageCutWidth){
                average = new Metrics(reversingEdges,averageCutWidth);
                minimum = new Metrics(reversingEdges,averageCutWidth);
                maximum = new Metrics(reversingEdges,averageCutWidth);
                this.count = count;
            }
        }

        HashMap<String,TotalMetrics> totalEades = new HashMap<>(50);    //my total
        HashMap<String,TotalMetrics> totalHussler = new HashMap<>(50);  //their total
        HashMap<String,TotalMetrics> totalVg = new HashMap<>(50);       //random sort
        Metrics averageEades;
        Metrics averageHussler;

        //read and write results into HashMap
        try (BufferedReader br = new BufferedReader(
                new FileReader(System.getProperty("user.dir")
                        +"/src/test/new test data/three_algorithms_comparison.txt"))) {
            Scanner scan = new Scanner(br);
            String s;
            String name = "";
            int lineCounter = 1;
            Scanner scanner;
            int reversing;
            double cutWidth;
            while (scan.hasNextLine()){
                s = scan.nextLine();
                switch (lineCounter % 5){
                    case 1:
                        scanner = new Scanner(s).useDelimiter("\\t");
                        int num = Integer.parseInt(scanner.findInLine("\\d+"));
                        if (num>99) {
                            name = s.substring(12);
                        } else if (num>9){
                            name = s.substring(11);
                        } else {
                            name = s.substring(10);
                        }
                        break;
                    case 2:
                        scanner = new Scanner(s).useDelimiter("\\t");
                        reversing = Integer.parseInt(scanner.findInLine("\\d+"));
                        cutWidth = Double.parseDouble(scanner.findInLine("[\\d,.]+"));
                        //System.out.println(reversing + " , " + cutWidth);
                        if (totalEades.containsKey(name)){
                            TotalMetrics tm = totalEades.get(name);
                            tm.average.reversingEdges = (tm.average.reversingEdges*tm.count + reversing)/(tm.count+1);
                            tm.average.averageCutWidth = (tm.average.averageCutWidth*tm.count + cutWidth)/(tm.count+1);
                            if (tm.minimum.reversingEdges > reversing) {
                                tm.minimum.reversingEdges = reversing;
                            }
                            if (tm.minimum.averageCutWidth > cutWidth) {
                                tm.minimum.averageCutWidth = cutWidth;
                            }
                            if (tm.maximum.reversingEdges < reversing) {
                                tm.maximum.reversingEdges = reversing;
                            }
                            if (tm.maximum.averageCutWidth < cutWidth) {
                                tm.maximum.averageCutWidth = cutWidth;
                            }
                            tm.count++;
                            totalEades.put(name,tm);
                        } else {
                            TotalMetrics tm = new TotalMetrics(1,reversing,cutWidth);
                            totalEades.put(name,tm);
                        }
                        break;
                    case 3:
                        scanner = new Scanner(s).useDelimiter("\\t");
                        reversing = Integer.parseInt(scanner.findInLine("\\d+"));
                        cutWidth = Double.parseDouble(scanner.findInLine("[\\d,.]+"));
                        //System.out.println(reversing + " , " + cutWidth);
                        if (totalVg.containsKey(name)){
                            TotalMetrics tm = totalVg.get(name);
                            tm.average.reversingEdges = (tm.average.reversingEdges*tm.count + reversing)/(tm.count+1);
                            tm.average.averageCutWidth = (tm.average.averageCutWidth*tm.count + cutWidth)/(tm.count+1);
                            if (tm.minimum.reversingEdges > reversing) {
                                tm.minimum.reversingEdges = reversing;
                            }
                            if (tm.minimum.averageCutWidth > cutWidth) {
                                tm.minimum.averageCutWidth = cutWidth;
                            }
                            if (tm.maximum.reversingEdges < reversing) {
                                tm.maximum.reversingEdges = reversing;
                            }
                            if (tm.maximum.averageCutWidth < cutWidth) {
                                tm.maximum.averageCutWidth = cutWidth;
                            }
                            tm.count++;
                            totalVg.put(name,tm);
                        } else {
                            TotalMetrics tm = new TotalMetrics(1,reversing,cutWidth);
                            totalVg.put(name,tm);
                        }
                        break;
                    case 4:
                        scanner = new Scanner(s).useDelimiter("\\t");
                        reversing = Integer.parseInt(scanner.findInLine("\\d+"));
                        cutWidth = Double.parseDouble(scanner.findInLine("[\\d,.]+"));
                        //System.out.println(reversing + " , " + cutWidth);
                        if (totalHussler.containsKey(name)){
                            TotalMetrics tm = totalHussler.get(name);
                            tm.average.reversingEdges = (tm.average.reversingEdges*tm.count + reversing)/(tm.count+1);
                            tm.average.averageCutWidth = (tm.average.averageCutWidth*tm.count + cutWidth)/(tm.count+1);
                            if (tm.minimum.reversingEdges > reversing) {
                                tm.minimum.reversingEdges = reversing;
                            }
                            if (tm.minimum.averageCutWidth > cutWidth) {
                                tm.minimum.averageCutWidth = cutWidth;
                            }
                            if (tm.maximum.reversingEdges < reversing) {
                                tm.maximum.reversingEdges = reversing;
                            }
                            if (tm.maximum.averageCutWidth < cutWidth) {
                                tm.maximum.averageCutWidth = cutWidth;
                            }
                            tm.count++;
                            totalHussler.put(name,tm);
                        } else {
                            TotalMetrics tm = new TotalMetrics(1,reversing,cutWidth);
                            totalHussler.put(name,tm);
                        }
                        break;
                }

                lineCounter++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> unsorted = new ArrayList<>();
        unsorted.addAll(totalEades.keySet());
        TreeSet<String> semiSorted =  new TreeSet<String>(new Comparator<String>() {
            public int compare(String s1, String s2) {
                Scanner scan1 = new Scanner(s1).useDelimiter("_");
                Scanner scan2 = new Scanner(s2);
                int nmb1 = Integer.parseInt(scan1.findInLine("\\d+"));
                int me1 = Integer.parseInt(scan1.findInLine("\\d+"));
                int ld1 = Integer.parseInt(scan1.findInLine("\\d+"));
                int du1 = Integer.parseInt(scan1.findInLine("\\d+"));
                int indel1 = Integer.parseInt(scan1.findInLine("\\d+"));
                int snp1 = Integer.parseInt(scan1.findInLine("\\d+"));
                int nmb2 = Integer.parseInt(scan2.findInLine("\\d+"));
                int me2 = Integer.parseInt(scan2.findInLine("\\d+"));
                int ld2 = Integer.parseInt(scan2.findInLine("\\d+"));
                int du2 = Integer.parseInt(scan2.findInLine("\\d+"));
                int indel2 = Integer.parseInt(scan2.findInLine("\\d+"));
                int snp2 = Integer.parseInt(scan2.findInLine("\\d+"));

                if (nmb1 > nmb2) return 1;
                if (nmb1 < nmb2) return -1;
                if (snp1 > snp2) return 1;
                if (snp1 < snp2) return -1;
                if (indel1 > indel2) return 1;
                if (indel1 < indel2) return -1;
                if (du1 > du2) return 1;
                if (du1 < du2) return -1;
                if (ld1 > ld2) return 1;
                if (ld1 < ld2) return -1;
                if (me1 > me2) return 1;
                if (me1 < me2) return -1;
                return 0;
            }
        });

        for (String str: totalEades.keySet()){
            semiSorted.add(str);
        }

        /*
        semiSorted.clear();

        ArrayList<String> sorted = new ArrayList<>();
        for (String str: totalEades.keySet()){
            if (str.contains("_LD_8_DU_3_InDel_60_SNP_300")){
                semiSorted.add(str);
                if (!str.contains("ME_12_LD_8_DU_3_InDel_60_SNP_300")) {
                    unsorted.remove(str);
                }
            }
        }
        sorted.addAll(semiSorted);
        semiSorted.clear();
        for (String str: totalEades.keySet()){
            if (str.contains("ME_12") && str.contains("_DU_3_InDel_60_SNP_300")){
                semiSorted.add(str);
                if (!str.contains("ME_12_LD_8_DU_3_InDel_60_SNP_300")) {
                    unsorted.remove(str);
                }
            }
        }
        sorted.addAll(semiSorted);
        semiSorted.clear();
        for (String str: totalEades.keySet()){
            if (str.contains("ME_12_LD_8") && str.contains("_InDel_60_SNP_300")){
                semiSorted.add(str);
                if (!str.contains("ME_12_LD_8_DU_3_InDel_60_SNP_300")) {
                    unsorted.remove(str);
                }
            }
        }
        sorted.addAll(semiSorted);
        semiSorted.clear();
        for (String str: totalEades.keySet()){
            if (str.contains("ME_12_LD_8_DU_3") && str.contains("_SNP_300")){
                semiSorted.add(str);
                if (!str.contains("ME_12_LD_8_DU_3_InDel_60_SNP_300")) {
                    unsorted.remove(str);
                }
            }
        }
        sorted.addAll(semiSorted);
        semiSorted.clear();
        for (String str: totalEades.keySet()){
            if (str.contains("ME_12_LD_8_DU_3_InDel_60")){
                semiSorted.add(str);
                if (!str.contains("ME_12_LD_8_DU_3_InDel_60_SNP_300")) {
                    unsorted.remove(str);
                }
            }
        }
        sorted.addAll(semiSorted);
        semiSorted.clear();
        semiSorted.addAll(unsorted);
        sorted.addAll(semiSorted);

        */

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(System.getProperty("user.dir")
                        +"/src/test/new test data/three_algorithms_results.csv"))) {
            //write from HashMap to console
            for (String name : semiSorted) {
                TotalMetrics tm = totalEades.get(name);
                TotalMetrics tm2 = totalHussler.get(name);
                TotalMetrics tm3 = totalVg.get(name);
                StringBuilder output = new StringBuilder(1000);
                if (tm != null && tm2 != null && tm3 != null) {
                /*
                System.out.println(name);
                System.out.println("Average (Eades)");
                System.out.println("Reversing edges: " + tm.average.reversingEdges);
                System.out.println("Average cut width: " + tm.average.averageCutWidth);

                System.out.println("Average (vg)");
                System.out.println("Reversing edges: " + tm3.average.reversingEdges);
                System.out.println("Average cut width: " + tm3.average.averageCutWidth);

                System.out.println("Average (Haussler)");
                System.out.println("Reversing edges: " + tm2.average.reversingEdges);
                System.out.println("Average cut width: " + tm2.average.averageCutWidth);
                */
                    output.append(name);
                    output.append(",Eades,");
                    output.append("Reversing edges,");
                    output.append(String.format("%2.3f", tm.average.reversingEdges));
                    output.append(",Cutwidth,");
                    output.append(String.format("%2.3f", tm.average.averageCutWidth));
                    output.append(",vg,");
                    output.append("Reversing edges,");
                    output.append(String.format("%2.3f", tm3.average.reversingEdges));
                    output.append(",Cutwidth,");
                    output.append(String.format("%2.3f", tm3.average.averageCutWidth));
                    output.append(",Haussler,");
                    output.append("Reversing edges,");
                    output.append(String.format("%2.3f", tm2.average.reversingEdges));
                    output.append(",Cutwidth,");
                    output.append(String.format("%2.3f", tm2.average.averageCutWidth));
                    output.append("\n");

                    System.out.println(output.toString());
                    bw.write(output.toString());
                /*
                System.out.println("Minimum (Eades)");
                System.out.println("Reversing edges: " + tm.minimum.reversingEdges);
                System.out.println("Average cut width: " + tm.minimum.averageCutWidth);
                System.out.println("Minimum (Haussler)");
                System.out.println("Reversing edges: " + tm2.minimum.reversingEdges);
                System.out.println("Average cut width: " + tm2.minimum.averageCutWidth);

                System.out.println("Maximum (Eades)");
                System.out.println("Reversing edges: " + tm.maximum.reversingEdges);
                System.out.println("Average cut width: " + tm.maximum.averageCutWidth);
                System.out.println("Maximum (Haussler)");
                System.out.println("Reversing edges: " + tm2.maximum.reversingEdges);
                System.out.println("Average cut width: " + tm2.maximum.averageCutWidth);
                */
                }
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
