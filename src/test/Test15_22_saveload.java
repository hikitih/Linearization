package test;

import graph.*;

public class Test15_22_saveload {

	public static void main(String[] args){
		Graph g = GraphSaveLoad.loadWeightedGraph("graph15_22.txt");

		if (g!=null){
			g.info();
		}

		GraphSaveLoad.saveGraph("graph15_22.txt",g,true);
	}
}
