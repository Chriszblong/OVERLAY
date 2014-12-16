package elements.regionBTree;

import interfaces.common.DistanceHelp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

import spatialIndexSrc.spatialindex.rtree.Index;

import Utils.Configure;
import Utils.IndexConfig;
import elements.common.Edge;
import elements.common.VertexPoint;
import elements.component.adjacency.AdjVertexEntry;
/**
 * 
 * @author BYRD
 * 说明：因为到最后访问的region都是一个Edge组成的Region
 */
public class Region implements Comparable<Region>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -796651296934868063L;

	/**
	 * regionID
	 */
	public int id;
	
	public int layID; //表示lay的层级
	
	
	public int regionBelong;//扩展之后所属的region
	
	public boolean aggregated=false;
	
	/**
	 * POI中所包含的关键字信息<keyword,Scores>
	 * Edge里面存储在该边里面每个keyword的单个Object的最大得分
	 */
	public HashMap<String, Float> keywordsMaxScore;
	/**
	 * kwd freq;
	 * 
	 */
	public HashMap<String,Integer> kwdFreqMap;
	
	public HashMap<Integer ,Integer> edges;
	/**
	 * Border集合
	 */
	public HashMap<Integer, String> borderVertexs;
	/**
	 * adjacency集合
	 */
	public HashMap<Integer, String> vertexs;
	
	/**
	 * POI的数量
	 */
	public int quantityOfPOI;
	
	/**
	 * borderVertexsDistance<V1,<V2,distance>>
	 */
	public HashMap<Integer, HashMap<Integer, Integer>> borderVertexsDistance;
	
	
	public Region() {
		// TODO Auto-generated constructor stub
		this.id=-1;
		this.layID=-1;
		this.keywordsMaxScore=new HashMap<String, Float>();
		this.edges=new HashMap<Integer, Integer>();
		this.quantityOfPOI=0;
		init(null);
		
	}
	public Region(int id,int layid,HashMap<Integer, Edge> edges)
	{
		this.id=id;
		this.layID=layid;
		this.keywordsMaxScore=new HashMap<String, Float>();
		this.edges=new HashMap<Integer, Integer>();
		
		init(edges);
	}
	
	
	public Region(int id,int layid,HashMap<String, Float> kwdMS,HashMap<Integer, Edge> edges)
	{
		this.id=id;
		this.layID=layid;
		this.keywordsMaxScore=kwdMS;
		this.edges=new HashMap<Integer, Integer>();
		init(edges);	
	}
	public Region(int id,int layid,HashMap<String, Float> kwdMS,HashMap<Integer, Edge> edges,int regionBelong)
	{
		this.id=id;
		this.layID=layid;
		this.keywordsMaxScore=kwdMS;
		this.edges=new HashMap<Integer, Integer>();
		init(edges);
		this.regionBelong=regionBelong;
			
	}
	
	/**
	 * 通用初始化
	 */
	public void init(HashMap<Integer, Edge> edges)
	{
		this.regionBelong=-1;
		
		this.aggregated=false;
		
		this.borderVertexsDistance=new HashMap<Integer, HashMap<Integer,Integer>>();
		
		this.vertexs=new HashMap<Integer, String>();
		
		this.borderVertexs=new HashMap<Integer, String>();
		
		this.kwdFreqMap=new HashMap<String, Integer>();
		
		this.quantityOfPOI=0;
		
		
		//seedRegion
		if(this.layID==0)
		{
			if(edges!=null)
			{
				
			}
			for(Edge edg:edges.values())
			{
				borderVertexs.put(edg.v1.id, IndexConfig.VERTEX_TYPE_BORDER);
				borderVertexs.put(edg.v2.id, IndexConfig.VERTEX_TYPE_BORDER);
				int dist=DistanceHelp.getRealDistanceToVertex(edg.v1,edg.v2);
				
			}
		}
	}
	
	
	
	/**
	 * 添加边并更新KwdFreq
	 * 更新KwdMaxScore
	 * @param edge
	 */
	public void addEdge(Edge edge)
	{
		if(edges.containsKey(edge.id))
		{
			System.err.println("怎么会已经有这个Edge呢？");
		}
		else
		{
			//Edges加入edge
			edges.put(edge.id, this.id);
			this.quantityOfPOI+=edge.poiMap.size();
			//vertexs更新
			
			//Update kwdFreq;
			
			for(String kwd:edge.kwdCount.keySet())
			{
				if(this.kwdFreqMap.containsKey(kwd))
				{
					this.kwdFreqMap.put(kwd, this.kwdFreqMap.get(kwd)+edge.kwdCount.get(kwd));
				}
				else
				{
					this.kwdFreqMap.put(kwd, edge.kwdCount.get(kwd));
				}
			}
			
			//Update kwdScore
			for(String kwd:edge.keywordsMaxScoreMap.keySet())
			{
				if(this.keywordsMaxScore.containsKey(kwd))
				{
					if(edge.keywordsMaxScoreMap.get(kwd)>this.keywordsMaxScore.get(kwd))
					{
						this.keywordsMaxScore.put(kwd, edge.keywordsMaxScoreMap.get(kwd));
					}
				}
				else
				{
					this.keywordsMaxScore.put(kwd, edge.keywordsMaxScoreMap.get(kwd));
				}
			}
			
		}
	}
	
	/**
	 * 添加边并更新KwdFreq
	 * 更新KwdMaxScore
	 * @param edge
	 */
	public void addEdgeInAggrating(Edge edge){
		if(edges.containsKey(edge.id))
		{
			System.err.println("怎么会已经有这个Edge呢？");
		}
		else
		{
			//Edges加入edge
			edges.put(edge.id, this.id);
			//vertexs更新
			
//			//Update kwdFreq;
//			
//			for(String kwd:edge.kwdCount.keySet())
//			{
//				if(this.kwdFreqMap.containsKey(kwd))
//				{
//					this.kwdFreqMap.put(kwd, this.kwdFreqMap.get(kwd)+edge.kwdCount.get(kwd));
//				}
//				else
//				{
//					this.kwdFreqMap.put(kwd, edge.kwdCount.get(kwd));
//				}
//			}
			
			//Update kwdScore
			for(String kwd:edge.keywordsMaxScoreMap.keySet())
			{
				if(this.keywordsMaxScore.containsKey(kwd))
				{
					if(edge.keywordsMaxScoreMap.get(kwd)>this.keywordsMaxScore.get(kwd))
					{
						this.keywordsMaxScore.put(kwd, edge.keywordsMaxScoreMap.get(kwd));
					}
				}
				else
				{
					this.keywordsMaxScore.put(kwd, edge.keywordsMaxScoreMap.get(kwd));
				}
			}
			
		}
//		reCountPOICount();
		//reCount the border
		
	}
	
	
