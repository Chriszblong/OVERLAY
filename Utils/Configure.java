package Utils;

public class Configure {
	/**
	 * ��ͼת������
	 */
	public static double MAP_CONVERSION_RATIO=0.0001;
	/**
	 * region
	 */
	public static int RELEVANT_TYPE_INTERMEDARY=1;
	/**
	 * border Connection
	 */
	public static int RELEVANT_TYPE_DIRECT_CONNECTION=2;
	/**
	 * regular Vertex
	 */
	public static int RELEVANT_TYPE_REGULAR_VERTEX=3;
	/**
	 * ���ݷָ���
	 */
	public static String SPLITE_FLAG=" ";
	/**
	 * ��Ŀ����·��
	 */
	public static String PROJECT_JAVA_ROOT="/home/jiangwei/mfs/kxp/DATA/Overlay";
	/**
	 * ��Ŀ����·��
	 */
	public static String PROJECT_DATA_ROOT="/home/jiangwei/mfs/kxp/ppZhao/DATA/OVERLAY_SF/";
//	public static String PROJECT_DATA_ROOT="E:/WorkSpace_Data_LAB/LAB_PPZHAO_DATA/OVERLAY_SF/";
	/**
	 * ��·����������������  Ҫ����VERTEX_PATH
	 */
	public static String EDGE_PATH="CommonData/Edge/FLA.cedge";
	/**
	 * ֻ�бߵ���Ϣ��û�����꣬������Ȩ��
	 */
	public static String LONDON_EDGE_PATH="CommonData/Edge/FLA.cedge";
	/**
	 * ����·��
	 */
	public static String VERTEX_PATH="CommonData/Vertex/FLA.cnode";
	/**
	 * seedRegion
	 */
	public static String SEED_REGION_PATH="CommonData/SeedRegion/seedRegion.txt";
	
	/**
	 * 
	 * queryPOIName
	 */
	public static String QUERY_POI="query/SpatialComponent/queryPOI.txt";
	/**
	 * OLD POI
	 */
//	public static String POI_OLD="query/SpatialComponent/SF_POI_EDGE.txt";
	
	public static String POI_SF_40M="query/SpatialComponent/OVERLAY_FLA_20M.txt";
	
	/**
	 * Term
	 */
	public static String TERM_DATA="CommonData/POI/OVERLAY_FLA_20M.txt";
	
	/**
	 * 
	 */
	public static String TEST_DATA_FILE_FOLDER="TEST/";
	/**
	 * The ratio between the lay and previous lay
	 */
	public static int LAY_REGION_RATIO=100;
	/**
	 * The Number of Region at lay 1
	 */
	public static int REGION_NUMBER_AT_LAY_1=1000;
	/**
	 * b
	 */
	public static float AGGRATION_PARAMETER=1;
	
	public static String TEMP_DATA_FOLDER="TEMP/";
	
	public static String TEMP_BORDER_TO_BORDER__DATA="tmpBorderToBorder.txt";
	
	public static String TEMP_BORDER_TO_BORDER__DATA_FOR_RANDOM_EDGE="tmpBorderToBorderForRandomEdge.txt";
	
	public static String STATIC_MIN_HEAP_REGION_ID_INFO="staticRandomMinHeap.txt";
	
	
	public static String EXTRA_DISTANCE="BorderToBorderWithReadDistance.txt";
	
	
	
	
}
