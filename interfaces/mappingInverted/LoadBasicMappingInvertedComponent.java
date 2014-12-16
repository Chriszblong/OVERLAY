package interfaces.mappingInverted;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Vector;

import Utils.Configure;
import Utils.IndirectConfig;

import elements.component.mappingInverted.InvertedFileEntry;
import elements.component.mappingInverted.MappingInvertedComponent;

public class LoadBasicMappingInvertedComponent {
	/**
	 * mapping Component&&Inverted mapping Component
	 */
	public MappingInvertedComponent mappingInvertedComponent;
	
	public String dataOfPOIWithEdge;
	
	public LoadBasicMappingInvertedComponent() {
	
		init();
	}
	
	public void init()
	{
		mappingInvertedComponent=new MappingInvertedComponent();
		dataOfPOIWithEdge=Configure.PROJECT_DATA_ROOT+IndirectConfig.POI_WITH_EDGE_RCA_FORMAT;
		
		loadDataInfo();
	}
	
	public void loadDataInfo()
	{
		loadBasicMappingInvertedComponent();
	}
	
	public void loadBasicMappingInvertedComponent()
	{
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(new FileInputStream(dataOfPOIWithEdge), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LineNumberReader poiWithEdgeLineReader=new LineNumberReader(isr);
		String line;
		
		int poiID=0;
		int invertedEntryID=0;
		try {
			
			
			while((line=poiWithEdgeLineReader.readLine())!=null)
			{
				String parts[]=line.split(Configure.SPLITE_FLAG);
				int edgeID=Integer.parseInt(parts[0]);
				HashMap<String, Integer> kwdCount = new HashMap<String, Integer>();
				if(parts.length%2==0)
				{
					continue;
				}
				for(int i=3;i<parts.length;i+=2)
				{
					String kwd = parts[i];
					
					int freq = Integer.parseInt(parts[i + 1]);
					if (kwdCount.containsKey(kwd)) {
						kwdCount.put(kwd, kwdCount.get(kwd) + freq);
					} else {
						kwdCount.put(kwd, freq);
					}
				}
				
				double sumeOfWeightSquare = 0;
				double weight;
				for (String kwd : kwdCount.keySet()) {
					int freq = kwdCount.get(kwd);
					weight = 1 + Math.log(freq);
					sumeOfWeightSquare += weight * weight;
				}
				sumeOfWeightSquare = Math.sqrt(sumeOfWeightSquare);	
				
				for (String kwd : kwdCount.keySet()) {
					int freq = kwdCount.get(kwd);
					float textScore=(float) ((1 + Math.log(freq))/ sumeOfWeightSquare);
					
					
					if(!mappingInvertedComponent.MBRTermMaxScoreMap.containsKey(edgeID))
					{
						mappingInvertedComponent.MBRTermMaxScoreMap.put(edgeID, new HashMap<String, Float>());
					}
					
					if(!mappingInvertedComponent.MBRTermMaxScoreMap.get(edgeID).containsKey(kwd))
					{
						mappingInvertedComponent.MBRTermMaxScoreMap.get(edgeID).put(kwd, textScore);
					}
					else
					{
						float MaxSocre=mappingInvertedComponent.MBRTermMaxScoreMap.get(edgeID).get(kwd);
						if(textScore>MaxSocre)
						{
							mappingInvertedComponent.MBRTermMaxScoreMap.get(edgeID).put(kwd, textScore);
						}
					}
					
					if(!mappingInvertedComponent.termInvertedFileMap.containsKey(edgeID))
					{
						mappingInvertedComponent.termInvertedFileMap.put(edgeID, new HashMap<String, Vector<InvertedFileEntry>>());
					}
					
					if(!mappingInvertedComponent.termInvertedFileMap.get(edgeID).containsKey(kwd))
					{
						mappingInvertedComponent.termInvertedFileMap.get(edgeID).put(kwd, new Vector<InvertedFileEntry>());
					}
					InvertedFileEntry tmpInvertFileEntry=new InvertedFileEntry(poiID, invertedEntryID, edgeID, kwd, textScore, 0);
					mappingInvertedComponent.termInvertedFileMap.get(edgeID).get(kwd).add(tmpInvertFileEntry);
					invertedEntryID++;
					
				}
				
				
				
				poiID++;
				
				
			}
			poiWithEdgeLineReader.close();
//			System.out.println("invertedEntryID\t"+invertedEntryID+"\tpoiID\t"+poiID);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void printKMapping(int k)
	{
		mappingInvertedComponent.printKMapping(k);
	}
	public void printKInverted(int k)
	{
		mappingInvertedComponent.printKInverted(k);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoadBasicMappingInvertedComponent test=new LoadBasicMappingInvertedComponent();
		test.printKInverted(10);
		test.printKMapping(10);
	}

}
