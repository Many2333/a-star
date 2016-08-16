package com.test;

import javax.swing.*;
import java.awt.*;

public class GUI {
	JFrame frame;
	
	public GUI(int[][] map,int x,int y,int num1,int num2,long time1,long time2,int cost1,int cost2){
		frame=new JFrame("A-Star");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(780,835);
		frame.setLayout(new BorderLayout());
		
		JPanel allPanel=new JPanel();
		for(int i=0; i<x; i++){
			for(int j=0; j<y; j++){
				JPanel panel=new JPanel();
				if(map[i][j]==0){
					panel.setBackground(Color.GREEN);
				}
				else if(map[i][j]==1){
					panel.setBackground(Color.BLACK);
				}
				else if(map[i][j]==2){
					panel.setBackground(Color.BLUE);
				}
				else if(map[i][j]==3){
					panel.setBackground(Color.RED);
				}
				else if(map[i][j]==4){
					panel.setBackground(Color.CYAN);
				}
				
				allPanel.add(panel);
			}	
		}
		
		JLabel label=new JLabel("<html><body>���������ڵ������"+num1+"����ʱ�䣺"+time1+"ms���ã�"+cost1
				+"<br>˫�������ڵ������"+num2+"����ʱ�䣺"+time2+"ms���ã�"+cost2+"</body></html>",
				JLabel.CENTER);
		
		frame.add(allPanel,BorderLayout.CENTER);
		frame.add(label,BorderLayout.SOUTH);
		frame.setVisible(true);
	}
}
