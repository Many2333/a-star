package com.aStar;

import com.aStar.common.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by many on 2016/8/20.
 */
public class Search {
    private List<List<Node>> openLists = new ArrayList<List<Node>>();
    private List<Node> openList0 = new ArrayList<Node>();
    private List<Node> openList1 = new ArrayList<Node>();
    private List<Node> closeList = new ArrayList<Node>();
    private int[][] map;
    private int length, width;
    private int cost = 0, nodeCount = 0;

    private Node startNode;
    private Node endNode;

    private Tools tools = new Tools();

    private final static int _DIR1 = 0;
    private final static int _DIR2 = 1;

    public Search(int[][] map, int length, int width, int startX, int startY, int endX, int endY){
        this.map = map;
        this.length = length;
        this.width = width;

        startNode = new Node(startX, startY, map[startX][startY]);
        endNode = new Node(endX, endY, map[endX][endY]);

        startNode.setFnode(null, _DIR1);
        startNode.setH(computH(startNode, endNode), _DIR1);
        endNode.setFnode(null, _DIR2);
        endNode.setH(computH(endNode, startNode), _DIR2);

        openList0.add(startNode);
        openList1.add(endNode);
        openLists.add(0, openList0);
        openLists.add(1, openList1);
    }

    public void doUnidirSearch(){
        while(openList0.size() != 0){
            Node thisNode = tools.findLowest(openList0, _DIR1);
            if (thisNode.getX() == endNode.getX() && thisNode.getY() == endNode.getY()){
                getPath(thisNode, _DIR1);
                System.out.println("搜索节点个数：" + nodeCount + "\n总花费：" + thisNode.getF(_DIR1));
                break;
            }

            System.out.print(thisNode.getX() + "," + thisNode.getY() + ":");

            openList0.remove(tools.getIndex(openList0, thisNode));
            closeList.add(thisNode);

            checkArround(thisNode, _DIR1);

            System.out.print("finish\n");
        }

        if (openList0.size() == 0)   System.out.println("没有可达通路");
    }

    public void doBidirSearch(){
        int dir = 0;

        while(openLists.get(0).size() != 0 && openLists.get(1).size() != 0){
            int thisDir = dir % 2;
            dir++;

            Node thisNode = tools.findLowest(openLists.get(thisDir), thisDir);
            if (thisNode.getX() == endNode.getX() && thisNode.getY() == endNode.getY()){
                getPath(thisNode, thisDir);
                System.out.println("搜索节点个数：" + nodeCount + "\n总花费：" + thisNode.getF(thisDir));
                break;
            }

            System.out.print(thisNode.getX() + "," + thisNode.getY() + ":");

            openList0.remove(tools.getIndex(openLists.get(thisDir), thisNode));
            closeList.add(thisNode);

            checkArround(thisNode, thisDir);

            System.out.print("finish\n");
        }
    }

    private void getPath(Node node, int direction){
        while(node != null){
            map[node.getX()][node.getY()] = 9;
            node = node.getFnode(direction);
        }

        for (int i = 0; i < length; i++){
            for (int j = 0; j < width; j++){
                if (map[i][j] == 9) System.out.print("* ");
                else    System.out.print(map[i][j] + " ");
            }
            System.out.print("\n");
        }
    }

    private void checkArround(Node fnode, int direction){
        int x = fnode.getX();
        int y = fnode.getY();

        if (x - 1 >= 0){
            Node node = new Node(x - 1, y, map[x - 1][y]);
            initNode(openLists.get(direction), fnode, node, direction, 10);
        }
        if (x + 1 < length){
            Node node = new Node(x + 1, y, map[x + 1][y]);
            initNode(openLists.get(direction), fnode, node, direction, 10);
        }
        if (y - 1 >= 0){
            Node node = new Node(x, y - 1, map[x][y - 1]);
            initNode(openLists.get(direction), fnode, node, direction, 10);
        }
        if (y + 1 < width){
            Node node = new Node(x, y + 1, map[x][y + 1]);
            initNode(openLists.get(direction), fnode, node, direction, 10);
        }
        if (x - 1 >= 0 && y - 1 >= 0){
            Node node = new Node(x - 1, y - 1, map[x - 1][y - 1]);
            initNode(openLists.get(direction), fnode, node, direction, 14);
        }
        if (x - 1 >= 0 && y + 1 < width){
            Node node = new Node(x - 1, y + 1, map[x - 1][y + 1]);
            initNode(openLists.get(direction), fnode, node, direction, 14);
        }
        if (x + 1 < length && y - 1 >= 0){
            Node node = new Node(x + 1, y - 1, map[x + 1][y - 1]);
            initNode(openLists.get(direction), fnode, node, direction, 14);
        }
        if (x + 1 < length && y + 1 < width){
            Node node = new Node(x + 1, y + 1, map[x + 1][y + 1]);
            initNode(openLists.get(direction), fnode, node, direction, 14);
        }

    }

    private void initNode(List<Node> nodeList, Node fnode, Node node, int direction, int cost){
        if (tools.getIndex(closeList, node) != -1)  return;
        if (node.getValue() == 0){
            closeList.add(node);
            return;
        }

        nodeCount++;
        boolean isBetter = false;
        int tentative_G = fnode.getG(direction) + cost;

        int index = tools.getIndex(nodeList, node);
        if (index != -1){
            node = nodeList.get(index);

            if (tentative_G < node.getG(direction))
                isBetter = true;
        }
        else{
            nodeList.add(node);
            isBetter = true;
        }

        if (isBetter){
            //System.out.print(fnode.getX() + " " + fnode.getY());
            node.setFnode(fnode, direction);
            node.setG(tentative_G, direction);
            node.setH(computH(node, endNode), direction);
            node.countF(direction);
        }
    }

    private int computH(Node startNode, Node endNode){
        return Math.abs(10*(startNode.getX() - endNode.getX() + startNode.getY() - endNode.getY()));
    }
}
