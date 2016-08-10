package graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Date;
import java.util.Random;
import java.util.Iterator;

public class Evolution{
	Date date = new Date();
	Random random = new Random(date.getTime());
	ArrayList<Permutation> parents;

	public Evolution(Graph g, int startNumberOfPermutations){
		int length = g.getCount();
		parents = new ArrayList<Permutation>();
		parents.ensureCapacity(startNumberOfPermutations);
		//initiate parents
		for (int i=0; i<startNumberOfPermutations; i++){
			Permutation parent = new Permutation(length);
			parent.setPermutation(g.getPermutation());
			parent.randomShuffle(random.nextInt());
			parent.setCutwidth(g.cutwidth(parent.getPermutation()));
			parents.add(parent);
		}
		//for (Permutation p: parents){p.viewPermutation();}
		//System.out.println("==========================");
	}

	public Evolution(Permutation permutation, int startNumberOfPermutations){
		parents = new ArrayList<Permutation>();
		parents.ensureCapacity(startNumberOfPermutations);
		//initiate parents
		for (int i=0; i<startNumberOfPermutations; i++){
			Permutation parent = new Permutation();
			parent.setPermutation(permutation.getPermutation());
			parent.setCutwidth(permutation.getCutwidth());
			parents.add(parent);
		}
	}

	public Permutation evolute(Graph g, int numberOfChildren, int numberOfSteps){
		int numberOfParents = parents.size();
		int min1 = 0;
		int min2 = 0;
		int min3 = 0;
		/*
		TreeSet<Permutation> children = new TreeSet<Permutation>(new Comparator<Permutation>() {
			public int compare(Permutation p1, Permutation p2) {
				return p1.compareTo(p2);
			}
		});
		*/
		TreeSet<Permutation> children = new TreeSet<Permutation>();

		ArrayList<Permutation> unsortedChildren = new ArrayList<Permutation>();
		unsortedChildren.ensureCapacity(numberOfChildren*numberOfParents);

		long currTime = 0;
		
		//get children
		for (int step=0; step<numberOfSteps; step++){
			currTime = System.currentTimeMillis();
			unsortedChildren.clear();
			//System.out.println("Get children");
			for (Permutation parent: parents){
				//System.out.print("o");
				for (int i=0; i<numberOfChildren; i++){	
					Permutation child = new Permutation();				
					child.setPermutation(parent.getPermutation());
					int numberOfSwaps = random.nextInt(2)+1;
					for (int j=0;j<numberOfSwaps;j++){
						child.randomSwap(random.nextInt());	
					}
					child.setCutwidth(g.cutwidth(child.getPermutation()));
					unsortedChildren.add(child);
				}
			}
			//System.out.println();
			//System.out.println(System.currentTimeMillis()-currTime);
			children.clear();
			children.addAll(unsortedChildren);
			System.out.println("STEP "+step+". Parents "+parents.size()
						+". Children "+children.size());
			parents.clear();

			int counter = 1;
			for (Permutation childo: children){
				parents.add(childo);
				//childo.viewPermutation();
				counter++;
				if (counter>numberOfParents) {break;}
			}
			parents.get(0).viewPermutation(true);

			/*
			Iterator<Permutation> iterator = children.iterator();
			Permutation childo = null;

			for (int i=0; i<numberOfParents; i++){
				if (iterator.hasNext()){
					childo = iterator.next();
					childo.viewPermutation();
				}
			}
			parents.addAll(children.headSet(childo));
			System.out.println("children: "+children.size());
			for (Permutation parent: parents){
				parent.viewPermutation();
			}
			*/
			/*
			parents.get(parents.size()-1).viewPermutation();
			if (step==numberOfSteps-1) {
				for(Permutation p: parents){p.viewPermutation();}
			}*/

			//Exit for-loop(step) condition
			//Cutwidth minimum remains the same 3 steps
			min3 = parents.get(0).getCutwidth();
			if ((min3==min2)&&(min1==min2)) {
				System.out.println("BREAK at step "+step);
				break;
			} else {
				min1 = min2;
				min2 = min3;
			}
		}
		return parents.get(0);		
	}

}
