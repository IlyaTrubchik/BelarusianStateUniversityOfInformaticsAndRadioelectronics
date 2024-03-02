package com.company;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Scanner;

public class StringInTxt2 {
    private static int BASE=50;
    private static int DIVIDER= 999;

    private static long substr_hashcode=0;
    private static int[] powers;
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        String pattern  =scanner.nextLine();
        String text = scanner.nextLine();
        powers =new int[pattern.length()];
        fillPowers();
        long mainhashcode = Patternhashcode(pattern);
        StringBuilder result  = new StringBuilder();
        ArrayDeque m=  new ArrayDeque();
        for(int i = text.length()-pattern.length();i>=0;i--)
        {
            substr_hashcode = gethashcode(text,pattern,i);
            if(substr_hashcode == mainhashcode)
            {
                if(text.startsWith(pattern,i)) {
                    m.addFirst(i);
                }
            }
        }
        while(!m.isEmpty())
        {
            result.append(m.removeFirst());
            result.append(" ");
        }
        System.out.println(result.toString());
    }
    private static void fillPowers(){
        int x = 1;
        for(int i = 0;i<powers.length;i++)
        {
            powers[i] = x;
            x=(x*BASE)%DIVIDER;

        }
    }
    public static long Patternhashcode(String pattern)
    {
        long hashcode =0;
        for(int i = 0;i<pattern.length();i++)
        {
            hashcode =((hashcode+(pattern.charAt(i)*powers[i])%DIVIDER)+DIVIDER)%DIVIDER;
        }
        return hashcode;
    }
    public static long gethashcode(String text,String pattern,int i)
    {
        long hashcode = 0;
        if(i == text.length()-pattern.length()){
            int k = 0;
            for(int j = i;j<text.length();k++,j++)
            {
                hashcode =((hashcode+(text.charAt(j)*powers[k])%DIVIDER)+DIVIDER)%DIVIDER;
            }
            return hashcode;
        }
        else
        {
            hashcode = substr_hashcode;
            hashcode = ((hashcode - (text.charAt(i+pattern.length())*powers[powers.length-1])%DIVIDER)+DIVIDER)%DIVIDER;
            hashcode = ((hashcode*BASE)+ text.charAt(i))%DIVIDER;
            return hashcode;
        }
    }
}
