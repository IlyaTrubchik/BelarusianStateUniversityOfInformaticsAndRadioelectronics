package com.company;
import java.util.Scanner;

public class MasInMinHeap {

    public static void main (String[]args)
    {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = 0;
        int[] mas =new int[n];
        StringBuilder str =new StringBuilder();
        for(int i = 0;i<n;i++)
        {
            mas[i] = scanner.nextInt();
        }
        int k=n/2;
        for(int i =k-1;i>=0;i--)
        {
            m =m+siftdown(i,mas,str);
        }
        System.out.println(m);
        System.out.println(str.toString());
    }

    private static int siftdown(int index,int[] mas,StringBuilder str)
    {
        int left = 2*index+1;
        int right = 2*index+2;
        int parent =index;
        int o= 0;
        while ((left<=mas.length-1 && mas[parent]>mas[left]) || (right<=mas.length-1 && mas[parent]>mas[right]))
        {
            if((right<=mas.length-1 && mas[left]<mas[right]))
            {
                int tmp;
                str.append(parent);
                str.append(" ");
                str.append(left);
                str.append("\n");
                tmp = mas[parent];
                mas[parent] = mas[left];
                mas[left] =tmp;
                parent = left;
                left = parent*2+1;
                right = parent*2+2;
                o++;
            }
            else if((right<=mas.length-1 && mas[left]>mas[right]))
            {
                int tmp;
                str.append(parent);
                str.append(" ");
                str.append(right);
                str.append("\n");
                tmp = mas[parent];
                mas[parent] = mas[right];
                mas[right] =tmp;
                parent = right;
                left = parent*2+1;
                right = parent*2+2;
                o++;
            }
            else {
                int tmp;
                str.append(parent);
                str.append(" ");
                str.append(left);
                str.append("\n");
                tmp = mas[parent];
                mas[parent] = mas[left];
                mas[left] =tmp;
                parent = left;
                left = parent*2+1;
                right = parent*2+2;
                o++;
            }
        }
        return o;
    }
}