package interfaces.common;

import interfaces.system.IndexFind;
import interfaces.system.IndexHelp;

import java.util.HashMap;
import java.util.Vector;

import Utils.Configure;
import elements.common.Edge;
import elements.common.VertexPoint;
import elements.component.adjacency.AdjVertexEntry;
import elements.regionBTree.Region;

/**
 * @category  ;
 * load the basic VertexRegionIDMap;
 * load the basic adjacencyVertexRegionMap;
 * @author root
 *
 */
public class LoadBasicVertexRegion {
	

	/**
	 * 
	 */
	public HashMap<Integer, HashMap<Integer,Integer>> basicVertexRegionIDMap;
	/**
	 * adjacency component
	 */
	public HashMap<Integer,HashMap<Integer, AdjVertexEntry>> basicSeedAdjacencyComponent; 
	/**
	 * seedRegionWithPOIMap
	 */
	public HashMap<Integer, Region> seedRegionWithPOIMap;
	/**
	 * 
	 */
	public int indexStartOfinterminalVertex=-1;
	/**
	 * 
	 */
	public HashMap<Integer,HashMap<Integer, Integer>> vertexToVertexAtEdge;
	
	public  HashMap<Integer, Edge> basicEdgeWithPoiInfo;
	
	public LoadBasicVertexRegion() {
		// TODO Auto-generated constructor stub
		
		init();
	}

	public void init()
	{
		
		basicVertexRegionIDMap=new HashMap<Integer, HashMap<Integer,Integer>>();
		
		basicEdgeWithPoiInfo=new HashMap<Integer, Edge>();
		
		basicSeedAdjacencyComponent=new HashMap<Integer, HashMap<Integer,AdjVertexEntry>>();
		
		seedRegionWithPOIMap=new HashMap<Integer, Region>();
		
		indexStartOfinterminalVertex=-1;
		
		vertexToVertexAtEdge=new HashMap<Integer, HashMap<Integer,Integer>>();
		
		
		//加载SeedRegion
		loadSeedRegionWithPOIMap();
		
		//加载基础的vertexToRegionMap
		loadBaseAdjVertexRegionMap();
		
		//加载加载基础的adj 针对Region中的Regular Vertex
		loadBasicSeedAdjacencyVertexRegionMap();
		
		
		
		
	}
	
	
	
	
	public void loadSeedRegionWithPOIMap()
	{
		LoadSeedRegionWithPOI seedRegionLoad=new LoadSeedRegionWithPOI();
	
		seedRegionWithPOIMap=seedRegionLoad.getSeedRegionWithPOI();
		basicEdgeWithPoiInfo=seedRegionLoad.basicEdgeWithPoiInfo;
		vertexToVertexAtEdge=seedRegionLoad.vertexToVertexAtEdge;
	}
	
	/**
	 * seedRegionLevel
	 * each seedRegion only have one Edge 
	 * @return
	 */
	public void loadBaseAdjVertexRegionMap()
	{
		for(Integer seedRegionID:seedRegionWithPOIMap.keySet())
		{
			//if seedRegion data is right,Only one Edge exits in Edges
			if(seedRegionWithPOIMap.get(seedRegionID).edges.size()==1)
			{
				Edge edge = null;
				for(Integer edID:seedRegionWithPOIMap.get(seedRegionID).edges.keySet())
				{
					edge=basicEdgeWithPoiInfo.get(edID);
				}
				if(edge==null)
				{
					System.out.println("dd");
				}
				
				VertexPoint v1=edge.v1;
				VertexPoint v2=edge.v2;
				
				int v1ID=v1.id;
				int v2ID=v2.id;
//				int dist=DistanceHelp.getRealDistanceToVertex(v1, v2);
				int dist=0;
				
				Region tmpRegion=seedRegionWithPOIMap.get(seedRegionID);
				
				if(!basicVertexRegionIDMap.containsKey(v1ID))
				{
//					basicVertexRegionMap.put(v1ID, new HashMap<Integer, Region>());
				
					basicVertexRegionIDMap.put(v1ID, new HashMap<Integer, Integer>());
				}
//					basicVertexRegionMap.get(v1ID).put(seedRegionID, tmpRegion);
					
					basicVertexRegionIDMap.get(v1ID).put(seedRegionID, dist);
				if(!basicVertexRegionIDMap.containsKey(v2ID))
				{
//					basicVertexRegionIDMap.put(v2ID, new HashMap<Integer, Region>());
					
					basicVertexRegionIDMap.put(v2ID, new HashMap<Integer, Integer>());
				}
//				basicVertexRegionIDMap.get(v2ID).put(seedRegionID, tmpRegion);
					
					basicVertexRegionIDMap.get(v2ID).put(seedRegionID, dist);
				
				
			}
			else
			{
				System.err.println("SeedRegion Data Error");
			}
		}
//		
//		IndexHelp indexTest=new IndexHelp();
//		IndexFind test=indexTest.findMaxMinIndex(seedRegionWithPOIMap.keySet());
//		test.printMaxMin();
		return ;
	}
	
