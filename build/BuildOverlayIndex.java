package build;

import java.io.File;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public class BuildOverlayIndex {
	public DB db;
	public String DBName;

	//创建MapDB
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

		//step 1:加载数据：edge，vertex，POI
		
		//step 2:构建RTree 以每个边的两个顶点构建矩形
		
		//step 3:每一个POI规划到相应的region中
		
		//step 4:每一个edge作为seed region  进行扩充  构建Overlay中的B-tree  region Mapping
	}

}
