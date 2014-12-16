package interfaces.common;

import Utils.QueryConfigure;

public class Scorer {

//	public final static int MAX_SPATIAL_DISTANCE=1000000000;
//	public final static int MAX_SPATIAL_DISTANCE=1032295;
	
//	public static float alpha = 0.7f;
	
	public static float getSpatialScore(int distance)
	{
		if(distance<0)
		{
			return 0;
		}else if (distance>QueryConfigure.MAX_SPATIAL_DISTANCE) {
			return 0;
		}
		else
		{
			return QueryConfigure.ALPHA*(1 - (float)(distance/QueryConfigure.MAX_SPATIAL_DISTANCE));
		}
		
	}
	
	
	public Scorer() {
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
