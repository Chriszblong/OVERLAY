


import interfaces.common.AggregateCost;
import interfaces.common.DistanceHelp;
import interfaces.common.LoadBasicVertexRegion;
import interfaces.mappingInverted.LoadBasicMappingInvertedComponent;
import interfaces.system.PrintMaxMinIndex;
import interfaces.system.TimeWatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Vector;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

import Utils.Configure;
import Utils.IndexConfig;
import elements.common.Edge;
import elements.component.adjacency.AdjVertexEntry;
import elements.component.mappingInverted.InvertedFileEntry;
import elements.component.mappingInverted.MappingInvertedComponent;
import elements.regionBTree.Region;

public class BuildWithRandomMinHeapRegion {
	
	public DB db_AdjComponet;
	
	public DB db_Regions;
	
	public DB db_Edges;
	
	public DB db_Vertexs;
	
//	public DB db_InvertedFiles;
	
	
	
	public String DB_AdjComponet_Name;
	
	public String DB_Regions_Name;
	
	public String DB_Edges_Name;
	
	public String DB_Vertexs_Name;
	
//	public String DB_InvertedFile_Name;
	
	/**
	 * regionMap:<regionID,region>
	 */
	public HashMap<Integer, Region> regionMap;
	/**
	 * <regionID,belongRegionID>
	 */
	public HashMap<Integer, Integer> regionBelongMap;
	
	public  HashMap<Integer, Edge> basicEdgeWithPoiInfo;
	/**
	 * seedRegion
	 */
	public HashMap<Integer, Region> seedRegionMapWithPOIInfo;
	/**
	 * MinHeap  但是其并不等同于seedRegion
	 * 其应该是seedRegion的一个子集，所以说seed应该是seedRegion的一部分
	 */
	public PriorityQueue<Region> minHeapRegion;
	/**
	 * 用来存储边和Region之间的关系的
	 * 
	 */
	public HashMap<Integer, HashMap<Integer, Integer>> vertexRegionIDMap;
	/**
	 * 用来存储边和Region之间的关系的
	 * 
	 */
//	public HashMap<Integer, HashMap<Integer, Region>> vertexRegionMap;
	/**
	 * adjacency component
	 */
	public HashMap<Integer, HashMap<Integer, AdjVertexEntry>> adjacencyComponetMap;
	/**
	 * MappingComponent with Inverted Component
	 */
	public MappingInvertedComponent mappingInvertedComponent;
	/**
	 * adjacent Vertex to Vertex
	 */
	public HashMap<Integer,HashMap<Integer, Integer>> vertexToVertexAtEdge;
	
	
	
	public HashMap<Integer, Integer> minHeapRegionIDMap;
	
	public HashMap<Integer, HashMap<Integer, Integer>> extraDist;
	
	
	/**
	 * 最大的RegionID
	 */
	public int maxRegionID;
	/**
	 * 最小的minRegionID
	 */
	public int minRegionID;
	/**
	 * HeapSize's size
	 */
	public int minHeapSize=0;
	/**
	 * process Timer
	 */
	private TimeWatcher timeWatcher;
	/**
	 * 
	 * new adjcentVertex ID
	 */
	private int newAdjacentVertexID=-1;
	
	private int maxMinHeapRegionIndex=-1;

//	private GTreeAPI distanceHelpAtVertex;
	
	private int GtreeIOCount=0;
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
//	public HTreeMap<Integer,HashMap<String, Vector<InvertedFileEntry>>> db_termInvertedFileMap;
	
	/**
	 * adjacent Vertex to Vertex
	 */
	public HTreeMap<Integer,HashMap<Integer, Integer>> db_VertexToVertexAtEdge;
	
	/* System help begin*/
//	private FileWriter vertexDistanceWriter;
	
//	private HashMap<Integer,HashMap<Integer, Integer>> tmpBorderToDirectedDistance;
	/* System help end*/
	
	
	
	
	
	/**
	 * 构造函数：
	 * function:init()->loadMapInfo
	 * @throws IOException 
	 */
	public BuildWithRandomMinHeapRegion() throws IOException {
		init();
	}
	
