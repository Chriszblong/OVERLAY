package interfaces.common;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;

import Utils.Configure;

import elements.common.VertexPoint;

public class LoadBasicVertex {

	public HashMap<Integer, VertexPoint> vertexsMap;
	
	public String verterxPath;
	
	public LoadBasicVertex() {
		verterxPath=Configure.PROJECT_DATA_ROOT+Configure.VERTEX_PATH;
		this.vertexsMap=new HashMap<Integer, VertexPoint>();
	}
	
	public HashMap<Integer, VertexPoint> loadVertexs() throws IOException
	{
		LineNumberReader lineReader=new LineNumberReader(new FileReader(verterxPath));
		String line;
		
		//Data Type:0 51.5219996 -0.1459115
		while((line=lineReader.readLine())!=null)
		{
			String[] parts=line.split(Configure.SPLITE_FLAG);
			if(parts.length!=3)
			{
				continue;
			}
			int vertexID=Integer.parseInt(parts[0]);
			double x=Double.parseDouble(parts[1]);
			double y=Double.parseDouble(parts[2]);
			VertexPoint v=new VertexPoint(vertexID, x, y);
			vertexsMap.put(vertexID, v);
		}
		lineReader.close();
		
		return vertexsMap;
	}
	

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		LoadBasicVertex loadV=new LoadBasicVertex();
		loadV.loadVertexs();

	}

}
