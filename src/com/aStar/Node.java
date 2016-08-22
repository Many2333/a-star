package com.aStar;


public class Node{
	private int x;
	private int y;
	private int value;
	private int F[] = {100000, 100000};
	private int[] G = new int[2], H = new int[2];
	private Node[] fnode = new Node[2];
	
	public Node(){};
	public Node(int x, int y, int value){
		this.x = x;
		this.y = y;
		this.value = value;
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getValue(){
		return value;
	}
	
	public void setFnode(Node fnode, int i){
		this.fnode[i] = fnode;
	}
	public Node getFnode(int i){
		return fnode[i];
	}
	
	public void countF(int i){
		this.F[i] = G[i] + H[i];
	}
	public int getF(int i){
		return F[i];
	}
	public void setG(int g, int i){
		this.G[i] = g;
	}
	public int getG(int i){
		return G[i];
	}
	public void setH(int h, int i){
		this.H[i] = h;
	}
	public int getH(int i){
		return H[i];
	}
}
