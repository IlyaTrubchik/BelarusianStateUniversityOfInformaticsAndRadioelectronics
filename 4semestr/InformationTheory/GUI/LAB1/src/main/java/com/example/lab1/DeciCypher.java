package com.example.lab1;

public class DeciCypher {
    String plaintext ="";
    String cyphertext="";

    int key;

    private static final int n = 26;
    private boolean checkKey() {
        if(this.key <= 0)
        {
            HelloController.isValid =false;
            return false;
        }
        if(this.key == n) {
            HelloController.isValid =false;
            return false;
        }
        if (this.key < n)
        {
            for(int i = 2;i<=this.key;i++)
            {
                if (this.key % i == 0 && n%i == 0)
                {
                    HelloController.isValid =false;
                    return false;
                }
            }
            HelloController.isValid =true;
            return  true;
        }
        for(int i = 2;i<=n;i++)
        {
            if(this.key % i ==0 && n%i == 0)
            {
                HelloController.isValid =false;
                return false;
            }
        }
        HelloController.isValid =true;
        return true;
    }
    public String getCypher()
    {
        StringBuilder cyphertxt = new StringBuilder();
         if(this.checkKey()) {
            char[] str = this.plaintext.toCharArray();
            for(int i = 0;i<str.length;i++)
            {
                char symb =str[i];
                if(str[i]>='a' && str[i]<='z') {
                   symb = (char) ((((str[i] - 'a') * this.key) % n)+('a'));
                   cyphertxt.append(symb);
                }

            }
             return cyphertxt.toString();
       }
       else return "";

    }
    public String decypher()
    {
        StringBuilder plaintxt = new StringBuilder();

        if(this.checkKey()) {

            char[] str = this.cyphertext.toCharArray();
            for(int i = 0;i<str.length;i++)
            {
                int b = 0;
                char symb =str[i];
                if(str[i]>='a' && str[i]<='z') {
                    while((26*b+str[i]-'a')%this.key !=0)
                    {
                        b++;
                    }
                    symb = (char)(((26*b+str[i]-'a')/this.key)+'a');//26*b+str[i] -'a'== (i*k)
                    plaintxt.append(symb);
                }

            }
            return plaintxt.toString();
        }
        else return "";

    }
    DeciCypher(String cyphertxt,String plaintxt,int key)
    {
        this.key = key;
        this.cyphertext =cyphertxt;
        this.plaintext = plaintxt;
    }
}
