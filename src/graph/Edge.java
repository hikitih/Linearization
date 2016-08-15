package graph;

class Edge{
	public int out;		//from
	public int in;		//to
	public int weight;	//weight

	Edge(int out, int in){
		this(out,in,1);
	}

	Edge(int out, int in, int weight){
		this.out = out;
		this.in = in;
		this.weight = weight;
	}

	@Override
	public String toString(){
		return "From "+out+" to "+in+"("+weight+")";
	}

	int getOtherEnd(int start){
		return ((start==out) ? in : out);
	}

	boolean isIn(int start){
		return (start == in);
	}
}
