package com.aStar;

import java.util.ArrayList;
import java.util.List;

public class SingleAStar {
	private int[][] map;
	private int length, width;
	private int nodeCount = 0, totalValue = 0;
	private Node startNode;
	private Node endNode;
	private List<Node> openList0 = new ArrayList<Node>();
	private List<Node> openList1 = new ArrayList<Node>();
	private List<Node> closeList = new ArrayList<Node>();

	private static final int DIR1 = 0;
	private static final int DIR2 = 1;

	private static final int NORTH = 1;
	private static final int NORTHEAST = 2;
	private static final int EAST = 3;
	private static final int SOUTHEAST = 4;
	private static final int SOUTH = 5;
	private static final int SOUTHWEST = 6;
	private static final int WEST = 7;
	private static final int NORTHWEST = 8;
	
	public SingleAStar(){};
	
	public SingleAStar(int[][] map, int length, int width, int startX, int startY, int endX, int endY){
		this.map = map;
		this.length = length;
		this.width = width;
		startNode = new Node(startX, startY, map[startX][startY]);
		startNode.setG(0, DIR1);
		endNode = new Node(endX, endY, map[endX][endY]);
		openList0.add(startNode);
		openList1.add(endNode);
	}
	
	public void singleSearch(){
		System.out.println("单向搜索开始：");
		System.out.println("Start:" + startNode.getX() + "," + startNode.getY());
		System.out.println("End:" + endNode.getX() + "," + endNode.getY());
		Node thisNode = startNode;
		Node nextNode;
		while(!openList0.isEmpty()){
			System.out.print(thisNode.getX() + "," + thisNode.getY() + ":");
			nextNode = getNewNode(thisNode, DIR1);
			if (findNode(nextNode) == -1){
				System.out.println("没有通路！");
				return;
			}
			openList0.remove(findNode(thisNode));
			closeList.add(thisNode);
			
			if(findNode(endNode) == -1){
				totalValue = nextNode.getF(DIR1);
				thisNode = nextNode;
			}
			else	break;
		}
		System.out.println("step 1 finished");
		
		while(thisNode.getFnode(DIR1) != null){
			System.out.println(thisNode.getX() + "***" + thisNode.getY());
			map[thisNode.getX()][thisNode.getY()] = 9;
			thisNode = thisNode.getFnode(DIR1);
		}
		map[startNode.getX()][startNode.getY()] = 9;
		map[endNode.getX()][endNode.getY()] = 9;
		System.out.println("step 2 finished");
		
		for(int i = 0; i < width; i++){
			for(int j = 0; j < length; j++){
				if (map[i][j] == 9)	System.out.print("* ");
				else	System.out.print(map[i][j] + " ");
			}
			System.out.print('\n');
		}
		System.out.println("搜索节点个数:" + nodeCount + "\n总花费:" + totalValue);
	}

	public void bidirSearch(){
		System.out.println("双向搜索开始：");
		System.out.println("Start:" + startNode.getX() + "," + startNode.getY());
		System.out.println("End:" + endNode.getX() + "," + endNode.getY());
		Node thisNode = startNode;
		Node nextNode;
		while(!openList0.isEmpty() && !openList1.isEmpty()){
			System.out.print(thisNode.getX() + "," + thisNode.getY() + ":");
			nextNode = getNewNode(thisNode, DIR1);
			if (findNode(nextNode) != -1){
				System.out.println("没有通路！");
				return;
			}
			openList0.remove(findNode(thisNode));
			closeList.add(thisNode);

			if(findNode(endNode) == -1){
				totalValue = nextNode.getF(DIR1);
				thisNode = nextNode;
			}
			else	break;
		}
		System.out.println("step 1 finished");

		while(thisNode.getFnode(DIR1) != null){
			System.out.println(thisNode.getX() + "***" + thisNode.getY());
			map[thisNode.getX()][thisNode.getY()] = 9;
			thisNode = thisNode.getFnode(DIR1);
		}
		map[startNode.getX()][startNode.getY()] = 9;
		map[endNode.getX()][endNode.getY()] = 9;
		System.out.println("step 2 finished");

	}
	
	private Node getNewNode(Node father, int DIR){
		Node node = new Node();
		
		if(father.getY() - 1 >= 0){
			node = initNewNode(node, father, NORTH);
		}
		if(father.getY() - 1 >= 0 && father.getX() + 1 < width){
			node = initNewNode(node, father, NORTHEAST);
		}
		if(father.getX() + 1 < width){
			node = initNewNode(node, father, EAST);
		}
		if(father.getY() + 1 < length && father.getX() + 1 < width){
			node = initNewNode(node, father, SOUTHEAST);
		}
		if(father.getY() + 1 < length){
			node = initNewNode(node, father, SOUTH);
		}
		if(father.getY() + 1 < length && father.getX() - 1 >= 0){
			node = initNewNode(node, father, SOUTHWEST);
		}
		if(father.getX() - 1 >= 0){
			node = initNewNode(node, father, WEST);
		}
		if(father.getY() - 1 >= 0 && father.getX() - 1 >= 0){
			node = initNewNode(node, father, NORTHWEST);
		}
		System.out.println(node.getF(DIR1) + "\n");
		
		return node;
	}
	
