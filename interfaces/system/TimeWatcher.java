package interfaces.system;

public class TimeWatcher {
	private long startTime;
	private long endTime;
	
	private long tmpTime;
	private long runtime;
	public TimeWatcher() {
		// TODO Auto-generated constructor stub
		clear();
	}
	
	public void start()
	{
		clear();
		startTime=System.currentTimeMillis();
		runtime=0;
	}
	public void stop()
	{
		endTime=System.currentTimeMillis();
		runtime=endTime-startTime;
		
	}
	public long getRunTime()
	{
		endTime=System.currentTimeMillis();
		runtime=endTime-startTime;
		return runtime;
		
	}
	
	public void seeCurrentRunTime()
	{
		tmpTime=System.currentTimeMillis();
		long tmpRunTime=tmpTime-startTime;
		if(tmpRunTime%10000==0)
		{
			System.out.println("Run Time:"+(tmpTime-startTime));	
		}
		
	}

	public void seeRunTime()
	{
		System.out.println("runTime is \t"+runtime);
	}
	
	public void seeRunTime(String processName)
	{
		System.out.println(processName+"\t\trunTime is \t"+runtime);
	}
	public void clear()
	{
		startTime=0;
		endTime=0;
		runtime=0;
		tmpTime=0;
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
