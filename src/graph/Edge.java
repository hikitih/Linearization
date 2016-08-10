package graph;

public class Edge{
	public int out;		//from
	public int in;		//to
	public int weight;	//weight

	public Edge(int out, int in){
		this(out,in,1);
	}

	public Edge(int out, int in, int weight){
		this.out = out;
		this.in = in;
		this.weight = weight;
	}

	public int getOtherEnd(int start){
		return ((start==out) ? in : out);
	}

	public boolean isIn(int start){
		return (start == in);
	}
}
