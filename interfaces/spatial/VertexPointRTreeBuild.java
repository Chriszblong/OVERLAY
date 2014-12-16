package interfaces.spatial;

// NOTE: Please read README.txt before browsing this code.

import java.io.*;
import java.util.*;

import Utils.Configure;
import Utils.IndexConfig;

import spatialIndexSrc.spatialindex.spatialindex.IData;
import spatialIndexSrc.spatialindex.spatialindex.INode;
import spatialIndexSrc.spatialindex.spatialindex.ISpatialIndex;
import spatialIndexSrc.spatialindex.spatialindex.IVisitor;
import spatialIndexSrc.spatialindex.spatialindex.Point;
import spatialIndexSrc.spatialindex.spatialindex.Region;
import spatialIndexSrc.spatialindex.storagemanager.*;
import spatialIndexSrc.spatialindex.rtree.*;

public class VertexPointRTreeBuild
{
	public String indexFolder;
	public String dataFileName;
	public String childNum;
	public String operationType;
	
	public VertexPointRTreeBuild(String indexPath,String DataPath,String i,String operatingType)
	{
		this.indexFolder=indexPath;
		this.dataFileName=DataPath;
		this.childNum=i;
		this.operationType=operatingType;
	}
	
	public VertexPointRTreeBuild()
	{
		this.indexFolder=Configure.PROJECT_DATA_ROOT+IndexConfig.SPATIAL_VERTEX_INDEX_DB;
		this.dataFileName=Configure.PROJECT_DATA_ROOT+IndexConfig.SPATIAL_VERTEX_INDEX_DATA;
		this.childNum=Utils.IndexConfig.SPATIAL_VERTEX_INDEX_CHILD_NUM;
		this.operationType=Utils.IndexConfig.SPATIAL_VERTEX_INDEX_OPRATION_TYPE;
		
	}
	
	public void buildeVertexPointRTreeIndex() throws IOException
	{
			LineNumberReader lr = new LineNumberReader(new FileReader(dataFileName));

			// Create a disk based storage manager.
			PropertySet ps = new PropertySet();

			Boolean b = new Boolean(true);
			ps.setProperty("Overwrite", b);
			ps.setProperty("FileName", indexFolder);
			Integer i = new Integer(4096);
			ps.setProperty("PageSize", i);
			IStorageManager diskfile = new DiskStorageManager(ps);
			IBuffer file = new RandomEvictionsBuffer(diskfile, 10, false);
			PropertySet ps2 = new PropertySet();

			Double f = new Double(0.7);
			ps2.setProperty("FillFactor", f);
			i = new Integer(childNum);
			ps2.setProperty("IndexCapacity", i);
			ps2.setProperty("LeafCapacity", i);
			i = new Integer(2);
			ps2.setProperty("Dimension", i);
			ISpatialIndex tree = new RTree(ps2, file);
			int count = 0;
			int indexIO = 0;
			int leafIO = 0;
			int id, op;
			double x1, x2, y1, y2;
			double[] f1 = new double[2];
			double[] f2 = new double[2];
			long start = System.currentTimeMillis();
			String line = lr.readLine();

			while (line != null)
			{
				String[] parts=line.split(Configure.SPLITE_FLAG);
				
				op = 1; 
				id =Integer.parseInt(parts[0]);
				x1=Double.parseDouble(parts[1]);
				y1=Double.parseDouble(parts[1]);
				x2=x1;
				y2=y1;
				

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

					tree.insertData(data.getBytes(), r, id);
				}
				else if (op == 2)
				{
					//query

					f1[0] = x1; f1[1] = y1;
					f2[0] = x2; f2[1] = y2;

					MyVisitor vis = new MyVisitor();

					if (operationType.equals("intersection"))
					{
						Region r = new Region(f1, f2);
						tree.intersectionQuery(r, vis);
							// this will find all data that intersect with the query range.
					}
					else if (operationType.equals("10NN"))
					{
						Point p = new Point(f1);
						tree.nearestNeighborQuery(10, p, vis);
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
			Integer indexID = (Integer) ps2.getProperty("IndexIdentifier");
			System.err.println("Index ID: " + indexID);

			boolean ret = tree.isIndexValid();
			if (ret == false) System.err.println("Structure is INVALID!");

			// flush all pending changes to persistent storage (needed since Java might not call finalize when JVM exits).
			tree.flush();
		
		
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

	public static void main(String[] args) throws IOException
	{
		VertexPointRTreeBuild vprBuild=new VertexPointRTreeBuild();
		vprBuild.buildeVertexPointRTreeIndex();
	}
}
