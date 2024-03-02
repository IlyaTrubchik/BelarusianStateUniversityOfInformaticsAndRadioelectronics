package com.company;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import static java.lang.Integer.valueOf;

public class HashTableWithCep {
    public static void  main(String[] args)
    {
        Scanner scanner =new Scanner(System.in);
        int m = scanner.nextInt();
        HashTable a = new HashTable(m);
        int n = scanner.nextInt();
        for(int i = 0;i<=n;i++)
        {
            String str = scanner.nextLine();
            StringBuilder command= new StringBuilder();
            int count =0;
            int pos=0;
            for(int j=0;j<str.length();j++)
            {
                if(count == 0 && str.charAt(j)!=' '){
                    command.append(str.charAt(j));
                }
                else if(count == 0 && str.charAt(j) == ' ')
                {
                    pos = j;
                    count++;
                }
            }
            switch(command.toString())
            {
                case "add"->{
                    StringBuilder STRING =new StringBuilder();
                    for(int j=pos+1;j<str.length();j++)
                    {
                        STRING.append(str.charAt(j));
                    }
                    String value = STRING.toString();
                    a.AddString(value);
                }
                case "find"->{
                    StringBuilder STRING =new StringBuilder();
                    for(int j=pos+1;j<str.length();j++)
                    {
                        STRING.append(str.charAt(j));
                    }
                    String value = STRING.toString();
                    a.FindString(value);
                }
                case "del"->{
                    StringBuilder STRING =new StringBuilder();
                    for(int j=pos+1;j<str.length();j++)
                    {
                        STRING.append(str.charAt(j));
                    }
                    String value = STRING.toString();
                    a.DelString(value);
                }
                case "check"->{
                    StringBuilder STRING =new StringBuilder();
                    for(int j=pos+1;j<str.length();j++)
                    {
                        STRING.append(str.charAt(j));
                    }
                    String value = STRING.toString();
                    int val =valueOf(value);
                    a.check(val);
                }
            }
        }

    }

    static class HashTable{
        private LinkedList[] array;
        HashTable(int m)
        {
            this.array = new LinkedList[m];
            for(int i=0;i<m;i++)
            {
                this.array[i] = new LinkedList();
            }
        }
        int  p=1000000007;
        private long HashCode(String str){
            long x = 1;
            long result  = 0;
            char[] arr =str.toCharArray();
            for(int i=0;i<arr.length;i++)
            {
                result = (((result + (arr[i] * pow(i))) % 1000000007) + 1000000007) % 1000000007;
            }
            result = result%p;
            result = result%(this.array.length);
            return result;
        }
        private long pow(int i)
        {
            long result =1;
            for(int j=0;j<i;j++)
            {
                result=(result*263)%1000000007;
            }
            return result;
        }
        private boolean AddString(String str)
        {
            long index = HashCode(str);
            /*if(index>currsize)
            {
                LinkedList[] tmp = new LinkedList[(int)index+1];
                for(int i=0;i<currsize;i++)
                {
                    tmp[i]=array[i];
                }
                array =tmp;
                currsize =(int)index+1;
            }*/
            if(this.array[(int)index].isEmpty() || this.array[(int) index].contains(str)==false){
                this.array[(int)index].add(0,str);
                return true;
            }
            return true;
        }
        private boolean DelString(String str)
        {
            long index = HashCode(str);
            if(this.array[(int) index].contains(str)){
                this.array[(int)index].remove(str);
            }
            return true;
        }
        private boolean FindString(String str)
        {
            long index = HashCode(str);
            if(this.array[(int) index].contains(str))
            {
                System.out.println("yes");
                return true;
            }
            else
            {System.out.println("no");return false;}
        }
        private void check(int i)
        {
            if(this.array[i].isEmpty()) System.out.println("");
            else print(i);
        }
        private void print(int i)
        {
            Object[] j = this.array[i].toArray();
            StringBuilder result= new StringBuilder();
            for(int k=0;k< j.length;k++)
            {
                result.append((String)j[k]);
                result.append(" ");
            }
            System.out.println(result);
        }
    }
}