	/**
	 * 由于basicRegion没有包含的Vertex所以没有RegularVertex
	 * @author root
	 * @category load The BasicSeedAdjacencyVertexRegionMap
	 * 
	 */
	public void loadBasicSeedAdjacencyVertexRegionMap()
	{
//		System.out.println("before"+basicSeedAdjacencyComponent.size());
		
		//由于basicVertexRegionMap中vertex就是所连接的Region的border
		
		//获得interminalVertexStartIndex；
		indexStartOfinterminalVertex=IndexHelp.FindTheMax(seedRegionWithPOIMap.keySet())+1;
		//中间Vertex计数
		int enteryID=indexStartOfinterminalVertex;
		
		
//		int interminalAdjCount=0;
		
//		HashMap<Integer, HashMap<Integer, Integer>>
		
		for(Integer borderVertexID:basicVertexRegionIDMap.keySet())
		{
//			int countOfRelatedRegion=0;
//			countOfRelatedRegion=basicVertexRegionIDMap.get(borderVertexID).size();//获得相连的Region的大小
			//就是可以直接作为Adjacency
//			if(countOfRelatedRegion>1)
//			{
				
				//对于VRi创建InterminalAdjacency
				for(Integer regID:basicVertexRegionIDMap.get(borderVertexID).keySet())
				{
					
					if(!basicSeedAdjacencyComponent.containsKey(borderVertexID))
					{
//						interminalAdjCount++;
						basicSeedAdjacencyComponent.put(borderVertexID, new HashMap<Integer, AdjVertexEntry>());
					}
					//创建一个adjacencyFileEntry
					AdjVertexEntry adjVE=new AdjVertexEntry(enteryID,enteryID,Configure.RELEVANT_TYPE_INTERMEDARY,regID,0);
					
					
					
					basicSeedAdjacencyComponent.get(borderVertexID).put(enteryID, adjVE);
					//对于InterminalAdjacency将与其相关联的Regular添加进去
					
					//保存底层Region数据
					if(!basicSeedAdjacencyComponent.containsKey(regID))
					{
						if(seedRegionWithPOIMap.containsKey(regID))
						{
							//保存数据
							
							if(!basicSeedAdjacencyComponent.containsKey(enteryID))
							{
//								//borderVertexID  顶端
								
								int adjEdgeID_2=-1;
								
								int adjVertexID_2=-1;
								
								int adjdistance_2=-1;
								
								for(Integer ttEdgeId:seedRegionWithPOIMap.get(regID).edges.keySet())
								{
									adjEdgeID_2=ttEdgeId;
								}
								if(adjEdgeID_2==-1)
								{
									continue;
								}
								
								if(basicEdgeWithPoiInfo.containsKey(adjEdgeID_2))
								{
									Edge ttEdge=basicEdgeWithPoiInfo.get(adjEdgeID_2);
									if(ttEdge.v1.id==borderVertexID)
									{
										adjVertexID_2=ttEdge.v2.id;
									}
									
									if(ttEdge.v2.id==borderVertexID)
									{
										adjVertexID_2=ttEdge.v1.id;
												
									}
									adjdistance_2=DistanceHelp.getRealDistanceToVertex(ttEdge.v1,ttEdge.v2);
									
								}
								
								if(adjdistance_2==-1)
								{
									System.out.println("ADJ:wrong at:border:"+borderVertexID+"\t Border:"+adjVertexID_2+"\tedge"+adjEdgeID_2);
									continue;
								}
								
								if(adjVertexID_2!=-1)
								{
									AdjVertexEntry regAdjEntery_2=new AdjVertexEntry(adjVertexID_2, adjVertexID_2, Configure.RELEVANT_TYPE_REGULAR_VERTEX, adjEdgeID_2, adjdistance_2);
									if(!basicSeedAdjacencyComponent.containsKey(enteryID))
									{
										basicSeedAdjacencyComponent.put(enteryID,new HashMap<Integer,AdjVertexEntry>());
									}
									basicSeedAdjacencyComponent.get(enteryID).put(adjVertexID_2, regAdjEntery_2);
								}
							}
						}
						else
						{
							System.out.println("not Region "+regID);
						}
					}
					
					
					enteryID++;
				
				}	
				
				
				
				
				
				
//				//对于其他的Border直接添加Direct Connection
//				for(Region relatedRegion:basicVertexRegionMap.get(borderVertexID).values())
//				{
//					HashMap<Integer, String> borders=relatedRegion.borderVertexs;
//					for(Integer dircBorderIden:borders.keySet())
//					{
//						if(dircBorderIden!=borderVertexID)
//						{
//							if(!basicSeedAdjacencyComponent.containsKey(borderVertexID))
//							{
//								basicSeedAdjacencyComponent.put(borderVertexID, new HashMap<Integer, AdjVertexEntry>());
//							}
//							int directEdgeID=-1;
//							//安全验证一下是否包含
//							if(vertexToVertexAtEdge.containsKey(borderVertexID)&&vertexToVertexAtEdge.get(borderVertexID).containsKey(dircBorderIden))
//							{
//								directEdgeID=vertexToVertexAtEdge.get(borderVertexID).get(dircBorderIden);
//								
//							}
//							else
//							{
//								System.err.println("Wrong at directEdgeID"+directEdgeID);
//							}
//							//再次验证Region是否包含该Edge
//							if(!relatedRegion.edges.containsKey(directEdgeID))
//							{
//								System.err.println("wrong directEdgeID"+directEdgeID);
//							}
//							
//							AdjVertexEntry directAdjaEntry=new AdjVertexEntry(enteryID, enteryID, Configure.RELEVANT_TYPE_DIRECT_CONNECTION, directEdgeID, relatedRegion.edges.get(directEdgeID).Distance);
//							basicSeedAdjacencyComponent.get(borderVertexID).put(dircBorderIden, directAdjaEntry);
//						}
//					}
//				}
				
//			}
			
			
			
		}
//		System.out.println(interminalCount+"interminalCount");
//		System.out.println(interminalAdjCount+"interminalAdjCount");
		
//		for(Integer tmpRegionID:seedRegionWithPOIMap.keySet())
//		{
//			//获得borders
//			HashMap<Integer, String> regionBorders=seedRegionWithPOIMap.get(tmpRegionID).borderVertexs;
//			
//			HashMap<Integer, Integer> intermialVertex=new HashMap<Integer, Integer>();
//			HashMap<Integer,Integer>  directVertex=new HashMap<Integer, Integer>();
//			
//			//对border进行判断
//			for(Integer borderID:regionBorders.keySet())
//			{
//				int regionCount=0;
//				
//				
//				if(basicVertexRegionMap.containsKey(borderID))
//				{
//					//将连接多个Region的border设置为IntermialVertex
//					if(basicVertexRegionMap.get(borderID).size()>1)
//					{
//						intermialVertex.put(borderID, tmpRegionID);
//					}else
//					{
//						directVertex.put(borderID, tmpRegionID);
//					}
//		
//				}value
//			}
//			
//		}
//		
		
		
		
		
//		System.out.println("before"+basicSeedAdjacencyComponent.size());
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoadBasicVertexRegion test=new LoadBasicVertexRegion();
		System.out.println("ALL DONE");
//		test.loadSeedRegionWithPOIMap();
	}

}
