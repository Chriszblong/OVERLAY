package elements.query;

import java.io.Serializable;

public class CandidateDoc implements Serializable,Comparable<CandidateDoc>{
	
	public int docID;
	

	public float score;
	
	public int distance; 

	public CandidateDoc()
	{
		this.docID=-1;
		this.score=0;
	}
	
	public CandidateDoc(int docId,float score,int distance) {
		// TODO Auto-generated constructor stub
		this.docID=docId;
		this.score=score;
		this.distance=distance;
	}

	
	public int getDocID() {
		return docID;
	}

	public void setDocID(int docID) {
		this.docID = docID;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}
	@Override
	public int compareTo(CandidateDoc o) {
		// TODO Auto-generated method stub
		if((score-o.score)>0)
		{
			return 1;
		}else if((score-o.score)<0)
		{
			return -1;
		}else
		{
			return 0;
		}

	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

	}

}
