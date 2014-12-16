package Utils;

public class IndexConfig {
//	public static String INDEX_DB_NAME="Index/OVERLAY_BORDERS";
	
	
	public static String INDEX_DB_ADJ_NAME="Index/OVERLAY_ADJ_FLA";
	
	public static String INDEX_DB_REGION_NAME="Index/OVERLAY_REGION_FLA";
	
	public static String INDEX_DB_EDGE_NAME="Index/OVERLAY_EDGE_FLA";
	
	public static String INDEX_DB_VERTEX_NAME="Index/OVERLAY_VERTEX_FLA";
	
	public static String INDEX_DB_INVERTEDFILE_NAME="Index/OVERLAY_INVERTEDFILE_FLA";
	
	
//	public static String INDEX_DB_NAME="Index_40M/OVERLAY_BORDERS";
//	
//	public static String INDEX_DB_ADJ_NAME="Index_40M/OVERLAY_ADJ_SF";
//	
//	public static String INDEX_DB_REGION_NAME="Index_40M/OVERLAY_REGION_SF";
//	
//	public static String INDEX_DB_EDGE_NAME="Index_40M/OVERLAY_EDGE_SF";
//	
//	public static String INDEX_DB_VERTEX_NAME="Index_40M/OVERLAY_VERTEX_SF";
//	
//	public static String INDEX_DB_INVERTEDFILE_NAME="Index_40M/OVERLAY_INVERTEDFILE_SF";
	
	
	
//	public static String INDEX_DB_NAME="Index_60M/OVERLAY_BORDERS";
//	
//	public static String INDEX_DB_ADJ_NAME="Index_60M/OVERLAY_ADJ_SF";
//	
//	public static String INDEX_DB_REGION_NAME="Index_60M/OVERLAY_REGION_SF";
//	
//	public static String INDEX_DB_EDGE_NAME="Index_60M/OVERLAY_EDGE_SF";
//	
//	public static String INDEX_DB_VERTEX_NAME="Index_60M/OVERLAY_VERTEX_SF";
//	
//	public static String INDEX_DB_INVERTEDFILE_NAME="Index_60M/OVERLAY_INVERTEDFILE_SF";
	
	
//	public static String INDEX_DB_NAME="Index_80M/OVERLAY_BORDERS";
//	
//	public static String INDEX_DB_ADJ_NAME="Index_80M/OVERLAY_ADJ_SF";
//	
//	public static String INDEX_DB_REGION_NAME="Index_80M/OVERLAY_REGION_SF";
//	
//	public static String INDEX_DB_EDGE_NAME="Index_80M/OVERLAY_EDGE_SF";
//	
//	public static String INDEX_DB_VERTEX_NAME="Index_80M/OVERLAY_VERTEX_SF";
//	
//	public static String INDEX_DB_INVERTEDFILE_NAME="Index_80M/OVERLAY_INVERTEDFILE_SF";
//	
	
	
	public static String MAP_DB_FLAG="OVERLAY_BORDERS_VERSION";
	
	public static String DB_ADJACENCY_COMPONENT_MAP_HASHNAME="adjcencyComponentMap";
	
	public static String DB_REGION_MAP_HASHNAME="regionMap";
	
	public static String DB_REGION_BELONG_MAP_HASHNAME="regionBelongMap";
	
	public static String DB_VERTEX_TO_VERTEX_AT_EDGE_HASHNAME="vertexToVertexAtEdgeMap";
	
	public static String DB_VERTEX_REGIONID_MAP_HASHNAME="vertexRegionIdMap";
	
	public static String DB_MBR_TERM_MAX_SCORE_MAP_HASHNAME="mbrTermMaxScoreMap";
	
	public static String DB_TERM_INVERTED_FILE_MAP_HASHNAME="termInvertedFileMap";
	
	public static String DB_BASIC_EDGE_WITH_INFO_HASHNAME="basicEdgeWithInfo";
	
	
	
	public static String SPATIAL_EDGE_INDEX_DB="SpatialComponent/Overlay_SpatiaComponent";
	
	public static String SPATIAL_EDGE_INDEX_CHILD_NUM="500";
	
	public static String SPATIAL_EDGE_INDEX_OPRATIONTYPE="intersection";
	
	
	public static String SPATIAL_VERTEX_INDEX_DB="SpatialComponent/Overlay_SpatiaComponent_Vertex";
	
	public static String SPATIAL_VERTEX_INDEX_DATA="CommonData/NetWork/london.cnode";
	
	public static String SPATIAL_VERTEX_INDEX_CHILD_NUM="500";
	public static String SPATIAL_VERTEX_INDEX_OPRATION_TYPE="10NN";
	
	
	public static String SPATIAL_VERTEX_INDEX_QEURY_DB="SpatialComponent/Overlay_SpatiaComponent_Vertex";
	public static String SPATIAL_VERTEX_INDEX_QEURY_QUERYFILE="CommonData/POI/OVERLAY_LONDON_POI.txt";
	public static String SPATIAL_VERTEX_INDEX_QEURY_QUERYTYPE="10NN";
	
	
	
	
	
	//REGION REGION's REGION VERTEX
	public static String REGION_VERTEX_TYPE_INTERMEDIARY="I";
	//REGION REGION's DIRECT VERTEX
	public static String REGION_VERTEX_TYPE_DIRECT="C";
	//REGION REGION's REGULAR VERTEX
	public static String REGION_VERTEX_TYPE_REGULAR="R";
	
	//region's Border
	public static String VERTEX_TYPE_BORDER="B";
	//region's UnBorder
	public static String VERTEX_TYPE_UNBORDER="U";
	
	public static int MIN_ROAD_VERTEX_INDEX=0;
	
	public static int MAX_ROAD_VERTEX_INDEX=1070375;
	
	public static int MAX_ROAD_EDGE_INDEX=3783173;
	
	public static int MIN_ROAD_EDGE_INDEX=1070376;
	
	public static int MIN_ROAD_REGION_INDEX=3783174;
	
	public static int MAX_ROAD_REGION_INDEX=6495971;
	
	
}
