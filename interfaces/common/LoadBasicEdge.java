package interfaces.common;

import interfaces.system.IndexFind;
import interfaces.system.IndexHelp;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;

import Utils.Configure;
import Utils.IndexConfig;
import elements.common.Edge;
import elements.common.VertexPoint;

public class LoadBasicEdge {
	public String edgeFilePath;
	public HashMap<Integer, Edge> baseEdges;
	public HashMap<Integer, VertexPoint> baseVertex;
	public HashMap<Integer,HashMap<Integer, Integer>> vertexToVertexAtEdge;
	
	
//	public int 
	
	private int max_vertex_index_To_NewEdge=-1; 

	public LoadBasicEdge() {
		// TODO Auto-generated constructor stub
		this.baseEdges=new HashMap<Integer, Edge>();
		this.baseVertex=new HashMap<Integer, VertexPoint>();
		this.edgeFilePath=Configure.PROJECT_DATA_ROOT+Configure.EDGE_PATH;
		this.vertexToVertexAtEdge=new HashMap<Integer, HashMap<Integer,Integer>>();
		
	}

	
	public HashMap<Integer, Edge> loadEdges() throws IOException
	{
		
		
		LineNumberReader lineNumberReader=new LineNumberReader(new FileReader(edgeFilePath));
		String line;
		LoadBasicVertex vertexsMapIDX=new LoadBasicVertex();
		baseVertex=vertexsMapIDX.loadVertexs();
		max_vertex_index_To_NewEdge=IndexConfig.MAX_ROAD_VERTEX_INDEX+1;
		System.out.println(max_vertex_index_To_NewEdge);
		
		boolean flagEdge=false;
		
		while((line=lineNumberReader.readLine())!=null)
		{
			String[] parts=line.split(Configure.SPLITE_FLAG);
//			if(parts.length!=7)
//			{
//				System.out.println(line);
//				continue;
//			}
//			int edgeID=Integer.parseInt(parts[0]);
			
			int edgeID=max_vertex_index_To_NewEdge;
			
			int v1ID=Integer.parseInt(parts[1]);
			int v2ID=Integer.parseInt(parts[2]);
//			System.out.println(line);
//			double dist=Double.parseDouble(parts[3]);
			
			if(v1ID==v2ID)
			{
				continue;
			}

			VertexPoint v1=baseVertex.get(v1ID);
			VertexPoint v2=baseVertex.get(v2ID);
			Edge edge=new Edge(edgeID, v1, v2);
//			edge.Distance=(int) (dist*Configure.MAP_CONVERSION_RATIO);
			if(!vertexToVertexAtEdge.containsKey(v1ID))
			{
				vertexToVertexAtEdge.put(v1ID, new HashMap<Integer, Integer>());
			}
			vertexToVertexAtEdge.get(v1ID).put(v2ID, edgeID);
			
			if(!vertexToVertexAtEdge.containsKey(v2ID))
			{
				vertexToVertexAtEdge.put(v2ID, new HashMap<Integer, Integer>());
			}
			vertexToVertexAtEdge.get(v2ID).put(v1ID, edgeID);
			
//			//��������
//			if(!vertexToVertexAtEdge.containsKey(v1ID))
//			{
//				//��ûV1-->��ôֱ���ж�V2ID�Ƿ����
//				if(!vertexToVertexAtEdge.containsKey(v2ID))
//				{
//					//û��V1-->û��V2
//					//��ôֱ�ӷ�
//					if(!vertexToVertexAtEdge.containsKey(v1ID))
//					{
//						vertexToVertexAtEdge.put(v1ID, new HashMap<Integer,Integer>());
//					}
//					vertexToVertexAtEdge.get(v1ID).put(v1ID, edgeID);
//					
//				}
//				else
//				{
//					//û��V1-��V2
//					if(vertexToVertexAtEdge.get(v2ID).containsKey(v1ID))
//					{
//						//��v2 ����v2-v1
//						//�Ͳ�����
//					}else
//					{
//						//��V2����û��V1
//						if(!vertexToVertexAtEdge.containsKey(v1ID))
//						{
//							vertexToVertexAtEdge.put(v1ID, new HashMap<Integer,Integer>());
//						}
//						vertexToVertexAtEdge.get(v1ID).put(v1ID, edgeID);
//					}
//					
//				}
//			}else
//			{
//				//��V1-->���ж��Ƿ���v1-v2-edge
//				if(vertexToVertexAtEdge.get(v1ID).containsKey(v2ID))
//				{
//					//��V1����V2
//				}
//				else
//				{
//					//��V1����û��v1-v2
//					if(!vertexToVertexAtEdge.containsKey(v2ID))
//					{
//						//��V1-->û��V2
//						//��ôֱ�ӷ�
//						if(!vertexToVertexAtEdge.containsKey(v1ID))
//						{
//							vertexToVertexAtEdge.put(v1ID, new HashMap<Integer,Integer>());
//						}
//						vertexToVertexAtEdge.get(v1ID).put(v1ID, edgeID);
//						
//					}
//					else
//					{
//						//û��V1-��V2
//						if(vertexToVertexAtEdge.get(v2ID).containsKey(v1ID))
//						{
//							//��v2 ����v2-v1
//							//�Ͳ�����
//						}else
//						{
//							//��V2����û��V1
//							if(!vertexToVertexAtEdge.containsKey(v1ID))
//							{
//								vertexToVertexAtEdge.put(v1ID, new HashMap<Integer,Integer>());
//							}
//							vertexToVertexAtEdge.get(v1ID).put(v1ID, edgeID);
//						}
//						
//					}
//					
//				}
//			}
//			
			
//			System.out.println(edgeID);
			baseEdges.put(edgeID, edge);
			max_vertex_index_To_NewEdge++;
			
		}
//		System.out.println(lineNumberReader.getLineNumber());
		lineNumberReader.close();
		System.out.println(baseEdges.size());
		
		
		return baseEdges;
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		LoadBasicEdge loadedge=new LoadBasicEdge();
		loadedge.loadEdges();
	}

}
