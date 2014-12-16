package interfaces.spatial;

// NOTE: Please read README.txt before browsing this code.

import java.io.*;
import java.util.*;

import Utils.Configure;
import Utils.IndexConfig;

import spatialIndexSrc.spatialindex.spatialindex.*;
import spatialIndexSrc.spatialindex.storagemanager.*;
import spatialIndexSrc.spatialindex.rtree.*;

public class VertexPointRTreeQuery
{
	public String queryFile;
	public String DBPath;
	public String queryType;
	public int queryCount;
	ISpatialIndex tree;
	
	public VertexPointRTreeQuery()
	{
		this.queryFile=Configure.PROJECT_DATA_ROOT+Configure.POI_SF_40M;
		this.DBPath=Configure.PROJECT_DATA_ROOT+IndexConfig.SPATIAL_VERTEX_INDEX_QEURY_DB;
		this.queryType=IndexConfig.SPATIAL_VERTEX_INDEX_QEURY_QUERYTYPE;
		queryCount=0;
		try {
			initQueryEnvironment();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	public VertexPointRTreeQuery(String queryFile,String dbName,String queryType)
	{
		this.queryFile=queryFile;
		this.DBPath=dbName;
		this.queryType=queryType;
		queryCount=0;
		try {
			initQueryEnvironment();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void initQueryEnvironment() throws SecurityException, NullPointerException, FileNotFoundException, IllegalArgumentException, IOException
	{
		PropertySet ps = new PropertySet();

		ps.setProperty("FileName", DBPath);
			// .idx and .dat extensions will be added.

		IStorageManager diskfile = new DiskStorageManager(ps);

		IBuffer file = new RandomEvictionsBuffer(diskfile, 10, false);
			// applies a main memory random buffer on top of the persistent storage manager
			// (LRU buffer, etc can be created the same way).

		PropertySet ps2 = new PropertySet();

		// If we need to open an existing tree stored in the storage manager, we only
		// have to specify the index identifier as follows
		Integer i = new Integer(1); // INDEX_IDENTIFIER_GOES_HERE (suppose I know that in this case it is equal to 1);
		ps2.setProperty("IndexIdentifier", i);
			// this will try to locate and open an already existing r-tree index from file manager file.

		tree = new RTree(ps2, file);

	}

	
	
	/**
	 * 返回的是Edge
	 * @param queryString
	 * @return
	 */
	public int queryAtStringLevel(String queryString)
	{
			int count = 0;
			int indexIO = 0;
			int leafIO = 0;
			int id;
			int op;
			double x1 = 0, x2 = 0, y1 = 0, y2 = 0;
			double[] f1 = new double[2];
			double[] f2 = new double[2];

			int nearestNode=-1;	
			//"1 105 51.5238028 -0.1430911 pub The Green Man"
			String[] parts=queryString.split(Configure.SPLITE_FLAG);
			
			//查询操作
			op=2;
			id=queryCount;
			if(parts.length<4)
			{
				return -1;
			}
			queryCount++;
			x1=Double.parseDouble(parts[2]);
			y1=Double.parseDouble(parts[3]);
			x2=x1;
			y2=y1;
			if (op == 2)
			{
				//query

				f1[0] = x1; f1[1] = y1;
				f2[0] = x2; f2[1] = y2;

				MyVisitor vis = new MyVisitor();

				if (queryType.equals("intersection"))
				{
					Region r = new Region(f1, f2);
					tree.intersectionQuery(r, vis);
						// this will find all data that intersect with the query range.
				}
				else if (queryType.equals("10NN"))
				{
					//
					Point p = new Point(f1);
					nearestNode=tree.nearestNeighborQuery(1, p, vis);	
				}
				else
				{
					System.err.println("Unknown query type.");
					System.exit(-1);
				}

				indexIO += vis.m_indexIO;
				leafIO += vis.m_leafIO;
					// example of the Visitor pattern usage, for calculating how many nodes
					// were visited.
			}
			else
			{
				System.err.println("This is not a query operation.");
			}

//			if ((count % 1000) == 0) System.err.println(count);

			count++;
	

		long end = System.currentTimeMillis();
		return nearestNode;
	}
	
	public void queryAtFileLevel() throws IOException
	{
			
		LineNumberReader lineNumberReader=new LineNumberReader(new FileReader(queryFile));
		String line;
		int nearestNodeID=-1;
		int noEdge=0;
		while((line=lineNumberReader.readLine())!=null)
		{
//			String[] parts=line.split(Configure.SPLITE_FLAG);
			nearestNodeID=queryAtStringLevel(line);
		}
			

		MyQueryStrategy2 qs = new MyQueryStrategy2();
		tree.queryStrategy(qs);
		// flush all pending changes to persistent storage (needed since Java might not call finalize when JVM exits).
		tree.flush();
		return ;
			
		
	}

	// example of a Visitor pattern.
	// findes the index and leaf IO for answering the query and prints
	// the resulting data IDs to stdout.
	class MyVisitor implements IVisitor
	{
		public int m_indexIO = 0;
		public int m_leafIO = 0;

		public void visitNode(final INode n)
		{
			if (n.isLeaf()) m_leafIO++;
			else m_indexIO++;
		}

		public int visitData(final IData d)
		{
			int returnNodeID=0;
//			System.out.println("d.getIdentifier(RTreeQuery.170��)="+d.getIdentifier());
				// the ID of this data entry is an answer to the query. I will just print it to stdout.
			returnNodeID=d.getIdentifier();
			return returnNodeID;
		}
	}

	// example of a Strategy pattern.
	// traverses the tree by level.
	class MyQueryStrategy implements IQueryStrategy
	{
		private ArrayList ids = new ArrayList();

		public void getNextEntry(IEntry entry, int[] nextEntry, boolean[] hasNext)
		{
			Region r = entry.getShape().getMBR();

			System.out.println(r.m_pLow[0] + " " + r.m_pLow[1]);
			System.out.println(r.m_pHigh[0] + " " + r.m_pLow[1]);
			System.out.println(r.m_pHigh[0] + " " + r.m_pHigh[1]);
			System.out.println(r.m_pLow[0] + " " + r.m_pHigh[1]);
			System.out.println(r.m_pLow[0] + " " + r.m_pLow[1]);
			System.out.println();
			System.out.println();
				// print node MBRs gnuplot style!

			// traverse only index nodes at levels 2 and higher.
			if (entry instanceof INode && ((INode) entry).getLevel() > 1)
			{
				for (int cChild = 0; cChild < ((INode) entry).getChildrenCount(); cChild++)
				{
					ids.add(new Integer(((INode) entry).getChildIdentifier(cChild)));
				}
			}

			if (! ids.isEmpty())
			{
				nextEntry[0] = ((Integer) ids.remove(0)).intValue();
				hasNext[0] = true;
			}
			else
			{
				hasNext[0] = false;
			}
		}
	};

	// example of a Strategy pattern.
	// find the total indexed space managed by the index (the MBR of the root).
	class MyQueryStrategy2 implements IQueryStrategy
	{
		public Region m_indexedSpace;

		public void getNextEntry(IEntry entry, int[] nextEntry, boolean[] hasNext)
		{
			// the first time we are called, entry points to the root.
			IShape s = entry.getShape();
			m_indexedSpace = s.getMBR();

			// stop after the root.
			hasNext[0] = false;
		}
	}

	public static void main(String[] args)
	{
		VertexPointRTreeQuery vpq=new VertexPointRTreeQuery();
		String queryString="1 105 51.5238028 -0.1430911 pub The Green Man";
//		vpq.queryAtStringLevel(queryString);
		try {
			vpq.queryAtFileLevel();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
