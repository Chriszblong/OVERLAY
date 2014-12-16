package interfaces.queryHanlder;

import interfaces.common.Scorer;
import interfaces.system.LineNumberReaderForUTF8;
import interfaces.system.TimeWatcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Vector;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

import elements.common.Edge;
import elements.common.InterestingPoint;
import elements.component.adjacency.AdjVertexEntry;
import elements.component.mappingInverted.InvertedFileEntry;
import elements.query.CandidateDoc;
import elements.query.CandidateVertex;
import elements.regionBTree.Region;

import Utils.Configure;
import Utils.IndexConfig;
import Utils.QueryConfigure;

public class SearchHandler {
	public DB db_AdjComponet;
	
	public DB db_Regions;
	
	public DB db_Edges;
	
	public DB db_Vertexs;
	
	public DB db_InvertedFiles;
	
	
	
	public String DB_AdjComponet_Name;
	
	public String DB_Regions_Name;
	
	public String DB_Edges_Name;
	
	public String DB_Vertexs_Name;
	
	public String DB_InvertedFile_Name;
	
	
	
	/*                         DB MAP                         */
	/*                         DB MAP                         */
	/**
	 * regionMap:<regionID,region>
	 */
	public HTreeMap<Integer, Region> db_RegionMap;
	/**
	 * <regionID,belongRegionID>
	 */
	public HTreeMap<Integer, Integer> db_RegionBelongMap;
	/**
	 * 
	 */
	public HTreeMap<Integer, Edge> db_BasicEdgeWithPoiInfo;
	
	/**
	 * 用来存储边和Region之间的关系的
	 * 
	 */
	public HTreeMap<Integer, HashMap<Integer, Integer>> db_VertexRegionIDMap;
	/**
	 * adjacency component
	 */
	public HTreeMap<Integer, HashMap<Integer, AdjVertexEntry>> db_AdjacencyComponetMap;

	/**
	 * MBR可能是Region也可能是Edge
	 * 这就要求Edge和region的Id不能有相同的
	 * <EdgeID or RegionID,<Term,MaxSroce>>
	 */
	public HTreeMap<Integer, HashMap<String, Float>> db_MBRTermMaxScoreMap;
	/**
	 * InvertedComponet
	 */
	public HTreeMap<Integer,HashMap<String, Vector<InvertedFileEntry>>> db_termInvertedFileMap;
	
	/**
	 * adjacent Vertex to Vertex
	 */
	public HTreeMap<Integer,HashMap<Integer, Integer>> db_VertexToVertexAtEdge;
	/**
	 * 
	 */
	
	
	
	
	//================================data To Operate======================//
	/**
	 * <regionID,belongRegionID>
	 */
//	public HashMap<Integer, Integer> load_regionBelongMap;
	/**
	 * regionMap:<regionID,region>
	 */
	public HashMap<Integer, Region> load_regionMap;

	
	public  HashMap<Integer, Edge> load_basicEdgeWithPoiInfo;
	
	public  HashMap<Integer, HashMap<Integer, Integer>> load_vertexToVertexAtEdge;
	/**
	 * adjacency component
	 */
	public HashMap<Integer, HashMap<Integer, AdjVertexEntry>> load_AdjacencyComponetMap;
	
	
//	public HashMap<K, V>
	
	public PriorityQueue<CandidateVertex> vertexMinHeap;
	/**
	 * adjVertex Visited
	 * <ID,Distance>
	 */
	public HashMap<Integer, Integer> query_AdjVertexVisited;
	/**
	 * Edge Visited
	 */
	public HashMap<Integer, Integer> query_EdgeVisited;
	/**
	 * region Visited
	 */
	public HashMap<Integer, Integer> query_RegionVisited;
	/**
	 * VertexToVertexAtEdge
	 */
	public HashMap<Integer, HashMap<Integer, Integer>> query_vertexToVertexAtEdge;
	
	/**
	 * Top-K Doc
	 */
	public  PriorityQueue<CandidateDoc> topKResult;
	
	/**
	 * Candidate
	 */
//	public HashMap<Integer, CandidateDoc> candidates;
	
	public int K;
	
	
	public int ItemAccess=0;
	
	public long loadTimeOfAdj=0;
	
	public long loadTimeOfRegion=0;
	
