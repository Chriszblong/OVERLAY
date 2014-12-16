package Utils;

public class IOCounter {

	public static int io = 0;
	
	public static void seqRead(int bytes)
	{
		io += Math.ceil(bytes/4096.0);
	}
	
}
