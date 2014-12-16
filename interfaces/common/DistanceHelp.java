package interfaces.common;

import Utils.Configure;
import elements.common.Edge;
import elements.common.VertexPoint;
import elements.query.CandidateEdge;

public class DistanceHelp {

	public DistanceHelp() {
		// TODO Auto-generated constructor stub
	}

	public static double getMapDistanceToVertex(VertexPoint v1,VertexPoint v2)
	{
		double xDist=Math.abs(v1.x-v2.x);
		double yDist=Math.abs(v1.y-v2.y);
		
		return Math.sqrt(xDist*xDist+yDist*yDist);
		
	}
	
	
	public static int getRealDistanceToVertex(VertexPoint v1,VertexPoint v2)
	{
		double mapDist=getMapDistanceToVertex(v1, v2)*Configure.MAP_CONVERSION_RATIO;
	
		return (int) Math.floor(mapDist);
	}
	
	public static double getMapDistanceToEdge(VertexPoint v1,Edge e1)
	{
		double x1=e1.v1.x;
		double y1=e1.v1.y;
		double x2=e1.v2.x;
		double y2=e1.v2.y;
		
		
		double x3=v1.x;
		double y3=v1.y;
		
		double x2_x1=x2-x1;
		double y2_y1=y2-y1;
		
		double x3_x1=x3-x1;
		double y3_y1=y3-y1;
		
		double adc_S=Math.abs(y3_y1*x2_x1-x3_x1*y2_y1)/2;
		
		double ab_l=Math.sqrt(x2_x1*x2_x1+y2_y1*y2_y1);
		 
		double h=adc_S*2/ab_l;
		return h;
	}
	
	public static int getRealDistanceToEdge(VertexPoint v1,Edge e1)
	{
		double mapDist=getMapDistanceToEdge(v1, e1)*Configure.MAP_CONVERSION_RATIO;
		
		return (int) Math.floor(mapDist);
		
	}
	
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		VertexPoint v1=new VertexPoint(0, -8.7421499E7, 3.0421829E7);
		VertexPoint v2=new VertexPoint(0, -8.7419205E7 ,3.0421875E7);
		System.out.println(DistanceHelp.getRealDistanceToVertex(v1, v2));
		
		

	}

	public static int getRealDistanceToEdge(VertexPoint qVertex,
			CandidateEdge candidateEdge) {
		double mapDist=getMapDistanceToEdge(qVertex, candidateEdge)*Configure.MAP_CONVERSION_RATIO;
		
		return (int) Math.floor(mapDist);
	}

	private static double getMapDistanceToEdge(VertexPoint qVertex,
			CandidateEdge candidateEdge) {
		double x1=candidateEdge.v1.x;
		double y1=candidateEdge.v1.y;
		double x2=candidateEdge.v2.x;
		double y2=candidateEdge.v2.y;
		
		
		double x3=qVertex.x;
		double y3=qVertex.y;
		
		double x2_x1=x2-x1;
		double y2_y1=y2-y1;
		
		double x3_x1=x3-x1;
		double y3_y1=y3-y1;
		
		double adc_S=Math.abs(y3_y1*x2_x1-x3_x1*y2_y1)/2;
		
		double ab_l=Math.sqrt(x2_x1*x2_x1+y2_y1*y2_y1);
		 
		double h=adc_S*2/ab_l;
		return h;
	}

}
