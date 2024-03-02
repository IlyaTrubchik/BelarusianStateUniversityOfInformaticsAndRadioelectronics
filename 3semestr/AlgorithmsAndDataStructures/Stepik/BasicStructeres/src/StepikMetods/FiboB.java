package StepikMetods;

import java.util.Scanner;

public class FiboB {
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] mas = new int[n+1];
        mas[0] =0;
        mas[1] =1;

        for(int i =2;i<=n;i++)
        {
            mas[i]=(mas[i-1]+mas[i-2])%10;
        }
        System.out.println(mas[n]);
    }

}
