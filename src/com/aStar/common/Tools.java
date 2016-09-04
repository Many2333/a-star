package com.aStar.common;

import com.aStar.Node;

import java.util.List;

/**
 * Created by many on 2016/7/20.
 */
public class Tools {

    public Node findMinCost(List<Node> nodeList, int direction){
        Node node = nodeList.get(0);
        for (int i = 0; i < nodeList.size(); i++){
            if (nodeList.get(i).getF(direction) < node.getF(direction)) node = nodeList.get(i);
        }
        return node;
    }

    public int getMinF(List<Node> nodeList, int direction){
        Node node = findMinCost(nodeList, direction);
        return node.getF(direction);
    }

    public int getIndex(List<Node> nodeList, Node node){
        for (int i = 0; i < nodeList.size(); i++){
            if (nodeList.get(i).getX() == node.getX() && nodeList.get(i).getY() == node.getY()){
                return i;
            }
        }
        return -1;
    }

    public void displayList(List<Node> nodeList, int direction){
        for (int i = 0; i < nodeList.size(); i++){
            Node node = nodeList.get(i);
            System.out.print("{" + node.getX() + "," + node.getY() + "," + node.getF(direction) + "},");
        }
        System.out.print("\n");
    }
}
