package interfaces.spatial;

// NOTE: Please read README.txt before browsing this code.

import java.io.*;
import java.util.*;

import Utils.Configure;
import Utils.IndexConfig;

import spatialIndexSrc.spatialindex.spatialindex.*;
import spatialIndexSrc.spatialindex.storagemanager.*;
import spatialIndexSrc.spatialindex.rtree.*;

public class EdgeRegionRTreeQuery
{

	private ISpatialIndex tree;
	private String queryType;
	public EdgeRegionRTreeQuery() throws SecurityException, NullPointerException, FileNotFoundException, IllegalArgumentException, IOException
	{
		String[] args = new String[2];
		args[0] = Configure.PROJECT_DATA_ROOT+IndexConfig.SPATIAL_EDGE_INDEX_DB;
		args[1] = IndexConfig.SPATIAL_EDGE_INDEX_OPRATIONTYPE;
		
		if (args.length != 2)
		{
			System.err.println("Usage: RTreeQuery query_file tree_file query_type [intersection | 10NN].");
			System.exit(-1);
		}
		queryType=args[1];

		// Create a disk based storage manager.
		PropertySet ps = new PropertySet();

		ps.setProperty("FileName", args[0]);
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
	
	public EdgeRegionRTreeQuery(String[] args) throws SecurityException, NullPointerException, IOException, IllegalArgumentException, IOException{
		
		args = new String[2];
		args[0] = Configure.PROJECT_DATA_ROOT+IndexConfig.SPATIAL_EDGE_INDEX_DB;
		args[1] = IndexConfig.SPATIAL_EDGE_INDEX_OPRATIONTYPE;
		
		if (args.length != 2)
		{
			System.err.println("Usage: RTreeQuery query_file tree_file query_type [intersection | 10NN].");
			System.exit(-1);
		}
		queryType=args[1];

		// Create a disk based storage manager.
		PropertySet ps = new PropertySet();

		ps.setProperty("FileName", args[0]);
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
	
	
	public void getTheNearestEdge(String queryFile) throws IOException
	{
		
		LineNumberReader lineNumberReader=new LineNumberReader(new FileReader(queryFile));
		String line;
		int nearestEdgeID=-1;
		int noEdge=0;
		
		while((line=lineNumberReader.readLine())!=null&&lineNumberReader.getLineNumber()<1000)
		{
			nearestEdgeID=positioningToRegion(line);
			if(nearestEdgeID==-1)
			{
				noEdge++;
			}
		}
		System.out.println("noEdge"+noEdge);
		MyQueryStrategy2 qs = new MyQueryStrategy2();
		tree.queryStrategy(qs);
		tree.flush();
		
	}
	
	public void createPOIsEdgeMap(String filePOI) throws IOException
	{
		LineNumberReader lineNumberReader=new LineNumberReader(new FileReader(filePOI));
		String line;
		int nearestEdge=-1;
		while((line=lineNumberReader.readLine())!=null)
		{
			nearestEdge=positioningToRegion(line);
		}
		MyQueryStrategy2 qs = new MyQueryStrategy2();
		tree.queryStrategy(qs);
		tree.flush();
	}
	
	public int positioningToRegion(String queryLine)
	{
		
			//定义计数参数
			int count = 0;
			int indexIO = 0;
			int leafIO = 0;
			int id;
			
			//定义查询坐标参数
			double x1=0, x2 = 0, y1 = 0, y2 = 0;
			double[] f1 = new double[2];
			double[] f2 = new double[2];
			int nearestEdge=-1;
		
		
			long start = System.currentTimeMillis();
			
			String line =queryLine;
			String[] parts=line.split(Configure.SPLITE_FLAG);
			if(parts.length<4)
			{
				System.err.println("error query ! please input a correct query Entry");
				return -1;
			}
			id=Integer.parseInt(parts[0]);
			x1=Double.parseDouble(parts[2]);
			y1=Double.parseDouble(parts[3]);
			x2=x1;
			y2=y1;
			f1[0] = x1; f1[1] = y1;
			f2[0] = x2; f2[1] = y2;

			MyVisitor vis = new MyVisitor();

			if (queryType.equals("intersection"))
			{
				Region r = new Region(f1, f2);
				nearestEdge=tree.intersectionQuery(r, vis);
							// this will find all data that intersect with the query range.
			}
					

			indexIO += vis.m_indexIO;
			leafIO += vis.m_leafIO;
			// example of the Visitor pattern usage, for calculating how many nodes
			// were visited.
//			if ((count % 1000) == 0) System.err.println(count);
			count++;	
			long end = System.currentTimeMillis();
//			System.out.println("ms: " + (end - start)  );
			return nearestEdge;
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
//			System.out.println("d.getIdentifier(RTreeQuery.170行)="+d.getIdentifier());
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
	
	public static void main(String[] args) throws SecurityException, NullPointerException, IllegalArgumentException, IOException
	{
		String queryFile= Configure.PROJECT_DATA_ROOT+Configure.POI_SF_40M;
		
		EdgeRegionRTreeQuery positioningQuery=new EdgeRegionRTreeQuery(args);
		positioningQuery.getTheNearestEdge(queryFile);

		
				
		
//		new SpatialComponentQuery(args);
	}
	
	
}
