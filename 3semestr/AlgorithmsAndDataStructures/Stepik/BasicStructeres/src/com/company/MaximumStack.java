package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;
import java.util.StringTokenizer;

public class MaximumStack {
    public static void main(String[] args) throws IOException {
        Stack<Integer> stack= new Stack<>();
        int maximum;
        MyReader reader = new MyReader(new InputStreamReader(System.in));
        int count = reader.nextInt();
        maximum= 0;

        for(int i=0;i<count;i++){
            String operation = reader.next();
            switch (operation){
                case "pop"->{
                    stack.pop();
                }
                case "max"->{
                    if(stack.empty()) {
                        System.out.println("Stack is empty");
                    }
                    else if(stack.empty())
                    {
                        System.out.println("0");
                    }
                    else
                    System.out.println(stack.peek());
                }
                default ->{
                    int n =reader.nextInt();
                    if(stack.empty())
                    {
                        stack.push(n);
                    }
                    else
                    stack.push(Math.max(stack.peek(),n));
                }
            }
        }
    }
    static class MyReader
    {
        private final BufferedReader reader;
        private StringTokenizer tokenizer = null;

        MyReader(Reader r)
        {
            reader = new BufferedReader(r);
        }

        int nextInt() throws IOException
        {
            return Integer.parseInt(next());
        }

        String next() throws IOException
        {
            while (tokenizer == null || !tokenizer.hasMoreTokens())
            {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }
    }
}