	public long loadTimeOfEdge=0;
	
	
	private TimeWatcher watcher;
	
//	private TimeWatcher queryTimeOVER;
	
	
	public SearchHandler() {
		this.DB_AdjComponet_Name=Configure.PROJECT_DATA_ROOT+IndexConfig.INDEX_DB_ADJ_NAME;
		
		this.DB_Regions_Name=Configure.PROJECT_DATA_ROOT+IndexConfig.INDEX_DB_REGION_NAME;
		
		this.DB_Edges_Name=Configure.PROJECT_DATA_ROOT+IndexConfig.INDEX_DB_EDGE_NAME;
		
		this.DB_Vertexs_Name=Configure.PROJECT_DATA_ROOT+IndexConfig.INDEX_DB_VERTEX_NAME;
		
		this.DB_InvertedFile_Name=Configure.PROJECT_DATA_ROOT+IndexConfig.INDEX_DB_INVERTEDFILE_NAME;
		
//		this.queryTimeOVER=new TimeWatcher();
		this.watcher=new TimeWatcher();
		loadDB();
		db_AdjacencyComponetMap=db_AdjComponet.getHashMap(IndexConfig.DB_ADJACENCY_COMPONENT_MAP_HASHNAME);
		db_MBRTermMaxScoreMap=db_InvertedFiles.getHashMap(IndexConfig.DB_MBR_TERM_MAX_SCORE_MAP_HASHNAME);
		db_RegionBelongMap=db_Regions.getHashMap(IndexConfig.DB_REGION_BELONG_MAP_HASHNAME);
		db_RegionMap=db_Regions.getHashMap(IndexConfig.DB_REGION_MAP_HASHNAME);
		db_termInvertedFileMap=db_InvertedFiles.getHashMap(IndexConfig.DB_TERM_INVERTED_FILE_MAP_HASHNAME);
		db_VertexRegionIDMap=db_Vertexs.getHashMap(IndexConfig.DB_VERTEX_REGIONID_MAP_HASHNAME);
		db_VertexToVertexAtEdge=db_Vertexs.getHashMap(IndexConfig.DB_VERTEX_TO_VERTEX_AT_EDGE_HASHNAME);
		db_BasicEdgeWithPoiInfo=db_Edges.getHashMap(IndexConfig.DB_BASIC_EDGE_WITH_INFO_HASHNAME);
		
//		
//		load_regionBelongMap=new HashMap<Integer, Integer>();
		load_regionMap=new HashMap<Integer, Region>();
		load_basicEdgeWithPoiInfo=new HashMap<Integer, Edge>();
		load_vertexToVertexAtEdge=new HashMap<Integer, HashMap<Integer,Integer>>();
		load_AdjacencyComponetMap=new HashMap<Integer, HashMap<Integer,AdjVertexEntry>>();
		watcher.start();
//		LoadBaseData();
		
		//init query data
		vertexMinHeap=new PriorityQueue<CandidateVertex>();
		
		query_AdjVertexVisited=new HashMap<Integer, Integer>();
		
		query_EdgeVisited=new HashMap<Integer, Integer>();
		
		query_RegionVisited=new HashMap<Integer, Integer>();
		
		query_vertexToVertexAtEdge=new HashMap<Integer, HashMap<Integer,Integer>>();
		
		topKResult=new PriorityQueue<CandidateDoc>();
		
//		candidates=new HashMap<Integer, CandidateDoc>();
		
		watcher.stop();
		watcher.seeRunTime("LoadBaseData");
	}
	
	
	public void  LoadBaseData()
	{
		
//		System.out.println(db_BasicEdgeWithPoiInfo.keySet());
//		load_basicEdgeWithPoiInfo.putAll(db_BasicEdgeWithPoiInfo.get(IndexConfig.MAP_DB_FLAG));
		
	}