//	public void AggratedRegion(Region regionJ, int targetBorder)
//	{
//		//更新RegionI中的内容
//		//step1:pseudo-document信息更新
//		HashMap<String, Float> kwdMaxScore=regionJ.keywordsMaxScore;
//		
//		for(Edge ed:regionJ.edges.values())
//		{
//			addEdge(ed);
//		}
//		//step2:borders信息更新信息更新
//			
//		//step2-1:两个Region到包含的BorderVertex
//		HashMap<Integer, String> commonBorders=new HashMap<Integer, String>();
//		//对RegionI进行遍历
//		for(Integer tmpBorderID:this.borderVertexs.keySet())
//		{
//			commonBorders.put(tmpBorderID, IndexConfig.VERTEX_TYPE_BORDER);
//		}
//		//对RegionJ进行遍历
//		for(Integer tmpBorderID:regionJ.borderVertexs.keySet())
//		{
//			commonBorders.put(tmpBorderID, IndexConfig.VERTEX_TYPE_BORDER);
//		}
//		
//		 for(Integer newborderID:regionJ.borderVertexs.keySet())
//		 {
//			 if(this.borderVertexs.containsKey(newborderID))
//			 {
//			 
//			 }
//			 else
//			 {
//				 this.borderVertexs.put(newborderID, IndexConfig.VERTEX_TYPE_BORDER);
//				 
//				 //
//			 }
//		 }
//		
//	}
	
	

	/**
	 * 标记已合并
	 * @return
	 */
	public boolean markAsAggregated()
	{
		
		this.aggregated=true;
		return aggregated;
	}
	/**
	 * 标记是否已合并
	 * @return
	 */
	public boolean markAsUnAggregated()
	{
		
		this.aggregated=false;
		return aggregated;
	}
//	/**
//	 * 计算POI数量
//	 * @param edges
//	 * @return
//	 */
//	public int countPOIQueality(HashMap<Integer, Edge> edges)
//	{
//		int poiCount=0;
//		
//		for(Edge ed:edges.values())
//		{
//			poiCount+=ed.poiMap.size();
//		}
////		this.quantityOfPOI=poiCount;
//		return poiCount;
//	}
	
//	public void reCountPOICount()
//	{
//		this.quantityOfPOI=0;
////		for(Edge ed:edges.values())
////		{
////			this.quantityOfPOI+=ed.poiMap.size();
////		}
//		
//		
//		
//	}
	
	/**
	 * 转化String 以便测试打印
	 * @return
	 */
	public String getRegionString()
	{
		String regionString="";
		regionString="regionID\t"+id+"\tlay:\t"+layID+"\n";
		for(int i=0;i<edges.size();i++)
		{
//			regionString+="v1:\t"+edges.get(i).v1.id+"\tv2:\t"+edges.get(i).v2.id;
		}
		
		return regionString;
	}
	/**
	 * 
	 */
	public void removeBorders(int borderID)
	{
		
		this.borderVertexs.remove(borderID);
		
//		this.vertexs.put(borderID, IndexConfig.VERTEX_TYPE_UNBORDER);
		for(Integer tmpborderID:borderVertexsDistance.keySet())
		{
			if(tmpborderID==borderID)
			{
				borderVertexsDistance.remove(tmpborderID);
			}else
			{
				for(Integer tmpBID:borderVertexsDistance.get(tmpborderID).keySet())
				{
					if(tmpBID==borderID)
					{
						borderVertexsDistance.get(tmpborderID).remove(tmpBID);
					}
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
	/**
	 * 这是按什么排序的呢？反正peek拿到的是最小的
	 */
	@Override
	public int compareTo(Region arg) {
		// TODO Auto-generated method stub
		
		if(this.quantityOfPOI==arg.quantityOfPOI)
		{
			return this.id-arg.id;
		}
		
		return this.quantityOfPOI-arg.quantityOfPOI;
	}

}
