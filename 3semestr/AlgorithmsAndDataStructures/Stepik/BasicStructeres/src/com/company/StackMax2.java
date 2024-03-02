package com.company;

import java.util.Scanner;
import java.util.Stack;

public class StackMax2 {
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        Stack<StackValue> maxstack =new Stack<>();
        int requestCount = scanner.nextInt();
        for(int i = 0;i<requestCount;i++)
        {
            String request = scanner.next();
            switch (request){
                case "push"->
                {
                    if(maxstack.isEmpty()) {
                        int n = scanner.nextInt();
                        maxstack.push(new StackValue(n,n));
                    }
                    else
                    {
                        int n = scanner.nextInt();
                        if(n>maxstack.peek().max)
                        {
                            maxstack.push(new StackValue(n,n));
                        }
                        else
                        {
                            maxstack.push(new StackValue(n,maxstack.peek().max));
                        }
                    }
                }
                case  "pop"->
                {
                    maxstack.pop();
                }
                case "max"->
                {
                    if(maxstack.isEmpty()) System.out.println(0);
                    else System.out.println(maxstack.peek().max);
                }

            }
        }
    }
     static class StackValue{
        int value;
        int max;
        StackValue(int value,int max)
        {
            this.value = value;
            this.max = max;
        }
    }
}
