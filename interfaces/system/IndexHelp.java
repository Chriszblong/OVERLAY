package interfaces.system;

import java.util.Set;

public class IndexHelp {
	
	public static int FindTheMax(Set<Integer> set)
	{
		int maxIndex=-1;
		for(Integer tmp:set)
		{
			if(tmp>maxIndex)
			{
				maxIndex=tmp;
			}
		}
		return maxIndex;
	}
	
	
	public static int FindTheMin(Set<Integer> set)
	{
		int minIndex=-1;
		for(Integer tmp:set)
		{
			if(minIndex==-1)
			{
				minIndex=tmp;
			}else
			{
				if(tmp<minIndex)
				{
					minIndex=tmp;
				}
			}
		}
		return minIndex;
	}

	public static IndexFind findMaxMinIndex(Set<Integer> set)
	{
		IndexFind result=new IndexFind();
		for(Integer tmp:set)
		{
			if(result.maxIndex==-1)
			{
				result.maxIndex=tmp;
				result.minIndex=tmp;
			}else
			{
				if(tmp>result.maxIndex)
				{
					result.maxIndex=tmp;
				}
				if(tmp<result.minIndex)
				{
					result.minIndex=tmp;
				}
				
			}
		}
		if(result.maxIndex==-1)
		{
			System.err.println("error IndexFindMaxMin");
			System.exit(-1);
		}
		
		System.out.println(result.minIndex+"====="+result.maxIndex);
		return result;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
