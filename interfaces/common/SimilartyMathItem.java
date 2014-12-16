package interfaces.common;

public class SimilartyMathItem {
	public String kwd;
	public int freqOfObj1=0;
	public int freqOfObj2=0;
	
	public SimilartyMathItem() {
		// TODO Auto-generated constructor stub
		
		init();
	}
	public void init()
	{
		this.kwd=new String();
		this.freqOfObj1=0;
		this.freqOfObj2=0;
	}
	public SimilartyMathItem(String kwd ,int freqobj1,int freqobj2)
	{
		this.kwd=kwd;
		this.freqOfObj1=freqobj1;
		this.freqOfObj2=freqobj2;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
