package com.company;

import java.util.Scanner;

public class TrueTreeSecond {
    private static node[] nodes;
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();


        if(n != 0)
        {
            nodes = new node[n];
            for(int i = 0;i<n;i++)
            {
                nodes[i] = new node(scanner.nextInt(),scanner.nextInt(),scanner.nextInt());
            }
            range range = new range(0,0);
            if(CheckTree(0,range))
            {
                System.out.println("CORRECT");
            }
            else System.out.println("INCORRECT");
        }
        else System.out.println("CORRECT");
    }
    private static boolean CheckTree(int index,range range)
    {
        if(index == -1 ) return true;
        range.left = nodes[index].value;
        range.right = nodes[index].value;

        if(nodes[index].left != -1)
        {
            range lRange = new range(0,0);
            if(!CheckTree(nodes[index].left,lRange) || lRange.right>=nodes[index].value)
            {
                return false;
            }
            range.left =lRange.left;
        }
        else if(nodes[index].right != -1)
        {
            range rRange = new range(0,0);
            if(!CheckTree(nodes[index].left,rRange) || rRange.left <nodes[index].value)
            {
                return false;
            }
            range.right =rRange.right;
        }
        return true;
    }
    static class range{
        int left;
        int right;
                range(int left,int right)
                {
                    this.left = left;
                    this.right = right;
                }
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