	public void loadDB()
	{
		//Step 1:
		System.out.println("loadDB "+DB_AdjComponet_Name+"begin");
		watcher.start();
//		System.out.println();
		File dbFile1=new File(DB_AdjComponet_Name);
		db_AdjComponet=DBMaker.newFileDB(dbFile1)
				.transactionDisable()
				.closeOnJvmShutdown()
				.make();
		watcher.stop();
		
		System.out.println("loadDB "+DB_AdjComponet_Name+"end");
		watcher.seeRunTime();
		
		//Step 2:
		System.out.println("loadDB"+DB_Regions_Name+" begin");
		watcher.start();
		File dbFile2=new File(DB_Regions_Name);
		db_Regions=DBMaker.newFileDB(dbFile2)
				.transactionDisable()
				.closeOnJvmShutdown()
				.make();
		watcher.stop();
				
		System.out.println("loadDB"+DB_Regions_Name+" end");
		watcher.seeRunTime();
		//Step 3:
		System.out.println("loadDB begin");
		watcher.start();
		File dbFile3=new File(DB_Edges_Name);
		db_Edges=DBMaker.newFileDB(dbFile3)
				.transactionDisable()
				.closeOnJvmShutdown()
				.make();
		watcher.stop();

		System.out.println("loadDB end");
		watcher.seeRunTime();
		//Step 4:
		System.out.println("loadDB "+DB_Vertexs_Name+"begin");
		watcher.start();
		File dbFile4=new File(DB_Vertexs_Name);
		db_Vertexs=DBMaker.newFileDB(dbFile4)
				.transactionDisable()
				.closeOnJvmShutdown()
				.make();
		watcher.stop();

		System.out.println("loadDB "+DB_Vertexs_Name+" end");
				watcher.seeRunTime();
		//Step 4:
		System.out.println("loadDB "+DB_InvertedFile_Name+"begin");
		watcher.start();
		File dbFile5=new File(DB_InvertedFile_Name);
		db_InvertedFiles=DBMaker.newFileDB(dbFile5)
				.transactionDisable()
				.closeOnJvmShutdown()
				.make();
		watcher.stop();

		System.out.println("loadDB "+DB_InvertedFile_Name+" end");
		watcher.seeRunTime();				
		
	}
	
	public void query(String queryFile) throws IOException
	{
//		if(true)
//		{
//			return;
//		}
		System.err.println("Top-K:"+K);
		System.err.println("ALPHA:"+QueryConfigure.ALPHA);
		LineNumberReader lineReader=new LineNumberReaderForUTF8(queryFile).lineReader;
		
		FileWriter resultFileWriter=new FileWriter(QueryConfigure.QUERY_RESULT_FOLDER_PATH+QueryConfigure.QUERY_TYPE_FACTOR+"/SF_"+QueryConfigure.KWDNUM+"_"+QueryConfigure.ALPHA+"_"+QueryConfigure.TOPK+"_"+QueryConfigure.INDEXNUM+"_"+QueryConfigure.MAX_SPATIAL_DISTANCE+".txt");
		String line;
		int queryNum=0;
		long queryTime=0;
		int quFlag=  20 ;
		int step=20;
		while((line=lineReader.readLine())!=null&&queryNum<quFlag)
		{
			if(queryNum<(quFlag-step))
			{
				queryNum++;
				continue;
			}
//			if(queryNum==7)
//			{
//				queryNum++;
//				continue;
//			}
			String[] parts=line.split(",");
			int queryVertex=Integer.parseInt(parts[0]);
			double lon=Double.parseDouble(parts[1]);
			double lan=Double.parseDouble(parts[2]);
			Vector<String> keyWords=new Vector<String>();
			
			
			for(int i=3;i<parts.length;i+=2)
			{
//				String term=parts[i];
				keyWords.add(parts[i]);
//				int freq=Integer.parseInt(parts[i+1]);

			}
			System.out.println("=========================================");
			System.out.println("关键字个数：\t"+(parts.length-3)/2);
//			System.out.println(queryNum+"\t:\t"+line);
			long start=System.currentTimeMillis();
			queryProcessor(queryNum,queryVertex,keyWords,K);
			long end=System.currentTimeMillis();
			queryTime+=end-start;
//			printTopK(line);
			
			queryNum++;
//			System.out.println("queryTime:"+queryTime/queryNum*1.0);
//			System.out.println("loadTime Of ADJ"+loadTimeOfAdj/queryNum*1.0);
//			System.out.println("loadTime Of REGION"+loadTimeOfRegion/queryNum*1.0);
//			System.out.println("loadTime Of EDGE"+loadTimeOfEdge/queryNum*1.0);
//			System.out.println("IndexLoadTime"+(loadTimeOfAdj+loadTimeOfEdge+loadTimeOfRegion)/queryNum*1.0);
//			System.out.println("CPU Time"+(queryTime-loadTimeOfAdj-loadTimeOfEdge-loadTimeOfRegion)/queryNum*1.0);
//			System.out.println("Avg Item Access"+ItemAccess/queryNum*1.0);
			resultFileWriter.write(queryNum+":\t"+(end-start));
			System.out.println(queryNum+":\t"+(end-start));
//			break;
		
		}
		System.out.println("=====================================");
		System.out.println("=====================================");
		System.out.println("=====================================");
//		System.err.println("queryTime:\t"+queryTime/queryNum*1.0);
		System.err.println("queryTime:\t"+queryTime/1*1.0);
		System.err.println("loadTime Of ADJ:\t"+loadTimeOfAdj/queryNum*1.0);
		System.err.println("loadTime Of REGION:\t"+loadTimeOfRegion/queryNum*1.0);
		System.err.println("loadTime Of EDGE:\t"+loadTimeOfEdge/queryNum*1.0);
		System.err.println("IndexLoadTime:\t"+(loadTimeOfAdj+loadTimeOfEdge+loadTimeOfRegion)/queryNum*1.0);
		System.err.println("CPU Time:\t"+(queryTime-loadTimeOfAdj-loadTimeOfEdge-loadTimeOfRegion)/queryNum*1.0);
		System.err.println("Avg Item Access:\t"+ItemAccess/queryNum*1.0);
		
		System.out.println("queryNum:"+queryNum);
		queryNum=1;
		
		
		resultFileWriter.write("queryTime:\t"+queryTime/queryNum*1.0+"\r\n");
		resultFileWriter.write("loadTime Of ADJ:\t"+loadTimeOfAdj/queryNum*1.0+"\r\n");
		resultFileWriter.write("loadTime Of REGION:\t"+loadTimeOfRegion/queryNum*1.0+"\r\n");
		resultFileWriter.write("loadTime Of EDGE:\t"+loadTimeOfEdge/queryNum*1.0+"\r\n");
		resultFileWriter.write("IndexLoadTime:\t"+(loadTimeOfAdj+loadTimeOfEdge+loadTimeOfRegion)/queryNum*1.0+"\r\n");
		resultFileWriter.write("CPU Time:\t"+(queryTime-loadTimeOfAdj-loadTimeOfEdge-loadTimeOfRegion)/queryNum*1.0+"\r\n");
		resultFileWriter.write("Avg Item Access:\t"+ItemAccess/queryNum*1.0+"\r\n");
		resultFileWriter.write("queryNum:\t"+queryNum+"\r\n");
		resultFileWriter.close();
		
	}
	
	
	
