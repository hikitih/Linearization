package test;

import graph.Evolution;
import graph.Graph;
import graph.Permutation;

import java.util.ArrayList;

public class TestPermutationCompareTo {

    public static void main(String[] args){
        int n=10;
        Graph g = new Graph(n);

        for (int i=0; i<n-1; i++){
            g.addEdge(i,i+1);
        }


        ArrayList<Integer> permutation_sorted = new ArrayList<>();
        for (int i=0;i<n;i++){
            permutation_sorted.add(i);
        }
        Permutation sorted = new Permutation();
        sorted.setPermutation(permutation_sorted);
        sorted.setCutwidth(g.cutwidth(sorted.getPermutation()));
        sorted.viewPermutation();

        ArrayList<Integer> permutation_sorted1 = new ArrayList<>();
        for (int i=0;i<n;i++){
            permutation_sorted1.add(i);
        }
        Permutation sorted1 = new Permutation();
        sorted1.setPermutation(permutation_sorted1);
        sorted1.setCutwidth(g.cutwidth(sorted.getPermutation()));
        sorted1.viewPermutation();

        System.out.println("WOW: ");
        System.out.println(sorted.compareTo(sorted1));

        //g.info();
        //g.sorting();
        //g.outputSorting();
    }
}
