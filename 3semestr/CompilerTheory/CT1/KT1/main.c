#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int Transitions[11][8] = { {0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 2, 3, 5, 0, 0, 0, 0 }, { 0, 2, 3, 5, 9, 10, 10, 5 }, { 0, 4, 3, 5, 0, 10, 10, 5 }, { 0, 4, 3, 5, 0, 10, 10, 5 }, { 0, 7, 8, 5, 0, 0, 10, 6 }, { 0, 7, 8, 5, 0, 0, 10, 6 }, { 0, 7, 8, 5, 0, 0, 10, 6 }, { 0, 7, 8, 5, 0, 0, 10, 6 }, { 0, 7, 8, 5, 0, 0, 10, 5 },{0,0,0,0,0,0,0,0} };

void main()
{
	printf("Enter string:\n");
	char String [100];
	fgets(String, 100, stdin);
	CheckString(String);
}


int CheckString(char String[100])
{
	int State = 1;
	int CharType;
	for (int i = 0; i < strlen(String)-1;i++)
	{
		State = Transitions[State][GetCharType(String[i],State)];
	}
	if (State == 9 || State == 10)
	{
		printf("String is correct\n");
	}
	else
	{
		printf("String is uncorrect\n");
	}
}
int GetCharType(char Char,int State)
{

	if (Char == '0' || Char == '1')
	{
		return 1;
	}
	else if (Char == '2' || Char == '3' || Char == '4' || Char == '5' || Char == '6' || Char == '7')
	{
		return 2;
	}
	else if (Char == '8' || Char == '9')
	{
		return 3;
	}
	else if ((Char == 'b' || Char == 'B') && State == 2)
	{
		return 4;
	}
	else if (Char == 'O' || Char == 'o')
	{
		return 5;
	}
	else if (Char == 'h' || Char == 'H')
	{
		return 6;
	}
	else if (Char == 'A' || Char == 'B' || Char == 'C' || Char == 'D' || Char == 'E' || Char == 'F' || Char == 'a' || Char == 'b' || Char == 'c' || Char == 'd' || Char == 'e' || Char == 'f')
	{
		return 7;
	}
	else
	return 0;
}