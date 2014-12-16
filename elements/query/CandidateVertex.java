package elements.query;

import java.io.Serializable;



public class CandidateVertex implements Serializable  ,Comparable<CandidateVertex>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2410286535222851815L;
	
	public int adjVertexID;
	
	public int distance;

	public CandidateVertex(int id,int distance) {
		// TODO Auto-generated constructor stub
		this.adjVertexID=id;
		this.distance=distance;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public int compareTo(CandidateVertex o) {
		// TODO Auto-generated method stub
		return distance-o.distance;
	}

}
