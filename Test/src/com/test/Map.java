package com.test;

import java.util.Random;

public class Map {
	int M;
	int N;
	int map[][];
	
	public Map(int x,int y){
		this.M=x;
		this.N=y;
		this.map=new int[M][N];
		
		int i=0,j=0,direc=2;
		int ran;
		
		for(i=0; i<M; i++){
			for(j=0; j<N; j++){
				map[i][j]=1;
			}
		}
		
		i=j=0;
		while(true){
			if(i>=M-1&&j>=N-1)	break;
			Random ra=new Random();
			ran=Math.abs(ra.nextInt()%4);
			
			if(ran<1){
	            if(direc!=1&&i<M-1){
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
	            if(direc!=0&&j<N-1){
	                j++;
	                direc=2;
	           }
	        }
		}
		
		for(i=0; i<M; i++){
			for(j=0; j<N; j++){
				if(map[i][j]==1){
					Random ra=new Random();
					ran=Math.abs(ra.nextInt()%10);
	                if(ran<3)	map[i][j]=0;
	            }
			}
		}
	}
}