	public void saveDataToDB()
	{
		
		this.db_AdjacencyComponetMap.putAll(this.adjacencyComponetMap);
		
//		this.db_MBRTermMaxScoreMap.putAll(this.mappingInvertedComponent.MBRTermMaxScoreMap);
	
		
		this.db_RegionBelongMap.putAll(this.regionBelongMap);
		
		this.db_RegionMap.putAll(this.regionMap);
		
		this.db_BasicEdgeWithPoiInfo.putAll(this.basicEdgeWithPoiInfo);
		
//		this.db_termInvertedFileMap.putAll(this.mappingInvertedComponent.termInvertedFileMap);
		
		this.db_VertexRegionIDMap.putAll(this.db_VertexRegionIDMap);
		
		this.db_VertexToVertexAtEdge.putAll(this.vertexToVertexAtEdge);
		
		db_Regions.close();
		System.out.println("save db_Regions");
		
		db_Edges.close();
		System.out.println("save db_Edges");
		
		db_Vertexs.close();
		System.out.println("save db_Vertexs");
		
//		db_InvertedFiles.close();
//		System.out.println("save db_InvertedFiles");
		
		db_AdjComponet.close();
		System.out.println("save db_AdjComponet");
		System.out.println("Data Save");
	}
	
	
	/**
	 * 通用初始化
	 * @throws IOException 
	 */
	public void init() throws IOException
	{
//		distanceHelpAtVertex=new GTreeAPI();
		
		regionMap=new HashMap<Integer, Region>();
		
		regionBelongMap=new HashMap<Integer, Integer>();
		
		seedRegionMapWithPOIInfo=new HashMap<Integer, Region>();
		
		minHeapRegion=new PriorityQueue<Region>();
		
		minHeapRegionIDMap=new HashMap<Integer, Integer>();
		
		vertexRegionIDMap=new HashMap<Integer, HashMap<Integer,Integer>>();
		
		basicEdgeWithPoiInfo=new HashMap<Integer, Edge>();
//		vertexRegionMap=new HashMap<Integer, HashMap<Integer,Region>>();
		
		adjacencyComponetMap=new HashMap<Integer, HashMap<Integer,AdjVertexEntry>>();
		
		mappingInvertedComponent=new MappingInvertedComponent();
		
		vertexToVertexAtEdge=new HashMap<Integer, HashMap<Integer,Integer>>();
		
		extraDist=new HashMap<Integer, HashMap<Integer,Integer>>();
		
		
		timeWatcher=new TimeWatcher();
		
//		tmpBorderToDirectedDistance=new HashMap<Integer, HashMap<Integer,Integer>>();
		
//		vertexDistanceWriter=new FileWriter(Configure.PROJECT_DATA_ROOT+Configure.TEMP_DATA_FOLDER+Configure.TEMP_BORDER_TO_BORDER__DATA);
		
		
		// TODO Auto-generated constructor stub
//		DBName = Configure.PROJECT_DATA_ROOT+IndexConfig.INDEX_DB_NAME;
		DB_AdjComponet_Name=Configure.PROJECT_DATA_ROOT+IndexConfig.INDEX_DB_ADJ_NAME;
		
		DB_Regions_Name=Configure.PROJECT_DATA_ROOT+IndexConfig.INDEX_DB_REGION_NAME;
		
		DB_Edges_Name=Configure.PROJECT_DATA_ROOT+IndexConfig.INDEX_DB_EDGE_NAME;
		
		DB_Vertexs_Name=Configure.PROJECT_DATA_ROOT+IndexConfig.INDEX_DB_VERTEX_NAME;
		
//		DB_InvertedFile_Name=Configure.PROJECT_DATA_ROOT+IndexConfig.INDEX_DB_INVERTEDFILE_NAME;
		
		createDB();
		
		db_AdjacencyComponetMap=db_AdjComponet.getHashMap(IndexConfig.DB_ADJACENCY_COMPONENT_MAP_HASHNAME);
		
//		db_MBRTermMaxScoreMap=db_InvertedFiles.getHashMap(IndexConfig.DB_MBR_TERM_MAX_SCORE_MAP_HASHNAME);
		
		db_RegionBelongMap=db_Regions.getHashMap(IndexConfig.DB_REGION_BELONG_MAP_HASHNAME);
		
		db_RegionMap=db_Regions.getHashMap(IndexConfig.DB_REGION_MAP_HASHNAME);
		
//		db_termInvertedFileMap=db_InvertedFiles.getHashMap(IndexConfig.DB_TERM_INVERTED_FILE_MAP_HASHNAME);
		
		db_VertexRegionIDMap=db_Vertexs.getHashMap(IndexConfig.DB_VERTEX_REGIONID_MAP_HASHNAME);
		
		db_VertexToVertexAtEdge=db_Vertexs.getHashMap(IndexConfig.DB_VERTEX_TO_VERTEX_AT_EDGE_HASHNAME);
		
		db_BasicEdgeWithPoiInfo=db_Edges.getHashMap(IndexConfig.DB_BASIC_EDGE_WITH_INFO_HASHNAME);
		initLoadData();
	}
	
