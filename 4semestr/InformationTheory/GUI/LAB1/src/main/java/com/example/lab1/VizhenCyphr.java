package com.example.lab1;

public class VizhenCyphr {
    String plaintext ="";
    String cyphertext="";
    private static final int n =33;

    private final static String Alphabet ="абвгдеёжзийклмнопрстуфхцчшщъыьэюя";


    String key;
    String strKey;
    VizhenCyphr(String plaintext,String cyphertext, String key)
    {
        this.key =key;
        this.cyphertext =cyphertext;
        this.plaintext =plaintext;

    }
    private boolean checkKey()
    {
        if(this.strKey == "")
        {

            return HelloController.isValid =false;
        }
        else return HelloController.isValid =true;
    }


    public String getCypher()
    {
        this.getkeyStr();
        if(this.checkKey()) {
            boolean isValid = false;
            StringBuilder sb = new StringBuilder();
            char[] strP = this.plaintext.toCharArray();
            char[] strK = this.strKey.toCharArray();
            int j = 0;
            for (int i = 0; i < strP.length; i++) {
                if (Alphabet.contains(String.valueOf(strP[i]))) {
                    sb.append(Alphabet.charAt(((Alphabet.indexOf(strP[i]) + Alphabet.indexOf(strK[j])) % n)));//(
                    j++;
                    isValid = true;
                } //else sb.append(strP[i]);
            }
            if (isValid == false) {
                HelloController.isValid = false;
                return "";
            } else {
                return sb.toString();
            }
        }else return "";
    }
    public String getDecypher()
    {
        this.getkeyStr();
        if(this.checkKey()) {
            StringBuilder sb = new StringBuilder();
            boolean isValid =false;
            char[] strC = this.cyphertext.toCharArray();
            char[] strK = this.strKey.toCharArray();
            int j = 0;
            for (int i = 0; i < strC.length; i++) {
                if (Alphabet.contains(String.valueOf(strC[i]))) {
                    sb.append(Alphabet.charAt(((Alphabet.indexOf(strC[i]) + n - Alphabet.indexOf(strK[j])) % n)));
                    j++;
                    isValid =true;
                } //else sb.append(strC[i]);
            }
            if(isValid) {
                return sb.toString();
            }
            else{
                HelloController.isValid =false;
                return "";
            }
        }
        else return "";
    }
    private void  getkeyStr()
    {
        StringBuilder sb = new StringBuilder();
        StringBuilder keys =new StringBuilder();
        char[] keyarr =key.toCharArray();
        for(int i = 0;i<keyarr.length;i++)
        {

            if(Alphabet.contains(String.valueOf(keyarr[i])))
            {
                sb.append(this.key.charAt(i));
            }
        }
        this.key = sb.toString();
        if(this.key.length() != 0) {
            for (int i = 0; i < (this.plaintext.length() + this.cyphertext.length()); i++) {
                keys.append(this.key.charAt(i % this.key.length()));
            }
            this.strKey = keys.toString();
        } else this.strKey = "";
    }
}
