#include "stdlib.h"
#include "stdio.h"
#include "string.h"
#include "stdbool.h"

bool Terminal(char* terminal);
bool S();
bool H();
bool B();
bool Y();
bool I();
bool X();
bool Z();
typedef struct tokens_array {
	char token[20];
}tokens;
tokens input[100];
tokens* curr_token = &input[0];
FILE* LEXER;

void  main()
{
	fopen_s(&LEXER,"output.txt","r+");
	int i = 0;
	char buffer[20] = {0};
	while (!feof(LEXER))
	{
		fgets(buffer, 20, LEXER);
		if(buffer[strlen(buffer) - 1] =='\n')
		buffer[strlen(buffer) - 1] = 0;
		strcpy_s(input[i].token,20, buffer);
		i++;
	}
	fclose(LEXER);
	input[i].token[0] = '$';
	input[i].token[1] = 0;
	bool accept = S();
	if (accept == true)
	{
		printf("Accept");
	}
	else
	{
		printf("Reject");
	}
}
bool S()
{
	return H() || B() || I();
}

bool H()
{
	bool Heading = Terminal("HSTART") && X();
	return Heading;
}
bool B()
{
	tokens* save = curr_token;
	bool MainSyntaxis = Terminal("STAR") && Terminal("STAR") && Y() && Terminal("STAR");
	bool AltSyntaxis = Terminal("SPACE") && Terminal("LOWERSLASH") && Terminal("LOWERSLASH") && Z() && Terminal("LOWERSLASH");
	curr_token = save;
	return MainSyntaxis || AltSyntaxis;
}
bool I() {
	bool MainSyntaxis =  Terminal("STAR") && Y();
	bool AltSyntaxis = Terminal("SPACE") && Terminal("LOWERSLASH") && Z() ;
	return MainSyntaxis || AltSyntaxis ;
}
bool X()
{

	bool ThirdText = Terminal("TEXT") && X();
	bool ForthText = Terminal("SPACE") && X();
	bool EndText = Terminal("$");
	return ThirdText || ForthText || EndText;
}
bool Y()
{
	
	bool ThirdText = Terminal("TEXT") && Y();
	bool ForthText = Terminal("SPACE") && Y();
	bool EndText = Terminal("STAR");
	return ThirdText || ForthText || EndText;
}
bool Z()
{
	bool ThirdText = Terminal("TEXT") && Z();
	bool ForthText = Terminal("SPACE") && Z();
	bool EndText = Terminal("LOWERSLASH");
	return ThirdText || ForthText || EndText;
}


bool Terminal(char* terminal) {

	if (curr_token->token != NULL && strcmp(curr_token->token, terminal) == 0) {
		curr_token++;
		return true;
	}
	else {
		return false;
	}


}
