package interfaces.common;


import interfaces.system.IndexHelp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Random;

import Utils.Configure;
import Utils.IndexConfig;
import Utils.IndirectConfig;

import elements.common.Edge;
import elements.common.InterestingPoint;
/**
 * POI所对应的EdgeID是随机的
 * @author BYRD
 *
 */
public class LoadBasicEdgesInfo {
	
	public  HashMap<Integer, Edge> basicEdgeWithPoiInfo;
	/**
	 * 获得Vertex边之间的Map
	 */
	public HashMap<Integer,HashMap<Integer, Integer>> vertexToVertexAtEdge;
	
	public int maxIndexOfEdge;
	
	public int minIndexOfEdge;
	
	
	public String dataOfPOIWithEdge;
	

	public LoadBasicEdgesInfo() {
		// TODO Auto-generated constructor stub
		basicEdgeWithPoiInfo=new HashMap<Integer, Edge>();
		
		dataOfPOIWithEdge=Configure.PROJECT_DATA_ROOT+IndirectConfig.POI_WITH_EDGE_RCA_FORMAT;
		
		vertexToVertexAtEdge=new HashMap<Integer, HashMap<Integer,Integer>>();
		
		maxIndexOfEdge=-1;
		minIndexOfEdge=-1;
		
		loadInfo();
	}
	
	public void loadInfo()
	{
		loadEdgesWithoutPOI();
		
		maxIndexOfEdge=IndexHelp.FindTheMax(basicEdgeWithPoiInfo.keySet());
		minIndexOfEdge=IndexHelp.FindTheMin(basicEdgeWithPoiInfo.keySet());
		
		addPOIsInfomationToEdges();
	}
	
