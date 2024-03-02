#include <stdio.h>
#include <string.h>
void task14();
void task23();
void task24();
void task27();
void main()
{
	//task14();//Completed
	//task23();//Completed
	//task24();//completed
	task27();
}

void task24()
{
	printf("Enter text:\n");
	char text[200];
	gets(text);
	int  i = 0;
	char newtext[200];
	int j = 0;
	for (i = 0;i < strlen(text);i++)
	{
		
		if ((text[i] >= 'A' && text[i] <= 'I') || ((text[i] >= 'a') && (text[i] <= 'i')))
		{
			if ((text[i] >= 'a') && (text[i] <= 'i'))
			{
				text[i] = text[i] - ('a' - 'A');
			}
			newtext[j] = text[i];
			j++;
		}
	}
	newtext[j] = 0;
	for (int i = 0; i <= strlen(newtext) - 2;i++)
	{
		int min = i;
		for (int j = i + 1;j <= strlen(newtext) - 1;j++)
		{
			if (newtext[j] <= newtext[min])
				min = j;
		}
		char tmp = newtext[min];
		newtext[min] = newtext[i];
		newtext[i] = tmp;
	}
	printf("New string:\n");
	puts(newtext);
}
void task23()
{
	char A[100];
	char B[100];
	int M, N;
	printf("Enter string A:\n");
	gets(A);
	printf("Enter string B:\n");
	gets(B);
	printf("Enter N:\n");
	scanf_s("%d",&N);
	printf("Enter M:\n");
	scanf_s("%d",&M);
	int  i = M - 1;
	for (int j = 0; j <= N - 1;j++)
	{
		if (i < strlen(A))
		{
			A[i] = B[j];
			i++;
		}
	}
	printf("New string A: \n");
	puts(A);
	printf("String B: \n");
	puts(B);
}

void task14()
{
	char text[255];
	printf("Enter string:\n");
	gets(text);
	printf("\n");
	printf("Enter word:\n");
	char word[40];
	gets(word);
	printf("\n");
	int start = 0;
	if (text[0] == ' ')
	{

		while (text[start] == ' ')
		{
			start++;
		}
	}
	int i = start;
	int counter = 0;
	while (i <= strlen(text))
	{
		char  checkword[40];
		int j = 0;
		while ((text[i] != ' ') && (i <= strlen(text)))
		{
			checkword[j] = text[i];
			j++;
			i++;
		}
		checkword[j] = 0;
		while ((text[i] == ' ') && (i <= strlen(text)))
		{
			i++;
		}
		int flag = 0;
		if (strlen(word) == strlen(checkword))
		{
			for (int j = 0;j <= strlen(word);j++)
			{
				if (word[j] != checkword[j])
				{
					flag = 1;
				}
			}
			if (flag == 0)
			{
				counter++;
			}
		}

	}
	printf("Number of repeating words:\t%d", counter);
}

	void task27()
	{
		char text[200];
		char newstr[20];
		printf("Enter string:\n");
		gets(text);
		printf("\n");
		char substr[20];
		printf("Enter substring:\n");
		gets(substr);
		printf("\n");
		printf("Enter insert string:\n");
		gets(newstr);
		char result[1000];
		int strlength = strlen(text);
		int substrlength = strlen(substr);
		int count = 0;
		char checkstr[5];
		int i = 0;
		int resulti = 0;
		while (i < strlength && count <9)
		{
			int starti = i;
			for (int j = 0;j < substrlength;j++)
			{
				checkstr[j] = text[i];
				i++;
			}
			checkstr[substrlength] = 0;
			int	lettercount = 0;
			for (int j = 0; j < substrlength;j++)
			{
				if (checkstr[j] == substr[j])
				{
					lettercount++;
				}
			}
			if (lettercount == substrlength)
			{
				count++;
				int lastresult = resulti;
				int j = 0;
				while (resulti - lastresult < strlen(newstr))
				{
					result[resulti] = newstr[j];
					resulti++;
					j++;
				}
				result[resulti] = "0"+count;
				resulti++;
				result[resulti] = 0;
				
			}
			else
			{
				int lastresult = resulti;
				
				while (resulti - lastresult < strlen(substr))
				{
					result[resulti] = text[starti];
					starti++;
					resulti++;

				}
				result[resulti] = 0;
			}
		}
		printf("Result:\n");
		puts(result);
	}