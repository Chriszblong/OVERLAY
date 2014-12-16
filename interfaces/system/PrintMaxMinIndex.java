package interfaces.system;

import java.util.Collection;
import java.util.Set;

public class PrintMaxMinIndex {

	public static void printTheMaxMin(Set<Integer> set)
	{
		int max=-1;
		int min=-1;
		for(Integer id:set)
		{
			if(max==-1&&min==-1)
			{
				max=id;
				min=id;
			}else
			{
				if(max<id)
				{
					max=id;
				}
				if(min>id)
				{
					min=id;
				}
			}
		}
		
		System.out.println("Min:"+min+"\t\tMax"+max);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


	public static void printTheMaxMin(Collection<Integer> values) {
		// TODO Auto-generated method stub
		int max=-1;
		int min=-1;
		for(Integer id:values)
		{
			if(max==-1&&min==-1)
			{
				max=id;
				min=id;
			}else
			{
				if(max<id)
				{
					max=id;
				}
				if(min>id)
				{
					min=id;
				}
			}
		}
		
		System.out.println("Min:"+min+"\t\tMax"+max);
		
	}

}
