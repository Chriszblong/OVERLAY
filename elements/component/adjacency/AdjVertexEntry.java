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
	 * Overlay��Vertex�����ID
	 */
	public  int adjVertexID;
	
	/**
	 * relevantType:
	 * 1:I  intermediary Vertex�м�����ڵ� 
	 * 2:C direct Connection Vertex��Ե�ڵ����ӵ� 
	 * 3:R region�ڲ��ڵ㼴regular Vertex
	 */
	public 	int relevantType; 
	
	/**
	 * relevantType=1 relevantID��ʾһ��region��ID
	 * relevantType=2 relevantID��ʾһ��region����Border Vertex�߱�ʾ
	 * relevantType=3 relevantID��ʾһ��region�ڲ���regular vertexID
	 * Configure.RELEVANT_TYPE_INTERMEDARY RELEVANT_TYPE_DIRECT_CONNECTION RELEVANT_TYPE_REGULAR_VERTEX
	 */
	public  int relevantID;
	
	
	
	/**
	 * key Vertex���Vertex֮��ľ��� ��Ȼ���relevant��һ������Ļ�distance����0
	 */
	public  int distance;
	
	
	public int fatherAdjID;
	
//	/**
//	 * ����ÿ�����ϵĽڵ�
//	 * ��Ȼ��relevantType��ʾΪregion��ʱ��verterPoint1,verterPoint1��������
//	 */
//	public VertexPoint verterPoint1;
//	
//	
//	public VertexPoint verterPoint2;
	
//	/**
//	 * POI���������Ĺؼ�����Ϣ<keyword,Scores>
//	 * Edge����洢�ڸñ�����ÿ��keyword�ĵ���Object�����÷�
//	 * ��relevantType��ʾregion��ʱ����Ч
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
