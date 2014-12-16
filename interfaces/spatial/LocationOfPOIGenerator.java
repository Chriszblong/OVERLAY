package interfaces.spatial;

import interfaces.common.DistanceHelp;
import interfaces.common.LoadBasicEdge;
import interfaces.common.LoadBasicVertex;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;

import Utils.Configure;
import Utils.IndirectConfig;
import elements.common.Edge;
import elements.common.VertexPoint;

public class LocationOfPOIGenerator {
	/**
	 * <VertexID,<EdgeID,Edge>>
	 */
	public HashMap<Integer, HashMap<Integer,Edge>> vertexEdgesMap;
	public HashMap<Integer, VertexPoint>  vertexsMap;
	public HashMap<Integer, Edge> edgesMap;
	public EdgeRegionRTreeQuery edgeQueryIDX;
	public VertexPointRTreeQuery vertexQueryIDX;
	public String SPLIT=Configure.SPLITE_FLAG;

	public LocationOfPOIGenerator() {
		vertexEdgesMap=new HashMap<Integer, HashMap<Integer,Edge>>();
		vertexsMap=new HashMap<Integer, VertexPoint>();
		edgesMap=new HashMap<Integer, Edge>();
		LoadVertexEdgeInfo();
		try {
			edgeQueryIDX=new EdgeRegionRTreeQuery();
			vertexQueryIDX=new VertexPointRTreeQuery();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void createNewPOIFileWithEdge() throws IOException
	{
		InputStreamReader isr = new InputStreamReader(new FileInputStream(Configure.PROJECT_DATA_ROOT+Configure.POI_SF_40M), "UTF-8");
		
		LineNumberReader oldPOILineReader=new LineNumberReader(isr);
		
		FileWriter NewPOIWithEdge=new FileWriter(Configure.PROJECT_DATA_ROOT+IndirectConfig.POI_WITH_EDGE_RCA_FORMAT);
		
		String line;
		String poiWithEdgeString;
		int nearestEdgeID=-1;
		int nearestNodeID=-1;
		int relocatingCount=0;
		int poiCount=0;
		while((line=oldPOILineReader.readLine())!=null)
		{
			//将nearestNodeID重置为-1
			nearestNodeID=-1;
			nearestEdgeID=edgeQueryIDX.positioningToRegion(line);
			String[] parts=line.split(Configure.SPLITE_FLAG);
			if(nearestEdgeID==-1)
			{
//				System.out.println("reLoacting");
				relocatingCount++;
				nearestNodeID=vertexQueryIDX.queryAtStringLevel(line);
				if(nearestNodeID==-1)
				{
					System.err.println("wrong line:"+line);
					continue;
				}
				if(vertexEdgesMap.containsKey(nearestNodeID))
				{
					HashMap<Integer, Edge> reloadEdgeMap=vertexEdgesMap.get(nearestNodeID);
					//没有变
					if(reloadEdgeMap.size()==0)
					{
						nearestEdgeID=-1;
						continue;
					}
					
					int tmpNearestEdgeID=-1;
					int tmpMinDistance=-1;
					
					double qx=Double.parseDouble(parts[2]);
					double qy=Double.parseDouble(parts[3]);
					
					VertexPoint qVertexPoint=new VertexPoint(0, qx, qy);
					
					for(Integer reEdgeID:reloadEdgeMap.keySet())
					{
						VertexPoint v1=reloadEdgeMap.get(reEdgeID).v1;
						VertexPoint v2=reloadEdgeMap.get(reEdgeID).v2;
						VertexPoint targetVertex=v1.id==nearestNodeID?v2:v1;
						if(tmpNearestEdgeID==-1)
						{
							tmpNearestEdgeID=reEdgeID;
							
							tmpMinDistance=DistanceHelp.getRealDistanceToVertex(qVertexPoint, targetVertex);
							break;
						}else
						{
//							int currentTmpDistance=DistanceHelp.getRealDistanceToVertex(qVertexPoint, targetVertex);
//							if(currentTmpDistance<tmpMinDistance)
//							{
//								tmpMinDistance=currentTmpDistance;
//								tmpNearestEdgeID=reEdgeID;
//							}
						}
					}
					nearestEdgeID=tmpNearestEdgeID;
					
				}
				else
				{
					System.err.println("Not loacting");
					continue;
				}
				
			}
			else
			{
				
			}
			
			if(nearestEdgeID==-1)
			{
				System.err.println("there still not locating");
				continue;
			}
			
			poiWithEdgeString=nearestEdgeID+SPLIT+parts[2]+SPLIT+parts[3];
			for(int i=4;i<parts.length;i++)
			{
				poiWithEdgeString+=SPLIT+parts[i]+SPLIT+"1";
			}
			poiWithEdgeString+="\r\n";
			NewPOIWithEdge.write(poiWithEdgeString);
			poiCount++;
			if(poiCount%1000==0)System.err.println(poiCount);
//			System.err.println("ss"+oldPOILineReader.getLineNumber());
			
		}
		NewPOIWithEdge.close();
		oldPOILineReader.close();
		System.out.println("Reloacting Count is"+relocatingCount);
		
	}
	
	
	public void LoadVertexEdgeInfo()
	{
//		LoadVertex loadVertex=new LoadVertex();
		LoadBasicEdge loadBaseEdge=new LoadBasicEdge();
		try {
			edgesMap=loadBaseEdge.loadEdges();
			vertexsMap=loadBaseEdge.baseVertex;
//			vertexsMap=loadVertex.loadVertexs();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(vertexsMap.size()==0||edgesMap.size()==0)
		{
			System.err.println("vertexsMap/edgesMap is empty！");
			return;
		}
		
		for(Integer edgeID:edgesMap.keySet())
		{
			Edge tmp=new Edge();
			tmp=edgesMap.get(edgeID);
			int v1ID=tmp.v1.id;
			int v2ID=tmp.v2.id;
			
			if(vertexEdgesMap.containsKey(v1ID))
			{
				if(!vertexEdgesMap.get(v1ID).containsKey(edgeID))
				{
					vertexEdgesMap.get(v1ID).put(edgeID, tmp);
				}
			}
			else
			{
				vertexEdgesMap.put(v1ID, new HashMap<Integer, Edge>());
				
				if(!vertexEdgesMap.get(v1ID).containsKey(edgeID))
				{
					vertexEdgesMap.get(v1ID).put(edgeID, tmp);
				}
			}
			
			
			if(vertexEdgesMap.containsKey(v2ID))
			{
				if(!vertexEdgesMap.get(v2ID).containsKey(edgeID))
				{
					vertexEdgesMap.get(v2ID).put(edgeID, tmp);
				}
			}
			else
			{
				vertexEdgesMap.put(v2ID, new HashMap<Integer, Edge>());
				
				if(!vertexEdgesMap.get(v2ID).containsKey(edgeID))
				{
					vertexEdgesMap.get(v2ID).put(edgeID, tmp);
				}
			}
		}
		
	}
	
	
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LocationOfPOIGenerator poiLocating=new LocationOfPOIGenerator();
		try {
			poiLocating.createNewPOIFileWithEdge();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(poiLocating.vertexEdgesMap.size());

	}

}
