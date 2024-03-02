package com.example.lab1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static List<Token> tok = new ArrayList<>();
    public void main(String[] args) {
        Scanner sc =new Scanner(System.in);
        System.out.println("Enter TypeScript code:");
        String code = sc.nextLine();
        ArrayList<String> codeArr  = new ArrayList<>();
        while (code != "")
        {
            codeArr.add(code);
            code = sc.nextLine();
        }
        init_tokens();
        StringBuilder toks = new StringBuilder();
        for(int i =0;i<codeArr.size();i++)
        {
            toks.append(tokenize(codeArr.get(i)));
        }
        //String toks = tokenize(code);
        System.out.println("Result:"+toks.toString());
        for(Token tk: tok){
            if(tk.get_count()>0){
                if( tk.getFunc()) System.out.println(tk.get_type()+" : "+tk.get_name()+" : "+tk.get_count()+" AS OPERATOR:" +tk.getOperatorCount() + " AS OPERAND" + tk.getOperandCount() );
                else System.out.println(tk.get_type()+" : "+tk.get_name()+" : "+tk.get_count());
            }
        }
    }
    public static String tokenize(String code){
        Token curTk = null;
        Token lastCorrectToken=null;
        Token prevToken =null;
        char[] code_arr=code.toCharArray();
        String part="";
        String long_part="";
        String token_res="";
        int last_correct_pos=1;
        int curr_pos=0;
        while(!Objects.equals(code, "")){
            while(curTk==null&&curr_pos!=code.length()){
                part=part.concat(String.valueOf(code_arr[curr_pos]));
                //token search(with maximal munch)
                curTk=correct_token(part);
                if(curTk==null){
                  curTk=reg(part);
                }
                if((curTk != null)){
                    long_part=part;
                    lastCorrectToken=curTk;
                    while(curTk!=null&&curr_pos+last_correct_pos<code_arr.length){
                        part=long_part;
                        lastCorrectToken=curTk;
                        long_part=long_part.concat(String.valueOf(code_arr[curr_pos+last_correct_pos]));
                        curTk=correct_token(long_part);
                        if(curTk==null){
                          curTk=reg(long_part);
                        }
                        last_correct_pos++;
                    }
                    curTk=lastCorrectToken;

                    if(last_correct_pos==1){
                        curr_pos++;
                    }
                    else{
                        curr_pos+=last_correct_pos-1;
                    }
                }
                else{
                    curr_pos++;
                }



            }
            //add tokens to result string
            if(curTk!=null){
                token_res=token_res.concat(" "+curTk.get_type()+" ");
                if(tok.contains(curTk)){
                    tok.get(tok.indexOf(curTk)).inc_count();
                    if(curTk.getFunc())
                    {
                        if (prevToken == null){
                            tok.get(tok.indexOf(curTk)).incOperator();
                        }
                        else {
                            tok.get(tok.indexOf(curTk)).incOperand();
                        }
                    }
                }
                else {
                    if( prevToken.get_type() =="FUNC" && curTk.get_type()=="ID") curTk.changeFunction();
                    tok.add(curTk);
                    tok.get(tok.indexOf(curTk)).inc_count();
                    tok.get(tok.indexOf(curTk)).incOperator();
                }
                prevToken = curTk;
            }
            else{
                token_res=token_res.concat(String.valueOf(part.charAt(0)));
                curr_pos=0;
            }
            //cutting default string processed part
            if(curr_pos==0){
                code="";
                for(int i=curr_pos+1;i<code_arr.length;i++){
                    code=code.concat(String.valueOf(code_arr[i]));
                }
            }
            else{
                code="";
                for(int i=curr_pos;i<code_arr.length;i++){
                    code=code.concat(String.valueOf(code_arr[i]));
                }
            }


            code_arr=code.toCharArray();
            code=code.trim();
            curTk=null;
            lastCorrectToken = null;
            curr_pos=0;
            last_correct_pos=1;
            part="";
            long_part="";
        }
        return token_res;
    }
    public static Token correct_token(String part){
        for (Token token : tok) {
            if (Objects.equals(token.get_name(), part)) {
                return token;
            }
        }
        return null;
    }
    public static Token reg(String part){

        if(part.replaceAll("\\d+(\\.\\d*)?","NUM").equals("NUM")) {
            part = part.trim();
            Token ret = new Token(part, "NUM");
            tok.add(ret);
            return ret;
        }
        else{
            if(part.replaceAll("^([a-zA-Z_$][a-zA-Z\\d_$]*)$", "ID").equals("ID")){
                part=part.trim();
                Token ret = new Token(part,"ID");
                return ret;
            }
        }
        return null;

    }
    public static void init_tokens(){
        //logical
        tok.add(new Token("&&","LOGIC"));
        tok.add(new Token("||","LOGIC"));
        tok.add(new Token("!","LOGIC"));
        //bitwise
        tok.add(new Token("&","BIT"));
        tok.add(new Token("|","BIT"));
        tok.add(new Token("^","BIT"));
        tok.add(new Token("~","BIT"));
        tok.add(new Token(">>","BIT"));
        tok.add(new Token("<<","BIT"));
        tok.add(new Token(">>>","BIT"));
        //compare
        tok.add(new Token(">","COMP"));
        tok.add(new Token("<","COMP"));
        tok.add(new Token("<=","COMP"));
        tok.add(new Token(">=","COMP"));
        tok.add(new Token("==","COMP"));
        tok.add(new Token("===","COMP"));
        tok.add(new Token("!=","COMP"));
        tok.add(new Token("!==","COMP"));
        //arithmetic
        tok.add(new Token("+","PLUS"));
        tok.add(new Token("-","MINUS"));
        tok.add(new Token("/","DIV"));
        tok.add(new Token("*","MULT"));
        tok.add(new Token("%","MOD"));
        tok.add(new Token("++","INC"));
        tok.add(new Token("--","DEC"));
        //keywords
        tok.add(new Token("for","FOR"));
        tok.add(new Token("while","WHILE"));
        tok.add(new Token("if","IF"));
        tok.add(new Token("else","ELSE"));
        tok.add(new Token("while","WHILE"));
        tok.add(new Token("function","FUNC"));
        tok.add(new Token("var","VAR"));
        tok.add(new Token("let","LET"));
        tok.add(new Token("const","CONST"));
        tok.add(new Token("class","CLASS"));
        //brackets
        tok.add(new Token("{","FIG_BRACK_OPEN"));
        tok.add(new Token("}","FIG_BRACK_CLOSE"));
        tok.add(new Token("(","BRACK_OPEN"));
        tok.add(new Token(")","BRACK_CLOSE"));
        tok.add(new Token("[","SQR_BRACK_OPEN"));
        tok.add(new Token("]","SQR_BRACK_CLOSE"));
        //assigment
        tok.add(new Token("=","ASSIGN"));
        tok.add(new Token("+=","ASSIGN"));
        tok.add(new Token("-=","ASSIGN"));
        tok.add(new Token("*=","ASSIGN"));
        tok.add(new Token("/=","ASSIGN"));
        tok.add(new Token("%=","ASSIGN"));
        tok.add(new Token("&=","ASSIGN"));
        tok.add(new Token("|=","ASSIGN"));
        tok.add(new Token("^=","ASSIGN"));
        tok.add(new Token("~=","ASSIGN"));
        tok.add(new Token("<<=","ASSIGN"));
        tok.add(new Token(">>=","ASSIGN"));
        tok.add(new Token(">>>=","ASSIGN"));
        //operators
        tok.add(new Token(";","DOT_COM"));
        tok.add(new Token(".","DOT"));
        tok.add(new Token(",","COM"));
        tok.add(new Token(":","D_COM"));
        tok.add(new Token("?:","QUES_D_COM"));
        //preprocess
        tok.add(new Token("\"","OPEN_LITER"));
        tok.add(new Token("//","LINE_COMMENT"));
        tok.add(new Token("/*","OPEN_COM_MUL"));
        tok.add(new Token("*/","CLOSE_COM_MUL"));
        //types
        tok.add(new Token("string","STR"));
        tok.add(new Token("number","NUMB"));
        tok.add(new Token("boolean","BOOL"));
        tok.add(new Token("void","VOID"));
        tok.add(new Token("null","NULL"));
        tok.add(new Token("undefined","UND"));
    }
}
class Token{
    private String name;
    private String type;

    private boolean isFunction = false;

    private int OperandCount =0 ;

    private int OperatorCount = 0;
    private int count;

    public  int getOperatorCount(){ return this.OperatorCount;}
    public  int getOperandCount(){return  this.OperandCount;}
    public void incOperator(){this.OperatorCount++;}
    public void incOperand(){this.OperandCount++;}

    public void changeFunction(){ this.isFunction = true;}

    public boolean getFunc(){return this.isFunction;}
    public void set_name(String name){
        this.name=name;
    }
    public void set_type(String type){
        this.type=type;
    }
    public String get_name(){
        return this.name;
    }
    public String get_type(){
        return this.type;
    }

    public int get_count(){
        return this.count;
    }
    public void inc_count(){
        this.count++;
    }
    Token(String name,String type){
        this.name=name;
        this.type=type;
        this.count=0;

    }
}