	public void createDB()
	{
		//step 1:DB_AdjComponet_Name
		 File dbFile = new File(DB_AdjComponet_Name);
	        if(dbFile.exists()){
	        	dbFile.delete();
	        }
	        db_AdjComponet = DBMaker.newFileDB(dbFile)
	        		//.asyncWriteEnable()
	        		.transactionDisable()
	        		.freeSpaceReclaimQ(0)
	        		.asyncWriteEnable()
	        		
	        		//.cacheSize(1024*1024*100)
	        		//.cacheLRUEnable()
	        		//.cacheDisable()
	        		//.fullChunkAllocationEnable()
	        		//.mmapFileEnable()
	        		//.syncOnCommitDisable()
	        		.make();
	        System.out.println("Created BD:\t"+DB_AdjComponet_Name);
	        
			//step 2：DB_Regions_Name
	        File dbFile2 = new File(DB_Regions_Name);
	        if(dbFile2.exists()){
	        	dbFile2.delete();
	        }
	        db_Regions = DBMaker.newFileDB(dbFile2)
	        		//.asyncWriteEnable()
	        		.transactionDisable()
	        		.freeSpaceReclaimQ(0)
	        		.asyncWriteEnable()
	        		
	        		//.cacheSize(1024*1024*100)
	        		//.cacheLRUEnable()
	        		//.cacheDisable()
	        		//.fullChunkAllocationEnable()
	        		//.mmapFileEnable()
	        		//.syncOnCommitDisable()
	        		.make();
	        System.out.println("Created BD:\t"+DB_Regions_Name);
	        
			//step 3
	        File dbFile3 = new File(DB_Edges_Name);
	        if(dbFile3.exists()){
	        	dbFile3.delete();
	        }
	        db_Edges = DBMaker.newFileDB(dbFile3)
	        		//.asyncWriteEnable()
	        		.transactionDisable()
	        		.freeSpaceReclaimQ(0)
	        		.asyncWriteEnable()
	        		
	        		//.cacheSize(1024*1024*100)
	        		//.cacheLRUEnable()
	        		//.cacheDisable()
	        		//.fullChunkAllocationEnable()
	        		//.mmapFileEnable()
	        		//.syncOnCommitDisable()
	        		.make();
	        System.out.println("Created BD:\t"+DB_Edges_Name);
			//step 4：DB_Vertexs_Name
	        File dbFile4 = new File(DB_Vertexs_Name);
	        if(dbFile4.exists()){
	        	dbFile4.delete();
	        }
	        db_Vertexs = DBMaker.newFileDB(dbFile4)
	        		//.asyncWriteEnable()
	        		.transactionDisable()
	        		.freeSpaceReclaimQ(0)
	        		.asyncWriteEnable()
	        		
	        		//.cacheSize(1024*1024*100)
	        		//.cacheLRUEnable()
	        		//.cacheDisable()
	        		//.fullChunkAllocationEnable()
	        		//.mmapFileEnable()
	        		//.syncOnCommitDisable()
	        		.make();
	        System.out.println("Created BD:\t"+DB_Vertexs_Name);
			//step 5
//	        File dbFile5 = new File(DB_InvertedFile_Name);
//	        if(dbFile5.exists()){
//	        	dbFile5.delete();
//	        }
//	        db_InvertedFiles = DBMaker.newFileDB(dbFile5)
//	        		//.asyncWriteEnable()
//	        		.transactionDisable()
//	        		.freeSpaceReclaimQ(0)
//	        		.asyncWriteEnable()
//	        		
//	        		//.cacheSize(1024*1024*100)
//	        		//.cacheLRUEnable()
//	        		//.cacheDisable()
//	        		//.fullChunkAllocationEnable()
//	        		//.mmapFileEnable()
//	        		//.syncOnCommitDisable()
//	        		.make();
//	        System.out.println("Created BD:\t"+DB_InvertedFile_Name);
	                
	}
	/**
	 * 加载各种Map数据
	 * @throws IOException 
	 */
	private void initLoadData() throws IOException {
		
		loadBaseVertexRegionIDMap();

		loadRegionMap();

		loadStaticMinHeap();
//		loadMinHeapMap();

//		loadBaseVertexRegionIDMap();

		LoadBaseMappingInvertedComponent();

		loadBasicRegionMappingComponent();

		
		
		loadExtraDistance();
//		printDataStatus("aggregtingRun end");
		
		//将Region内的信息加入到里面
//		addRegionAdjToAdjComponent();
		
		seedRegionMapWithPOIInfo.clear();
		newAdjacentVertexID=getNewAdjacencyIndex();
		aggregtingRun();
//		String toWrite;
//		for(Integer borderId:tmpBorderToDirectedDistance.keySet())
//		{
//			 
//			
//			for(Integer subBorderID:tmpBorderToDirectedDistance.get(borderId).keySet())
//			{
//				 vertexDistanceWriter.write(borderId+Configure.SPLITE_FLAG+subBorderID+"\r\n");
//			}
//			
//		}
//		vertexDistanceWriter.close();

		for(Integer regionIID:regionMap.keySet())
		{
				regionMap.get(regionIID).borderVertexsDistance.clear();
		}
		regionBelongMap.clear();
		printDataStatus("aggregtingRun end");

		System.out.println("ALL Done");
	}
	
	
//	public void addRegionAdjToAdjComponent()
//	{
//		newAdjacentVertexID=getNewAdjacencyIndex();
//		for(Integer seedRegionID:seedRegionMapWithPOIInfo.keySet())
//		{
//			Region regionSeed=seedRegionMapWithPOIInfo.get(seedRegionID);
//			if(regionSeed.edges.size()==1)
//			{
//				
//				int edgeToR=-1;
//				for(Integer inEdge:regionSeed.edges.keySet())
//				{
//					edgeToR=inEdge;
//				}
//				//计算距离
//				int distance=-1;
//				if(basicEdgeWithPoiInfo.containsKey(edgeToR))
//				{
//					distance=DistanceHelp.getRealDistanceToVertex(basicEdgeWithPoiInfo.get(edgeToR).v1, basicEdgeWithPoiInfo.get(edgeToR).v2);
//				}else
//				{
//					distance=40;
//					System.out.println("not find Edge:"+edgeToR);
//				}
//				while(adjacencyComponetMap.containsKey(newAdjacentVertexID))
//				{
//					newAdjacentVertexID++;
//				}
//				adjacencyComponetMap.put(newAdjacentVertexID, new HashMap<Integer,AdjVertexEntry>());
//				
//				adjacencyComponetMap.get(newAdjacentVertexID).put(newAdjacentVertexID, new AdjVertexEntry(newAdjacentVertexID, newAdjacentVertexID, Configure.RELEVANT_TYPE_REGULAR_VERTEX, edgeToR, distance));
//			}
//		}
//		newAdjacentVertexID=getNewAdjacencyIndex();
//	}
	
	
	
	public void loadExtraDistance() throws IOException
	{
		LineNumberReader lineReader=new LineNumberReader(new FileReader(Configure.PROJECT_DATA_ROOT+Configure.TEMP_DATA_FOLDER+Configure.EXTRA_DISTANCE));
		String line;
		while((line=lineReader.readLine())!=null)
		{
			String[] parts=line.split(Configure.SPLITE_FLAG);
			
			if(parts.length!=3)
			{
				continue;
			}
			
			int v1=Integer.parseInt(parts[0]);
			int v2=Integer.parseInt(parts[1]);
			int distace=Integer.parseInt(parts[2]);
			if(extraDist.containsKey(v1))
			{
				extraDist.get(v1).put(v2, distace);
			}
			else
			{
				extraDist.put(v1, new HashMap<Integer, Integer>());
				extraDist.get(v1).put(v2, distace);
			}
			
		}
		lineReader.close();
	}
	
	public void loadBasicRegionMappingComponent()
	{
		System.out.println("here begin");
		for(Region baseRegion:regionMap.values())
		{
//			if(!mappingInvertedComponent.MBRTermMaxScoreMap.containsKey(baseRegion.id))
//			{
//				mappingInvertedComponent.MBRTermMaxScoreMap.put(baseRegion.id, baseRegion.keywordsMaxScore);
//			}else
//			{
////				System.out.println("Warning:mappingInvertedComponent.MBRTermMaxScoreMap.containsKey");
//				mappingInvertedComponent.MBRTermMaxScoreMap.put(baseRegion.id, baseRegion.keywordsMaxScore);
//			}
		}
		System.out.println("here end");
	}
	
