package com.company;

import java.util.ArrayDeque;
import java.util.Scanner;

public class Simulation {
    private static int next_processortime = 0;
    private static ArrayDeque<package_record> package_array;
    private static int buffer_size;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        buffer_size = scanner.nextInt();
        package_array = new ArrayDeque<>(buffer_size);
        int Package_Count = scanner.nextInt();

        if (Package_Count == 0)
        {
            System.out.println("");
        }
        else if(Package_Count == 1)
        {
            System.out.println(scanner.nextInt());
        }
        else
        {
            for(int i = 0;i<Package_Count;i++)
            {
                package_record pack;
                pack =  new package_record(scanner.nextInt(),scanner.nextInt());
                if(package_array.size()<buffer_size)
                {
                    if(pack.start_time> next_processortime)
                    {
                        next_processortime = pack.start_time;
                    }
                    System.out.println(next_processortime);
                    pack.end_time =next_processortime+pack.duration;
                    package_array.add(pack);

                }
                else if(pack.start_time >= package_array.getFirst().end_time)
                {
                    package_array.removeFirst();
                    if(pack.start_time> next_processortime)
                    {
                        next_processortime = pack.start_time;
                    }
                    System.out.println(next_processortime);
                    pack.end_time =next_processortime+pack.duration;
                    package_array.add(pack);
                }
                else
                {
                    System.out.println("-1");
                }
            }
        }

    }

    private static class package_record
    {
        int duration;
        int start_time;
        int end_time;

        package_record(int start,int duration)
        {
            this.duration = duration;
            this.start_time =start;
        }
    }
}
