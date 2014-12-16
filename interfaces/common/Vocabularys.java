package interfaces.common;

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

public class Vocabularys {
	
	//Vocabulary文档统计 <关键字,文档频率>即<term,df>
	public HashMap<String, Integer> vocabularys;
	public String poiDataPath;
	
	public Vocabularys() {
		// TODO Auto-generated constructor stub
		vocabularys=new HashMap<String, Integer>();
		this.poiDataPath="";
	}

	public Vocabularys(String poiPath) {
		// TODO Auto-generated constructor stub
		vocabularys=new HashMap<String, Integer>();
		this.poiDataPath=poiPath;
	}
	public HashMap<String, Integer> calculateVocabularys() throws IOException
	{
		//数据格式NodeID  longitude latitude {term,freq}
		//nodeID没有用
		//59180 51.5238028 -0.1430911 pub 1 The 1 Green 1 Man 1
		poiDataPath=Configure.PROJECT_DATA_ROOT+Configure.TERM_DATA;
		InputStreamReader isr = new InputStreamReader(new FileInputStream(poiDataPath), "UTF-8");
		LineNumberReader vocabularyLineReader=new LineNumberReader(isr);
		
		String vocabularyOutPath=Configure.PROJECT_DATA_ROOT+IndirectConfig.VOCABULARY_LONDON;
		FileWriter vocabularyWriter=new FileWriter(vocabularyOutPath);
		
		String line;
		
		while((line=vocabularyLineReader.readLine())!=null)
		{
			String[] parts=line.split(Configure.SPLITE_FLAG);
			
			if((parts.length%2)==0||parts.length<5)
			{
				System.err.println("error line "+line);
				continue;
			}
			
			for(int i=3;i<parts.length;i=i+2)
			{
				String term=parts[i];
				if(vocabularys.containsKey(term))
				{
					vocabularys.put(term, vocabularys.get(term)+1);
				}else
				{
					vocabularys.put(term, 1);
				}
				
			}
		}
		vocabularyLineReader.close();
		
		for(String term:vocabularys.keySet())
		{
			String termDF=term+Configure.SPLITE_FLAG+vocabularys.get(term)+"\r\n";
			vocabularyWriter.write(termDF);
		}
		vocabularyWriter.close();
		return vocabularys;
	}
	
	
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Vocabularys vocabularys=new Vocabularys();
		vocabularys.calculateVocabularys();
		

	}

}
