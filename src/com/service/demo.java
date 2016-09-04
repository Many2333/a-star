package com.service;

import com.aStar.Map;
import com.aStar.Search;

public class demo {

	public static void main(String[] args) throws InterruptedException {

		int length = 40, width = 40;
		int startX = 1, startY = 1, endX = 39, endY = 39;
		int[][] map = new Map(length, width).getMap();
//		"D:\\Code\\Java\\map.txt"

//		for (int i = 0; i < length; i++) {
//			for (int j = 0; j < width; j++)
//				System.out.print(map[i][j] + " ");
//			System.out.print("\n");
//		}

		map[startX][startY] = map[endX][endY] = 1;

		Search search = new Search(map, length, width, startX, startY, endX, endY);
		search.isDisplay = true;
		search.doUnidirSearch();
		search.doBidirSearch();
	}

}
