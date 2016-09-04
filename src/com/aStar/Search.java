package com.aStar;

import com.aStar.common.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by many on 2016/7/20.
 */
public class Search {
    private int cnt=0;
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

    public boolean isDisplay = false;

    public Search(int[][] map, int length, int width, int startX, int startY, int endX, int endY){
        this.map = map;
        this.length = length;
        this.width = width;

        cnt=0;

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
        System.out.println("单向搜索开始：");
        long startTime = System.currentTimeMillis();

        while(openList0.size() != 0){
            Node thisNode = tools.findMinCost(openList0, _DIR1);
            //System.out.println(thisNode.getX() + " , " + thisNode.getY());
            if (thisNode.getX() == endNode.getX() && thisNode.getY() == endNode.getY()){
                getPath(thisNode, _DIR1);
                if (isDisplay)  displayMap();
                System.out.println("搜索节点个数：" + nodeCount + "\n总花费：" + thisNode.getF(_DIR1));

                double useTime = System.currentTimeMillis() - startTime;
                System.out.println("所用时间：" + useTime + "ms");
                break;
            }

            openList0.remove(tools.getIndex(openList0, thisNode));
            closeList.add(thisNode);

            checkAround(thisNode, endNode, _DIR1);
            //tools.displayList(openList0, _DIR1);
            //tools.displayList(openList0, _DIR2);
        }

        if (openList0.size() == 0)   System.out.println("没有可达通路");
    }

    public void doBidirSearch(){
        reInit();
        System.out.println("双向搜索开始：");
        long startTime = System.currentTimeMillis();

        int dir = 0;

        Node[] nodes = {startNode, endNode};
        while(openLists.get(0).size() != 0 && openLists.get(1).size() != 0){
            updateList(_DIR1, nodes[_DIR2]);
            updateList(_DIR2, nodes[_DIR1]);
            int thisDir = dir % 2;
            dir++;
            int otherDir = dir % 2;

            nodes[thisDir] = tools.findMinCost(openLists.get(thisDir), thisDir);
            nodes[otherDir] = tools.findMinCost(openLists.get(otherDir), otherDir);
            if (nodes[thisDir].getG(thisDir) > nodes[otherDir].getG(dir % 2))   continue;
            cost = nodes[thisDir].getG(thisDir) + nodes[otherDir].getG(dir % 2);

            if (nodes[thisDir].getX() == nodes[otherDir].getX() && nodes[thisDir].getY() == nodes[otherDir].getY()
                    && nodes[thisDir].getF(thisDir) <=
                    Math.max(tools.getMinF(openLists.get(thisDir), thisDir), tools.getMinF(openLists.get(otherDir), otherDir))){
                getPath(nodes[thisDir], thisDir);
                getPath(nodes[otherDir], dir % 2);
                map[nodes[thisDir].getX()][nodes[thisDir].getY()] = 100;
                if (isDisplay)  displayMap();
                System.out.println("搜索节点个数：" + nodeCount + "\n总花费：" + cost);

                double useTime = System.currentTimeMillis() - startTime;
                System.out.println("所用时间：" + useTime + "ms");
                break;
            }

            openLists.get(thisDir).remove(tools.getIndex(openLists.get(thisDir), nodes[thisDir]));
            closeList.add(nodes[thisDir]);

            checkAround(nodes[thisDir], nodes[otherDir], thisDir);
            //tools.displayList(openLists.get(thisDir), thisDir);
        }

        if (openLists.get(0).size() == 0 || openLists.get(1).size() == 0)   System.out.println("没有可达通路");
    }

    private void updateList(int direction, Node endNode){
        for (int i = 0; i < openLists.get(direction).size(); i++){
            int newH = computH(openLists.get(direction).get(i), endNode);
            openLists.get(direction).get(i).setH(newH, direction);
            openLists.get(direction).get(i).countF(direction);
        }
    }

    private void reInit(){
        for (int i = 0; i < length; i++){
            for (int j = 0; j < width; j++){
                if (map[i][j] > 9) map[i][j] = 1;
            }
        }
        nodeCount = 0;

        openLists.get(_DIR1).clear();
        openLists.get(_DIR1).add(startNode);
        openLists.get(_DIR2).clear();
        openLists.get(_DIR2).add(endNode);
        closeList.clear();

//        tools.displayList(openList0, _DIR1);
//        tools.displayList(openList1, _DIR2);
//        System.out.println(closeList.size());
//        System.out.println(startNode.toString());
//        System.out.println(endNode.toString());
    }

    private void displayMap(){
        for (int i = 0; i < length; i++){
            for (int j = 0; j < width; j++){
                if (map[i][j] == 100)   System.out.print("* ");
                //else    if (map[i][j] == 99)    System.out.print("$ ");
                else    if (map[i][j] > 9)  System.out.print(map[i][j]%10+" ");
                else    if (map[i][j] == 0) System.out.print("_ ");
                else    System.out.print(". ");
            }
            System.out.print("\n");
        }
    }

