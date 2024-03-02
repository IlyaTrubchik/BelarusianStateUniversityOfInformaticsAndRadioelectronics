#include <stdio.h>
#include <string.h>
void sort(struct students* sp, int count);
void printfstr(char str[]);
void printmas(struct students* sp, int count);
void solving(struct students* sp, int count);
typedef struct students {
	int	group_number;
	float averagerating;
	char lastname[50];
}students;
void initstudents(students Allstudents[])
{
	Allstudents[0].averagerating = 9.5;
	Allstudents[0].group_number = 151001;
	strcpy_s(Allstudents[0].lastname, 50, "Sasfsadgas");
	Allstudents[1].averagerating = 9.1;
	Allstudents[1].group_number = 151001;
	strcpy_s(Allstudents[1].lastname, 50, "Eefasfsadgas");
	Allstudents[2].averagerating = 8.5;
	Allstudents[2].group_number = 151001;
	strcpy_s(Allstudents[2].lastname, 50, "Kdsfsadgas");
	Allstudents[3].averagerating = 7.5;
	Allstudents[3].group_number = 151001;
	strcpy_s(Allstudents[3].lastname, 50, "Dasfsadgas");
	Allstudents[4].averagerating = 6.8;
	Allstudents[4].group_number = 151001;
	strcpy_s(Allstudents[4].lastname, 50, "Fasfadfasdf");

	Allstudents[5].averagerating = 9.7;
	Allstudents[5].group_number = 151002;
	strcpy_s(Allstudents[5].lastname, 50, "Kjghjhg");
	Allstudents[6].averagerating = 9.33;
	Allstudents[6].group_number = 151002;
	strcpy_s(Allstudents[6].lastname, 50, "Bbjhbjhg");
	Allstudents[7].averagerating = 5.6;
	Allstudents[7].group_number = 151002;
	strcpy_s(Allstudents[7].lastname, 50, "Jhjkhkkh");
	Allstudents[8].averagerating = 6.7;
	Allstudents[8].group_number = 151002;
	strcpy_s(Allstudents[8].lastname, 50, "Ikjkdlf");
	Allstudents[9].averagerating = 8.8;
	Allstudents[9].group_number = 151002;
	strcpy_s(Allstudents[9].lastname, 50, "HJjdhsfk");

	Allstudents[10].averagerating = 8.4;
	Allstudents[10].group_number = 151003;
	strcpy_s(Allstudents[10].lastname, 50, "Efefreger");
	Allstudents[11].averagerating = 8.5;
	Allstudents[11].group_number = 151003;
	strcpy_s(Allstudents[11].lastname, 50, "Fadsfasdf");
	Allstudents[12].averagerating = 6.3;
	Allstudents[12].group_number = 151003;
	strcpy_s(Allstudents[12].lastname, 50, "Ikdsfjks");
	Allstudents[13].averagerating = 6.8;
	Allstudents[13].group_number = 151003;
	strcpy_s(Allstudents[13].lastname, 50, "Okdfjsfd");
	Allstudents[14].averagerating = 4.5;
	Allstudents[14].group_number = 151003;
	strcpy_s(Allstudents[14].lastname, 50, "Xasdada");
}
void Enterstudent(students sp[],int count)
{
	for (int i = 0;i < count;i++)
	{
		printf("Enter Lastname\n");
		rewind(stdin);
		gets(sp[i].lastname);
		printf("Enter avarage rating\n");
		scanf_s("%f", &sp[i].averagerating);
		printf("Enter group number\n");
		scanf_s("%d", &sp[i].group_number);

	}
}
void main() {
	int realcount = 15;
	printf("Enter number of students\n");
	scanf_s("%d", &realcount);
	students Allstudents[20];
	//initstudents(Allstudents);
	Enterstudent(Allstudents, realcount);
	solving(Allstudents, realcount);
}
void sort(students sp[],int count)
{
	for (int i = 0;i < count-1;i++)
	{
		int max = i;
		for (int j = i + 1;j < count;j++)
		{
			if (sp[j].averagerating >= sp[max].averagerating)
			{
				max = j;
			}
		}
		students tmp = sp[max];
		sp[max] = sp[i];
		sp[i] = tmp;
	}
}
void printfstr(char str[])
{
	for (int i = 0;i < strlen(str);i++)
	{
		printf("%c", str[i]);
	}
}
void printmas(students* sp,int count)
{
	for (int i = 0;i < count;i++)
	{
		printfstr(sp[i].lastname);
		printf("  ");
		printf("%4f", sp[i].averagerating);
		printf("\n");
	}
}
void solving(students sp[], int count)
{
	students groupmas[15];
	int groups[15];
	int groupcount = 0;
	for (int i = 0;i < count;i++)
	{
		int check = 0;
		for (int j = 0;j < groupcount;j++)
		{
			if (groups[j] == sp[i].group_number)
			{
				check = 1;
			}
		}
		if (check != 1)
		{
			printf("<<<<<<NEXT_GROUP>>>>>>\n");
			groups[groupcount] = sp[i].group_number;
			groupcount++;
			int k = 1;
			for (int j = i+1; j < count;j++)
			{
				groupmas[0] = sp[i];
				if (sp[j].group_number == sp[i].group_number)
				{
					groupmas[k] = sp[j];
					k++;
				}
			}
			sort(groupmas, k);
			printmas(groupmas, k);
		}
	}
}
