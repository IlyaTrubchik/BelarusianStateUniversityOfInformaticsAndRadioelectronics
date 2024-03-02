#include <stdio.h>

typedef struct Worker {
	char lastname[50];
	char department[50];
	int salary;
}worker;
void CreateFile();
void ReadFile(worker mas[], int* counter);
void add_elem(worker mas[], int* counter);
void save_file(worker mas[], int* counter);
void printf_file(worker mas[], int counter);
void summ(worker mas[], int count, char name[50]);
void menu(worker mas[], int* counter);
void deleteelem(worker mas[], int* count);
void corretitem(worker mas[], int* count);
FILE* file;
void main() 
{
	worker mas[20];
	int count = 0;

	ReadFile(mas, &count,file);
//	printf_file(mas, count);
	//summ(mas, count, "A\n");
	menu(mas, &count);
	save_file(mas, &count, file);
}

void CreateFile()
{
	FILE* file;
	fopen_s(&file,"List", "wb");
	fclose(file);
}

void ReadFile(worker mas[],int *counter)//FILE* file)
{
	char filename[20] = "List";
	fopen_s(&file,filename, "rb");
	if (file == 0)
	{
		printf("Cannot read file,creating new file");
		CreateFile(filename);
		fopen_s(&file, filename, "r");
	}
	else {
		fseek(file, 0, SEEK_SET);
		while (!feof(file))
		{
			fread(&mas[*counter], sizeof(worker), 1, file);
			*counter = *counter + 1;
		}
		fclose(file);
	}
	*counter = *counter-1;
}
void printfstr(char str[50])
{
	for (int i = 0;i < strlen(str); i++)
	{
		printf("%c", str[i]);
	}
	printf(" ");
}
void printf_file(worker mas[],int counter)
{
	for (int i = 0;i < (  counter); i++)
	{
		printfstr(mas[i].lastname);
		printfstr(mas[i].department);
		printf("%d", mas[i].salary);
		printf("\n\n");
	}
}
void add_elem(worker mas[], int* counter) {
	printf("Enter lastname\n");
	rewind(stdin);
	fgets(mas[(*counter)].lastname, 50, stdin);
	printf("Enter department\n");
	rewind(stdin);
	fgets(mas[(*counter)].department, 50, stdin);
	printf("Enter salary\n");
	rewind(stdin);
	scanf_s("%d", &mas[(*counter)].salary);
	printf("\n");
	*counter = *counter + 1;
}
void save_file(worker mas[], int* counter)
{
	
	char filename[20] = "List";
	fopen_s(&file, filename, "wb");
	for (int i = 0;i < (*counter);i++)
	{
		fwrite(&mas[i], sizeof(worker), 1, file);
	}
	fclose(file);
}
void summ(worker mas[],int count,char name[50])
{
	int sum = 0;
	int counter = 0;
	float average = 0;
	for (int i = 0;i < count;i++)
	{
		if (strcmp(name, mas[i].department) == 0)
		{
			sum = sum + mas[i].salary;
			counter++;
		}
	}
	printf("Summary cash:\n%d", sum);
	average = sum / counter;
	printf("\n");
	printf("Average salary:\n%3f", average);
}
void menu(worker mas[],int *counter) {
	int key = 0;
	printf("<<<<MENU>>>>\n");
	printf("1.Addelem\n");
	printf("2.Check Average\n");
	printf("3.Delete\n");
	printf("4.Print list\n");
	printf("5.Correct item\n");
	printf("6.Exit\n");
	rewind(stdin);
	scanf_s("%d", &key);

	while (key != 6)
	{
		switch (key)
		{
			case 6:
			{
				break;
			}
			case 1:
			{
				add_elem(mas,counter);
				break;
			}
			case 2:
			{
				summ(mas, *counter, "A\n");
				printf("\n");
				break;
			}
			case 3:
			{
				deleteelem(mas,counter);
				printf("\n");
				break;
			}
			case 4:
			{
				printf_file(mas, *counter);
				printf("\n");
				break;
			}
			case 5:
			{
				corretitem(mas, counter);
				break;
			}
			
		}
		printf("<<<<MENU>>>>\n");
		printf("1.Addelem\n");
		printf("2.Check Average\n");
		printf("3.Delete\n");
		printf("4.Print list\n");
		printf("5.Correct item\n");
		printf("6.Exit\n");
		rewind(stdin);
		scanf_s("%d", &key);
	}
}
void deleteelem(worker mas[], int* count)
{
	printf("Writeln LastName\n");
	char name[50];
	rewind(stdin);
	int delindex = -1;
	fgets(name, 50, stdin);
	for (int i = 0;i < (*count);i++)
	{
		if (strcmp(mas[i], name) == 0)
		{
			delindex = i;
		}
	}
	for (int i = delindex;i < (*count) - 1;i++)
	{
		mas[i] = mas[i + 1];
	}
	(*count) = (*count) - 1;
}
void corretitem(worker mas[], int* count)
{
	printf("Writeln LastName\n");
	char name[50];
	rewind(stdin);
	int corrindex = -1;
	fgets(name, 50, stdin);
	for (int i = 0;i < (*count);i++)
	{
		if (strcmp(mas[i], name) == 0)
		{
			corrindex = i;
		}
	}
	printf("Write new lastname:\n");
	fgets(mas[corrindex].lastname, 50, stdin);
	printf("Write new department:\n");
	fgets(mas[corrindex].department, 50, stdin);
	printf("Write new salary\n");
	rewind(stdin);
	scanf_s("%d", &mas[corrindex].salary);
}
