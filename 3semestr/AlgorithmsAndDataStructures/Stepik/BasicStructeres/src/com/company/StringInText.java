package com.company;

import java.util.Scanner;

public class StringInText {
    private static int DIVIDER = 999;
    private static int BASE = 50;
    private static int powers[];

    private static long substr_hashcode;
    private static void fillPowers(String pattern)
    {
        int x = 1;
        powers =new int[pattern.length()];
        for(int i = 0;i< pattern.length();i++)
        {
            x = (x*BASE)%DIVIDER;
            powers[i] =x;
        }
    }
    private static long HashCode(String str,String pattern){
        long x = 1;
        long result  = 0;
        char[] arr =str.toCharArray();
        for(int i=0;i<pattern.length();i++)
        {
            result = (((result + (arr[i] * powers[powers.length-1-i])) % DIVIDER) + DIVIDER) % DIVIDER;
        }
        return result;
    }
    private static long TextHashCode(String pattern,String text,int i)
    {
        long hashcode =0;
        hashcode =substr_hashcode;
        char [] arr =text.toCharArray();
        hashcode=((hashcode - (arr[i-1] * powers[powers.length-1])%DIVIDER) + DIVIDER) % DIVIDER;
        hashcode = (hashcode*BASE+text.charAt(i+pattern.length()-1))%DIVIDER;
        return  hashcode;
    }
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String pattern = scanner.nextLine();
        String text= scanner.nextLine();
        fillPowers(pattern);
        int i =1;
        substr_hashcode = HashCode(text,pattern);
        long patternhash = HashCode(pattern,pattern);
        StringBuilder sb = new StringBuilder();
        while(i!= text.length()-pattern.length()+2)
        {
            if(substr_hashcode == patternhash)
            {
                sb.append(i-1);
                sb.append(" ");
            }
            if(i != text.length()- pattern.length()+1)
            substr_hashcode =TextHashCode(pattern,text,i);
            i++;
        }
        System.out.println(sb.toString());
    }
}
