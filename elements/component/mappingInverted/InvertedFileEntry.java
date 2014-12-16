package elements.component.mappingInverted;

import java.io.Serializable;

public class InvertedFileEntry  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6325304204539650872L;
	/**
	 * 唯一的ID编号
	 */
	public int id;
	/**
	 * 在Edge中包含Term的POI的ID
	 */
	public int poiID;
	/**
	 * 当前记录所对应的Edge
	 */
	public int EdgeID;
	/**
	 * POI到边的距离
	 */
	public int distanceToEdge;
	/**
	 * 当前id所对应的Term
	 */
	public String term;
	/**
	 * 当前POI所在Edge上在Term上的得分
	 */
	public float sorce;


	public InvertedFileEntry() {
		// TODO Auto-generated constructor stub
		this.poiID=-1;//表示为初始化正确值的POI
		this.id=-1;
	}
	
	public InvertedFileEntry(int poiId,int id,int edgeID,String kwd,float textScore,int distance) 
	{
		this.distanceToEdge=distance;
		this.poiID=poiId;
		this.id=id;
		this.EdgeID=edgeID;
		this.term=kwd;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
