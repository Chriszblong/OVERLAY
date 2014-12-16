package interfaces.common;


import interfaces.system.IndexHelp;

import java.util.HashMap;
import java.util.Vector;

import Utils.IndexConfig;

import elements.common.Edge;
import elements.common.VertexPoint;
import elements.component.adjacency.AdjVertexEntry;
import elements.regionBTree.Region;

public class LoadSeedRegionWithPOI {
	
	/**
	 * seedRegion
	 */
	public HashMap<Integer, Region> seedRegionWithPOIMap;

	
	
	/**
	 * basicEdgeWithPOI
	 */
	public  HashMap<Integer, Edge> basicEdgeWithPoiInfo;
	/**
	 * 
	 */
	public int startIndexOfRegion=-1;
	public HashMap<Integer,HashMap<Integer, Integer>> vertexToVertexAtEdge;
	/**
	 * 
	 */
	public LoadSeedRegionWithPOI() {
		// TODO Auto-generated constructor stub
		seedRegionWithPOIMap=new HashMap<Integer, Region>();
		
		vertexToVertexAtEdge=new HashMap<Integer, HashMap<Integer,Integer>>();
		
		basicEdgeWithPoiInfo=new HashMap<Integer, Edge>();
		
		startIndexOfRegion=-1;
		init();
	}
	
	public void init()
	{
		loadBasicEdgeWithPoiInfo();
	}
	
	public HashMap<Integer, Region> getSeedRegionWithPOI()
	{
		int seedRegionID=startIndexOfRegion+1;
		int cout=0;
		
		for(Integer edgeID:basicEdgeWithPoiInfo.keySet())
		{
//			HashMap<Integer, Edge> edgesMap=new HashMap<Integer, Edge>();
//			edgesMap.put(edgeID, basicEdgeWithPoiInfo.get(edgeID));
			Region region=new Region(seedRegionID, 0, new HashMap<Integer, Edge>());
			if(!basicEdgeWithPoiInfo.containsKey(edgeID))
			{
				System.out.println(edgeID);
			}
			region.addEdge(basicEdgeWithPoiInfo.get(edgeID));
			region.borderVertexs.put(basicEdgeWithPoiInfo.get(edgeID).v1.id,IndexConfig.VERTEX_TYPE_BORDER);
			region.borderVertexs.put(basicEdgeWithPoiInfo.get(edgeID).v2.id,IndexConfig.VERTEX_TYPE_BORDER);
		
			
			if(!region.borderVertexsDistance.containsKey(basicEdgeWithPoiInfo.get(edgeID).v1.id))
			{
				region.borderVertexsDistance.put(basicEdgeWithPoiInfo.get(edgeID).v1.id,new HashMap<Integer,Integer>());
			}
//			System.out.println("ss"+DistanceHelp.getRealDistanceToVertex(basicEdgeWithPoiInfo.get(edgeID).v1, basicEdgeWithPoiInfo.get(edgeID).v2));
			region.borderVertexsDistance.get(basicEdgeWithPoiInfo.get(edgeID).v1.id).put(basicEdgeWithPoiInfo.get(edgeID).v2.id, DistanceHelp.getRealDistanceToVertex(basicEdgeWithPoiInfo.get(edgeID).v1, basicEdgeWithPoiInfo.get(edgeID).v2));

			seedRegionWithPOIMap.put(seedRegionID, region);
			seedRegionID++;

		}

		
		
		return seedRegionWithPOIMap;
	}
	

	
	
	public int getTheStartIndexOfRegion()
	{
		
		startIndexOfRegion=IndexHelp.FindTheMax(basicEdgeWithPoiInfo.keySet());
//		System.err.println("startIndex"+startIndexOfRegion);
		return startIndexOfRegion;
	}
	
	
	public void loadBasicEdgeWithPoiInfo()
	{
		LoadBasicEdgesInfo tmp=new LoadBasicEdgesInfo();
//		tmp.loadEdgesWithoutPOI();
//		tmp.addPOIsInfomationToEdges();
////		tmp.printKEdge(1000);
		basicEdgeWithPoiInfo=tmp.basicEdgeWithPoiInfo;
		vertexToVertexAtEdge=tmp.vertexToVertexAtEdge;
		getTheStartIndexOfRegion();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoadSeedRegionWithPOI test=new LoadSeedRegionWithPOI();
		
		test.getSeedRegionWithPOI();
		System.out.println("ALL DONE");
		int count=0;
		for(Region re:test.seedRegionWithPOIMap.values())
		{
			System.out.println(re.edges.keySet().toString());
			count++;
			if(count>10)
			{
				return;
			}
		}

	}

}
