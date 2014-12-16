package build;

import java.io.File;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public class BuildOverlayIndex {
	public DB db;
	public String DBName;

	//����MapDB
	public BuildOverlayIndex(String dbName) {
		// TODO Auto-generated constructor stub
		DBName = dbName;
		createDB();
	}

	public void createDB()
	{
		 File dbFile = new File(DBName);
	        if(dbFile.exists()){
	        	dbFile.delete();
	        }
	        db = DBMaker.newFileDB(dbFile)
	        		//.asyncWriteEnable()
	        		.transactionDisable()
	        		.freeSpaceReclaimQ(0)
	        		.asyncWriteEnable()
	        		
	        		//.cacheSize(1024*1024*100)
	        		//.cacheLRUEnable()
	        		//.cacheDisable()
	        		//.fullChunkAllocationEnable()
	        		//.mmapFileEnable()
	        		//.syncOnCommitDisable()
	        		.make();
	                
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//step 1:�������ݣ�edge��vertex��POI
		
		//step 2:����RTree ��ÿ���ߵ��������㹹������
		
		//step 3:ÿһ��POI�滮����Ӧ��region��
		
		//step 4:ÿһ��edge��Ϊseed region  ��������  ����Overlay�е�B-tree  region Mapping
	}

}
