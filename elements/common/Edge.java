package elements.common;

import interfaces.common.DistanceHelp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

public class Edge  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8138426999583828565L;
	public int id;
	public VertexPoint v1;
	public VertexPoint v2;
	/**
	 * 根据经纬坐标转换到实际地图中坐标的距离
	 * 转换公式：实际距离=经纬坐标距离*比例
	 * 转换公式：Distance=经纬坐标距离*100000
	 */
	public int Distance;  
	
	/**
	 * POI中所包含的关键字信息<keyword,Scores>
	 * Edge里面存储在该边里面每个keyword的单个Object的最大得分
	 */
	public HashMap<String, Float> keywordsMaxScoreMap;
	/**
	 * <kwd,<POIID,Float>>
	 */
	public HashMap<String,HashMap<Integer, Float>> kwdPOISocresMap;
	/**
	 * POIMap
	 */
	public HashMap<Integer,InterestingPoint> poiMap;
	/**
	 * <kwd,Frequent>
	 */
	public HashMap<String,Integer>kwdCount;
	 
	
	public Edge()
	{
		this.id=-1;
		this.kwdPOISocresMap=new HashMap<String, HashMap<Integer,Float>>();
		this.keywordsMaxScoreMap=new HashMap<String, Float>();
		this.poiMap=new HashMap<Integer, InterestingPoint>();
		this.kwdCount=new HashMap<String, Integer>();
		this.Distance=-1;
	}
	public Edge(int id)
	{
		this.id=id;
		this.kwdPOISocresMap=new HashMap<String, HashMap<Integer,Float>>();
		this.keywordsMaxScoreMap=new HashMap<String, Float>();
		this.poiMap=new HashMap<Integer, InterestingPoint>();
		this.kwdCount=new HashMap<String, Integer>();
		this.Distance=-1;
	}

	public Edge(int id,VertexPoint v1,VertexPoint v2) {
		this.id=id;
		this.v1=new VertexPoint(v1);
		this.v2=new VertexPoint(v2);
		this.Distance=DistanceHelp.getRealDistanceToVertex(v1, v2);
		this.kwdPOISocresMap=new HashMap<String, HashMap<Integer,Float>>();
		this.keywordsMaxScoreMap=new HashMap<String, Float>();
		this.poiMap=new HashMap<Integer, InterestingPoint>();
		this.kwdCount=new HashMap<String, Integer>();
	}
	
	public String Scores()
	{
		String scoreString="";
		for(String kwd:keywordsMaxScoreMap.keySet())
		{
			scoreString+=kwd+"\t"+keywordsMaxScoreMap.get(kwd)+"\n";
		}
	
		return scoreString;
	}

	
	public void addPOI(InterestingPoint poi)
	{
		
		if(this.poiMap.containsKey(poi.id))
		{
			this.poiMap.put(poi.id, poi);
		}
		else
		{
			this.poiMap.put(poi.id, poi);
			//Update kwdCount
			for(String kwd:poi.kwdCount.keySet())
			{
				if(this.kwdCount.containsKey(kwd))
				{
					this.kwdCount.put(kwd,this.kwdCount.get(kwd)+poi.kwdCount.get(kwd));
				}else
				{
					this.kwdCount.put(kwd, poi.kwdCount.get(kwd));
				}
				
			}
		}
		
	
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
