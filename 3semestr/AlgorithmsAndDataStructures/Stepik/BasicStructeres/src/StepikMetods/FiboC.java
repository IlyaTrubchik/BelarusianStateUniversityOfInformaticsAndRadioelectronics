package StepikMetods;

import java.util.ArrayList;
import java.util.Scanner;

public class FiboC {
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextLong();
        int m = scanner.nextInt();
        ArrayList<Long> mas = new ArrayList();
        mas.add(0l);
        mas.add(1l);
        int i = 2;
        while (((mas.get(i-1)!=1  || mas.get(i-2) != 0)||(i==2)) && i<=m*m+1){
            mas.add((mas.get(i-1)+mas.get(i-2))%m);
            i++;
        }
        long res =0;
        if(i<=n) res =mas.get((int)(n%(i-2))); else res =n%m;
        System.out.println(res);
    }

}