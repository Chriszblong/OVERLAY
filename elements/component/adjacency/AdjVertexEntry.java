package elements.component.adjacency;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

import elements.common.InterestingPoint;


public class AdjVertexEntry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -272455115840791716L;
	
	public int id;
	/**
	 * Overlay中Vertex顶点的ID
	 */
	public  int adjVertexID;
	
	/**
	 * relevantType:
	 * 1:I  intermediary Vertex中间虚拟节点 
	 * 2:C direct Connection Vertex边缘节点连接点 
	 * 3:R region内部节点即regular Vertex
	 */
	public 	int relevantType; 
	
	/**
	 * relevantType=1 relevantID表示一个region的ID
	 * relevantType=2 relevantID表示一个region两个Border Vertex边表示
	 * relevantType=3 relevantID表示一个region内部的regular vertexID
	 * Configure.RELEVANT_TYPE_INTERMEDARY RELEVANT_TYPE_DIRECT_CONNECTION RELEVANT_TYPE_REGULAR_VERTEX
	 */
	public  int relevantID;
	
	
	
	/**
	 * key Vertex与该Vertex之间的距离 当然如果relevant是一个区域的话distance就是0
	 */
	public  int distance;
	
	
	public int fatherAdjID;
	
//	/**
//	 * 定义每条边上的节点
//	 * 当然当relevantType表示为region的时候verterPoint1,verterPoint1不起作用
//	 */
//	public VertexPoint verterPoint1;
//	
//	
//	public VertexPoint verterPoint2;
	
//	/**
//	 * POI中所包含的关键字信息<keyword,Scores>
//	 * Edge里面存储在该边里面每个keyword的单个Object的最大得分
//	 * 当relevantType表示region的时候有效
//	 */
//	public HashMap<String, Float> keywordsMaxScore;
//	
//
//	
//
//	public HashMap<String, Float> getKeywordsMaxScore() {
//		return keywordsMaxScore;
//	}
//
//	public void setKeywordsMaxScore(HashMap<String, Float> keywordsMaxScore) {
//		this.keywordsMaxScore = keywordsMaxScore;
//	}

	public AdjVertexEntry() {
		this.id=-1;
		this.adjVertexID=-1;
		this.relevantType=-1;
		this.relevantID=-1;
		this.distance=-1;
	}
	public AdjVertexEntry(int id,int adjVertexID,int relevantType,int relevantId,int distance) {
		this.id=id;
		this.adjVertexID=adjVertexID;
		this.relevantType=relevantType;
		this.relevantID=relevantId;
		this.distance=distance;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public int getVertexID() {
		return adjVertexID;
	}

	public void setVertexID(int adjVertexID) {
		this.adjVertexID = adjVertexID;
	}

	public int getRelevantType() {
		return relevantType;
	}

	public void setRelevantType(int relevantType) {
		this.relevantType = relevantType;
	}

	public int getRelevantID() {
		return relevantID;
	}

	public void setRelevantID(int relevantID) {
		this.relevantID = relevantID;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

}
