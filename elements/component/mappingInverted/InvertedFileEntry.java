package elements.component.mappingInverted;

import java.io.Serializable;

public class InvertedFileEntry  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6325304204539650872L;
	/**
	 * Ψһ��ID���
	 */
	public int id;
	/**
	 * ��Edge�а���Term��POI��ID
	 */
	public int poiID;
	/**
	 * ��ǰ��¼����Ӧ��Edge
	 */
	public int EdgeID;
	/**
	 * POI���ߵľ���
	 */
	public int distanceToEdge;
	/**
	 * ��ǰid����Ӧ��Term
	 */
	public String term;
	/**
	 * ��ǰPOI����Edge����Term�ϵĵ÷�
	 */
	public float sorce;


	public InvertedFileEntry() {
		// TODO Auto-generated constructor stub
		this.poiID=-1;//��ʾΪ��ʼ����ȷֵ��POI
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