	private Node initNewNode(Node node, Node father, int direction){
		nodeCount++;
		Node temp = new Node();

		switch(direction){
		case 1:
			if(map[father.getX()][father.getY()-1] != 0){
				temp = new Node(father.getX(),father.getY()-1,map[father.getX()][father.getY()-1]);
				temp.setFnode(father, DIR1);
				temp.setG(father.getG(DIR1) + 10, DIR1);
				temp.setH(Math.abs((temp.getX()-endNode.getX())*10 + (temp.getY()-endNode.getY())*10), DIR1);
				temp.countF(DIR1);
			}
			break;
		case 2:
			if(map[father.getX()+1][father.getY()-1] != 0){
				temp = new Node(father.getX()+1,father.getY()-1,map[father.getX()+1][father.getY()-1]);
				temp.setFnode(father, DIR1);
				temp.setG(father.getG(DIR1) + 14, DIR1);
				temp.setH(Math.abs((temp.getX()-endNode.getX())*10 + (temp.getY()-endNode.getY())*10), DIR1);
				temp.countF(DIR1);
			}
			break;
		case 3:
			if(map[father.getX()+1][father.getY()] != 0){
				temp = new Node(father.getX()+1,father.getY(),map[father.getX()+1][father.getY()]);
				temp.setFnode(father, DIR1);
				temp.setG(father.getG(DIR1) + 10, DIR1);
				temp.setH(Math.abs((temp.getX()-endNode.getX())*10 + (temp.getY()-endNode.getY())*10), DIR1);
				temp.countF(DIR1);
			}
			break;
		case 4:
			if(map[father.getX()+1][father.getY()+1] != 0){
				temp = new Node(father.getX()+1,father.getY()+1,map[father.getX()+1][father.getY()+1]);
				temp.setFnode(father, DIR1);
				temp.setG(father.getG(DIR1) + 14, DIR1);
				temp.setH(Math.abs((temp.getX()-endNode.getX())*10 + (temp.getY()-endNode.getY())*10), DIR1);
				temp.countF(DIR1);
			}		
			break;
		case 5:
			if(map[father.getX()][father.getY()+1] != 0){
				temp = new Node(father.getX(),father.getY()+1,map[father.getX()][father.getY()+1]);
				temp.setFnode(father, DIR1);
				temp.setG(father.getG(0) + 10, DIR1);
				temp.setH(Math.abs((temp.getX()-endNode.getX())*10 + (temp.getY()-endNode.getY())*10), DIR1);
				temp.countF(DIR1);
			}
			break;
		case 6:
			if(map[father.getX()-1][father.getY()+1] != 0){
				temp = new Node(father.getX()-1,father.getY()+1,map[father.getX()-1][father.getY()+1]);
				temp.setFnode(father, DIR1);
				temp.setG(father.getG(DIR1) + 14, DIR1);
				temp.setH(Math.abs((temp.getX()-endNode.getX())*10 + (temp.getY()-endNode.getY())*10), DIR1);
				temp.countF(DIR1);
			}	
			break;
		case 7:
			if(map[father.getX()-1][father.getY()] != 0){
				temp = new Node(father.getX()-1,father.getY(),map[father.getX()-1][father.getY()]);
				temp.setFnode(father, DIR1);
				temp.setG(father.getG(0) + 10, DIR1);
				temp.setH(Math.abs((temp.getX()-endNode.getX())*10 + (temp.getY()-endNode.getY())*10), DIR1);
				temp.countF(DIR1);
			}
			break;
		case 8:
			if(map[father.getX()-1][father.getY()-1] != 0){
				temp = new Node(father.getX()-1,father.getY()-1,map[father.getX()-1][father.getY()-1]);
				temp.setFnode(father, DIR1);
				temp.setG(father.getG(DIR1) + 14, DIR1);
				temp.setH(Math.abs((temp.getX()-endNode.getX())*10 + (temp.getY()-endNode.getY())*10), DIR1);
				temp.countF(DIR1);
			}
			break;
		}

		return maintain(node, temp, DIR1);
	}

	private Node maintain(Node minNode, Node node, int DIR){
		if(node != null && findNode(node) == -1){
			openList0.add(node);
			System.out.print("{" + node.getX() + node.getY() + "," + node.getF(DIR) + "},");
			return min(minNode, node, DIR);
		}
		else	return minNode;
	}

	private Node min(Node node1, Node node2, int DIR){

		if (node1.getF(DIR) < node2.getF(DIR))	return node1;
		else	return node2;
	}

	private int findNode(Node node){
		int num = -1;
		for(int i = 0; i < openList0.size(); i++){
			if(openList0.get(i).getX() == node.getX() && openList0.get(i).getY() == node.getY()){
				num = i;
				break;
			}
		}
		return num;
	}
}
