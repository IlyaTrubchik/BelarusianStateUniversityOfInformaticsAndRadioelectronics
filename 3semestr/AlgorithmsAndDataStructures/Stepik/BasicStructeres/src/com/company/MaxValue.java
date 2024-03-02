package com.company;

import java.util.Scanner;

public class MaxValue {
    public static void main(String[] args)
    {
        Scanner scanner  =  new Scanner(System.in);
        int n = scanner.nextInt();
        int[] mas = new int[n];
        for(int i = 0 ;i<n;i++)
        {
            mas[i]=scanner.nextInt();
        }
        int m = scanner.nextInt();

        int left = 0;
        int right = m-1;
        int MaxValueIndex=-1;
        StringBuilder result = new StringBuilder();
        if(m==n)
        {
            getMaxValueIndex(mas,0,n-1);
            result.append(mas[MaxValueIndex]);
        }
        else if(m==1)
        {
            for(int i = 0;i<n;i++)
            {
                result.append(mas[i]);
                result.append(" ");
            }
        }
        else{
            while (right<n)
            {
                if(left>MaxValueIndex)
                {
                    MaxValueIndex =getMaxValueIndex(mas,left,right);
                }
                if(mas[right]>mas[MaxValueIndex])
                {
                    MaxValueIndex = right;
                }
                result.append(mas[MaxValueIndex]);
                result.append(" ");
                left++;
                right++;
            }
        }
        System.out.println(result.toString());

    }
    private static int getMaxValueIndex(int[] values, int startPos, int endPos)
    {
        int maxValueIndex = startPos;
        for (int i = startPos; i <= endPos; i++)
        {
            maxValueIndex = values[i] > values[maxValueIndex] ? i : maxValueIndex;
        }
        return maxValueIndex;
    }
}
