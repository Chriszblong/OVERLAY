package interfaces.system;

import java.io.FileWriter;
import java.io.IOException;

import Utils.Configure;

import elements.regionBTree.Region;

public class TestDataWriter1 {

	private Region regionI;
	private Region regionJ;
	private int tmpBorderI;
	private int tmpBorderJ;
	private int targetBorder;
	public TestDataWriter1(Region ri,Region rj,int bi,int bj,int bt) {
		// TODO Auto-generated constructor stub
		this.regionI=ri;
		this.regionJ=rj;
		this.tmpBorderI=bi;
		this.tmpBorderJ=bj;
		this.targetBorder=bt;
	}
	
	
	public void writeDataToFile()
	{
		try {
			FileWriter dataFileWriter=new FileWriter(Configure.PROJECT_DATA_ROOT+Configure.TEST_DATA_FILE_FOLDER+System.currentTimeMillis()+".txt");

			dataFileWriter.write("RegionI\r\n");
			dataFileWriter.write("RegionI======borderVertexs\r\n");
			for(Integer idI:regionI.borderVertexs.keySet())
			{
				dataFileWriter.write(idI+"\t=====================\t"+regionI.borderVertexs.get(idI)+"\r\n");
			}
			dataFileWriter.write("RegionI======borderVertexsDistance\r\n");
//			for(Integer idI:regionI.borderVertexsDistance.keySet())
//			{
//				for(Integer idII:regionI.borderVertexsDistance.get(idI).keySet())
//				{
//					dataFileWriter.write(idI+"\t======\t"+idII+"\t======\t"+regionI.borderVertexsDistance.get(idI).get(idII)+"\r\n");
//				}
//				
//			}
			dataFileWriter.write("\r\n\r\n\r\n\r\n\r\n");
			dataFileWriter.write("RegionJ\r\n");
			dataFileWriter.write("RegionJ======borderVertexs\r\n");
			for(Integer idI:regionJ.borderVertexs.keySet())
			{
				dataFileWriter.write(idI+"\t=====================\t"+regionJ.borderVertexs.get(idI)+"\r\n");
			}
			dataFileWriter.write("regionJ======borderVertexsDistance\r\n");
//			for(Integer idI:regionJ.borderVertexsDistance.keySet())
//			{
//				for(Integer idII:regionJ.borderVertexsDistance.get(idI).keySet())
//				{
//					dataFileWriter.write(idI+"\t======\t"+idII+"\t======\t"+regionJ.borderVertexsDistance.get(idI).get(idII)+"\r\n");
//				}
//				
//			}
			
			dataFileWriter.write("\r\n\r\n\r\n\r\n\r\n");
			
			dataFileWriter.write("tmpBorderI"+tmpBorderI+"\r\n");
			dataFileWriter.write("tmpBorderJ"+tmpBorderJ+"\r\n");
			dataFileWriter.write("targetBorder"+targetBorder+"\r\n");
			dataFileWriter.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
