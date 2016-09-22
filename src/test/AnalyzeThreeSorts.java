package test;

import java.io.*;
import java.util.*;

public class AnalyzeThreeSorts {
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
            ArrayList<Integer> allReversingEdges;
            ArrayList<Double> allAverageCutWidths;
            Metrics median;
            Metrics average;
            Metrics minimum;
            Metrics maximum;
            int count;

            TotalMetrics(int count, int reversingEdges, double averageCutWidth){
                average = new Metrics(reversingEdges,averageCutWidth);
                median = new Metrics(reversingEdges,averageCutWidth);
                minimum = new Metrics(reversingEdges,averageCutWidth);
                maximum = new Metrics(reversingEdges,averageCutWidth);
                allReversingEdges = new ArrayList<>();
                allAverageCutWidths = new ArrayList<>();
                this.count = count;
            }
            
            void update(int reversing, double cutWidth){
                this.average.reversingEdges = (this.average.reversingEdges*this.count + reversing)/(this.count+1);
                this.average.averageCutWidth = (this.average.averageCutWidth*this.count + cutWidth)/(this.count+1);
                if (this.minimum.reversingEdges > reversing) {
                    this.minimum.reversingEdges = reversing;
                }
                if (this.minimum.averageCutWidth > cutWidth) {
                    this.minimum.averageCutWidth = cutWidth;
                }
                if (this.maximum.reversingEdges < reversing) {
                    this.maximum.reversingEdges = reversing;
                }
                if (this.maximum.averageCutWidth < cutWidth) {
                    this.maximum.averageCutWidth = cutWidth;
                }
                allReversingEdges.add(reversing);
                allAverageCutWidths.add(cutWidth);
                this.count++;
            }

