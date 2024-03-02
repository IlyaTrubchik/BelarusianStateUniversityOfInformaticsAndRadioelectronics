package com.company;

import java.util.LinkedList;
import java.util.Scanner;

public class Orders {
    private static elem[] tree;
   static StringBuilder Inord =new StringBuilder();
   static StringBuilder Postord =new StringBuilder();
   static StringBuilder Preord =new StringBuilder();
    public static void main(String[] args)
    {
        Scanner scanner= new Scanner(System.in);
        int n = scanner.nextInt();
         tree =new elem[n];
        for(int i = 0;i<n;i++)
        {
           int v = scanner.nextInt();
           int l= scanner.nextInt();
           int r = scanner.nextInt();
           elem m = new elem(l,r,v);
           tree[i] =m;
        }

        Inorder(0);
        Preorder(0);
        Postorder(0);
        System.out.println(Inord.toString());
        System.out.println(Preord.toString());
        System.out.println(Postord.toString());

    }
    private static void Inorder(int i)
    {
        if(tree[i].left != -1) Inorder(tree[i].left);
        Inord.append(tree[i].value);
        Inord.append(" ");
        if(tree[i].right!=-1)Inorder(tree[i].right);
    }
    private static void Preorder(int i)
    {
        Preord.append(tree[i].value);
        Preord.append(" ");
        if(tree[i].left!=-1) Preorder(tree[i].left);
        if(tree[i].right!=-1)Preorder(tree[i].right);
    }
    private static void Postorder(int i)
    {
        if(tree[i].left!= -1) Postorder(tree[i].left);
        if(tree[i].right!=-1)Postorder(tree[i].right);
        Postord.append(tree[i].value);
        Postord.append(" ");
    }
    static class elem{
        int left;
        int right;
        int value;
        elem (int l,int r,int value)
        {
            this.left =l;
            this.right =r;
            this.value =value;
        }
    }
}
