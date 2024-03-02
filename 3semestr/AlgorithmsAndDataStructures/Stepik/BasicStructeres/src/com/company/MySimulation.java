package com.company;

import java.util.ArrayDeque;
import java.util.Scanner;

public class MySimulation {
    private static ArrayDeque<packagerecord> packagebuffer;
    private static int buffersize;
    private static int processfreetime = 0;
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        buffersize = scanner.nextInt();
        packagebuffer= new ArrayDeque<>();
        int n= scanner.nextInt();
        for(int i = 0;i<n;i++)
        {
            packagerecord currpackage = new packagerecord(scanner.nextInt(),scanner.nextInt());
            if (packagebuffer.size()<buffersize)
            {
                if(processfreetime< currpackage.arrival)
                {
                    processfreetime = currpackage.arrival;
                }
                System.out.println(processfreetime);
                processfreetime +=currpackage.duration;
                currpackage.endtime= processfreetime;
                packagebuffer.add(currpackage);
            }
            else if(packagebuffer.size()==buffersize)
            {
                if( packagebuffer.getFirst().endtime<=currpackage.arrival)
                {
                    if(processfreetime< currpackage.arrival)
                    {
                        processfreetime = currpackage.arrival;
                    }
                    packagebuffer.removeFirst();
                    System.out.println(processfreetime);
                    processfreetime +=currpackage.duration;
                    currpackage.endtime= processfreetime;
                    packagebuffer.add(currpackage);
                }
                else System.out.println("-1");
            }
            else{
                System.out.println("-1");
            }
        }
    }
    static class packagerecord{
        int arrival;
        int duration;
        int endtime;
        packagerecord(int a, int b)
        {
            this.arrival = a;
            this.duration =b;
        }
    }
}
