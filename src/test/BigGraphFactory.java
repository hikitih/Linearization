package test;

import graph.Graph;
import graph.GraphFactory;
import graph.GraphSaveLoad;
import graph.Permutation;

import java.util.ArrayList;

public class BigGraphFactory {
    static GraphFactory graphFactory;
    static int backboneLength;
    static int numberOfGraphs;

    static void generateTestSet(int numberOfME, int numberOfLD, int numberOfDU, int numberOfINDEL, int numberOfSNP, ArrayList<Integer> path){
        graphFactory.setParameters(backboneLength,
                numberOfME, 4, 30, 30,   //MOBILE_ELEMENT
                numberOfLD, 4, 30, 100,  //LARGE DELETION
                0, 10, 20,      //INVERSION
                numberOfDU, 3, 1000,     //DUPLICATION
                numberOfINDEL, 6, 2, 15,   //INSERTION
                numberOfINDEL, 6, 2, 15,   //SHORT DELETION
                10, numberOfSNP);        //SNP
        for (int number = 1; number<= numberOfGraphs; number++) {
            graphFactory.makeGraph(false);
            Graph g = graphFactory.getGraph();
            System.out.println("Vertices in graph: " + g.getCount());
            GraphSaveLoad.saveGFA("new test data/with paths/good_data/biograph" + number + graphFactory.getParametersString() + ".gfa", g, path, graphFactory.getPaths());
        }
    }

    static void generateAnotherTestSet(int numberOfME, int numberOfLD, int numberOfDU, int numberOfINDEL, int numberOfSNP, ArrayList<Integer> path){
        graphFactory.setParameters(backboneLength,
                numberOfME, 4, 30, 30,   //MOBILE_ELEMENT
                numberOfLD, 4, 30, 100,  //LARGE DELETION
                0, 10, 20,      //INVERSION
                numberOfDU, 3, 1000,     //DUPLICATION
                6, numberOfINDEL, 2, 15,   //INSERTION
                6, numberOfINDEL, 2, 15,   //SHORT DELETION
                10, numberOfSNP);        //SNP
        for (int number = 1; number<= numberOfGraphs; number++) {
            graphFactory.makeGraph(false);
            Graph g = graphFactory.getGraph();
            System.out.println("Vertices in graph: " + g.getCount());
            GraphSaveLoad.saveGFA("new test data/with paths/good_data/biograph_with_paths_" + number + graphFactory.getParametersString() + ".gfa", g, path, graphFactory.getPaths());
            GraphSaveLoad.saveGFA("new test data/with paths/good_data/Zbiograph_only_ref_" + number + graphFactory.getParametersString() + ".gfa", g, path/*, graphFactory.getPaths()*/);
        }
    }

    public static void main(String[] args) {
        graphFactory = new GraphFactory();

        backboneLength = 16000;
        numberOfGraphs = 40;
        ArrayList<Integer> path = new ArrayList<>(backboneLength);
        //generating base test set
        for (int i = 1; i <= backboneLength; i++) {
            path.add(i);
        }

        generateAnotherTestSet(2,2,5,10,30,path);
        generateAnotherTestSet(4,4,10,20,60,path);
        generateAnotherTestSet(6,6,15,30,90,path);
        generateAnotherTestSet(8,8,20,40,120,path);
        generateAnotherTestSet(10,10,25,50,150,path);
        generateAnotherTestSet(12,12,30,60,180,path);
        generateAnotherTestSet(14,14,35,70,210,path);
        generateAnotherTestSet(16,16,40,80,240,path);


        //for vg vs Eades comparison
        /*
        path.clear();
        for (int i = 1; i <= backboneLength; i++) {
            path.add(i);
        }
        generateAnotherTestSet(3,4,3,5,30,path);


        backboneLength = 16000;
        path.clear();
        for (int i = 1; i <= backboneLength; i++) {
            path.add(i);
        }
        generateAnotherTestSet(6,8,6,10,60,path);


        backboneLength = 32000;
        path.clear();
        for (int i = 1; i <= backboneLength; i++) {
            path.add(i);
        }
        generateAnotherTestSet(12,16,12,20,120,path);


        backboneLength = 64000;
        path.clear();
        for (int i = 1; i <= backboneLength; i++) {
            path.add(i);
        }
        generateAnotherTestSet(24,32,24,40,240,path);


        backboneLength = 128000;
        path.clear();
        for (int i = 1; i <= backboneLength; i++) {
            path.add(i);
        }
        generateAnotherTestSet(48,64,48,80,480,path);
        */
        /*
        //generating ME set
        generateTestSet(4,2,3,3,3,path);
        generateTestSet(8,2,3,3,3,path);
        generateTestSet(16,2,3,3,3,path);
        generateTestSet(32,2,3,3,3,path);
        generateTestSet(64,2,3,3,3,path);
        //generating LD set
        generateTestSet(2,4,3,3,3,path);
        generateTestSet(2,8,3,3,3,path);
        generateTestSet(2,16,3,3,3,path);
        generateTestSet(2,32,3,3,3,path);
        generateTestSet(2,64,3,3,3,path);

        //generating DU set
        generateTestSet(2,2,6,3,3,path);
        generateTestSet(2,2,12,3,3,path);
        generateTestSet(2,2,24,3,3,path);
        generateTestSet(2,2,48,3,3,path);

        generateTestSet(2,2,96,3,3,path);
        //generating INDEL set
        generateTestSet(2,2,3,6,3,path);
        generateTestSet(2,2,3,12,3,path);
        generateTestSet(2,2,3,24,3,path);
        generateTestSet(2,2,3,48,3,path);
        generateTestSet(2,2,3,96,3,path);
        //generating SNP set
        generateTestSet(2,2,3,3,6,path);
        generateTestSet(2,2,3,3,12,path);
        generateTestSet(2,2,3,3,24,path);
        generateTestSet(2,2,3,3,48,path);
        generateTestSet(2,2,3,3,96,path);
        */
    }
}
