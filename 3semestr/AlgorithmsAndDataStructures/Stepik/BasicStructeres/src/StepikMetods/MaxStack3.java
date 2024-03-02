package StepikMetods;

import com.company.StackMax2;

import java.util.Scanner;
import java.util.Stack;

public class MaxStack3 {
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        MaxStack stack = new MaxStack();
        int requestCount = scanner.nextInt();
        for(int i = 0;i<requestCount;i++)
        {
            String request = scanner.next();
            switch (request){
                case "push"->
                {
                   stack.Push(scanner.nextInt());
                }
                case  "pop"->
                {
                    stack.Pop();
                }
                case "max"->
                {
                    if(stack.IsEmpty()) System.out.println("0");
                    else System.out.println(stack.Max());
                }

            }
        }
    }
    static class MaxStack{
        Stack<Integer> stack;
        Stack<Integer> max;
        MaxStack()
        {
            this.stack =  new Stack();
            this.max = new Stack();
        }
        private void Push(int value)
        {

            if(this.stack.isEmpty())
            {
                this.max.push(value);
            }
            else {
                if (value > this.max.peek()) {
                    this.max.push(value);
                } else this.max.push(this.max.peek());
            }
            this.stack.push(value);
        }
        private void Pop()
        {
            this.stack.pop();
            this.max.pop();
        }
        private int Max()
        {
            return this.max.peek();
        }
        private boolean IsEmpty()
        {
            if(this.stack.isEmpty()) return true;
            else return false;
        }
    }
}
