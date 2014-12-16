package elements.component.mappingInverted;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;


public class MappingInvertedComponent  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7346050333432831182L;
	/**
	 * MBR可能是Region也可能是Edge
	 * 这就要求Edge和region的Id不能有相同的
	 * <EdgeID or RegionID,<Term,MaxSroce>>
	 */
	public HashMap<Integer, HashMap<String, Float>> MBRTermMaxScoreMap;
	/**
	 * InvertedComponet
	 */
	public HashMap<Integer,HashMap<String, Vector<InvertedFileEntry>>> termInvertedFileMap;
	
	public MappingInvertedComponent() {
		// TODO Auto-generated constructor stub
		MBRTermMaxScoreMap=new HashMap<Integer, HashMap<String,Float>>();
		termInvertedFileMap=new HashMap<Integer, HashMap<String,Vector<InvertedFileEntry>>>();
	}

	public void printKMapping(int k)
	{
		int count=0;
		for(Integer MBRID:MBRTermMaxScoreMap.keySet())
		{
			if(count<k)
			{
				System.out.println("Mapping:\t"+MBRTermMaxScoreMap.get(MBRID).toString());
			}
			else
			{
				return;
			}
			count++;
		}
	}
	public void printKInverted(int k)
	{
		int count=0;
		for(Integer invertID:termInvertedFileMap.keySet())
		{
			if(count<k)
			{
				System.out.println("Inverted:\t"+termInvertedFileMap.get(invertID).keySet().toString());
			}
			else
			{
				return;
			}
			count++;
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