	public void printTopK(String line)
	{
		CandidateDoc cand=null;
//		System.out.println("===========================================");
////		System.out.println("query Line:\t"+line);
//		System.out.println("Result:");
////		PriorityQueue<CandidateDoc> tmp=new PriorityQueue<CandidateDoc>();
		while(topKResult.size()>0)
		{
			
			cand=topKResult.poll();
			System.out.println("doc："+cand.docID+"\t======Scores:"+cand.score);
		}
////		topKResult=tmp;
//		System.out.println("===========================================");
	}
	public void printTopK2()
	{
		CandidateDoc cand=null;
		System.out.println("===========================================");
		System.out.println("Result:");
		PriorityQueue<CandidateDoc> tmp=new PriorityQueue<CandidateDoc>();
		while(topKResult.size()>0)
		{
			cand=topKResult.poll();
			tmp.add(cand);
			System.out.println("doc："+cand.docID+"\t======Scores:"+cand.score+"==============="+cand.distance);
		}
		topKResult=tmp;
		
		System.out.println("ItemAcess:"+ItemAccess);
		System.out.println("===========================================");
	}
	
	public void queryProcessor(int queryNum, int qVertex, Vector<String> keyWords, int k)
	{
		//初始化最近的Vertex
//		ItemAccess=0;
//		loadTimeOfAdj=0;
//		loadTimeOfEdge=0;
//		loadTimeOfRegion=0;
		
		initQueryData(qVertex,keyWords, k);
		
//		queryTimeOVER.start();
		CandidateVertex currentAdjVertex=null;
		
		HashMap<Integer, AdjVertexEntry> relatedAdjVertexsMap=new HashMap<Integer, AdjVertexEntry>();
		
		while((currentAdjVertex=vertexMinHeap.poll())!=null)
		{
			//判断其是否已经访问过
//			System.err.println(currentAdjVertex.adjVertexID);
			if(query_AdjVertexVisited.containsKey(currentAdjVertex.adjVertexID))
			{
				continue;
			}
			
//			if(currentAdjVertex.distance>QueryConfigure.MAX_SPATIAL_DISTANCE)
//			{
//				
//				System.out.println("提前结束");
//				printTopK("s");
//				break;
//				
//			}
//			queryTimeOVER.seeCurrentRunTime();
			//将该adj设置已访问
			query_AdjVertexVisited.put(currentAdjVertex.adjVertexID, currentAdjVertex.distance);
			
//			System.out.println("vertexMinHeap size"+vertexMinHeap.size());
//			//test
//			if(vertexMinHeap.size()>100)
//			{
//				break;
//			}
			
			//currentAdjVertex  Algorithm Line4:v<-N.pop
			
			//先假设文本得分为1  判断空间得分是否满足查询条件  Algorithm Line5:e<-1/(1+a*d(v.l+q.l))
			float tmpScore=0;
			if(QueryConfigure.SCORE_TYPE==QueryConfigure.SCORE_TYPE_1)
			{
				tmpScore=1/(1+Scorer.getSpatialScore(currentAdjVertex.distance));
			}
			else
			{
				 tmpScore=(1-QueryConfigure.ALPHA)+Scorer.getSpatialScore(currentAdjVertex.distance);
			}
			
			
			

			
			//假设当前估计最大得分比TOP-K的最小值还小的话，直接结束
			if(tmpScore<topKResult.peek().score)
			{
				break;
			}
			
			//清理之前得到的数据
			relatedAdjVertexsMap.clear();
			
			//获取当前数据
//			System.out.println(currentAdjVertex.adjVertexID);
			watcher.start();
			relatedAdjVertexsMap.putAll(db_AdjacencyComponetMap.get(currentAdjVertex.adjVertexID));
			loadTimeOfAdj+=watcher.getRunTime();
//			printTempAdj(currentAdjVertex, relatedAdjVertexsMap);
			
			if(relatedAdjVertexsMap.size()>0)
			{
				//在针对其adj进行查询
//				System.out.println("here");
				for(Integer adjVertexU:relatedAdjVertexsMap.keySet())
				{
					//对于没有进行访问的adjVertex才进行查询
					if(!query_AdjVertexVisited.containsKey(adjVertexU))
					{
						//判断其是否满足查询要求
						if(relatedAdjVertexsMap.get(adjVertexU).relevantType==Configure.RELEVANT_TYPE_INTERMEDARY)
						{
							//判断得到相应的Regin
							if(query_RegionVisited.containsKey(relatedAdjVertexsMap.get(adjVertexU).relevantID))
							{
								continue;
							}
							watcher.start();
							Region tmpRegionR=db_RegionMap.get(relatedAdjVertexsMap.get(adjVertexU).relevantID);
							loadTimeOfRegion+=watcher.getRunTime();
							if(tmpRegionR!=null)
							{
								//判断其是否满足查询要求
								//先判断是否满足要求
								float tmpRegionMaxScore=0;
								if(tmpRegionR.edges.size()==1)
								{
									//
//									System.out.println("直接访问");
									for(int i=0;i<keyWords.size();i++)
									{
											if(tmpRegionR.keywordsMaxScore.containsKey(keyWords.get(i)))
											{
												tmpRegionMaxScore+=tmpRegionR.keywordsMaxScore.get(keyWords.get(i));
											}
									}
									if(tmpRegionMaxScore<=0.0)
									{
//										System.out.println("3");
										continue;
									}
									if(QueryConfigure.SCORE_TYPE==QueryConfigure.SCORE_TYPE_1)
									{
									
										tmpRegionMaxScore=tmpRegionMaxScore/(1+Scorer.getSpatialScore(currentAdjVertex.distance));
									}
									else
									{
										tmpRegionMaxScore=(1-QueryConfigure.ALPHA)*tmpRegionMaxScore+Scorer.getSpatialScore(currentAdjVertex.distance);
									}
									if(tmpRegionMaxScore<=topKResult.peek().score)
									{
										continue;
									}
//									System.out.println("tmpRegionMaxScore"+tmpRegionMaxScore+"=============peek:"+topKResult.peek().score);
									
									for(Integer edgeQID:tmpRegionR.edges.keySet())
									{
										if(query_EdgeVisited.containsKey(edgeQID))
										{
											continue;
										}
										watcher.start();
										Edge targetEdge=db_BasicEdgeWithPoiInfo.get(edgeQID);
										loadTimeOfEdge+=watcher.getRunTime();
										
										
										if(targetEdge!=null)
										{
											for(Integer poiIID:targetEdge.poiMap.keySet())
											{
												
												InterestingPoint tmpPOI=targetEdge.poiMap.get(poiIID);
												float poiScore=0;
												for(int i=0;i<keyWords.size();i++)
												{
													if(tmpPOI.keywords.containsKey(keyWords.get(i)))
													{
														poiScore+=tmpPOI.keywords.get(keyWords.get(i));
													}
												}
												if(poiScore>0)
												{
													
													if(QueryConfigure.SCORE_TYPE==QueryConfigure.SCORE_TYPE_1)
													{
														poiScore=poiScore/(1+Scorer.getSpatialScore(currentAdjVertex.distance));
													}else
													{
														poiScore=(1-QueryConfigure.ALPHA)*poiScore+Scorer.getSpatialScore(currentAdjVertex.distance);
													}
													if(poiScore>topKResult.peek().score)
													{
														CandidateDoc cand=new CandidateDoc(poiIID, poiScore,currentAdjVertex.distance);
//														System.out.println("distance "+currentAdjVertex.distance);
														
														updateTopK(cand);
													}
												}
												ItemAccess++;
												
												
												
											}
											
											if(currentAdjVertex.adjVertexID==targetEdge.v1.id)
											{
												if(!query_AdjVertexVisited.containsKey(targetEdge.v2.id))
												{
													vertexMinHeap.add(new CandidateVertex(targetEdge.v2.id, currentAdjVertex.distance+targetEdge.Distance));
												}
												
											}else
											{
												if(currentAdjVertex.adjVertexID==targetEdge.v2.id)
												{
													if(!query_AdjVertexVisited.containsKey(targetEdge.v1.id))
													{
														vertexMinHeap.add(new CandidateVertex(targetEdge.v1.id, currentAdjVertex.distance+targetEdge.Distance));
													}
												}
												else
												{
													if(!query_AdjVertexVisited.containsKey(targetEdge.v1.id))
													{
														vertexMinHeap.add(new CandidateVertex(targetEdge.v1.id, currentAdjVertex.distance+targetEdge.Distance));
													}
													if(!query_AdjVertexVisited.containsKey(targetEdge.v2.id))
													{
														vertexMinHeap.add(new CandidateVertex(targetEdge.v2.id, currentAdjVertex.distance+targetEdge.Distance));
													}
												}
												
											}
											query_EdgeVisited.put(edgeQID, currentAdjVertex.distance);
//											System.out.println("ssssssssss"+currentAdjVertex.adjVertexID+"======"+edgeQID);
//											System.out.println(targetEdge.Distance+"==================="+targetEdge.v1.id+"============"+targetEdge.v2.id);
//											return;
											
										}else
										{
											System.out.println("no Edge:"+edgeQID);
										}
										
									}
									
									
								}else{
										
//									System.out.println("Deep web");
									
									for(int i=0;i<keyWords.size();i++)
									{
											if(tmpRegionR.keywordsMaxScore.containsKey(keyWords.get(i)))
											{
												tmpRegionMaxScore+=tmpRegionR.keywordsMaxScore.get(keyWords.get(i));
											}
									}
									tmpRegionMaxScore=tmpRegionMaxScore/(1+Scorer.getSpatialScore(currentAdjVertex.distance));
//									tmpRegionMaxScore=(1-QueryConfigure.ALPHA)*tmpRegionMaxScore+Scorer.getSpatialScore(currentAdjVertex.distance);
									if(tmpRegionMaxScore>topKResult.peek().score)
									{
											vertexMinHeap.add(new CandidateVertex(adjVertexU, currentAdjVertex.distance));
									}
								}
								
							}
						}else if(relatedAdjVertexsMap.get(adjVertexU).relevantType==Configure.RELEVANT_TYPE_DIRECT_CONNECTION)
						{
							
							vertexMinHeap.add(new CandidateVertex(adjVertexU, currentAdjVertex.adjVertexID+relatedAdjVertexsMap.get(adjVertexU).distance));			
						}else if(relatedAdjVertexsMap.get(adjVertexU).relevantType==Configure.RELEVANT_TYPE_REGULAR_VERTEX)
						{
//							System.out.println("ss");
							
							int edgeID=relatedAdjVertexsMap.get(adjVertexU).relevantID;
							if(query_EdgeVisited.containsKey(edgeID))
							{
								continue;
							}
							watcher.start();
							Edge targetEdge=db_BasicEdgeWithPoiInfo.get(edgeID);
							loadTimeOfEdge+=watcher.getRunTime();
							if(targetEdge!=null)
							{
								
								
								
								//先判断时候有kwds
								
								float tempMaxScore=0;
								for(int i=0;i<keyWords.size();i++)
								{
									if (targetEdge.keywordsMaxScoreMap.containsKey(keyWords)) {
										tempMaxScore+=targetEdge.keywordsMaxScoreMap.get(keyWords.get(i));
									}
								}
								if(tempMaxScore<=0)
								{
									
//									System.out.println(targetEdge.keywordsMaxScoreMap.keySet().toString());
//									return;
									continue;
								}
								if(QueryConfigure.SCORE_TYPE==QueryConfigure.SCORE_TYPE_1)
								{
//								
									tempMaxScore=tempMaxScore/(1+Scorer.getSpatialScore(currentAdjVertex.distance));
								
								}
								else
								{
									tempMaxScore=(1-QueryConfigure.ALPHA)*tempMaxScore+Scorer.getSpatialScore(currentAdjVertex.distance);
								}
								if(tempMaxScore<=topKResult.peek().score)
								{
									continue;
								}
								
								for(Integer poiIID:targetEdge.poiMap.keySet())
								{
									InterestingPoint tmpPOI=targetEdge.poiMap.get(poiIID);
									float poiScore=0;
									for(int i=0;i<keyWords.size();i++)
									{
										if(tmpPOI.keywords.containsKey(keyWords.get(i)))
										{
											poiScore+=tmpPOI.keywords.get(keyWords.get(i));
										}
									}
									if(poiScore>0)
									{
										poiScore=poiScore/(1+Scorer.getSpatialScore(currentAdjVertex.distance));
//										poiScore=(1-QueryConfigure.ALPHA)*poiScore+Scorer.getSpatialScore(currentAdjVertex.distance);
										if(poiScore>topKResult.peek().score)
										{
											CandidateDoc cand=new CandidateDoc(poiIID, poiScore,currentAdjVertex.distance);
//											System.out.println("distance "+currentAdjVertex.distance);
											updateTopK(cand);
//											printTopK2();
										}
									}
									
									
									
									ItemAccess++;
									
								}
								
								if(currentAdjVertex.adjVertexID==targetEdge.v1.id)
								{
									if(!query_AdjVertexVisited.containsKey(targetEdge.v2.id))
									{
										vertexMinHeap.add(new CandidateVertex(targetEdge.v2.id, currentAdjVertex.distance+targetEdge.Distance));
									}
									
								}else
								{
									if(currentAdjVertex.adjVertexID==targetEdge.v2.id)
									{
										if(!query_AdjVertexVisited.containsKey(targetEdge.v1.id))
										{
											vertexMinHeap.add(new CandidateVertex(targetEdge.v1.id, currentAdjVertex.distance+targetEdge.Distance));
										}
									}
									else
									{
										if(!query_AdjVertexVisited.containsKey(targetEdge.v1.id))
										{
											vertexMinHeap.add(new CandidateVertex(targetEdge.v1.id, currentAdjVertex.distance+targetEdge.Distance));
										}
										if(!query_AdjVertexVisited.containsKey(targetEdge.v2.id))
										{
											vertexMinHeap.add(new CandidateVertex(targetEdge.v2.id, currentAdjVertex.distance+targetEdge.Distance));
										}
									}
								}
								query_EdgeVisited.put(targetEdge.id,currentAdjVertex.distance);
								
							}else
							{
								System.out.println("LAB DATA Error");
							}
							
							
//							System.out.println(currentAdjVertex.adjVertexID+"======"+adjVertexU);
//							return;
//							printTopK2();
						}else
						{
							System.out.println("Why here："+relatedAdjVertexsMap.get(adjVertexU).relevantType);
						}
						
//						query_AdjVertexVisited.put(currentAdjVertex.adjVertexID, currentAdjVertex.distance+relatedAdjVertexsMap.get(adjVertexU).distance);
					}
				}
			}else
			{
				System.out.println("here 2");
//				//找到相应的Edge
//				HashMap<Integer, Integer> tmpVertexToVertexEdge=db_VertexToVertexAtEdge.get(currentAdjVertex.adjVertexID);
//				
//				if(tmpVertexToVertexEdge==null)
//				{
//					System.out.println("here3");
//					continue;
//				}
//				System.out.println(tmpVertexToVertexEdge.keySet().size());
//				for(Integer tmpVertexID:tmpVertexToVertexEdge.keySet())
//				{
//					if(tmpVertexID==currentAdjVertex.adjVertexID)
//					{
//						System.out.println("pass 1");
//						continue;
//					}
//					if(query_AdjVertexVisited.containsKey(tmpVertexID))
//					{
//						System.out.println("pass 2");
//						continue;
//					}
//					System.out.println("here 4："+tmpVertexID);
//					
//					Edge tmpEdge=db_BasicEdgeWithPoiInfo.get(tmpVertexToVertexEdge.get(tmpVertexID));
//					
//					if(tmpEdge==null)
//					{
//						System.out.println("here 5");
//					}
//					else
//					{
//						System.out.println("here 6");
//						vertexMinHeap.add(new CandidateVertex(tmpVertexID, currentAdjVertex.distance+DistanceHelp.getRealDistanceToVertex(tmpEdge.v1, tmpEdge.v2)));
//						
//					}
//					
//
//				}
				
				
				
			}
		
			
		}
//		printTopK();
	}
	
	
	public void printTempAdj(CandidateVertex currentAdjVertex,HashMap<Integer, AdjVertexEntry> relatedAdjVertexsMap)
	{
		System.out.println("VID:"+currentAdjVertex.adjVertexID);
		for(Integer idII:relatedAdjVertexsMap.keySet())
		{
			System.out.println("adjID:"+idII+"=="+relatedAdjVertexsMap.get(idII).relevantType+"==="+relatedAdjVertexsMap.get(idII).relevantID);
		}
	}
	
