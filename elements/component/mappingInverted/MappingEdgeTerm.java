package elements.component.mappingInverted;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;


/**
 * 所有的查询都要用到，故此步加载时间不算
 * @author BYRD
 *
 */
public class MappingEdgeTerm  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4316445510132854630L;
	/**
	 * 
	 * <EdgeID,<Term,MaxSroce>>
	 */
	public HashMap<Integer, HashMap<String, Float>> edgeTermMaxScoreMap;
	/**
	 * <Term,InvertedFileList>
	 */
	public HashMap<Integer,HashMap<String, Vector<InvertedFileEntry>>> termInvertedFileMap;

	public MappingEdgeTerm() {
		// TODO Auto-generated constructor stub
		this.edgeTermMaxScoreMap=new HashMap<Integer, HashMap<String,Float>>();
		this.termInvertedFileMap=new HashMap<Integer, HashMap<String,Vector<InvertedFileEntry>>>();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
