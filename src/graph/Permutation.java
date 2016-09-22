package graph;

import java.util.ArrayList;
import java.util.Random;
import java.util.Date;

/**
 * Permutation of graph vertices
 *
 * <br><br> permutation = ArrayList of Integer with permutation id
 * <br> cutwidth = cut width
 * <br> cutwidthAverage = average cut width, counts automatically when changing cut width
 *
 */
public class Permutation implements Comparable<Permutation>{
	private ArrayList<Integer> permutation;
	private int cutwidth = 0;
	private double cutwidthAverage = 0d;


	public Permutation(){
		this(0);
	}

	public Permutation(int length){
		permutation = new ArrayList<Integer>();
		permutation.ensureCapacity(length);
	}

	public int compareTo(Permutation anotherPermutation){
		int diff = this.getCutwidth() - anotherPermutation.getCutwidth();
		if (diff == 0) {
			int counter = 0;
			for (int number: permutation){
				diff = number - anotherPermutation.permutation.get(counter);
				if (diff!=0){
					return diff;
				}
				counter++;
			}
			return 0;
		} else {
			return diff;
		}
	}

	public void randomShuffle(long plus){
		int shuffle;
		Date date = new Date();
		Random random = new Random(date.getTime()+plus);

		int i;
		int length = length();
		for(int k=0; k<length; k++){
			i = random.nextInt(length-1);
			shuffle = permutation.get(i);
			permutation.remove(i);
			permutation.add(shuffle);					
		}
		
	}
	public void setPermutation(ArrayList<Integer> permutation){

		this.permutation.clear();
		int length = permutation.size();
		this.permutation.ensureCapacity(length);
		for (Integer x: permutation) {
			this.permutation.add(x);
		}
	}

	public ArrayList<Integer> getPermutation(){
		return permutation;
	}

	/**
	 * Set cut width value.
	 *
	 * You should get this value by using Graph.cutWidthNew on this permutation received by getPermutation.
	 *
	 * @param cutwidth cut width for this permutation from some Graph
	 */
	public void setCutwidth(int cutwidth){
		this.cutwidth = cutwidth;
		if (permutation.size()>1) {
			cutwidthAverage = (double) cutwidth/(permutation.size()-1);
		}
	}

	public int getCutwidth(){
		return cutwidth;
	}

	public double getCutwidthAverage() { return cutwidthAverage; }

	public int length(){
		return permutation.size();
	}

	@Override
	public String toString(){
		String s="";
		for (int x: permutation) {
			s = s + x + ", ";
		}
		s=s+" cutwidth = "+cutwidth+" average = ";
		return s;	
	}

	public void viewPermutation(){
		viewPermutation(false);
	}

	/**
	 * View permutation.
	 *
	 * To look at result of sorting or control process.
	 *
	 * @param onlyCutwidth (true) write only cut width and average cut width values
	 */
	public void viewPermutation(boolean onlyCutwidth){
		String s="";
		if (!onlyCutwidth){
			for (int x: permutation) {
				s = s + x + ", ";
			}
		}
		s=s+" cutwidth = "+cutwidth+" average = ";
		System.out.printf(s+"%2.3f"+"\n",cutwidthAverage);
	}

	/**
	 * Change position of two id in permutation.
	 *
	 * Specially for Evolution.
	 * @param plus seed for random number generator (using current time is kinda of OK)
	 */
	public void randomSwap(long plus){
		Date date = new Date();
		Random random = new Random(date.getTime()+plus);

		int length = length();
		int x1 = random.nextInt(length-1);
		int x2 = random.nextInt(length-1);
		if (length>1){
			while (x2==x1){
				x2 = random.nextInt(length-1);
			}
		}

		int i1 = permutation.indexOf(x1);
		int i2 = permutation.indexOf(x2);
		
		if ((i1>-1)&&(i2>-1)){
				permutation.set(i1,x2);
				permutation.set(i2,x1);
		}
	}
}
