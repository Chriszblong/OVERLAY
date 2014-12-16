package elements.query;

import java.io.Serializable;

public class CandidateForOverlay implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 427998051917149728L;
	/**
	 * the id of POI
	 */
	public int id;
	/**
	 * 文本得分
	 */
	public float textSocre;
	/**
	 * 空间得分
	 */
	public float spatailScore;
	/**
	 * 所有得分
	 */
	public float queryScore;
	
	public CandidateForOverlay(int id,float textSocre,float spatailScore,float queryScore) {
		// TODO Auto-generated constructor stub
		this.id=id;
		this.textSocre=textSocre;
		this.spatailScore=spatailScore;
		this.queryScore=queryScore;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