	private void LoadBaseMappingInvertedComponent() {
		// TODO Auto-generated method stub
		timeWatcher.start();
//		LoadBasicMappingInvertedComponent lbmicLoader=new LoadBasicMappingInvertedComponent();
//		mappingInvertedComponent=lbmicLoader.mappingInvertedComponent;
		timeWatcher.stop();
		timeWatcher.seeRunTime("LoadBaseMappingInvertedComponent");
		
	}
	/**
	 * 加载RegionMap
	 */
	public void loadRegionMap()
	{
		timeWatcher.start();
		for(Integer regionID:seedRegionMapWithPOIInfo.keySet())
		{
			regionMap.put(regionID,seedRegionMapWithPOIInfo.get(regionID));

		}
		timeWatcher.stop();
		timeWatcher.seeRunTime("loadRegionMap");
	}
	/**
	 * 创建MinHeap并即时Mark到RegionBelongMap里面
	 * @throws IOException 
	 */
	public void loadStaticMinHeap() throws IOException
	{
		CalculateMAXMINRegionID();
		int regionIDAtLay1=maxRegionID+1;
		
		LineNumberReader staticMinHeapIdReader=new LineNumberReader(new FileReader(Configure.PROJECT_DATA_ROOT+Configure.TEMP_DATA_FOLDER+Configure.STATIC_MIN_HEAP_REGION_ID_INFO));
		String line;
		
		while((line=staticMinHeapIdReader.readLine())!=null)
		{
			int tmpRegionId=Integer.parseInt(line);
			if(seedRegionMapWithPOIInfo.containsKey(tmpRegionId))
			{
				Region regionToHeap=new Region();
				regionToHeap=seedRegionMapWithPOIInfo.get(tmpRegionId);
				regionToHeap.layID=1;
				regionToHeap.id=regionIDAtLay1;
				minHeapRegion.add(regionToHeap);
				minHeapRegionIDMap.put(regionToHeap.id, 1);
				regionBelongMap.put(tmpRegionId, regionToHeap.id);
				regionMap.put(regionToHeap.id, regionToHeap);
				if(maxMinHeapRegionIndex<regionIDAtLay1)
				{
					maxMinHeapRegionIndex=regionIDAtLay1;
				}
				regionIDAtLay1++;
				
				

			}
		}
		
	}
	/**
	 * 创建MinHeap并即时Mark到RegionBelongMap里面
	 * @throws IOException 
	 */
	public void loadMinHeapMap() throws IOException
	{
		timeWatcher.start();
		int seedRegionSize=seedRegionMapWithPOIInfo.size();
		if(seedRegionSize/Configure.LAY_REGION_RATIO>1)
		{
			minHeapSize=seedRegionSize/Configure.LAY_REGION_RATIO;
		}
		
		
		CalculateMAXMINRegionID();
		Random rand=new Random();
		int regionIDAtLay1=maxRegionID+1;
		
		FileWriter minReginIdToSaveWriter=new FileWriter(Configure.PROJECT_DATA_ROOT+Configure.TEMP_DATA_FOLDER+Configure.STATIC_MIN_HEAP_REGION_ID_INFO);
		
		
//		System.out.println("minHeapRegion minIndexis"+regionIDAtLay1);
		
		while(minHeapSize>0)
		{
//			System.out.println(maxRegionID+" "+minRegionID);
			int tmpRegionID=rand.nextInt(maxRegionID-minRegionID);
			tmpRegionID+=minRegionID;
			
			
			
//			System.out.println("tmpRegionID"+tmpRegionID);
			if(seedRegionMapWithPOIInfo.containsKey(tmpRegionID))
			{
//				System.out.println("here one");
				Region regionToHeap=new Region();
				 regionToHeap=seedRegionMapWithPOIInfo.get(tmpRegionID);
				 regionToHeap.layID=1;
				 regionToHeap.id=regionIDAtLay1;
				 
				if(!minHeapRegion.contains(regionToHeap))
				{
					//重新给Region命名
					minReginIdToSaveWriter.write(tmpRegionID+"\r\n");
					regionToHeap.id=regionIDAtLay1;
					
//					System.out.println(regionToHeap.borderVertexs.size());
					if(regionMap.containsKey(regionToHeap.id))
					{
						System.err.println("2 id is same"+regionToHeap.id);
					}
					
					minHeapRegion.add(regionToHeap);
					minHeapRegionIDMap.put(regionToHeap.id, 1);
//					minHeapIDSet.put(regionToHeap.id, regionIDAtLay1);
					regionBelongMap.put(tmpRegionID, regionToHeap.id);
					regionMap.put(regionToHeap.id, regionToHeap);
//					System.out.println(regionToHeap.borderVertexs.keySet().toString());
					regionIDAtLay1++;
					
					minHeapSize--;
				}
			}

		}
		minReginIdToSaveWriter.close();
//		System.out.println("minHeapRegion minIndexis"+regionIDAtLay1);
		timeWatcher.stop();
		timeWatcher.seeRunTime("loadMinHeapMap");
	}
	/**
	 * 加载边-RegionMap
	 */
	public void loadBaseVertexRegionIDMap()
	{
			//LoadBaseVertexRegion
			timeWatcher.start();
			LoadBasicVertexRegion vrLoad=new LoadBasicVertexRegion();
			
			vertexRegionIDMap=vrLoad.basicVertexRegionIDMap;
			
			adjacencyComponetMap=vrLoad.basicSeedAdjacencyComponent;
			
			seedRegionMapWithPOIInfo=vrLoad.seedRegionWithPOIMap;
			
			vertexToVertexAtEdge=vrLoad.vertexToVertexAtEdge;
			
			basicEdgeWithPoiInfo=vrLoad.basicEdgeWithPoiInfo;
			
//			newAdjacentVertexID=getNewAdjacencyIndex();
			
			timeWatcher.stop();
			timeWatcher.seeRunTime("loadBaseVertexRegionIDMap");
			
			
	}
	
	public int getNewAdjacencyIndex()
	{
		newAdjacentVertexID=-1;
		//获得最大的ID
		for(Integer adjID:adjacencyComponetMap.keySet())
		{
			if(adjID>newAdjacentVertexID)
			{
				newAdjacentVertexID=adjID; 
			}
			for(Integer tmpadjID:adjacencyComponetMap.get(adjID).keySet())
			{
				if (tmpadjID>newAdjacentVertexID) {
					newAdjacentVertexID=tmpadjID;
				}
			}
		}
		//比较NewAdj
		if(maxMinHeapRegionIndex>=newAdjacentVertexID)
		{
			newAdjacentVertexID=maxMinHeapRegionIndex;
		}
		for(Integer idRegionDD:regionMap.keySet())
		{
			if(newAdjacentVertexID<idRegionDD)
			{
				newAdjacentVertexID=idRegionDD;
			}
		}
		
		//最大的ID ++
		newAdjacentVertexID++;
		
		return newAdjacentVertexID;
		
	}

