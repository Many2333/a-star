package com.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//A*算法

public class AStar {
    private int[][] map;//地图(1可通过 0不可通过)
    private List<Node> openList;//开启列表
    private List<Node> closeList;//关闭列表
    private List<Node> openList1;//开启列表
    private List<Node> closeList1;//关闭列表
    private List<Node> openList2;//开启列表
    private List<Node> closeList2;//关闭列表
    private final int COST_STRAIGHT = 10;//垂直方向或水平方向移动的路径评分
    private final int COST_DIAGONAL = 14;//斜方向移动的路径评分
    private int row;//行
    private int column;//列
    public int searchNum1=0;//单向搜索节点的个数
    public int searchNum2=0;//双向搜索节点的个数
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
    
    //查找坐标（-1：错误，0：没找到，1：找到了）
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
    
    //查找核心算法
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
    
    //查询此路是否能走通
    private boolean checkPath(int x,int y,Node parentNode,Node eNode,int cost,
    		List<Node> openList,List<Node> closeList){
        Node node=new Node(x, y, parentNode);
        //查找地图中是否能通过
        if(map[x][y]==0){
            closeList.add(node);
            return false;
        }
        //查找关闭列表中是否存在
        if(isListContains(closeList, x, y)!=-1){
            return false;
        }
        //查找开启列表中是否存在
        int index=-1;
        if((index=isListContains(openList, x, y))!=-1){
            //G值是否更小，即是否更新G，F值
            if((parentNode.getG()+cost)< openList.get(index).getG()){
                node.setParentNode(parentNode);
                count(node, eNode, cost);
                
                openList.set(index, node);
            }
        }else{
            //添加到开启列表中
            node.setParentNode(parentNode);
            count(node, eNode, cost);
            openList.add(node);
        }
        return true;
    }
    private Node checkAround(Node node,Node eNode,List<Node> openList,List<Node> closeList){
    	//取出开启列表中最低F值，即第一个存储的值的F为最低的
    	node=openList.get(0);
        //上
        if((node.getY()-1)>=0){
            checkPath(node.getX(),node.getY()-1,node, eNode, COST_STRAIGHT,openList,closeList);
        }
        //下
        if((node.getY()+1)< column){
            checkPath(node.getX(),node.getY()+1,node, eNode, COST_STRAIGHT,openList,closeList);
        }
        //左
        if((node.getX()-1)>=0){
            checkPath(node.getX()-1,node.getY(),node, eNode, COST_STRAIGHT,openList,closeList);
        }
        //右
        if((node.getX()+1)< row){
            checkPath(node.getX()+1,node.getY(),node, eNode, COST_STRAIGHT,openList,closeList);
        }
        //左上
        if((node.getX()-1)>=0&&(node.getY()-1)>=0){
            checkPath(node.getX()-1,node.getY()-1,node, eNode, COST_DIAGONAL,openList,closeList);
        }
        //左下
        if((node.getX()-1)>=0&&(node.getY()+1)< column){
            checkPath(node.getX()-1,node.getY()+1,node, eNode, COST_DIAGONAL,openList,closeList);
        }
        //右上
        if((node.getX()+1)< row&&(node.getY()-1)>=0){
            checkPath(node.getX()+1,node.getY()-1,node, eNode, COST_DIAGONAL,openList,closeList);
        }
        //右下
        if((node.getX()+1)< row&&(node.getY()+1)< column){
            checkPath(node.getX()+1,node.getY()+1,node, eNode, COST_DIAGONAL,openList,closeList);
        }
        //从开启列表中删除
        //添加到关闭列表中
        closeList.add(openList.remove(0));
        //开启列表中排序，把F值最低的放到最底端
        Collections.sort(openList, new NodeFComparator());
        return node;
    }
    
    //集合中是否包含某个元素(-1：没有找到，否则返回所在的索引)
    private int isListContains(List<Node> list,int x,int y){
        for(int i=0;i< list.size();i++){
            Node node=list.get(i);
            if(node.getX()==x&&node.getY()==y){
                return i;
            }
        }
        return -1;
    }
    
    //搜索是否可以结束
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
    
    //从终点往返回到起点
    private void getPath(List<Node> resultList,Node node){
        if(node.getParentNode()!=null){
            getPath(resultList, node.getParentNode());
        }
        resultList.add(node);
    }
    
    //计算G,H,F值
    private void count(Node node,Node eNode,int cost){
        countG(node, eNode, cost);
        countH(node, eNode);
        countF(node);
    }
    //计算G值
    private void countG(Node node,Node eNode,int cost){
        if(node.getParentNode()==null){
            node.setG(cost);
        }else{
            node.setG(node.getParentNode().getG()+cost);
        }
    }
    //计算H值
    private void countH(Node node,Node eNode){
        node.setF((Math.abs(node.getX()-eNode.getX())+Math.abs(node.getY()-eNode.getY()))*10);
    }
    //计算F值
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
