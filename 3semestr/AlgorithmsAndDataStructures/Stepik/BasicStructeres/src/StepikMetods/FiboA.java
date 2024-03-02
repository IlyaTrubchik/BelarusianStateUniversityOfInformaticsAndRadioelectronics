package StepikMetods;

import java.util.Scanner;

public class FiboA {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int n =scanner.nextInt();
        System.out.println(Fibo(n));
    }
    public static long Fibo(int n)
    {
        if(n==0) return 0;
        else if(n==1) return 1;
        return Fibo(n-1)+Fibo(n-2);
    }
}
