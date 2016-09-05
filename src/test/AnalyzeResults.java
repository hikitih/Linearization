package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

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
        Metrics averageEades;
        Metrics averageHussler;

        //read and write results into HashMap
        try (BufferedReader br = new BufferedReader(
                new FileReader(System.getProperty("user.dir")
                        +"/src/test/new test data/results.txt"))) {
            Scanner scan = new Scanner(br);
            String s;
            String name = "";
            int lineCounter = 1;
            Scanner scanner;
            int reversing;
            double cutWidth;
            while (scan.hasNextLine()){
                s = scan.nextLine();
                switch (lineCounter % 4){
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

        //write from HashMap to console
        for (String name: totalEades.keySet()){
            TotalMetrics tm = totalEades.get(name);
            TotalMetrics tm2 = totalHussler.get(name);
            System.out.println(name);
            System.out.println("Average (Eades)");
            System.out.println("Reversing edges: "+tm.average.reversingEdges);
            System.out.println("Average cut width: "+tm.average.averageCutWidth);
            System.out.println("Minimum (Eades)");
            System.out.println("Reversing edges: "+tm.minimum.reversingEdges);
            System.out.println("Average cut width: "+tm.minimum.averageCutWidth);
            System.out.println("Maximum (Eades)");
            System.out.println("Reversing edges: "+tm.maximum.reversingEdges);
            System.out.println("Average cut width: "+tm.maximum.averageCutWidth);
            System.out.println("Average (Hussler)");
            System.out.println("Reversing edges: "+tm2.average.reversingEdges);
            System.out.println("Average cut width: "+tm2.average.averageCutWidth);
            System.out.println("Minimum (Hussler)");
            System.out.println("Reversing edges: "+tm2.minimum.reversingEdges);
            System.out.println("Average cut width: "+tm2.minimum.averageCutWidth);
            System.out.println("Maximum (Hussler)");
            System.out.println("Reversing edges: "+tm2.maximum.reversingEdges);
            System.out.println("Average cut width: "+tm2.maximum.averageCutWidth);
            System.out.println("===================================================");
        }
    }
}