            void setMedian(){
                Collections.sort(allReversingEdges);
                Collections.sort(allAverageCutWidths);
                
                int size = allReversingEdges.size();
                if (size%2==0){
                    median.reversingEdges = (allReversingEdges.get(size/2) + allReversingEdges.get(size/2 - 1) )/2;
                } else {
                    median.reversingEdges = allReversingEdges.get(size/2);
                }
                
                size = allAverageCutWidths.size();
                if (size%2==0){
                    median.averageCutWidth = (allAverageCutWidths.get(size/2) + allAverageCutWidths.get(size/2 - 1) )/2;
                } else {
                    median.averageCutWidth = allAverageCutWidths.get(size/2);
                }
            }
        }

        HashMap<String,TotalMetrics> totalEades = new HashMap<>(50);    //my total
        HashMap<String,TotalMetrics> totalHussler = new HashMap<>(50);  //their total
        HashMap<String,TotalMetrics> totalVg = new HashMap<>(50);       //random sort
        HashMap<String,TotalMetrics> totalReallyGood = new HashMap<>(50);       //really good Eades

        //read and write results into HashMap
        try (BufferedReader br = new BufferedReader(
                new FileReader(System.getProperty("user.dir")
                        +"/src/test/wwpaths/good_data/vg_vs_eades_comparison.txt"))) {
            Scanner scan = new Scanner(br);
            String s;
            String name = "";
            Scanner scanner;
            int reversing;
            double cutWidth;
            while (scan.hasNextLine()){
                s = scan.nextLine();
                //name = s.contains("Z") ? "ref" : "paths";
                if (s.contains("Z")){
                    name = "ref";
                } else {
                    name = "paths";
                }
                scanner = new Scanner(s).useDelimiter("\\t");
                Integer.parseInt(scanner.findInLine("\\d+"));
                name += scanner.findInLine(".+\\.gfa");
                /*
                vg
                 */
                scanner.findInLine("edges");
                reversing = Integer.parseInt(scanner.findInLine("\\d+"));
                cutWidth = Double.parseDouble(scanner.findInLine("[\\d,.]+"));
                //System.out.println(reversing + " , " + cutWidth);
                if (totalVg.containsKey(name)){
                    TotalMetrics tm = totalVg.get(name);
                    tm.update(reversing,cutWidth);
                    totalVg.put(name,tm);
                } else {
                    TotalMetrics tm = new TotalMetrics(1,reversing,cutWidth);
                    totalVg.put(name,tm);
                }
                /*
                Eades
                 */
                scanner.findInLine("edges");
                reversing = Integer.parseInt(scanner.findInLine("\\d+"));
                cutWidth = Double.parseDouble(scanner.findInLine("[\\d,.]+"));
                //System.out.println(reversing + " , " + cutWidth);
                if (totalEades.containsKey(name)){
                    TotalMetrics tm = totalEades.get(name);
                    tm.update(reversing,cutWidth);
                    totalEades.put(name,tm);
                } else {
                    TotalMetrics tm = new TotalMetrics(1,reversing,cutWidth);
                    totalEades.put(name,tm);
                }
                /*
                Haussler
                 */
                /*
                scanner.findInLine("edges");
                reversing = Integer.parseInt(scanner.findInLine("\\d+"));
                cutWidth = Double.parseDouble(scanner.findInLine("[\\d,.]+"));
                //System.out.println(reversing + " , " + cutWidth);
                if (totalHussler.containsKey(name)){
                    TotalMetrics tm = totalHussler.get(name);
                    tm.update(reversing,cutWidth);
                    totalHussler.put(name,tm);
                } else {
                    TotalMetrics tm = new TotalMetrics(1,reversing,cutWidth);
                    totalHussler.put(name,tm);
                }
                /*
                Really good sorting
                 */
                scanner.findInLine("edges");
                reversing = Integer.parseInt(scanner.findInLine("\\d+"));
                cutWidth = Double.parseDouble(scanner.findInLine("[\\d,.]+"));
                //System.out.println(reversing + " , " + cutWidth);
                if (totalReallyGood.containsKey(name)){
                    TotalMetrics tm = totalReallyGood.get(name);
                    tm.update(reversing,cutWidth);
                    totalReallyGood.put(name,tm);
                } else {
                    TotalMetrics tm = new TotalMetrics(1,reversing,cutWidth);
                    totalReallyGood.put(name,tm);
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> unsorted = new ArrayList<>();
        unsorted.addAll(totalEades.keySet());
        TreeSet<String> semiSorted =  new TreeSet<>();
        semiSorted.addAll(unsorted);


        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(System.getProperty("user.dir")
                        +"/src/test/wwpaths/good_data/vg_vs_eades_results.csv"))) {
            //write from HashMap to console
            for (String name : semiSorted) {
                TotalMetrics tm1 = totalVg.get(name);
                TotalMetrics tm2 = totalEades.get(name);
                TotalMetrics tm3 = totalHussler.get(name);
                TotalMetrics tm4 = totalReallyGood.get(name);
                StringBuilder output = new StringBuilder(1000);
                if (tm1 != null && tm2 != null && /*tm3 != null &&*/ tm4 != null) {
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


                    output.append(",vg,Reversing edges,");
                    tm1.setMedian();
                    output.append(String.format("%2.3f", tm1.median.reversingEdges));
                    output.append(",Cutwidth,");
                    output.append(String.format("%2.3f", tm1.median.averageCutWidth));

                    output.append(",Eades,Reversing edges,");
                    tm2.setMedian();
                    output.append(String.format("%2.3f", tm2.median.reversingEdges));
                    output.append(",Cutwidth,");
                    output.append(String.format("%2.3f", tm2.median.averageCutWidth));

                    output.append(",Haussler,Reversing edges,");
                    tm3.setMedian();
                    output.append(String.format("%2.3f", tm3.median.reversingEdges));
                    output.append(",Cutwidth,");
                    output.append(String.format("%2.3f", tm3.median.averageCutWidth));

                    output.append(",ReallyGood,Reversing edges,");
                    tm4.setMedian();
                    output.append(String.format("%2.3f", tm4.median.reversingEdges));
                    output.append(",Cutwidth,");
                    output.append(String.format("%2.3f", tm4.median.averageCutWidth));

                    
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
