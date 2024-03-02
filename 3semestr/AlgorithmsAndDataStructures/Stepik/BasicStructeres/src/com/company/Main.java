package com.company;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner stdin = new Scanner(System.in);
        String str = stdin.nextLine();

        int n = CheckStr(str);
        if (n == 0) {
            System.out.println("Success");
        } else System.out.print(n);
    }

    public static int CheckStr(String str) {
        char[] Symbols = str.toCharArray();
        Stack<Character> st = new Stack<>();
        int counter = 0;
        LinkedList<Integer> opened = new LinkedList<>();
        for (char s : Symbols) {

            if (s == '(' || s == '[' || s == '{') {
                st.push(s);
                counter++;
                opened.addLast(counter);
            } else {
                counter++;
                if (s == ')' || s == ']' || s == '}') {
                    if (st.empty()) return counter;
                    char top = st.pop();
                    if (top == '[' && s != ']') {
                        return counter;
                    } else if (top == '{' && s != '}') {

                        return counter;
                    } else if (top == '(' && s != ')') {
                        return counter;
                    }

                    opened.removeLast();
                }
            }
        }
        if (st.empty()) {
            return 0;
        } else {
            return opened.getFirst();
        }
    }
}
