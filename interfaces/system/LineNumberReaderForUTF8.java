package interfaces.system;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;

public class LineNumberReaderForUTF8 {

	public LineNumberReader lineReader;
	public LineNumberReaderForUTF8(String queryFile)
	{
		//open the query File
		InputStreamReader isr = null;
		try {
				isr = new InputStreamReader(new FileInputStream(queryFile), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lineReader=new LineNumberReader(isr);
				
	}

	/**
	 * @param args
	 */
	public static void main(String[] args){
		// TODO Auto-generated method stub

	}

}
