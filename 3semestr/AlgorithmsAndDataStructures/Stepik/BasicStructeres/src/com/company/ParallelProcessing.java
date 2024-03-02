package com.company;

import java.util.PriorityQueue;
import java.util.Scanner;

public class ParallelProcessing {
    public static void main(String[] args)
    {

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        long t[] = new long[m];
        for(int  i=0;i<m;i++)
        {
            t[i] = scanner.nextInt();
        }
        Processor_record[] processors = new Processor_record[n];
        for(int i = 0;i<n;i++)
        {
            Processor_record p = new Processor_record(i,0);
            processors[i] = p;
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i<m;i++)
        {
            Processor_record p = processors[0];
            sb.append(p.id);
            sb.append(" ");
            sb.append(p.freetime);
            sb.append("\n");
            processors[0].freetime +=t[i];
            siftdown(processors,processors.length);
        }
        System.out.println(sb.toString());
    }
    private static void siftdown(Processor_record[] mas,int size)
    {
        int left = 1;
        int right = 2;
        int parent =0;
        while ((left<=size-1 && mas[parent].freetime>mas[left].freetime) || (right<=size-1 && mas[parent].freetime>mas[right].freetime)|| (right<=size-1 && mas[parent].freetime==mas[right].freetime) || (left<=size-1 && mas[parent].freetime==mas[left].freetime) )
        {
            if(right<=size-1 && ((mas[left].freetime<mas[right].freetime) || (mas[left].freetime==mas[right].freetime &&  mas[left].id<mas[right].id)))
            {
                Processor_record tmp;
                tmp = mas[parent];
                mas[parent] = mas[left];
                mas[left] =tmp;
                parent = left;
                left = parent*2+1;
                right = parent*2+2;

            }
            else if(right<=size-1 && ((mas[left].freetime>mas[right].freetime) || (mas[left].freetime==mas[right].freetime &&  mas[left].id>mas[right].id)))
            {
                Processor_record tmp;
                tmp = mas[parent];
                mas[parent] = mas[right];
                mas[right] =tmp;
                parent = right;
                left = parent*2+1;
                right = parent*2+2;
            }
            else {
                Processor_record tmp;
                tmp = mas[parent];
                mas[parent] = mas[left];
                mas[left] =tmp;
                parent = left;
                left = parent*2+1;
                right = parent*2+2;
            }
        }

    }
    private static class Processor_record
    {
        int id;
        long freetime;
        Processor_record(int a,long b)
        {
            this.id = a;
            this.freetime =b;
        }
    }
}
