package elements.query;

import java.io.Serializable;

public class CandidateVertexOfMinHeapV implements Serializable,Comparable<CandidateVertexOfMinHeapV> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9126563002043318772L;

	public int vertexID;
	
	public int distance;

	public CandidateVertexOfMinHeapV(int vertexID,int distance) {
		// TODO Auto-generated constructor stub
		this.vertexID=vertexID;
		this.distance=distance;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public int compareTo(CandidateVertexOfMinHeapV o) {
		// TODO Auto-generated method stub
		return this.distance-o.distance;
	}

}
