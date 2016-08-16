package com.test;

import java.util.*;
public class Test {
    
    public static void main(String[] args){
    	int M=50,N=50;
    	int[][] map=new Map(M,N).map;
        
    	int searchNum1=0,searchNum2=0;
    	int cost1=0,cost2=0;
    	long searchTime1=0,searchTime2=0;
    	
        AStar aStar=new AStar(map, M, N);
        map[1][1]=map[48][48]=1;

        int flag=aStar.searchSingle(1, 1, 48, 48);
        if(flag==-1){
            System.out.println("������������");
        }else if(flag==0){
            System.out.println("û�ҵ���");
        }else{          
        	searchNum1=aStar.searchNum1;
        	searchTime1=aStar.searchTime1;
        	cost1=aStar.cost1;
        }
        
        flag=aStar.searchDouble(1, 1, 48, 48);
        if(flag==-1){
            System.out.println("������������");
        }else if(flag==0){
            System.out.println("û�ҵ���");
        }else{          
        	searchNum2=aStar.searchNum2;
        	searchTime2=aStar.searchTime2;
        	cost2=aStar.cost2;
        }
        
        GUI gui=new GUI(map,M,N,searchNum1,searchNum2,searchTime1,searchTime2,cost1,cost2);

    }
}

//�ڵ���
class Node {
    private int x;//X����
    private int y;//Y����
    private Node parentNode;//����ڵ�
    private int g;//��ǰ�㵽�����ƶ��ķ�
    private int h;//��ǰ�㵽�յ���ƶ��ķѣ��������پ���|x1-x2|+|y1-y2|(�����ϰ���)
    private int f;//f=g+h
    
    public Node(int x,int y,Node parentNode){
        this.x=x;
        this.y=y;
        this.parentNode=parentNode;
    }
    
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public Node getParentNode() {
        return parentNode;
    }
    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }
    public int getG() {
        return g;
    }
    public void setG(int g) {
        this.g = g;
    }
    public int getH() {
        return h;
    }
    public void setH(int h) {
        this.h = h;
    }
    public int getF() {
        return f;
    }
    public void setF(int f) {
        this.f = f;
    }
     public String toString(){
        return "("+x+","+y+","+f+")";
     }
}
//�ڵ�Ƚ���
class NodeFComparator implements Comparator< Node>{
    @Override
    public int compare(Node o1, Node o2) {
        return o1.getF()-o2.getF();
    }
    
}
