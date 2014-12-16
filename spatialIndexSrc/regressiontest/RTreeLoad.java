package spatialIndexSrc.regressiontest;

// NOTE: Please read README.txt before browsing this code.

import java.io.*;
import java.util.*;

import Utils.Configure;

import elements.common.VertexPoint;

import spatialIndexSrc.spatialindex.spatialindex.*;
import spatialIndexSrc.spatialindex.storagemanager.*;
import spatialIndexSrc.spatialindex.rtree.*;

public class RTreeLoad
{
	
	public HashMap<Integer, VertexPoint> vertexMap;
	
	
	public static void main(String[] args)
	{
		new RTreeLoad(args);
	}
	
	RTreeLoad(String[] args){
		
		args = new String[4];
		/**
		 * 输入的边数据转换成region
		 */
		args[0] = Configure.PROJECT_DATA_ROOT+"london.cedge";
		args[1] =  Configure.PROJECT_DATA_ROOT+"Overlay_SpatiaComponent";
		args[2] = "500";
		args[3] = "intersection";
		
		
		try
		{
			if (args.length != 4)
			{
				//输入文件 ，生成树文件，容量，查询类型
				System.err.println("Usage: RTreeLoad input_file tree_file capacity query_type [intersection | 10NN].");
				System.exit(-1);
			}

			LineNumberReader lr = null;

			try
			{
				lr = new LineNumberReader(new FileReader(args[0]));
			}
			catch (FileNotFoundException e)
			{
				System.err.println("Cannot open data file " + args[0] + ".");
				System.exit(-1);
			}

			// Create a disk based storage manager.
			PropertySet ps = new PropertySet();

			Boolean b = new Boolean(true);
			ps.setProperty("Overwrite", b);
				//overwrite the file if it exists.

			ps.setProperty("FileName", args[1]);
				// .idx and .dat extensions will be added.

			Integer i = new Integer(4096);
			ps.setProperty("PageSize", i);
				// specify the page size. Since the index may also contain user defined data
				// there is no way to know how big a single node may become. The storage manager
				// will use multiple pages per node if needed. Off course this will slow down performance.

			IStorageManager diskfile = new DiskStorageManager(ps);

			IBuffer file = new RandomEvictionsBuffer(diskfile, 10, false);
				// applies a main memory random buffer on top of the persistent storage manager
				// (LRU buffer, etc can be created the same way).

			// Create a new, empty, RTree with dimensionality 2, minimum load 70%, using "file" as
			// the StorageManager and the RSTAR splitting policy.
			PropertySet ps2 = new PropertySet();

			Double f = new Double(0.7);
			ps2.setProperty("FillFactor", f);

			i = new Integer(args[2]);
			ps2.setProperty("IndexCapacity", i);
			ps2.setProperty("LeafCapacity", i);
				// Index capacity and leaf capacity may be different.

			i = new Integer(2);
			ps2.setProperty("Dimension", i);

			ISpatialIndex tree = new RTree(ps2, file);

			int count = 0;
			int indexIO = 0;
			int leafIO = 0;
			int id = 0, op=2;
			double x1 = 0, x2 = 0, y1 = 0, y2 = 0;
			double[] f1 = new double[2];
			double[] f2 = new double[2];

			long start = System.currentTimeMillis();
			String line = lr.readLine();

			while (line != null)
			{
				//切割数据把数据放到
				String[] parts=line.split(" ");
				op=2;
//				//相当于split的函数默认的分隔符空格java默认的分隔符是“空格”、“制表符(‘\t’)”、“换行符(‘\n’)”、“回车符(‘\r’)”
//				StringTokenizer st = new StringTokenizer(line);
////				op = new Integer(st.nextToken()).intValue(); //op表示这条数据的操作是插入，删除还是查询
//				
//				op = 2; //插入
//				id = new Integer(st.nextToken()).intValue(); //表示该条的数据的id
//				x1 = new Double(st.nextToken()).doubleValue();
//				y1 = new Double(st.nextToken()).doubleValue();
////				x2 = new Double(st.nextToken()).doubleValue();
////				y2 = new Double(st.nextToken()).doubleValue();
//				x2=x1;
//				y2=y1;
//				

				if (op == 0)
				{
					//delete

					f1[0] = x1; f1[1] = y1;
					f2[0] = x2; f2[1] = y2;
					Region r = new Region(f1, f2);

					if (tree.deleteData(r, id) == false)
					{
						System.err.println("Cannot delete id: " + id + " , count: " + count + ".");
						System.exit(-1);
					}
				}
				else if (op == 1)
				{
					//insert

					f1[0] = x1; f1[1] = y1;
					f2[0] = x2; f2[1] = y2;
					Region r = new Region(f1, f2);

					String data = r.toString();
						// associate some data with this region. I will use a string that represents the
						// region itself, as an example.
						// NOTE: It is not necessary to associate any data here. A null pointer can be used. In that
						// case you should store the data externally. The index will provide the data IDs of
						// the answers to any query, which can be used to access the actual data from the external
						// storage (e.g. a hash table or a database table, etc.).
						// Storing the data in the index is convinient and in case a clustered storage manager is
						// provided (one that stores any node in consecutive pages) performance will improve substantially,
						// since disk accesses will be mostly sequential. On the other hand, the index will need to
						// manipulate the data, resulting in larger overhead. If you use a main memory storage manager,
						// storing the data externally is highly recommended (clustering has no effect).
						// A clustered storage manager is NOT provided yet.
						// Also you will have to take care of converting you data to and from binary format, since only
						// array of bytes can be inserted in the index (see RTree::Node::load and RTree::Node::store for
						// an example of how to do that).

					//tree.insertData(data.getBytes(), r, id);

					tree.insertData(null, r, id);
						// example of passing a null pointer as the associated data.
				}
				else if (op == 2)
				{
					//query

					f1[0] = x1; f1[1] = y1;
					f2[0] = x2; f2[1] = y2;

					MyVisitor vis = new MyVisitor();

					if (args[3].equals("intersection"))
					{
						Region r = new Region(f1, f2);
						tree.intersectionQuery(r, vis);
							// this will find all data that intersect with the query range.
					}
					else if (args[3].equals("10NN"))
					{
						Point p = new Point(f1);
						tree.nearestNeighborQuery(1, p, vis);
							// this will find the 10 nearest neighbors.
					}
					else
					{
						System.err.println("Unknown query type.");
						System.exit(-1);
					}
				}

				if ((count % 1000) == 0) System.err.println(count);

				count++;
				line = lr.readLine();
			}

			long end = System.currentTimeMillis();

			System.err.println("Operations: " + count);
			System.err.println(tree);
			System.err.println("Minutes: " + ((end - start) / 1000.0f) / 60.0f);

			// since we created a new RTree, the PropertySet that was used to initialize the structure
			// now contains the IndexIdentifier property, which can be used later to reuse the index.
			// (Remember that multiple indices may reside in the same storage manager at the same time
			//  and every one is accessed using its unique IndexIdentifier).
			Integer indexID = (Integer) ps2.getProperty("IndexIdentifier");
			System.err.println("Index ID: " + indexID);

			boolean ret = tree.isIndexValid();
			if (ret == false) System.err.println("Structure is INVALID!");

			// flush all pending changes to persistent storage (needed since Java might not call finalize when JVM exits).
			tree.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// example of a Visitor pattern.
	// see RTreeQuery for a more elaborate example.
	class MyVisitor implements IVisitor
	{
		public void visitNode(final INode n) {}

		public int visitData(final IData d)
		{
			System.out.println(d.getIdentifier());
				// the ID of this data entry is an answer to the query. I will just print it to stdout.
			return d.getIdentifier();
		}
	}
}
