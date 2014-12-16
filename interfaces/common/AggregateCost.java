package interfaces.common;

import java.util.HashMap;

import Utils.Configure;
import Utils.IndexConfig;
import elements.common.Edge;
import elements.regionBTree.Region;

public class AggregateCost {

	public AggregateCost() {
		// TODO Auto-generated constructor stub
	}
	public static float getRegionAggrateCost(Region regI,Region regT)
	{
		
		float cost=-1;
		
		
		float similartyValue=0;
		HashMap<String, Integer> kwdCount1=regI.kwdFreqMap;
		HashMap<String, Integer> kwdCount2=regT.kwdFreqMap;
		HashMap<String,SimilartyMathItem> kwdRegionFreqMap=new HashMap<String, SimilartyMathItem>();
		for(String kwd:kwdCount1.keySet())
		{
			int kwdObj1Freq=kwdCount1.get(kwd);
			int kwdObj2Freq=0;
			if(kwdCount2.containsKey(kwd))
			{
				kwdObj2Freq=kwdCount2.get(kwd);
			}
			SimilartyMathItem simiItem=new SimilartyMathItem(kwd,kwdObj1Freq,kwdObj2Freq);
			kwdRegionFreqMap.put(kwd, simiItem);
		}
		for(String kwd:kwdCount2.keySet())
		{
			//对于1中未包含的Region进行计算
			if(!kwdRegionFreqMap.containsKey(kwd))
			{
				int kwdObj1Freq=0;
				int kwdObj2Freq=kwdCount2.get(kwd);
				SimilartyMathItem simiItem=new SimilartyMathItem(kwd,kwdObj1Freq,kwdObj2Freq);
				kwdRegionFreqMap.put(kwd, simiItem);
			}
		}
		
		float numerator=0;
		float denominatorLeft=0;
		float denominatorRight=0;
		//to Calculate numerator and denominator
		for(SimilartyMathItem simiItem:kwdRegionFreqMap.values())
		{
			numerator+=simiItem.freqOfObj1*simiItem.freqOfObj2;
			denominatorLeft+=simiItem.freqOfObj1*simiItem.freqOfObj1;
			denominatorRight+=simiItem.freqOfObj2*simiItem.freqOfObj2;
		}
		
		
		
		if(numerator==0)
		{
			return 0;
		}
		
		//Calculate the result similartyValue;
		similartyValue=(float) (numerator/((Math.sqrt(denominatorRight))*(Math.sqrt(denominatorLeft))));
		
//		return similartyValue;
		HashMap<Integer, String> regIBorders=new HashMap<Integer, String>();
	
		HashMap<Integer, String> newBorders=new HashMap<Integer, String>();

		for(Integer boid:regI.borderVertexs.keySet())
		{
			if(regI.borderVertexs.get(boid).equals(IndexConfig.VERTEX_TYPE_BORDER))
			{
				regIBorders.put(boid, regI.borderVertexs.get(boid));
				newBorders.put(boid, regI.borderVertexs.get(boid));
			}
		}
		for(Integer boid:regT.borderVertexs.keySet())
		{
			if(regT.borderVertexs.get(boid).equals(IndexConfig.VERTEX_TYPE_BORDER))
			{
				newBorders.put(boid, regT.borderVertexs.get(boid));
				
			}
		}
//		newBorders.putAll(regI.borderVertexs);
//		newBorders.putAll(regT.borderVertexs);
		
		int newVertexNums=newBorders.size()-regIBorders.size();
		
		cost=newVertexNums/(1+Configure.AGGRATION_PARAMETER*similartyValue);
		return cost;
		
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
