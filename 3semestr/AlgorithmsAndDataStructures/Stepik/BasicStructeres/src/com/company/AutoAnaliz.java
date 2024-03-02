package com.company;

import java.util.Scanner;

public class AutoAnaliz {
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int e = scanner.nextInt();
        int d = scanner.nextInt();
        int[] tree =new int[n];
        for(int i = 0;i<n;i++)
        {
            tree[i] = i;
        }
        for(int k = 0;k<e;k++)
        {
            int i = scanner.nextInt();
            int j = scanner.nextInt();
            tree[j-1] =tree[i-1];
        }
        int flag= 0;
        for(int k =0;k<d;k++)
        {
            int i = scanner.nextInt();
            int j =scanner.nextInt();
            if(tree[i-1] == tree[j-1]) flag =1;
        }
        if(flag ==1 )
        {
            System.out.println(0);
        }
        else System.out.println(1);
    }
}
