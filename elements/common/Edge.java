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
	 * ���ݾ�γ����ת����ʵ�ʵ�ͼ������ľ���
	 * ת����ʽ��ʵ�ʾ���=��γ�������*����
	 * ת����ʽ��Distance=��γ�������*100000
	 */
	public int Distance;  
	
	/**
	 * POI���������Ĺؼ�����Ϣ<keyword,Scores>
	 * Edge����洢�ڸñ�����ÿ��keyword�ĵ���Object�����÷�
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
