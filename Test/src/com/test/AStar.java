package com.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//A*�㷨

public class AStar {
    private int[][] map;//��ͼ(1��ͨ�� 0����ͨ��)
    private List<Node> openList;//�����б�
    private List<Node> closeList;//�ر��б�
    private List<Node> openList1;//�����б�
    private List<Node> closeList1;//�ر��б�
    private List<Node> openList2;//�����б�
    private List<Node> closeList2;//�ر��б�
    private final int COST_STRAIGHT = 10;//��ֱ�����ˮƽ�����ƶ���·������
    private final int COST_DIAGONAL = 14;//б�����ƶ���·������
    private int row;//��
    private int column;//��
    public int searchNum1=0;//���������ڵ�ĸ���
    public int searchNum2=0;//˫�������ڵ�ĸ���
    public long searchTime1=0;
    public long searchTime2=0;
    public int cost1=0;
    public int cost2=0;
    public int f=1000;
    
    public AStar(int[][] map,int row,int column){
        this.map=map;
        this.row=row;
        this.column=column;
        openList=new ArrayList<Node>();
        closeList=new ArrayList<Node>();
        openList1=new ArrayList<Node>();
        closeList1=new ArrayList<Node>();
        openList2=new ArrayList<Node>();
        closeList2=new ArrayList<Node>();
    }
    
    //�������꣨-1������0��û�ҵ���1���ҵ��ˣ�
    public int searchSingle(int x1,int y1,int x2,int y2){
        if(x1< 0||x1>=row||x2< 0||x2>=row||y1< 0||y1>=column||y2< 0||y2>=column){
            return -1;
        }
        if(map[x1][y1]==0||map[x2][y2]==0){
            return -1;
        }
        Node sNode=new Node(x1,y1,null);
        Node eNode=new Node(x2,y2,null);
        openList.add(sNode);
        long startTime=System.currentTimeMillis();
        List<Node> resultList=searchSingle(sNode, eNode);
        if(resultList.size()==0){
            return 0;
        }
        for(Node node:resultList){
            map[node.getX()][node.getY()]++;
        }
        this.searchTime1=(System.currentTimeMillis()-startTime);
        return 1;
    }
    private List<Node> searchSingle(Node sNode,Node eNode){
        List<Node> resultList=new ArrayList<Node>();
        boolean isFind=false;
        Node node1=null;
        Node node=null;
        while(openList.size()>0){
        	if(openList.size()>0){
        		node=checkAround(null,eNode,openList,closeList);
        		searchNum1++;
        	}
        	if((node1=isOver1(eNode))!=null){
        		isFind=true;
        		node=node1;
        		break;
        	}
        }
        if(isFind){
            getPath(resultList, node);
            this.cost1=node.getF();
        }
        return resultList;
    }
    
    public int searchDouble(int x1,int y1,int x2,int y2){
        if(x1< 0||x1>=row||x2< 0||x2>=row||y1< 0||y1>=column||y2< 0||y2>=column){
            return -1;
        }
        if(map[x1][y1]==0||map[x2][y2]==0){
            return -1;
        }
        Node sNode=new Node(x1,y1,null);
        Node eNode=new Node(x2,y2,null);
        openList1.add(sNode);
        openList2.add(eNode);
        long startTime=System.currentTimeMillis();
        List<Node> resultList=searchDouble(sNode, eNode);
        if(resultList.size()==0){
            return 0;
        }
        for(Node node:resultList){
            map[node.getX()][node.getY()]+=2;
        }
        this.searchTime2=(System.currentTimeMillis()-startTime);
        return 1;
    }
    
    //���Һ����㷨
    private List<Node> searchDouble(Node sNode,Node eNode){
        List<Node> resultList=new ArrayList<Node>();
        boolean isFind=false;
        Node[] nodes=null;
        Node forwardNode=null;
        Node backwardNode=null;
        while(openList1.size()>0||openList2.size()>0){
        	if(openList1.size()>0){
        		forwardNode=checkAround(null,eNode,openList1,closeList1);
        		searchNum2++;
        	}
        	if(openList2.size()>0){
        		backwardNode=checkAround(null,sNode,openList2,closeList2);
        		searchNum2++;
        	}
             //System.out.println(openList);
        	if((nodes=isOver2())!=null){
        		isFind=true;
        		forwardNode=nodes[0];
        		backwardNode=nodes[1];
        		this.cost2=forwardNode.getF()+backwardNode.getF();
        		break;
        	}
        }
        if(isFind){
            getPath(resultList, forwardNode);
            getPath(resultList, backwardNode);
        }
        return resultList;
    }
    
