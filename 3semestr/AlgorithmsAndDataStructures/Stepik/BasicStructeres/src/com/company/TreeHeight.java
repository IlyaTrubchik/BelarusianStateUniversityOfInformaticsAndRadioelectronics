package com.company;

import java.util.Scanner;

public class TreeHeight {
    public static void main(String[] args) {
        // put your code here
        Scanner stdin = new Scanner(System.in);
        int n = stdin.nextInt();
        int[] tree_Nodes = new int[n];

        for(int i = 0;i<n;i++)
        {
             tree_Nodes[i] = stdin.nextInt();
        }
        int maxheight = 0;
        for(int Curr_Node:tree_Nodes)
        {
            int parent = Curr_Node;
            int currheight = 1;
            while(parent != -1)
            {
                parent = tree_Nodes[parent];
                currheight++;
            }
            maxheight = Math.max(maxheight,currheight);
        }
        System.out.print(maxheight);
    }
}
