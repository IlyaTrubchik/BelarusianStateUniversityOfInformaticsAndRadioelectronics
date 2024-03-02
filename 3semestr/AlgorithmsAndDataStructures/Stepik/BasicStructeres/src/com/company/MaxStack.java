package com.company;

import java.util.Scanner;

public class MaxStack {
     static maxStack output =null;
    public static void main(String[] args)
    {
        Scanner scanner= new Scanner(System.in);
        int n =scanner.nextInt();
        int[] mas =new int[n];
        for(int i = 0;i<n;i++)
        {
            mas[i] =scanner.nextInt();
        }
        int wsize =scanner.nextInt();
        maxStack input = null;

        StringBuilder sb= new StringBuilder();
        for(int i =0;i<wsize;i++)
        {
            input = maxStack.add(mas[i],input);
        }
        int i = wsize;
        while(i<=n)
        {
            if(output == null)
            {
                for(int j=0;j<wsize;j++)
                {
                    input =maxStack.pop(input);
                }
            }
            else
            {
                int m1 = maxStack.SeekMax(input);
                int m2 = maxStack.SeekMax(output);
                if(m2>m1){
                    sb.append(m2);
                    sb.append(" ");
                }
                else
                {
                    sb.append(m1);
                    sb.append(" ");
                }
                if(i<n) {
                    input = maxStack.add(mas[i], input);
                    output = output.remove(output);
                }
                i++;
            }
        }
        System.out.println(sb.toString());
    }
    static class maxStack
    {
        int value;
        int max;
        maxStack next;
        maxStack(int value)
       {
            this.max=value;
            this.next =null;
            this.value=value;
       }

       public static  maxStack add(int value,maxStack top)
       {
           if(top==null)
           {
               return new maxStack(value);
           }
           else
           {
               if(top.max<value)
               {
                   maxStack tmp = new maxStack(value);
                   tmp.next =top;
                   return tmp;
               }
               else
               {
                   maxStack tmp = new maxStack(value);
                   tmp.max = top.max;
                   tmp.next = top;
                   return tmp;
               }
           }
       }
       public static maxStack pop (maxStack top)
       {
           if(top!=null)
           {
              output=output.add(top.value,output);
              return top.next;
           }
           return null;
       }
       private static int SeekMax(maxStack top)
       {
           if (top==null)
               return 0;
           else return top.max;
       }
       private  maxStack remove(maxStack top)
       {
           if(top == null)
               return null;
           else return top.next;
       }
    }
}
