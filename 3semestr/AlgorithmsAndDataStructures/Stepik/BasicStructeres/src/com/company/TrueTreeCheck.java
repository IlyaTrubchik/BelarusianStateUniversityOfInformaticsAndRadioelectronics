package com.company;
import java.util.Scanner;
public class TrueTreeCheck {
    private static node[] nodes;
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        nodes = new node[n];
        for(int i = 0;i<n;i++)
        {
            nodes[i] = new node(scanner.nextInt(),scanner.nextInt(),scanner.nextInt());
        }
        if(CheckTree(0,Integer.MIN_VALUE,Integer.MAX_VALUE))
        System.out.println("CORRECT");
        else System.out.println("INCORRECT");
    }
    private static boolean CheckTree(int index,int leftborder,int rightborder)
    {
        if (index==-1)
        {
            return true;
        }
        if(nodes[index].value<leftborder || nodes[index].value>rightborder)
        {
            return false;
        }
        return CheckTree(nodes[index].left,leftborder,nodes[index].value) && CheckTree(nodes[index].right,nodes[index].value,rightborder);
    }
    static class node{
        int left;
        int right;
        int value;
        node(int value,int left,int right)
        {
            this.left = left;
            this.right =right;
            this.value = value;
        }
    }
}