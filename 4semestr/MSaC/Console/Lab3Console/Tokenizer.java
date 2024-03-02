

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Tokenizer {
    public static List<Token> tok = new ArrayList<>();
    ArrayList<Token> lineTokens;
    ArrayList<Token> opM;
    ArrayList<Token> opC;
    ArrayList<Token> opT;
    ArrayList<Token> opP;
    ArrayList<Token> IOop;

    private boolean isLogicLine = false;
    private boolean isConsoleLogLine = false;
    public Tokenizer(){
        tok = new ArrayList<>();
        init_tokens();
    }
    public  LinkedList<Token> tokenize(String code){
        lineTokens = new ArrayList<>();
        isLogicLine=false;
        isConsoleLogLine=false;
        Token prevTk=null;
        Token curTk = null;
        Token lastCorrectToken=null;
        char[] code_arr=code.toCharArray();
        LinkedList<Token> result=new LinkedList<>();
        String part="";
        String long_part="";
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
            //add tokens to result list
            if(curTk!=null){
                lineTokens.add(curTk);
                result.add(curTk);
                if(tok.contains(curTk)){
                    if(curTk.get_type().equals("IF") || curTk.get_type().equals("WHILE")
                            || curTk.get_type().equals("FOR") || curTk.get_type().equals("SWITCH")) {
                        isLogicLine = true;
                    }
                    else if(curTk.get_type().equals("CONSOLELOG") || (curTk.get_type().equals("PROMPT") && isConsistId()!=-1))
                    {
                        isConsoleLogLine = true;
                        if(curTk.get_type().equals("PROMPT") && !tok.get(tok.indexOf(lineTokens.get(isConsistId()))).isModified
                        && !tok.get(tok.indexOf(lineTokens.get(isConsistId()))).getOperandType().equals("C"))
                        {
                            tok.get(tok.indexOf(lineTokens.get(isConsistId()))).changeOnDataOperand();
                            tok.get(tok.indexOf(lineTokens.get(isConsistId()))).isIO=true;
                        }
                    }
                    if(curTk.get_type().equals("ASSIGN") && isConsistId()!=-1 && !isInitialization()){
                        curTk.isModified = true;
                        if(!lineTokens.get(isConsistId()).getOperandType().equals("C"))
                            lineTokens.get(isConsistId()).changeOnModificatorOperand();
                    }
                    if(curTk.get_type().equals("ID"))
                    {
                        if(isLogicLine) tok.get(tok.indexOf(curTk)).changeOnConditionalOperand();
                        if(isConsoleLogLine && !curTk.getOperandType().equals("C") && !curTk.isModified) {
                            tok.get(tok.indexOf(curTk)).isIO =true;
                        }
                    }
                    tok.get(tok.indexOf(curTk)).inc_count();
                }
                else {
                    tok.add(curTk);
                    if(curTk.get_type().equals("ID") && prevTk!=null && prevTk.get_type().equals("FUNC"))
                    {
                        tok.get(tok.indexOf(curTk)).isFunction=true;
                    }
                    if(curTk.get_type().equals("IF") || curTk.get_type().equals("WHILE")
                            || curTk.get_type().equals("FOR") || curTk.get_type().equals("SWITCH")) {
                        isLogicLine = true;
                    }
                    else if(curTk.get_type().equals("CONSOLELOG") || (curTk.get_type().equals("PROMPT") && isConsistId()!=-1))
                    {
                        //isConsoleLogLine = true;
                        tok.get(tok.indexOf(lineTokens.get(isConsistId()))).isIO=true;
                        if(curTk.get_type().equals("PROMPT"))
                        {
                            tok.get(tok.indexOf(lineTokens.get(isConsistId()))).changeOnDataOperand();
                            tok.get(tok.indexOf(lineTokens.get(isConsistId()))).isIO=true;
                        }
                    }
                    if(curTk.get_type().equals("ID"))
                    {
                        if(isLogicLine) tok.get(tok.indexOf(curTk)).changeOnConditionalOperand();
                        if(isConsoleLogLine && !curTk.getOperandType().equals("C") && !curTk.isModified) {
                            tok.get(tok.indexOf(curTk)).changeOnDataOperand();
                        }
                    }
                    tok.get(tok.indexOf(curTk)).inc_count();
                }
                prevTk =curTk;
            }
            else{
                //token_res=token_res.concat(String.valueOf(part.charAt(0)));
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
            lastCorrectToken=null;
            curr_pos=0;
            last_correct_pos=1;
            part="";
            long_part="";
        }
        return result;
    }
    private int isConsistId(){
        for (Token token: lineTokens) {
            if(token.get_type().equals("ID")) return lineTokens.indexOf(token);
        }
        return -1;
    }
    private boolean isInitialization(){
        for (Token token: lineTokens) {
            if(token.get_type().equals("VAR") || token.get_type().equals("LET")) return true;
        }
        return false;
    }

    public Token correct_token(String part){
        for (Token token : tok) {
            if (Objects.equals(token.get_name(), part)) {
                return token;
            }
        }
        return null;
    }
    public void printOperands(){
        this.opM = new ArrayList<>();
        this.opP = new ArrayList<>();
        this.opC = new ArrayList<>();
        this.opT = new ArrayList<>();
        this.IOop = new ArrayList<>();
        for (Token token:tok) {
            if(token.isFunction==false) {
                if (token.getOperandType().equals("M") && token.get_type().equals("ID")) opM.add(token);
                if (token.getOperandType().equals("C") && token.get_type().equals("ID")) opC.add(token);
                if (token.getOperandType().equals("P") && token.get_type().equals("ID")) opP.add(token);
                if (token.getOperandType().equals("T") && token.get_type().equals("ID")) opT.add(token);
                if(token.isIO) IOop.add(token);
            }
        }
    }
    public void checkOnParasitOperands(){
        for (Token token:tok)
        {
            if(token.get_type().equals("ID"))
            {
                if(token.get_count()==1 && token.getOperandType().equals("M"))
                {
                    token.changeOnParasitOperand();
                }
            }
        }
    }

    public  Token reg(String part){

        if(part.replaceAll("\\d+(\\.\\d*)?","NUM").equals("NUM")) {
            part = part.trim();
            Token ret = new Token(part, "NUM",false,false);
            return ret;
        }
        else{
            if(part.replaceAll("^([a-zA-Z_$][a-zA-Z\\d_$]*)$", "ID").equals("ID")){
                part=part.trim();
                Token ret = new Token(part,"ID",false,false);
                return ret;
            }
        }
        return null;

    }
    public void init_tokens(){
        //logical
        tok.add(new Token("&&","L_AND",true,false));
        tok.add(new Token("||","L_OR",true,false));
        tok.add(new Token("!","L_NOT",true,false));
        //bitwise
        tok.add(new Token("&","B_AND",true,false));
        tok.add(new Token("|","BIT",true,false));
        tok.add(new Token("^","BIT",true,false));
        tok.add(new Token("~","BIT",true,false));
        tok.add(new Token(">>","BIT",true,false));
        tok.add(new Token("<<","BIT",true,false));
        tok.add(new Token(">>>","BIT",true,false));
        //compare
        tok.add(new Token(">","COMP",true,false));
        tok.add(new Token("<","COMP",true,false));
        tok.add(new Token("<=","COMP",true,false));
        tok.add(new Token(">=","COMP",true,false));
        tok.add(new Token("==","COMP",true,false));
        tok.add(new Token("===","COMP",true,false));
        tok.add(new Token("!=","COMP",true,false));
        tok.add(new Token("!==","COMP",true,false));
        //arithmetic
        tok.add(new Token("+","ADDICT",true,false));
        tok.add(new Token("-","ADDICT",true,false));
        tok.add(new Token("/","MULT",true,false));
        tok.add(new Token("*","MULT",true,false));
        tok.add(new Token("%","MULT",true,false));
        tok.add(new Token("++","INC",true,false));
        tok.add(new Token("--","DEC",true,false));
        tok.add(new Token("true","TRUE",true,false));
        tok.add(new Token("false","FALSE",true,false));
        tok.add(new Token("return","RETURN",true,false));
        //keywords
        tok.add(new Token("for","FOR",true,true));
        tok.add(new Token("while","WHILE",true,true));
        tok.add(new Token("if","IF",true,true));
        tok.add(new Token("else","ELSE",false,true));
        tok.add(new Token("do","DO",true,true));
        tok.add(new Token("default","DEF",true,true));
        tok.add(new Token("switch","SWITCH",true,true));
        tok.add(new Token("break","BREAK",true,false));
        tok.add(new Token("case","CASE",true,true));
        tok.add(new Token("function","FUNC",true,false));
        tok.add(new Token("var","VAR",false,false));
        tok.add(new Token("let","LET",false,false));
        tok.add(new Token("const","CONST",false,false));
        //brackets
        tok.add(new Token("{","FIG_BRACK_OPEN",true,true));
        tok.add(new Token("}","FIG_BRACK_CLOSE",true,true));
        tok.add(new Token("(","BRACK_OPEN",false,false));
        tok.add(new Token(")","BRACK_CLOSE",false,false));
        tok.add(new Token("[","SQR_BRACK_OPEN",true,false));
        tok.add(new Token("]","SQR_BRACK_CLOSE",true,false));
        //assigment
        tok.add(new Token("=","ASSIGN",true,false));
        tok.add(new Token("+=","ASSIGN",true,false));
        tok.add(new Token("-=","ASSIGN",true,false));
        tok.add(new Token("*=","ASSIGN",true,false));
        tok.add(new Token("/=","ASSIGN",true,false));
        tok.add(new Token("%=","ASSIGN",true,false));
        tok.add(new Token("&=","ASSIGN",true,false));
        tok.add(new Token("|=","ASSIGN",true,false));
        tok.add(new Token("^=","ASSIGN",true,false));
        tok.add(new Token("~=","ASSIGN",true,false));
        tok.add(new Token("<<=","ASSIGN",true,false));
        tok.add(new Token(">>=","ASSIGN",true,false));
        tok.add(new Token(">>>=","ASSIGN",true,false));
        //operators
        tok.add(new Token(";","DOT_COM",true,false));
        tok.add(new Token(".","DOT",true,false));
        tok.add(new Token(",","COM",true,false));
        tok.add(new Token(":","D_COM",true,false));
        tok.add(new Token("?:","QUES_D_COM",true,false));
        //preprocess
        tok.add(new Token("\"","OPEN_LITER",false,false));
        tok.add(new Token("//","LINE_COMMENT",false,false));
        tok.add(new Token("/*","OPEN_COM_MUL",false,false));
        tok.add(new Token("*/","CLOSE_COM_MUL",false,false));
        //types
        tok.add(new Token("string","STR",false,false));
        tok.add(new Token("number","NUMB",false,false));
        tok.add(new Token("boolean","BOOL",false,false));
        tok.add(new Token("void","VOID",false,false));
        tok.add(new Token("null","NULL",false,false));
        tok.add(new Token("undefined","UND",false,false));
        tok.add(new Token("console","CONSOLELOG",true,false));
        tok.add(new Token("log","LOG",true,false));
        tok.add(new Token("prompt","PROMPT",false,false));
    }
}
class Token{
    private String name;
    private String type;

    public boolean isModified = false;

    public boolean isIO =false;
    private String operandType="M";
    private boolean isOperator;
    private boolean isContainer;
    public boolean isFunction;
    private int count;

    public void changeOnDataOperand(){
        this.operandType = "P";
    }
    public void changeOnConditionalOperand(){
        this.operandType="C";
    }
    public void changeOnModificatorOperand(){this.operandType="M";}
    public String getOperandType(){
        return  this.operandType;
    }
    public void changeOnParasitOperand(){
        this.operandType = "T";
    }
    public boolean get_oper(){
        return this.isOperator;
    }
    public boolean get_cont(){
        return this.isContainer;
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
    Token(String name,String type,boolean isOperator,boolean isContainer){
        this.name=name;
        this.type=type;
        this.count=0;
        this.isContainer=isContainer;
        this.isOperator=isOperator;

    }
}