package com.company;

import java.util.Scanner;

public class Sets {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int nt = scanner.nextInt();
        int m = scanner.nextInt();
        int[] tree= new int [nt];
        int [] rank = new int[nt];
        for(int i = 0;i<nt;i++)
        {

            tree[i] = i;
            rank[i] = scanner.nextInt();
        }
        int max =GetMax(rank);
        for(int i = 0;i<m;i++)
        {
            int a =scanner.nextInt()-1;
            int b =scanner.nextInt()-1;
            max =union(a,b,tree,rank,max);
            System.out.println(max);
        }
    }
    private static int union(int destination, int source,int[] mas,int[] rank,int max)
    {
        int destinationID = find(destination,mas);
        int sourceID = find(source,mas);

        if (destinationID == sourceID)
        {
            return max;
        }
        mas[sourceID] = destinationID;
        rank[destinationID] += rank[sourceID];
        if(rank[destinationID]>max) max=rank[destinationID];
        return max;
    }
    private static int find(int i,int[] parent)
    {
        while (i!=parent[i])
        {
            i= parent[i];
        }
        return i;
    }
    private static int GetMax(int[] m)
    {
       int max =m[0];
       for(int i = 1;i<m.length;i++)
       {
           if(m[i]>max)max= m[i];
       }
       return max;
    }
}
