public class Parser {
    String[] tokens;
    int lCount = 0;
    int ifCount = 0;
    int swCount = 0;
    int currtok = 0;
    /*
     S->OPERATOR
     EXPR->T && ARITHM && T | T && BITWISE && T | T             T->ID ||NUM||'(' EXPR ')'|K   "{"->FIGOPEN,"}"->FIGCLOSE
     STATEMENT->ID && '=' && EXPR
     LEXPR-> E
     E-> EXPR && comp && EXPR | LEXPR LOGIC LEXPR
     OPERATOR-> IF|LOOPS|SWITCH|....
     */

    int b =0;
    int a =0;
    public Parser(String [] tokens)
    {
        this.tokens = tokens;
    }
    public boolean parse(){
        boolean isOkey=S();
        if(isOkey && currtok == tokens.length)
        {
            return true;
        }
        return  false;

    }
    private boolean S(){
       // return  STATEMENT() || LEXPR();
        // return EXPRESSION();
        return STATEMENT();
    }

    private boolean K(){
        int save  = this.currtok;
        if (EXPRESSION()) return true;
        return false;
    }
    private boolean LEXPR(){
        int save = this.currtok;

        return false;

    }

    private boolean STATEMENT(){
        int save =this.currtok;
        if(checkTerminal(currtok,"ID") && checkTerminal(currtok,"ASSIGN") && EXPRESSION()) {
            System.out.println("It's statement");
            return true;
        }
        this.currtok = save;
        return false;
    }
    private boolean T(){
        int save = this.currtok;
        if( checkTerminal(currtok,"ID") && checkTerminal(currtok,"ARITHM_S"))
        {
            return true;
        }
         this.currtok =save;
        if(  checkTerminal(currtok,"NUM") || checkTerminal(currtok,"ID")) {
            return true;
        }
        if(checkTerminal(currtok,"OPENBR") && EXPRESSION() && checkTerminal(currtok,"CLOSEBR")) return true;
        this.currtok = save;
        return false;
    }
    private boolean EXPRESSION()
    {
        int save = currtok;
        if(( T() && checkTerminal(currtok,"ARITHM") && T())) return  true;
        this.currtok = save;
        if(( T() && checkTerminal(currtok,"BITWISE") && T())) return true;
        this.currtok = save;
        if(T()) return true;
        this.currtok=save;
        return false;

    }
    private boolean checkTerminal(int currtok,String currToken)
    {
        if(currtok< tokens.length && tokens[currtok].equals(currToken)) {
            this.currtok++;
            return true;
        }
        else return false;
    }

}
