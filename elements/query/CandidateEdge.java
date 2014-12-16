package elements.query;

import java.io.Serializable;

import elements.common.VertexPoint;

public class CandidateEdge  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5460292745419861726L;
	public int id;
	public int edgeID;
	public VertexPoint v1;
	public VertexPoint v2;

	public CandidateEdge() {
		// TODO Auto-generated constructor stub
		this.id=-1;		//表示空的，未插入任何数据
		
	}
	public CandidateEdge(int id,int edgeid)
	{
		this.id=id;
		this.edgeID=edgeid;
	}
	
	public CandidateEdge(int id,int edgeid,VertexPoint v1,VertexPoint v2)
	{
		this.id=id;
		this.edgeID=edgeid;
		v1=new VertexPoint();
		v2=new VertexPoint();
//		this.v1.clone(v1);
//		this.v2.clone(v2);
		this.v1=v1;
		this.v2=v2;
				
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
