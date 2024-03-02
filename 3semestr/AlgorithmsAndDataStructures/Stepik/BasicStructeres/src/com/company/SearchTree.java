package com.company;



import java.util.LinkedList;
import java.util.Scanner;

public class SearchTree {
    private static elem[] tree;
    static long[] inord;
    static int k = 0;
    static int j = 0;
    public static void main(String[] args)
    {
        Scanner scanner= new Scanner(System.in);
        int n = scanner.nextInt();
        if(n != 0) {
            tree = new elem[n];
            for (int i = 0; i < n; i++) {
                long v = scanner.nextInt();
                int l = scanner.nextInt();
                int r = scanner.nextInt();
                elem m = new elem(l, r, v);
                tree[i] = m;
            }
            inord = new long[n];
            Inorder(0);
            int flag = 0;
            int i = 0;
            while (flag == 0 && i < inord.length - 1) {
                if (inord[i + 1] < inord[i]) flag = 1;
                i++;
            }
            if(j==0 && flag==0)System.out.println("CORRECT");
            else System.out.println("INCORRECT");
        }
        else System.out.println("CORRECT");


    }
    private static void Inorder(int i)
    {
        if(tree[i].left != -1) {
            if(tree[tree[i].left].value == tree[i].value || tree[tree[i].left].value>tree[i].value) j=1;
            Inorder(tree[i].left);
        }
        inord[k] =tree[i].value;
        k++;
        if(tree[i].right!=-1) {
            if( tree[tree[i].right].value<tree[i].value) j=1;
            Inorder(tree[i].right);
        }
    }
    static class elem{
        int left;
        int right;
        long value;
        elem (int l,int r,long value)
        {
            this.left =l;
            this.right =r;
            this.value =value;
        }
    }
}
