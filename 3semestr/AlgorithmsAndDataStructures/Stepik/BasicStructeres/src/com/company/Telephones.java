package com.company;

import java.util.HashMap;
import java.util.Scanner;

public class Telephones {
    public static void main(String[] args)
    {
        HashMap<Integer,String> telbook =new HashMap<>();
        Scanner scanner =new Scanner(System.in);
        int n =scanner.nextInt();
        for(int j=0;j<=n;j++)
        {
            String str =scanner.nextLine();
            StringBuilder command =new StringBuilder();
            StringBuilder number = new StringBuilder();
            StringBuilder name = new StringBuilder();
            int count = 0;
            for(int i = 0;i<str.length();i++)
            {
                if(count == 0 && str.charAt(i)!=' ')
                {
                    command.append(str.charAt(i));
                }
                else if(count == 1 && str.charAt(i)!=' ')
                {
                    number.append(str.charAt(i));
                }
                else if(count == 2 && str.charAt(i)!=' ')
                {
                    name.append(str.charAt(i));
                }else count++;
            }
            switch (command.toString()){
                case "add"->{
                    if(telbook.isEmpty() || telbook.containsKey(number.toString().hashCode()) == false)
                    {
                        telbook.put(number.toString().hashCode(),name.toString());
                    }
                    else
                    {
                        telbook.replace(number.toString().hashCode(), name.toString());
                    }
                }
                case "find"->{
                    if(telbook.containsKey(number.toString().hashCode()))
                    {
                        System.out.println(telbook.get(number.toString().hashCode()));
                    }
                    else {
                        System.out.println("not found");
                    }
                }
                case "del"->{
                    if(telbook.containsKey(number.toString().hashCode()))
                    {
                        telbook.remove(number.toString().hashCode());
                    }
                }
            }
        }

    }
}