	/**
	 * 计算当前SeedRegion中的RegionID的最大值以及最小值
	 */
	public void CalculateMAXMINRegionID()
	{
		this.maxRegionID=0;
		this.minRegionID=0;
		int itemAcessCount=0;
		for(Integer regID:seedRegionMapWithPOIInfo.keySet())
		{
			if(itemAcessCount==0)
			{
				maxRegionID=regID;
				minRegionID=regID;
			
			}else
			{
				maxRegionID=maxRegionID>regID?maxRegionID:regID;
				minRegionID=minRegionID<regID?minRegionID:regID;
			}
			itemAcessCount++;
		}
//		System.out.println("Max"+maxRegionID+"\tmin"+minRegionID);
	}
	
	


	/**
	 * 对于Minheap按照论文中Algorithm3实现
	 */
	public void aggregtingRun(){
		
		//minHeapRegion is null
		if(minHeapRegion.size()==0)
		{
			System.err.println("minHeapRegion is null");
			System.exit(-1);
			return;
		}
		int count=0;
		int saveCount=0;
		
		
		while(minHeapRegion.size()>0)
		{
			count++;
			if(count%1000==0)
			{
//				System.out.println(count);
			}
			Region regionI=minHeapRegion.poll();

			
			/**
			 * 目标Regina将要合并到regionI的
			 * 
			 */
			Region regionJ=null;
			/**
			 * 临时Region
			 */
			Region regionT=null;
			
			float costToAggrating=-1;
			
			int targetBorder=-1;
			//找到Border
			for(Integer borderId:regionI.borderVertexs.keySet())
			{
//				System.out.println("here");
//				System.exit(0);
				
				if(regionI.borderVertexs.get(borderId).equals(IndexConfig.VERTEX_TYPE_UNBORDER))
				 {
					 continue;
				 }

				if(!adjacencyComponetMap.containsKey(borderId))
				{
//					System.out.println("why here2");
					continue;
				}
//				if(adjacencyComponetMap.containsKey(borderId))
//				{
////					continue;
//				}
//				System.out.println("why here3");
				//找到adj
				for(Integer adjVID:adjacencyComponetMap.get(borderId).keySet())
				{
					//根据adj找到相关的Region
					
//					int relevantType=adjacencyComponetMap.get(borderId).get(adjVID).relevantType;
					
					//Step 1:判断是Interminal or regular Vertex
					
					//在Relevant中找相应的Region
					if(adjacencyComponetMap.get(borderId).get(adjVID).relevantType==Configure.RELEVANT_TYPE_INTERMEDARY)
					{
						//step 2：找到Region
						int relatedRegionID=adjacencyComponetMap.get(borderId).get(adjVID).relevantID;
						
						if(regionI.id==relatedRegionID)
						{
							continue;
						}
						
						if(minHeapRegionIDMap.containsKey(relatedRegionID))
						{
							continue;
						}
						
						//对于为assign's Region 
						if(!regionBelongMap.containsKey(relatedRegionID))
						{
							 regionT=regionMap.get(relatedRegionID);
						
							 
							 if(regionT.id==regionI.id)
							 {
								 continue;
							 }
							float tmpCostToAggrating=AggregateCost.getRegionAggrateCost(regionI,regionT);
							//for the first Time
							if(regionJ==null||costToAggrating==-1)
							{
								if(!regionBelongMap.containsKey(regionT.id))
								{
									regionJ=regionT;
									costToAggrating=tmpCostToAggrating;
									targetBorder=borderId;
								}
							
							}else
							{
								//RegionJ is not null   
								if(tmpCostToAggrating<costToAggrating)
								{
									if(!regionBelongMap.containsKey(regionT.id))
									{
									regionJ=regionT;
									costToAggrating=tmpCostToAggrating;
									targetBorder=borderId;
									}
								}
							}
							
						}
						
					}
					//在VertexRegion中找到相应的Region 
					if(adjacencyComponetMap.get(borderId).get(adjVID).relevantType==Configure.RELEVANT_TYPE_REGULAR_VERTEX)
					{
						for(Integer tmpRegularRegionID:vertexRegionIDMap.get(adjVID).keySet())
						{
							Region tmpRegularRegion=regionMap.get(tmpRegularRegionID);
							if(!regionBelongMap.containsKey(tmpRegularRegion.id))
							{
								regionT=tmpRegularRegion;
								float tmpCostToAggrating=AggregateCost.getRegionAggrateCost(regionI,regionT);
								//for the first Time
								if(regionJ==null||costToAggrating==-1)
								{
									if(tmpCostToAggrating<costToAggrating)
									{
									regionJ=regionT;
									costToAggrating=tmpCostToAggrating;
									targetBorder=borderId;
									}
								}else
								{
									//RegionJ is not null   
									if(tmpCostToAggrating<costToAggrating)
									{
										if(tmpCostToAggrating<costToAggrating)
										{
										regionJ=regionT;
										costToAggrating=tmpCostToAggrating;
										targetBorder=borderId;
										}
									}
								}
							}
						}
					}
				}
			}//End For
		
			//RegionJ!=null 则将RegionJ归并到RegionI中
			if(regionJ!=null)
			{
				
				if(regionJ.id==regionI.id)
				{
					continue;
				}
//				System.out.println("regionJID"+regionJ.id+"\tRegionIID"+regionI.id);
				//将RegionJ设置为已Assigned
				regionBelongMap.put(regionJ.id, regionI.id);//将RegionJ标记为RegionI的一部分
				regionMap.get(regionJ.id).kwdFreqMap.clear();
				regionMap.get(regionJ.id).aggregated=true;
				regionMap.get(regionJ.id).regionBelong=regionI.id;
				
//				Region newRegionI=regionI;
				
				
//				newRegionI.AggratedRegion(regionJ,targetBorder);//此方法行不通，因为要对border进行判断是否属于某个region
				//更新RegionI中的内容
				//step1:pseudo-document信息更新
				HashMap<String, Float> kwdMaxScore=regionJ.keywordsMaxScore;
				
				for(Integer edID:regionJ.edges.keySet())
				{
					regionI.addEdgeInAggrating(basicEdgeWithPoiInfo.get(edID));
				}

				//step2:borders信息更新信息更新
				//采用原始的方法计算
				for(Integer tmpBorderFromRegionI:regionI.borderVertexs.keySet())
				{
					int baseDist=0;
					//得到baseDist
					if(tmpBorderFromRegionI.equals(targetBorder))
					{
						baseDist=0;
					}else
					{
						if(regionI.borderVertexsDistance.containsKey(tmpBorderFromRegionI))
						{
							if(regionI.borderVertexsDistance.get(tmpBorderFromRegionI).containsKey(targetBorder))
							{
								baseDist=regionI.borderVertexsDistance.get(tmpBorderFromRegionI).get(targetBorder);
							}
							
						}
						if(regionI.borderVertexsDistance.containsKey(targetBorder))
						{
							if(regionI.borderVertexsDistance.get(targetBorder).containsKey(tmpBorderFromRegionI))
							{
								baseDist=regionI.borderVertexsDistance.get(targetBorder).get(tmpBorderFromRegionI);
							}
						}
						
//							System.out.println("tmpBorderFromRegionI"+tmpBorderFromRegionI);
//							System.out.println("baseDist"+baseDist);
//						return;
					}
					
					for(Integer tmpBorderFromRegionJ:regionJ.borderVertexs.keySet())
					{
						int newDistance=0;
						if(tmpBorderFromRegionJ.equals(targetBorder))
						{
							newDistance=0;
						}else
						{
							if(regionJ.borderVertexsDistance.containsKey(targetBorder))
							{
								if(regionJ.borderVertexsDistance.get(targetBorder).containsKey(tmpBorderFromRegionJ))
								{
									newDistance=regionJ.borderVertexsDistance.get(targetBorder).get(tmpBorderFromRegionJ);
								}
							}
							
							if(regionJ.borderVertexsDistance.containsKey(tmpBorderFromRegionJ))
							{
								if(regionJ.borderVertexsDistance.get(tmpBorderFromRegionJ).containsKey(targetBorder))
								{
									newDistance=regionJ.borderVertexsDistance.get(tmpBorderFromRegionJ).get(targetBorder);
								}
							}
//							
//							System.out.println("tmpBorderFromRegionI"+tmpBorderFromRegionI);
//							System.out.println("baseDist"+baseDist);
							
						}
						
						newDistance+=baseDist;
						if(newDistance>0)
						{
							if(!regionI.borderVertexsDistance.containsKey(tmpBorderFromRegionJ))
							{
								regionI.borderVertexsDistance.put(tmpBorderFromRegionJ, new HashMap<Integer, Integer>());
							}
							regionI.borderVertexsDistance.get(tmpBorderFromRegionJ).put(tmpBorderFromRegionI, newDistance);
						}
					}
					
					
					
				}
				
				
				
				
				 for(Integer newborderID:regionJ.borderVertexs.keySet())
				 {
					 
	//				 //非border直接跳过
					 if(regionJ.borderVertexs.get(newborderID).equals(IndexConfig.VERTEX_TYPE_UNBORDER))
					 {
						 continue;
					 }
					 if(regionI.borderVertexs.containsKey(newborderID))
					 {

						 //判断border是否只属于regionI
						 if(borderIsBelongToRegionI(newborderID,regionI.id))
						 {
							 //将该Vertex设置为非border
	//						 regionI.vertexs.put(newborderID, IndexConfig.VERTEX_TYPE_UNBORDER);
							 regionI.removeBorders(newborderID);
							 System.out.println("remove"+newborderID);
							
						 }
						 
					 }
					 else
					 {
						 
						 regionI.borderVertexs.put(newborderID, regionJ.borderVertexs.get(newborderID));
						//计算距离
						 //已经计算好了上面 ==>超前计算部分哦
						 
						 //不算了 直接用Gtree
					 }
				 }
			 
			 
			 
			 //step 4:update RegionI POICount
			 	regionI.quantityOfPOI=regionI.quantityOfPOI+regionJ.quantityOfPOI;
//			    newRegionI=regionI;
			 	if(regionMap.containsKey(regionI.id))
			 	{
			 		regionMap.remove(regionI.id);
			 	}
			 	regionMap.put(regionI.id, regionI);
				minHeapRegion.add(regionI);
				

			}else//regionI不能在扩展则进行保存
			{
				
				
				
//				System.err.println(regionI.id+"\t"+regionI.quantityOfPOI);
//				System.out.println("nono"+regionI.id);
				//将Region放到Map中
//				regionI.reCountPOICount();
				regionMap.put(regionI.id, regionI);
				regionMap.get(regionI.id).kwdFreqMap.clear();
				
//				if(regionMap.containsKey(regionI.id))
//				{
//					continue;
//				}
				
//				if(!mappingInvertedComponent.MBRTermMaxScoreMap.containsKey(regionI.id))
//				{
//					mappingInvertedComponent.MBRTermMaxScoreMap.put(regionI.id,regionI.keywordsMaxScore);
//				}
//				else
//				{
//					mappingInvertedComponent.MBRTermMaxScoreMap.put(regionI.id,regionI.keywordsMaxScore);
////					System.out.println("Region I is in MappingComponent but WHY"+regionI.id);
//				}
				for(Integer boreID:regionI.borderVertexs.keySet())
				{
					if(regionI.borderVertexs.get(boreID).equals(IndexConfig.VERTEX_TYPE_BORDER))
					{
//						vertexRegionMap.get(boreID).put(regionI.id,regionI);
						vertexRegionIDMap.get(boreID).put(regionI.id, 0);
				
						
					}else
					{
						System.out.println("no way unborder"+boreID);
					}
				}
				

//				saveCount++;
//				System.out.println(saveCount);
				
				//Algorithm 3 Line23:for each v=>Ri.V do
				for(Integer borderID:regionI.borderVertexs.keySet())
				{
					
					if(!regionI.borderVertexs.get(borderID).equals(IndexConfig.VERTEX_TYPE_BORDER))
					{
						continue;
					}
					  //Algorithm 3: U<=current adjacent vertices of v
					/**
					 * vertexU<vertexID,relevantedType>
					 */
					HashMap<Integer,Integer > vertexsU=new HashMap<Integer, Integer>();

					if(!adjacencyComponetMap.containsKey(borderID))
					{
						System.out.println("borderID"+borderID);
						
					}
					
					for(Integer uBorderID:adjacencyComponetMap.get(borderID).keySet())
					{				
						vertexsU.put(uBorderID, adjacencyComponetMap.get(borderID).get(uBorderID).relevantType);
					}
					//新建一个U《=null its size is zero ;T is the new adjacent vertices
					HashMap<Integer,HashMap<Integer,AdjVertexEntry> >adjcentVertexT=new HashMap<Integer, HashMap<Integer,AdjVertexEntry>>();
					
					//Algorithm 3: for each u in U do
					for(Integer uBorderID:vertexsU.keySet())
					{
						//Algorithm 3: get the Region associated with uBorderID
						//如果是Intermediary Vertex
						if(vertexsU.get(uBorderID).equals(Configure.RELEVANT_TYPE_INTERMEDARY))
						{
//							System.out.println("I");
							
							//获得相关联的Region 
							int uRegionID=adjacencyComponetMap.get(borderID).get(uBorderID).relevantID;
//							if(regionBelongMap.containsKey(uRegionID))
//							{
//								continue;
//							}
							
//							//获得最高级别的Region  如果有的话
							while(regionBelongMap.containsKey(uRegionID))
							{
								uRegionID=regionBelongMap.get(uRegionID);

							}
							
							
							
//							
							//判断是否包含T中是否包含该Region
							boolean tmpFlag=false;
							/**
							 * vRJ
							 */
							int tmpAdjVertexRj=-1;
							if(adjcentVertexT.containsKey(borderID))
							{
								for(Integer adcjT:adjcentVertexT.get(borderID).keySet())
								{
									if(adjcentVertexT.get(borderID).get(adcjT).relevantID==uRegionID)
									{
										tmpFlag=true;
										tmpAdjVertexRj=adjcentVertexT.get(borderID).get(adcjT).id;
										if(adcjT!=tmpAdjVertexRj)
										{
											System.err.println("adcjT!=tmpAdjVertex");
											System.exit(-1);
										}
										break;
									}
								}
							}
							else
							{
								adjcentVertexT.put(borderID, new HashMap<Integer, AdjVertexEntry>());
							}
							//没有该Region
							if(tmpFlag==false&&tmpAdjVertexRj!=-1)
							{
								//则创建一个adjcent Vertex
								AdjVertexEntry newAdjEntry=new AdjVertexEntry(newAdjacentVertexID, newAdjacentVertexID, Configure.RELEVANT_TYPE_INTERMEDARY, uRegionID, 0);
								adjcentVertexT.get(borderID).put(newAdjacentVertexID, newAdjEntry);
								tmpAdjVertexRj=newAdjacentVertexID;
								newAdjacentVertexID++;
								AdjVertexEntry  childAdjEntry=new AdjVertexEntry(uBorderID, uBorderID, Configure.RELEVANT_TYPE_INTERMEDARY, adjacencyComponetMap.get(borderID).get(uBorderID).relevantID, 0);
						
								if(!adjcentVertexT.containsKey(tmpAdjVertexRj))
								{
									adjcentVertexT.put(tmpAdjVertexRj, new HashMap<Integer,  AdjVertexEntry>());
								}
								adjcentVertexT.get(tmpAdjVertexRj).put(uBorderID, childAdjEntry);
//									newAdjacentVertexID++;
							}
							else
							{
								//tmpFlag=true;==>tmpAdjVertex is not -1 any more
								AdjVertexEntry  childAdjEntry=new AdjVertexEntry(uBorderID, uBorderID, Configure.RELEVANT_TYPE_INTERMEDARY, adjacencyComponetMap.get(borderID).get(uBorderID).relevantID, 0);
								if(!adjcentVertexT.containsKey(tmpAdjVertexRj))
								{
									adjcentVertexT.put(tmpAdjVertexRj, new HashMap<Integer,  AdjVertexEntry>());
								}
								adjcentVertexT.get(tmpAdjVertexRj).put(uBorderID, childAdjEntry);
//								newAdjacentVertexID++;
							}
							
							
							Region tmpRegionJ=regionMap.get(uRegionID);
//							if(!minHeapRegionIDMap.containsKey(uRegionID))
//								{
//									continue;
//								}
//							System.out.println(tmpRegionJ.borderVertexs.keySet());
							if(tmpRegionJ.borderVertexs.containsKey(borderID))
							{
								//如果v是Rj的border
								if(tmpRegionJ.borderVertexs.get(borderID).equals(IndexConfig.VERTEX_TYPE_BORDER))
								{
									
									
									
									
									for(Integer borderW:tmpRegionJ.borderVertexs.keySet())
									{
										
										if(borderW.equals(borderID))
										{
											continue;
										}
										if(tmpRegionJ.borderVertexs.get(borderW).equals(IndexConfig.VERTEX_TYPE_UNBORDER))
										{
											continue;
										}
										
										int distanceForC=-1;
										
										if(tmpRegionJ.borderVertexsDistance.containsKey(borderW))
										{
											if(tmpRegionJ.borderVertexsDistance.get(borderW).containsKey(borderID))
											{
												distanceForC=tmpRegionJ.borderVertexsDistance.get(borderW).get(borderID);
											}
										}
										if(tmpRegionJ.borderVertexsDistance.containsKey(borderID))
										{
											if(tmpRegionJ.borderVertexsDistance.get(borderID).containsKey(borderW))
											{
												distanceForC=tmpRegionJ.borderVertexsDistance.get(borderID).get(borderW);
											}
										}
										
										
										if(distanceForC==-1)
										{
											
											//经测试  都是边丢了
											//找到相应的边
											
											int tempEdgeIDForLost=-1;
											if(vertexToVertexAtEdge.containsKey(borderID))
											{
												if(vertexToVertexAtEdge.get(borderID).containsKey(borderW))
												{
													tempEdgeIDForLost=vertexToVertexAtEdge.get(borderID).get(borderW);
												}
											}
											
											if(vertexToVertexAtEdge.containsKey(borderW))
											{
												if(vertexToVertexAtEdge.get(borderW).containsKey(borderID))
												{
													tempEdgeIDForLost=vertexToVertexAtEdge.get(borderW).get(borderID);
												}
											}
											if(tempEdgeIDForLost!=-1)
											{
												if(basicEdgeWithPoiInfo.containsKey(tempEdgeIDForLost))
												{
													Edge tmepEdgeForLost=basicEdgeWithPoiInfo.get(tempEdgeIDForLost);
													distanceForC=DistanceHelp.getRealDistanceToVertex(tmepEdgeForLost.v1,tmepEdgeForLost.v2);
												}
												else
												{
													distanceForC=50000;
												}
											}
											else
											{
												distanceForC=50000;
											}	
										}
										int tmpEdgeID=newAdjacentVertexID;
										newAdjacentVertexID++;
										AdjVertexEntry dircAdjEntrt=new AdjVertexEntry(borderW, borderW, Configure.RELEVANT_TYPE_DIRECT_CONNECTION,tmpEdgeID,distanceForC);
										adjcentVertexT.get(borderID).put(borderW, dircAdjEntrt);
									}
									
								}
							}

						}//如果是directed Vertex
						else if(vertexsU.get(uBorderID).equals(Configure.RELEVANT_TYPE_DIRECT_CONNECTION))
						{
//							System.out.println("C why here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
							
							//对于
							
						}//如果是regular Vertex
						else if(vertexsU.get(uBorderID).equals(Configure.RELEVANT_TYPE_REGULAR_VERTEX))
						{
//							System.out.println("R");
							///regular不见了
//							if()
							
							//step1：
							//找到borderID与Uborder都在的SeedRegion
							
							
							
//							HashMap<Integer, AdjVertexEntry> tmpData=adjacencyComponetMap.get(borderID);
//							
//							for(Integer IIID:tmpData.keySet())
//							{
//								if(tmpData.get(IIID).relevantType==Configure.RELEVANT_TYPE_INTERMEDARY)
//								{
//									
//								}
//							}
							
						}
						else
						{
//							System.out.println("vertexsU relevantType is:\t"+vertexsU.get(uBorderID));
						}

					
						
					}
					if(adjcentVertexT.containsKey(borderID))
					{
						if(adjcentVertexT.get(borderID).size()>0)
						{
							adjacencyComponetMap.get(borderID).remove(borderID);
						}
					}

					
					
					for(Integer adVID:adjcentVertexT.keySet())
					{
						if(!adjacencyComponetMap.containsKey(adVID))
						{
							adjacencyComponetMap.put(adVID, new HashMap<Integer, AdjVertexEntry>());
						}
						for(Integer adVIDChild:adjcentVertexT.get(adVID).keySet())
						{
							adjacencyComponetMap.get(adVID).put(adVIDChild, adjcentVertexT.get(adVID).get(adVIDChild));
						}
					}
//					adjacencyComponetMap.putAll(adjcentVertexT);
					
				}//for(Integer borderID:regionI.borderVertexs.keySet())
				
//				System.out.println("save:\t"+regionI.id);
				
				if(saveCount%10==0)
				{
					System.out.println("Save"+saveCount+"/"+minHeapRegionIDMap.size());
//					return;
				}
				saveCount++;
			}//else  RegionI不能再扩充了
			
			
		}//while(minHeapRegion.size()==0)EndWhile
		
		System.out.println("Finished");
	}
	
