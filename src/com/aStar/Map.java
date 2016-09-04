package com.aStar;

import java.io.*;
import java.util.Random;

public class Map {
	private int[][] map;
	private int length;
	private int width;

	public Map(int x,int y){
		this.length=x;
		this.width=y;
		this.map=new int[x][y];

		int i=0,j=0,direc=2;
		int ran;

		for(i=0; i<x; i++){
			for(j=0; j<y; j++){
				map[i][j]=1;
			}
		}

		i=j=0;
		while(true){
			if(i>=x-1&&j>=y-1)	break;
			Random ra=new Random();
			ran=Math.abs(ra.nextInt()%4);

			if(ran<1){
				if(direc!=1&&i<x-1){
					i++;
					direc=3;
				}
			}
			else if(ran<2){
				if(direc!=2&&j>0){
					j--;
					direc=0;
				}
			}
			else if(ran<3){
				if(direc!=3&&i>0){
					i--;
					direc=1;
				}
			}
			else {
				if(direc!=0&&j<y-1){
					j++;
					direc=2;
				}
			}
		}

		for(i=0; i<x; i++){
			for(j=0; j<y; j++){
				if(map[i][j]==1){
					Random ra=new Random();
					ran=Math.abs(ra.nextInt()%10);
					if(ran<3)	map[i][j]=0;
				}
			}
		}
	}

	public int[][] getMap(){
		return map;
	}
	public int[][] getMap(String path){
		File file = new File(path);
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			String line = bufferedReader.readLine();
			int lineNum = 0;
			while(line != null){
				for(int i = 0, j = 0; i < line.length(); i += 2){
					map[lineNum][j] = (int)line.charAt(i) - '0';
					j++;
				}
				lineNum++;
				line = bufferedReader.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}

}
