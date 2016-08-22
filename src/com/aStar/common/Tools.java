package com.aStar.common;

import com.aStar.Node;

import java.util.List;

/**
 * Created by many on 2016/8/20.
 */
public class Tools {

    public Node findLowest(List<Node> nodeList, int direction){
        Node node = nodeList.get(0);
        for (int i = 0; i < nodeList.size(); i++){
            if (nodeList.get(i).getF(direction) < node.getF(direction)) node = nodeList.get(i);
        }
        return node;
    }

    public int getIndex(List<Node> nodeList, Node node){
        for (int i = 0; i < nodeList.size(); i++){
            if (nodeList.get(i).getX() == node.getX() && nodeList.get(i).getY() == node.getY()){
                return i;
            }
        }
        return -1;
    }
}