    private void getPath(Node node, int direction){
        while(node != null){
            map[node.getX()][node.getY()] = (cnt++)%10+10;
            node = node.getFnode(direction);
        }
    }

    private void checkAround(Node fnode, Node endNode, int direction){
        int x = fnode.getX();
        int y = fnode.getY();
        int extraCost = 0;

        if (x - 1 >= 0){
            Node node = new Node(x - 1, y, map[x - 1][y]);
            //extraCost = getExtraCost(node, endNode);
            initNode(openLists.get(direction), fnode, node, endNode, direction, 10, extraCost);
        }
        if (x + 1 < length){
            Node node = new Node(x + 1, y, map[x + 1][y]);
            //extraCost = getExtraCost(node, endNode);
            initNode(openLists.get(direction), fnode, node, endNode, direction, 10, extraCost);
        }
        if (y - 1 >= 0){
            Node node = new Node(x, y - 1, map[x][y - 1]);
            //extraCost = getExtraCost(node, endNode);
            initNode(openLists.get(direction), fnode, node, endNode, direction, 10, extraCost);
        }
        if (y + 1 < width){
            Node node = new Node(x, y + 1, map[x][y + 1]);
            //extraCost = getExtraCost(node, endNode);
            initNode(openLists.get(direction), fnode, node, endNode, direction, 10, extraCost);
        }
        if (x - 1 >= 0 && y - 1 >= 0){
            Node node = new Node(x - 1, y - 1, map[x - 1][y - 1]);
            //extraCost = getExtraCost(node, endNode);
            initNode(openLists.get(direction), fnode, node, endNode, direction, 14, extraCost);
        }
        if (x - 1 >= 0 && y + 1 < width){
            Node node = new Node(x - 1, y + 1, map[x - 1][y + 1]);
            //extraCost = getExtraCost(node, endNode);
            initNode(openLists.get(direction), fnode, node, endNode, direction, 14, extraCost);
        }
        if (x + 1 < length && y - 1 >= 0){
            Node node = new Node(x + 1, y - 1, map[x + 1][y - 1]);
            //extraCost = getExtraCost(node, endNode);
            initNode(openLists.get(direction), fnode, node, endNode, direction, 14, extraCost);
        }
        if (x + 1 < length && y + 1 < width){
            Node node = new Node(x + 1, y + 1, map[x + 1][y + 1]);
            //extraCost = getExtraCost(node, endNode);
            initNode(openLists.get(direction), fnode, node, endNode, direction, 14, extraCost);
        }

    }

    private int getExtraCost(Node node, Node endNode){
        int extraCost = 0;
        int dx = endNode.getX() - node.getX();
        int dy = endNode.getY() - node.getY();

        if (dx > 0) dx = 1;
        if (dx < 0) dx = -1;
        if (dy > 0) dx = 1;
        if (dy < 0) dx = -1;

        if (node.getX() + dx >= 0 && node.getX() + dx < length){
            if (map[node.getX() + dx][node.getY()] == 0) extraCost += 2;
        }
        if (node.getY() + dy >= 0 && node.getY() + dy < width){
            if (map[node.getX()][node.getY() + dy] == 0) extraCost += 2;
        }
        if (node.getX() + dx >= 0 && node.getX() + dx < length && node.getY() + dy >= 0 && node.getY() + dy < width){
            if (map[node.getX() + dx][node.getY() + dy] == 0) extraCost += 2;
        }
        return extraCost;
    }

    private void initNode(List<Node> nodeList, Node fnode, Node node, Node endNode, int direction, int cost, int extraCost){
        if (tools.getIndex(closeList, node) != -1)  return;
        if (node.getValue() == 0){
            closeList.add(node);
            return;
        }

        nodeCount++;
        //map[node.getX()][node.getY()] = 99;
        boolean isBetter = false;
        int tentative_G = fnode.getG(direction) + cost;

        int index = tools.getIndex(nodeList, node);

        if (index == -1){
            isBetter = true;
            nodeList.add(node);
            index = nodeList.size() - 1;
        }
        else if (tentative_G < node.getG(direction)){
            isBetter = true;
        }
        else{
            isBetter = false;
        }

        if (isBetter){
            nodeList.get(index).setFnode(fnode, direction);
            nodeList.get(index).setG(tentative_G, direction);
            nodeList.get(index).setH(computH(node, endNode) + extraCost, direction);
            nodeList.get(index).countF(direction);
        }
    }

    private int computH(Node startNode, Node endNode){
        int dx = Math.abs(startNode.getX() - endNode.getX());
        int dy = Math.abs(startNode.getY() - endNode.getY());
        return 8 * (dx + dy) + (12 - 2 * 8) * Math.min(dx, dy);
//        return Math.abs(5*(startNode.getX() - endNode.getX() + startNode.getY() - endNode.getY()));
    }
}
