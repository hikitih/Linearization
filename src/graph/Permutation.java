package graph;

import java.util.ArrayList;
import java.util.Random;
import java.util.Date;

public class Permutation implements Comparable<Permutation>{
	private ArrayList<Integer> permutation;
	private int cutwidth = 0;
	private double cutwidthAverage = 0d;


	public Permutation(){
		this.permutation = new ArrayList<Integer>();
	}

	public Permutation(int length){
		this.permutation = new ArrayList<Integer>();
		for (int i = 0; i<length; i++){
			permutation.add(i);
		}
	}

	public int compareTo(Permutation anotherPermutation){
		return this.getCutwidth() - anotherPermutation.getCutwidth();
	}

	public void randomShuffle(long plus){
		int shuffle;
		Date date = new Date();
		Random random = new Random(date.getTime()+plus);

		int i;
		int l = length();
		for(int k=0; k<l; k++){
			i = random.nextInt(length()-1);
			shuffle = permutation.get(i);
			permutation.remove(i);
			permutation.add(shuffle);					
		}
		
	}

	public void setPermutation(ArrayList<Integer> permutation){
		this.permutation.clear();
		for (Integer x: permutation) {
			this.permutation.add(x);
		}//need save way to ensure this is real permutation from 0 to l-1
	}

	public ArrayList<Integer> getPermutation(){
		return permutation;
	}

	public void setCutwidth(int cutwidth){
		this.cutwidth = cutwidth;
		if (permutation.size()>0) cutwidthAverage = (double) cutwidth/permutation.size();
	}

	public int getCutwidth(){
		return cutwidth;
	}

	public int length(){
		return permutation.size();
	}

	public void viewPermutation(){
		viewPermutation(false);
	}

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

	public void randomSwap(long plus){
		Date date = new Date();
		Random random = new Random(date.getTime()+plus);

		int x1 = random.nextInt(length()-1);
		int x2 = random.nextInt(length()-1);
		if (length()>1){
			while (x2==x1){
				x2 = random.nextInt(length()-1);
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