	public void updateTopK(CandidateDoc cand)
	{
		
		
		if(topKResult.contains(cand))
		{
			topKResult.remove(cand);
			topKResult.add(cand);
		}else
		{
			topKResult.poll();
			topKResult.add(cand);
		}
		if(ItemAccess%10==0)
		{
//			System.out.println("Accessing "+ItemAccess+"Item");
//			printTopK2();
		}
//		System.out.println(ItemAccess);
//		System.out.println(topKResult.peek().score);
//		printTopK2();
	}
	
	public void initQueryData(int queryVertex, Vector<String> keyWords, int topkNum)
	{
		
		vertexMinHeap.add(new CandidateVertex(queryVertex, 0));
		
		//init TopK  
		
		System.out.println("Init");
		topKResult.clear();
		
		query_AdjVertexVisited.clear();
		query_EdgeVisited.clear();
		query_RegionVisited.clear();
		query_vertexToVertexAtEdge.clear();
		System.out.println(query_AdjVertexVisited.size());
		for(int i=0;i<topkNum;i++)
		{
			topKResult.add(new CandidateDoc());
		}
		
		
		
	}
	
	public void testFunction(int testId)
	{
		if(!load_AdjacencyComponetMap.containsKey(testId))
		{
			load_AdjacencyComponetMap.put(testId, new HashMap<Integer, AdjVertexEntry>());
		}
		watcher.start();
		load_AdjacencyComponetMap.get(testId).putAll(db_AdjacencyComponetMap.get(testId));
		watcher.stop();
		watcher.seeRunTime("load_AdjacencyComponetMap");
		for(AdjVertexEntry term:load_AdjacencyComponetMap.get(testId).values())
		{
			
				System.out.println("id/t"+term.id+"\t========RELEVANTTYPE:"+term.relevantType+"/t===================relevantID/t"+term.relevantID+"/t=================Distance/t"+term.distance);	
//			}
			
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SearchHandler test=new SearchHandler();
		test.K=QueryConfigure.TOPK;
//		Scorer.alpha=QueryConfigure.ALPHA;
		
		try {
			String queryFIle=QueryConfigure.QUERY_DATA_FOLDER_PATH+QueryConfigure.INDEXNUM+"/"+QueryConfigure.KWDNUM+".txt";
			System.out.println(queryFIle);
			test.query(queryFIle);
//			test.testFunction(631531);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
