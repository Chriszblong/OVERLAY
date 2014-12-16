package elements.common;

import java.io.Serializable;

public class VertexPoint  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4809840781633941373L;
	public int id;
	public double x;
	public double y;

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 根据参数设置
	 * @param id
	 * @param x
	 * @param y
	 */
	public VertexPoint(int id,double x,double y) {
		// TODO Auto-generated constructor stub
		this.id=id;
		this.x=x;
		this.y=y;
	}
	/**
	 * -1表示空Vertex未被赋值
	 */
	public VertexPoint()
	{
		this.id=-1;//-1表示空Vertex未被赋值
	}
	
	public VertexPoint(VertexPoint a)
	{
		this.id=a.id;
		this.x=a.x;
		this.y=a.y;
	}
	
	
	public void clone(VertexPoint a)
	{
		
		this.id=a.id;
		this.x=a.x;
		this.y=a.y;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
