package com.company;

import java.io.BufferedReader;
import java.util.Scanner;

import static java.lang.Integer.valueOf;
import static java.lang.Math.abs;


public class SumRequests {
    static tree p;
    public static long s = 0;
    static int DIVIDER = 1_000_000_001;



    public static void main(String[] args)
    {
      /*  tree checktree = new tree(1);
        checktree =tree.insert(checktree,2);
        checktree =tree.insert(checktree,3);
        checktree =tree.insert(checktree,4);
        tree[] mas = new tree[2];

        checktree =tree.insert(checktree,5);
        checktree =tree.insert(checktree,6);
        checktree =tree.insert(checktree,7);
        checktree =tree.insert(checktree,8);
        checktree =tree.insert(checktree,9);
        checktree = tree.insert(checktree,10);
        mas = tree.Split(checktree,4);
        tree n = tree.Merge(mas[0],mas[1]);
        n = tree.GetSum(4,7,n);
        */
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        String str  = scanner.nextLine();
        for(int i =0;i<n;i++)
        {

            str=scanner.next();

            switch (str){
                case "+"->{
                    long num = scanner.nextInt();
                    num = (num+s)%DIVIDER;
                       p=tree.insert(p,num);
                }
                case "-"->{
                    long num = scanner.nextInt();
                    num = (num+s)%DIVIDER;
                    p = tree.remove(p,num);
                }
                case "?"->{
                    long num = scanner.nextInt();
                    num  =  (num+s)%DIVIDER;
                    tree.find(p,num);
                }
                case "s"->{
                    long l =scanner.nextInt();
                    long r = scanner.nextInt();
                    long fl = f(l);
                    long fr =f(r);
                    if(fl > fr)
                    {
                        System.out.println(0);
                        s =0;
                    }
                    else
                    {
                        p = tree.GetSum(fl,fr,p);
                    }

                }
            }
        }
    }

    private static long f(long num)
    {
        return (num+s)%DIVIDER;
    }
   static  class tree {
       long key;
       tree left;
       tree right;
       long sum;
       int height;

       tree(long k) {
           this.key = k;
           this.sum = k;
           this.left = null;
           this.right = null;
           this.height = 1;
       }

       private static int height(tree p) {
           if (p == null) {
               return 0;
           } else return p.height;
       }

       private static tree Merge(tree v1, tree v2)
       {
           tree T;
            if(v1 != null) {
                T= Max(v1);
                v1 = tree.remove(v1, T.key);
                T = AVLMergeWithRoot(v1,v2,T);
            }
            else {
                T= v2;
            }
            return T;
       }
       private static tree Max(tree p)
       {
           if (p.right !=null){
               return Max(p.right);
           }
           else return p;
       }
        private static int bfactor(tree p)
        {
            return height(p.right)-height(p.left);
        }
        private static void fixheight(tree p)
        {
            int lh=height(p.left);
            int rh = height(p.right);
            p.height = (lh>rh?lh:rh)+1;
        }
        public static void find(tree p,long k)
        {
            while (p!= null &&  p.key!=k)
            {
                if(k>p.key) p = p.right;
                else p = p.left;
            }
            if(p == null) System.out.println("Not found");
            else System.out.println("Found");
        }
        private static tree GetSum(long l,long r,tree p)
        {
            tree[] mas1  =new tree[2];
            mas1= tree.Split(p,l-1);
            tree[] mas2 = new tree[2];
            mas2 = tree.Split(mas1[1],r);
            if(mas2[0]!=null) {
                System.out.println(mas2[0].sum);
                s = mas2[0].sum;
            }else{
                System.out.println(0);
                s =0;
            }
            tree n =tree.Merge(mas2[0],mas2[1]);
            n = tree.Merge( mas1[0],n);
            if(n!=null) n = balance(n);
            return n;
        }
        private static tree rotateright(tree p)
        {
            tree q = p.left;
            p.left = q.right;
            q.right = p;
            fixheight(p);
            fixheight(q);
            return q;
        }
        private static tree MergeWithRoot(tree v1, tree v2, tree  T)
        {
            T.left = v1;
            T.right = v2;
            return T;
        }
        private static tree AVLMergeWithRoot(tree v1,tree v2,tree T){

            if(v1==null || v2==null || abs(v1.height -v2.height)<= 1)
            {
                MergeWithRoot(v1,v2,T);
                return balance(T);
            }
            else if(v1.height > v2.height)
            {
                tree T1 = AVLMergeWithRoot(v1.right,v2,T);
                v1.right =T1;
                return balance(v1);
            }
            else
            {
                tree T1 = AVLMergeWithRoot(v2.left,v1,T);
                v2.left = T1;
                return balance(v2);
            }

       }
       private static tree[] Split(tree v,long key)
       {
           if (v == null) return new tree[]{null,null};

           if(v.key > key)
           {
               tree[] mas =new tree[2];

               mas =tree.Split(v.left,key);
               tree v2 = AVLMergeWithRoot(mas[1],v.right,v);
               return new tree[]{mas[0],v2};
           }
           else
           {
               tree[] mas = new tree[2];
               mas = tree.Split(v.right,key);
               tree v2 =AVLMergeWithRoot(v.left,mas[0],v);
               return new tree[]{v2,mas[1]};
           }
       }

        private static tree rotateleft(tree q)
        {
            tree p = q.right;
            q.right = p.left;
            p.left = q;
            fixheight(q);
            fixheight(p);
            return p;
        }
        private static tree balance(tree p)
        {
            fixheight(p);
            if( bfactor(p)==2 )
            {
                if( bfactor(p.right) < 0 )
                    p.right = rotateright(p.right);
                p = rotateleft(p);
            }
            if( bfactor(p)==-2 )
            {
                if( bfactor(p.left) > 0  )
                    p.left = rotateleft(p.left);
                p = rotateright(p);
            }
            FixSum(p);
            return p;
        }
        public static long FixSum(tree p)
        {
            if(p == null) return 0;
            long a =FixSum(p.right);
            long b = FixSum(p.left);
            return p.sum = p.key+a+b;
        }
        public static tree insert(tree p,long k)
        {
            if(p==null) return new tree(k);
            if(k<p.key) {
                p.left = insert(p.left, k);

            }
            else if(k>p.key) {
                p.right = insert(p.right, k);

            }
            return balance(p);
        }
        public static tree findmin(tree p)
        {
            if(p.left == null) return p;
            else return findmin(p.left);
        }
        public static tree removemin(tree p)
        {
            if(p.left == null) return p.right;
            p.left = removemin(p.left);
            return balance(p);
        }
        public static tree remove(tree p,long k)
        {
            if( p==null ) return null;
            if( k < p.key ) {
                p.left = remove(p.left, k);

            }
                else if( k > p.key ) {
                p.right = remove(p.right, k);

                }
                else //  k == p->key
            {
                tree q = p.left;
                tree r = p.right;
                if( r==null ) return q;
                tree min = findmin(r);
                min.right = removemin(r);
                min.left = q;
                return balance(min);
            }
            return balance(p);
        }

   }
    static class data{
        long sum;
        data(long sum)
        {
            this.sum = sum;
        }
    }
}

