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
                numberOfME, 6, 30, 30,   //MOBILE_ELEMENT
                numberOfLD, 4, 45, 500,  //LARGE DELETION
                0, 10, 20,      //INVERSION
                numberOfDU, 3, 1000,     //DUPLICATION
                numberOfINDEL, 10, 2, 20,   //INSERTION
                numberOfINDEL, 10, 2, 20,   //SHORT DELETION
                numberOfSNP, 100);        //SNP
        for (int number = 1; number<= numberOfGraphs; number++) {
            graphFactory.makeGraph(false);
            Graph g = graphFactory.getGraph();
            System.out.println("Vertices in graph: " + g.getCount());
            GraphSaveLoad.saveGFA("new test data/biograph" + number + graphFactory.getParametersString() + ".gfa", g, path);
        }
    }

    public static void main(String[] args) {
        graphFactory = new GraphFactory();

        backboneLength = 8000;
        numberOfGraphs = 100;
        ArrayList<Integer> path = new ArrayList<>(backboneLength);
        for (int i = 1; i <= backboneLength; i++) {
            path.add(i);
        }
        //generating base test set

        generateTestSet(2,2,3,3,3,path);
        generateTestSet(4,4,6,6,6,path);
        generateTestSet(6,6,9,9,9,path);
        generateTestSet(8,8,12,12,12,path);
        generateTestSet(10,10,15,15,15,path);
        generateTestSet(12,12,18,18,18,path);
        generateTestSet(14,14,21,21,21,path);
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
    }
}
