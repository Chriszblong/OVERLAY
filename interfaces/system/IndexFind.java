package interfaces.system;

public class IndexFind {

	public int maxIndex;
	public int minIndex;
	
	public IndexFind() {
		maxIndex=-1;
		minIndex=-1;
		// TODO Auto-generated constructor stub
	}

	
	public void printMaxMin()
	{
		System.out.println("this Time Max is:\t"+maxIndex+"\tMin is :\t"+minIndex);
	}
	public void printMaxMin(String MapName)
	{
		System.out.println(MapName+"\tthis Time Max is:\t"+maxIndex+"\tMin is :\t"+minIndex);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