	public int getMaxIndexOfEdge()
	{
		return maxIndexOfEdge;
	}
	
	
	public void addPOIsInfomationToEdges()
	{
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(new FileInputStream(dataOfPOIWithEdge), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LineNumberReader poiWithEdgeLineReader=new LineNumberReader(isr);
		String line;
		int poiID=0;
		Random rand=new Random();
		
		try {
			while((line=poiWithEdgeLineReader.readLine())!=null)
			{
				//
				String parts[]=line.split(Configure.SPLITE_FLAG);
				if(parts.length<5)
				{
//					System.err.println("Error Line AT"+poiWithEdgeLineReader.getLineNumber());
					System.err.println("error Line:"+parts[1]);
//					return;
				}
//				if(parts.length<5)
//				{
//					parts=line.split(",");
//				}
				
				//随机数据
				int edgeID=Integer.parseInt(parts[0]);
				if(!basicEdgeWithPoiInfo.containsKey(edgeID))
				{
					System.out.println(line);
					System.out.println("No Edge"+edgeID);
					continue;
				}
				
//				int edgeID=rand.nextInt(maxIndexOfEdge-minIndexOfEdge)+minIndexOfEdge;
//				while((edgeID=rand.nextInt(maxIndexOfEdge-minIndexOfEdge)+minIndexOfEdge)>IndexConfig.MAX_ROAD_VERTEX_INDEX);
				
				//random
				
				double x=Double.parseDouble(parts[1]);
				double y=Double.parseDouble(parts[2]);
				HashMap<String, Float> kwdScore=new HashMap<String, Float>();
				HashMap<String, Integer> kwdCount = new HashMap<String, Integer>();
				if(parts.length%2==0)
				{
					continue;
				}
				for(int i=3;i<parts.length;i+=2)
				{
					String kwd = parts[i];
					
					int freq = Integer.parseInt(parts[i + 1]);
					if (kwdCount.containsKey(kwd)) {
						kwdCount.put(kwd, kwdCount.get(kwd) + freq);
					} else {
						kwdCount.put(kwd, freq);
					}
				}
				
				double sumeOfWeightSquare = 0;
				double weight;
				for (String kwd : kwdCount.keySet()) {
					int freq = kwdCount.get(kwd);
					weight = 1 + Math.log(freq);
					sumeOfWeightSquare += weight * weight;
				}
				sumeOfWeightSquare = Math.sqrt(sumeOfWeightSquare);	
				for (String kwd : kwdCount.keySet())
				{
					int freq = kwdCount.get(kwd);
					float textScore=(float) ((1 + Math.log(freq))/ sumeOfWeightSquare);
					kwdScore.put(kwd, textScore);
					//更新MaxScore
					
					if(!basicEdgeWithPoiInfo.containsKey(edgeID))
					{
						System.out.println("No Edge"+edgeID);
						continue;
					}
					
					if(!basicEdgeWithPoiInfo.get(edgeID).keywordsMaxScoreMap.containsKey(kwd))
					{
						basicEdgeWithPoiInfo.get(edgeID).keywordsMaxScoreMap.put(kwd, textScore);
					}
					else
					{
						if(textScore<basicEdgeWithPoiInfo.get(edgeID).keywordsMaxScoreMap.get(kwd))
						{
							basicEdgeWithPoiInfo.get(edgeID).keywordsMaxScoreMap.put(kwd, textScore);
						}
					}
					//插入POIInfo
					if(!basicEdgeWithPoiInfo.get(edgeID).kwdPOISocresMap.containsKey(kwd))
					{
						basicEdgeWithPoiInfo.get(edgeID).kwdPOISocresMap.put(kwd, new HashMap<Integer, Float>());
					}
					
					basicEdgeWithPoiInfo.get(edgeID).kwdPOISocresMap.get(kwd).put(poiID, textScore);
					
				}
				
				if(!basicEdgeWithPoiInfo.get(edgeID).poiMap.containsKey(poiID))
				{
					
					InterestingPoint tmpPOI=new InterestingPoint(poiID,x, y, kwdScore, edgeID,kwdCount);
					basicEdgeWithPoiInfo.get(edgeID).addPOI(tmpPOI);
				}
				
				
				poiID++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void addPOIsInfomationToRandomEdges()
	{
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(new FileInputStream(dataOfPOIWithEdge), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LineNumberReader poiWithEdgeLineReader=new LineNumberReader(isr);
		String line;
		int poiID=0;
		
		Random randEdge=new Random();
		try {
			while((line=poiWithEdgeLineReader.readLine())!=null)
			{
				//
				String parts[]=line.split(Configure.SPLITE_FLAG);
//				int edgeID=Integer.parseInt(parts[0]);
				int edgeID=randEdge.nextInt(maxIndexOfEdge);
				
				
				//random
				
				double x=Double.parseDouble(parts[1]);
				double y=Double.parseDouble(parts[2]);
				HashMap<String, Float> kwdScore=new HashMap<String, Float>();
				HashMap<String, Integer> kwdCount = new HashMap<String, Integer>();
				if(parts.length%2==0)
				{
					continue;
				}
				for(int i=3;i<parts.length;i+=2)
				{
					String kwd = parts[i];
					
					int freq = Integer.parseInt(parts[i + 1]);
					if (kwdCount.containsKey(kwd)) {
						kwdCount.put(kwd, kwdCount.get(kwd) + freq);
					} else {
						kwdCount.put(kwd, freq);
					}
				}
				
				double sumeOfWeightSquare = 0;
				double weight;
				for (String kwd : kwdCount.keySet()) {
					int freq = kwdCount.get(kwd);
					weight = 1 + Math.log(freq);
					sumeOfWeightSquare += weight * weight;
				}
				sumeOfWeightSquare = Math.sqrt(sumeOfWeightSquare);	
				for (String kwd : kwdCount.keySet())
				{
					int freq = kwdCount.get(kwd);
					float textScore=(float) ((1 + Math.log(freq))/ sumeOfWeightSquare);
					kwdScore.put(kwd, textScore);
					//更新MaxScore
					if(!basicEdgeWithPoiInfo.get(edgeID).keywordsMaxScoreMap.containsKey(kwd))
					{
						basicEdgeWithPoiInfo.get(edgeID).keywordsMaxScoreMap.put(kwd, textScore);
					}
					else
					{
						if(textScore<basicEdgeWithPoiInfo.get(edgeID).keywordsMaxScoreMap.get(kwd))
						{
							basicEdgeWithPoiInfo.get(edgeID).keywordsMaxScoreMap.put(kwd, textScore);
						}
					}
					//插入POIInfo
					if(!basicEdgeWithPoiInfo.get(edgeID).kwdPOISocresMap.containsKey(kwd))
					{
						basicEdgeWithPoiInfo.get(edgeID).kwdPOISocresMap.put(kwd, new HashMap<Integer, Float>());
					}
					
					basicEdgeWithPoiInfo.get(edgeID).kwdPOISocresMap.get(kwd).put(poiID, textScore);
					
				}
				
				if(!basicEdgeWithPoiInfo.get(edgeID).poiMap.containsKey(poiID))
				{
					
					InterestingPoint tmpPOI=new InterestingPoint(poiID,x, y, kwdScore, edgeID,kwdCount);
					basicEdgeWithPoiInfo.get(edgeID).addPOI(tmpPOI);
				}
				
				
				poiID++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获得基本的Edge
	 * loadEdgesWithoutPOI
	 */
	public void loadEdgesWithoutPOI()
	{
		LoadBasicEdge baseEdge=new LoadBasicEdge();
		try {
			baseEdge.loadEdges();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		basicEdgeWithPoiInfo=baseEdge.baseEdges;
		IndexHelp.findMaxMinIndex(basicEdgeWithPoiInfo.keySet());
		
		vertexToVertexAtEdge=baseEdge.vertexToVertexAtEdge;
	}

	public void printKEdge(int k)
	{
		int itemCount=0;
		for(Edge ed:basicEdgeWithPoiInfo.values())
		{
			if(itemCount>k)
			{
				break;
			}
			if(ed.keywordsMaxScoreMap.size()==0)
			{
				continue;
			}
			System.out.println("================================================");
			System.out.println(ed.Scores());
//			System.out.println(ed.kwdCount.toString());
			System.out.println("================================================");
			itemCount++;
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoadBasicEdgesInfo test=new LoadBasicEdgesInfo();
		IndexHelp.findMaxMinIndex(test.vertexToVertexAtEdge.keySet());
		IndexHelp.findMaxMinIndex(test.basicEdgeWithPoiInfo.keySet());
//		test.loadEdgesWithoutPOI();
//		test.addPOIsInfomationToEdges();
//		test.printKEdge(100);
	}

}