	public boolean borderIsBelongToRegionI(int border,int regionIID)
	{
		boolean flag=true;
		
		
		for(Integer tmpRegionID:vertexRegionIDMap.get(border).keySet())
		{
			Region relatedRegion=regionMap.get(tmpRegionID);
			//relatedRegion不是目标Region  即找到该Vertex相连的Region
			if(relatedRegion.id!=regionIID)
			{
				//regionBelong中包含该region  
				if(regionBelongMap.containsKey(relatedRegion))
				{
					//判断该region是否是regionID
					if(!regionBelongMap.get(relatedRegion).equals(regionIID))
					{
						//不是RegionID则表示该Vertex不能删掉其border的身份
						flag=false;
						break;
					}
				}else{
					//region还没有被归并则直接返回该border不只属于RegionI
					flag=false;
					break;
				}
					
			}
		}	
		
		return flag;
	}
	
	
	public void printDataStatus(String period)
	{
		System.out.println("==================="+period+"========================");
		System.out.println();
		
		System.out.println("seedRegionMapWithPOIInfo SIZE:\t"+seedRegionMapWithPOIInfo.size());
		PrintMaxMinIndex.printTheMaxMin(seedRegionMapWithPOIInfo.keySet());
		
		System.out.println("regionMap SIZE:\t"+regionMap.size());
		PrintMaxMinIndex.printTheMaxMin(regionMap.keySet());
		
		System.out.println("regionBelongMap SIZE:\t"+regionBelongMap.size());
		PrintMaxMinIndex.printTheMaxMin(regionBelongMap.keySet());
		
		System.out.println("minHeapRegion Size SIZE:\t"+minHeapRegion.size());
		
		
		
		System.out.println("vertexRegionIDMap SIZE:\t"+vertexRegionIDMap.size());
		PrintMaxMinIndex.printTheMaxMin(vertexRegionIDMap.keySet());
		
		System.out.println("vertexToVertexAtEdge SIZE:\t"+vertexToVertexAtEdge.size());
		PrintMaxMinIndex.printTheMaxMin(vertexToVertexAtEdge.keySet());
		
		System.out.println("basicEdgeWithPoiInfo SIZE:\t"+basicEdgeWithPoiInfo.size());
		PrintMaxMinIndex.printTheMaxMin(basicEdgeWithPoiInfo.keySet());
		
//		System.out.println("mappingInvertedComponent.MBRTermMaxScoreMap SIZE:\t"+mappingInvertedComponent.MBRTermMaxScoreMap.size());
//		PrintMaxMinIndex.printTheMaxMin(mappingInvertedComponent.MBRTermMaxScoreMap.keySet());
		
//		System.out.println("mappingInvertedComponent.termInvertedFileMap SIZE:\t"+mappingInvertedComponent.termInvertedFileMap.size());
//		PrintMaxMinIndex.printTheMaxMin(mappingInvertedComponent.termInvertedFileMap.keySet());
		
		System.out.println();
		
		System.out.println("==================="+period+"========================");
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		
	
		BuildWithRandomMinHeapRegion test=new BuildWithRandomMinHeapRegion();
		test.saveDataToDB();
//		test.aggregtingRun();
	
		
		//step 3：save the mapping
		
	}

}
