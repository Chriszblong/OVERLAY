import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;

import Utils.Configure;


public class CaculateDistance {

	
	HashMap<Integer, HashMap<Integer, Integer>> vertexToBorderDistance;
	public CaculateDistance() {
		
		init();
		
	}
	
	
	public void init()
	{
		vertexToBorderDistance=new HashMap<Integer, HashMap<Integer,Integer>>();
		try {
			loadBasicInfo();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void loadBasicInfo() throws IOException
	{
		LineNumberReader lineReader=new LineNumberReader(new FileReader(Configure.PROJECT_DATA_ROOT+Configure.TEMP_DATA_FOLDER+Configure.TEMP_BORDER_TO_BORDER__DATA));
		String line;
		
		while((line=lineReader.readLine())!=null)
		{
			String[] parts=line.split(Configure.SPLITE_FLAG);
			int vertex=Integer.parseInt(parts[0]);
			int border=Integer.parseInt(parts[1]);
			if(border==vertex)
			{
				continue;
			}
			
			if(!vertexToBorderDistance.containsKey(vertex))
			{
				vertexToBorderDistance.put(vertex,new HashMap<Integer,Integer>() );
			}
			
			vertexToBorderDistance.get(vertex).put(border, -1);
		}
		lineReader.close();
		
	}
	public void run() throws IOException
	{
		
		FileWriter fw=new FileWriter("E:/WorkSpace_Data_LAB/LAB_PPZHAO_DATA/OVERLAY_SF/TEMP/1.txt");
		
		GTreeAPI gtrapi=new GTreeAPI();
		int fileCount=1;
		int vertexCount=1;
		for(Integer vertexID:vertexToBorderDistance.keySet())
		{
			int[] tempV=new int[vertexToBorderDistance.get(vertexID).size()];
			int cout=0;
			for(Integer ttVID:vertexToBorderDistance.get(vertexID).keySet())
			{	
				tempV[cout]=ttVID;
				cout++;
			}
			VertexDist[] result=gtrapi.getAllCanditateDist(vertexID, tempV);
			
			if(vertexCount%40==0)
			{
				fileCount++;
				fw.close();
				System.out.println("save"+vertexCount);
				 fw=new FileWriter("E:/WorkSpace_Data_LAB/LAB_PPZHAO_DATA/OVERLAY_SF/TEMP/"+fileCount+".txt");
			}
			for(int i=0;i<result.length;i++)
			{
				fw.write(vertexID+" "+result[i].vertexID+" "+result[i].distance+"\r\n");
				
			}
//			System.out.println("save"+vertexID);
			vertexCount++;
			
		}
		fw.close();
		
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		CaculateDistance test=new CaculateDistance();
		test.run();
	}

}
