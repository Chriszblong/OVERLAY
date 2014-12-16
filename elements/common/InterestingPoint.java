package elements.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

public class InterestingPoint  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9139936694135603522L;
	public int id;
	/**
	 * POI所落在的Edge
	 */
	public int edgeID;
	public double x;
	public double y;

	/**
	 * POI中所包含的关键字信息<keyword,Scores>
	 * 存储关键字及其该关键字得分
	 */
	public HashMap<String, Float> keywords;
	/**
	 * <kwd,Frequent>
	 */
	public HashMap<String,Integer>kwdCount;
	

	public InterestingPoint() {
		// TODO Auto-generated constructor stub
		this.id=-1;
		kwdCount=new HashMap<String, Integer>();
	}
	public InterestingPoint(int id,double x,double y,HashMap<String, Float>keywords,int edgeID, HashMap<String,Integer> kwdCount) {
		this.id=id;
		this.x=x;
		this.y=y;
//		this.keywords=new HashMap<String, Float>();
		this.keywords=keywords;
		this.edgeID=edgeID;
		/*this.kwdCount=new HashMap<String, Integer>();*/
		this.kwdCount=kwdCount;
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEdgeID() {
		return edgeID;
	}
	public void setEdgeID(int edgeID) {
		this.edgeID = edgeID;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public HashMap<String, Float> getKeywords() {
		return keywords;
	}
	public void setKeywords(HashMap<String, Float> keywords) {
		this.keywords = keywords;
	}
	public HashMap<String, Integer> getKwdCount() {
		return kwdCount;
	}
	public void setKwdCount(HashMap<String, Integer> kwdCount) {
		this.kwdCount = kwdCount;
	}

}