    //��ѯ��·�Ƿ�����ͨ
    private boolean checkPath(int x,int y,Node parentNode,Node eNode,int cost,
    		List<Node> openList,List<Node> closeList){
        Node node=new Node(x, y, parentNode);
        //���ҵ�ͼ���Ƿ���ͨ��
        if(map[x][y]==0){
            closeList.add(node);
            return false;
        }
        //���ҹر��б����Ƿ����
        if(isListContains(closeList, x, y)!=-1){
            return false;
        }
        //���ҿ����б����Ƿ����
        int index=-1;
        if((index=isListContains(openList, x, y))!=-1){
            //Gֵ�Ƿ��С�����Ƿ����G��Fֵ
            if((parentNode.getG()+cost)< openList.get(index).getG()){
                node.setParentNode(parentNode);
                count(node, eNode, cost);
                
                openList.set(index, node);
            }
        }else{
            //��ӵ������б���
            node.setParentNode(parentNode);
            count(node, eNode, cost);
            openList.add(node);
        }
        return true;
    }
    private Node checkAround(Node node,Node eNode,List<Node> openList,List<Node> closeList){
    	//ȡ�������б������Fֵ������һ���洢��ֵ��FΪ��͵�
    	node=openList.get(0);
        //��
        if((node.getY()-1)>=0){
            checkPath(node.getX(),node.getY()-1,node, eNode, COST_STRAIGHT,openList,closeList);
        }
        //��
        if((node.getY()+1)< column){
            checkPath(node.getX(),node.getY()+1,node, eNode, COST_STRAIGHT,openList,closeList);
        }
        //��
        if((node.getX()-1)>=0){
            checkPath(node.getX()-1,node.getY(),node, eNode, COST_STRAIGHT,openList,closeList);
        }
        //��
        if((node.getX()+1)< row){
            checkPath(node.getX()+1,node.getY(),node, eNode, COST_STRAIGHT,openList,closeList);
        }
        //����
        if((node.getX()-1)>=0&&(node.getY()-1)>=0){
            checkPath(node.getX()-1,node.getY()-1,node, eNode, COST_DIAGONAL,openList,closeList);
        }
        //����
        if((node.getX()-1)>=0&&(node.getY()+1)< column){
            checkPath(node.getX()-1,node.getY()+1,node, eNode, COST_DIAGONAL,openList,closeList);
        }
        //����
        if((node.getX()+1)< row&&(node.getY()-1)>=0){
            checkPath(node.getX()+1,node.getY()-1,node, eNode, COST_DIAGONAL,openList,closeList);
        }
        //����
        if((node.getX()+1)< row&&(node.getY()+1)< column){
            checkPath(node.getX()+1,node.getY()+1,node, eNode, COST_DIAGONAL,openList,closeList);
        }
        //�ӿ����б���ɾ��
        //��ӵ��ر��б���
        closeList.add(openList.remove(0));
        //�����б������򣬰�Fֵ��͵ķŵ���׶�
        Collections.sort(openList, new NodeFComparator());
        return node;
    }
    
    //�������Ƿ����ĳ��Ԫ��(-1��û���ҵ������򷵻����ڵ�����)
    private int isListContains(List<Node> list,int x,int y){
        for(int i=0;i< list.size();i++){
            Node node=list.get(i);
            if(node.getX()==x&&node.getY()==y){
                return i;
            }
        }
        return -1;
    }
    
    //�����Ƿ���Խ���
    private Node isOver1(Node eNode){
    	Node node1;
    	for(Node node:openList){
    		if(node.getX()==eNode.getX()&&node.getY()==eNode.getY()&&node.getParentNode()!=null){
    			node1=node;
    			return node1;
    		}		
    	}
    	return null;
    }
    
    private Node[] isOver2(){
    	Node[] nodes=new Node[2];
    	for(Node forwardNode:openList1){
    		for(Node backwardNode:openList2)
    			if(forwardNode.getX()==backwardNode.getX()&&forwardNode.getY()==backwardNode.getY()
    			&&forwardNode.getParentNode()!=null&&backwardNode.getParentNode()!=null&&
    			backwardNode.getF()<=Math.max(getMinF(openList1), getMinF(openList2))){
    				//System.out.println(forwardNode.getX()+" "+forwardNode.getY());
    				map[forwardNode.getX()][forwardNode.getY()]-=2;
    				nodes[0]=forwardNode;
    				nodes[1]=backwardNode;
    				return nodes;
    			}		
    	}
    	return null;
    }
    
    //���յ������ص����
    private void getPath(List<Node> resultList,Node node){
        if(node.getParentNode()!=null){
            getPath(resultList, node.getParentNode());
        }
        resultList.add(node);
    }
    
    //����G,H,Fֵ
    private void count(Node node,Node eNode,int cost){
        countG(node, eNode, cost);
        countH(node, eNode);
        countF(node);
    }
    //����Gֵ
    private void countG(Node node,Node eNode,int cost){
        if(node.getParentNode()==null){
            node.setG(cost);
        }else{
            node.setG(node.getParentNode().getG()+cost);
        }
    }
    //����Hֵ
    private void countH(Node node,Node eNode){
        node.setF((Math.abs(node.getX()-eNode.getX())+Math.abs(node.getY()-eNode.getY()))*10);
    }
    //����Fֵ
    private void countF(Node node){
        node.setF(node.getG()+node.getH());
    }
    private int getMinF(List<Node> list){
    	int x=list.get(0).getG();
    	for(int i=1; i<list.size(); i++){
    		if(x>list.get(i).getF())	x=list.get(i).getF();
    	}
    	return x;
    }
    
}
