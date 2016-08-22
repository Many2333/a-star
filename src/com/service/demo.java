package com.service;

import com.aStar.Map;
import com.aStar.Search;
import com.aStar.SingleAStar;

public class demo {

	public static void main(String[] args) throws InterruptedException {

		int length = 20, width = 20;
		int[][] map = new Map(length, width).getMap();
//		for (int i = 0; i < length; i++) {
//			for (int j = 0; j < width; j++)
//				map[i][j] = 1;
//		}
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < width; j++)
				System.out.print(map[i][j] + " ");
			System.out.print("\n");
		}

		Search search = new Search(map, length, width, 1, 1, 19, 19);
		search.doUnidirSearch();
//		SingleAStar singleAStar = new SingleAStar(map, length, width, 1, 1, 19, 19);
//		singleAStar.singleSearch();
	}

}
