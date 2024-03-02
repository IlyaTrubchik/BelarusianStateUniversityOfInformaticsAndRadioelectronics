#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <Windows.h>
struct list {
	int number;
	int weight;
	struct list* next;
};
typedef struct path {
	char vers[30];
	int weight;
}path;
struct pathlist {
	path ourpath;
	struct pathlist* next;
};
void center(struct list* mas[]);
char inttochar(int number);
void minimizedist(struct list* mas[]);
void sortsp(struct pathlist* head);
void findmaxdist(struct list* mas[]);
void getallpath(struct list* mas[], int visited[], int currver, int endver, struct pathlist* head, int weight, char route[30]);
void main()
{
	/// ѕостроение списка смежности
	//SetConsoleOutputCp(1251);
	//SetConsoleCp(1251);
	struct list* slist[6];
	for (int i = 0;i < 6;i++)
	{
		slist[i] = (struct list*)malloc(sizeof(struct list));
		slist[i]->next = NULL;
	}
	struct list* temp;
	temp = (struct list*)malloc(sizeof(struct list));
	temp->next = NULL;
	temp->weight = 3;
	temp->number = 2;
	slist[0]->next = temp;
	temp = (struct list*)malloc(sizeof(struct list));
	temp->next = NULL;
	temp->number = 4;
	temp->weight = 5;
	slist[0]->next->next = temp;
	temp = (struct list*)malloc(sizeof(struct list));
	temp->next = NULL;
	temp->number = 3;
	temp->weight = 3;
	slist[1]->next = temp;
	temp = (struct list*)malloc(sizeof(struct list));
	temp->next = NULL;
	temp->number = 4;
	temp->weight = 1;
	slist[1]->next->next = temp;
	temp = (struct list*)malloc(sizeof(struct list));
	temp->next = NULL;
	temp->number = 5;
	temp->weight = 9;
	slist[1]->next->next->next = temp;
	temp = (struct list*)malloc(sizeof(struct list));
	temp->next = NULL;
	temp->number = 5;
	temp->weight = 2;
	slist[2]->next= temp;
	temp = (struct list*)malloc(sizeof(struct list));
	temp->next = NULL;
	temp->number = 6;
	temp->weight = 11;
	slist[2]->next->next = temp;
	temp = (struct list*)malloc(sizeof(struct list));
	temp->next = NULL;
	temp->number = 3;
	temp->weight = 10;
	slist[3]->next = temp;
	temp = (struct list*)malloc(sizeof(struct list));
	temp->next = NULL;
	temp->number = 5;
	temp->weight = 7;
	slist[3]->next->next = temp;
	temp = (struct list*)malloc(sizeof(struct list));
	temp->next = NULL;
	temp->number = 6;
	temp->weight = 5;
	slist[4]->next = temp;
	/// ѕостроение списка смежности
	int A[6][6];//матрица смежности
	for (int i = 0;i < 6;i++)
	{
		for (int j = 0;j < 6;j++)
		{
			A[i][j] = 0;
		}
	}
	for (int i = 0;i < 6;i++)
	{
		for (int j = 0;j < 6;j++)
		{
			if (i == j)
			{
				A[i][j] = 0;
			}
			else
			{
				int flag = 0;
				struct list* temp = slist[i]->next;
				while (temp != NULL && flag == 0)
				{
					if (temp->number == j + 1)
					{
						A[i][j] = 1;
						flag = 1;
					}
					temp = temp->next;
				}

			}
		}
	}
	printf("Matrix smezhnosti:\n");
	printf("     1  2  3  4  5  6 \n");
	for (int i = 0;i < 6;i++)
	{
		printf("%3d", i + 1);
		for (int j = 0;j < 6;j++)
		{
			printf("%3d", A[i][j]);
		}
		printf("\n");
	}
	printf("Node 1 is start\n");
	printf("Node 6 is result\n");
	minimizedist(slist);
	findmaxdist(slist);
	struct pathlist* head = (struct pathlist*)malloc(sizeof(struct pathlist));
	head->next = NULL;
	int visited[6];
	for (int i = 0;i < 6;i++)
	{
		visited[i] = 0;
	}
	char route[30]=" ";
	getallpath(slist, visited, 0, 5, head, 0, route);
	printf("\nAll routes:\n");
	sortsp(head);
	struct pathlist* tmp = head->next;
	while (tmp != NULL)
	{
		printf("Route:%s,weight:%d\n", tmp->ourpath.vers, tmp->ourpath.weight);
		tmp = tmp->next;
	}
	center(slist);
}
void minimizedist(struct list* mas[])
{
	int flagmas[6];
	for (int i = 0;i < 6;i++)
	{
		flagmas[i] = 0;
	}
	int currdist[6];
	currdist[0] = 0;
	for (int i = 1;i < 6;i++)
	{
		currdist[i] = 10000;
	}
	int flag = 0;
	while (flag == 0)
	{
		int vernum = 0;
		int min = 10000;
		for (int i = 1;i < 6;i++)
		{
			if (currdist[i] < min)
			{
				if (flagmas[i] == 0)
				{
					vernum = i;
					min = currdist[i];
				}
			}
		}
		flagmas[vernum] = 1;
		struct list* temp = mas[vernum]->next;
		while (temp != NULL)
		{
			int i = temp->number - 1;
			int weight = currdist[vernum] + temp->weight;
			if (currdist[i] > weight)
			{
				currdist[i] = weight;
			}
			temp = temp->next;
		}
		flag = 1;
		for (int i = 0;i < 6;i++)
		{
			if (flagmas[i] == 0)
			{
				flag = 0;
			}
		}
	}
	for (int i = 0;i < 6;i++)
	{
		printf("Shortest distance for node number %d:%d\n", (i + 1), currdist[i]);
	}
	int ver[6];
	int end = 5;
	ver[0] = end+1;
	
	int weight = currdist[5];
	int k = 1;
	while (end != 0)
	{
		for (int i = 0;i < 6;i++)
		{
			struct list* temp = mas[i]->next;
			int svflag = 0;
			int tempweight = 0;
			while (temp != NULL)
			{
				if (temp->number == end + 1)
				{
					svflag = 1;
					tempweight = temp->weight;
				}
				temp = temp->next;
			}
			if (svflag == 1)
			{
				if (weight - tempweight == currdist[i])
				{
					weight = weight - tempweight;
					ver[k] = i + 1;
					k++;
					end = i;
				}
			}
		}
	}
	printf("Print shortest route:\n");
	for (int i = k - 1; i >= 0; i--)
		printf("%3d ", ver[i]);
}
void findmaxdist(struct list* mas[])
{
	int flagmas[6];
	for (int i = 0;i < 6;i++)
	{
		flagmas[i] = 0;
	}
	int currdist[6];
	currdist[0] = 0;
	for (int i = 1;i < 6;i++)
	{
		currdist[i] = 0;
	}
	int flag = 0;
	while (flag == 0)
	{
		int vernum = 0;
		int max = 0;
		for (int i = 1;i < 6;i++)
		{
			if (currdist[i] > max)
			{
				if (flagmas[i] == 0)
				{
					vernum = i;
					max = currdist[i];
				}
			}
		}
		flagmas[vernum] = 1;
		struct list* temp = mas[vernum]->next;
		while (temp != NULL)
		{
			int i = temp->number - 1;
			int weight = currdist[vernum] + temp->weight;
			if (currdist[i] < weight)
			{
				currdist[i] = weight;
			}
			temp = temp->next;
		}
		flag = 1;
		for (int i = 0;i < 6;i++)
		{
			if (flagmas[i] == 0)
			{
				flag = 0;
			}
		}
	}
	printf("\nMax distance:%d\n", currdist[5]);
	int ver[6];
	int end = 5;
	ver[0] = end + 1;

	int weight = currdist[5];
	int k = 1;
	while (end != 0)
	{
		for (int i = 0;i < 6;i++)
		{
			struct list* temp = mas[i]->next;
			int svflag = 0;
			int tempweight = 0;
			while (temp != NULL)
			{
				if (temp->number == end + 1)
				{
					svflag = 1;
					tempweight = temp->weight;
				}
				temp = temp->next;
			}
			if (svflag == 1)
			{
				if (weight - tempweight == currdist[i])
				{
					weight = weight - tempweight;
					ver[k] = i + 1;
					end = i;
					k++;
				}
			}
		}
	}
	printf("Max dist direction:\n");
	for (int i = k - 1; i >= 0; i--)
		printf("%3d ", ver[i]);
}

 
void getallpath(struct list* mas[], int visited[], int currver,int endver,struct pathlist* head,int weight,char route[30])
{
	if (currver == endver)
	{
		while (head->next != NULL)
		{
			head = head->next;
		}
		struct pathlist* temp = (struct pathlist*)malloc(sizeof(struct pathlist));
		temp->next = NULL;
		head->next = temp;
		temp->ourpath.weight = weight;
		for (int i = 0; i <= strlen(route);i++)
		{
			temp->ourpath.vers[i] = route[i];
		}
		temp->ourpath.vers[strlen(temp->ourpath.vers) + 1] = 0;
		temp->ourpath.vers[strlen(temp->ourpath.vers)] = ' ';
		temp->ourpath.vers[strlen(temp->ourpath.vers) + 1] = 0;
		temp->ourpath.vers[strlen(temp->ourpath.vers)] = inttochar(currver + 1);
		exit;
	}
	else
	{	
		for (int i = 0;i <= endver;i++)
		{
			struct list* tmp = mas[currver]->next;
			while (tmp != NULL)
			{
				if (tmp->number == (i + 1))
				{
					int tempweight = tmp->weight + weight;
					
					char temproute[30];
					for (int i = 0; i <= strlen(route);i++)
					{
						temproute[i] = route[i];
					}
					temproute[strlen(temproute) + 1] = 0;
					temproute[strlen(temproute)] = ' ';
					temproute[strlen(temproute) + 1] = 0;
					temproute[strlen(temproute)] = inttochar(currver + 1);
					getallpath(mas, visited, i, endver, head, tempweight, temproute);
				}
				tmp = tmp->next;
			}
		}
	}
}
char inttochar(int number)
{
	char result;
	result = '0' + number;
	return result;
}
void sortsp(struct pathlist* head)
{
	struct pathlist* predstart = head;
	struct pathlist* startelem = head->next;
	struct pathlist* minelem = NULL;
	while (startelem->next != NULL)
	{
		minelem = startelem;
		struct pathlist* tmp = minelem->next;
		struct pathlist* currpred = minelem;
		struct pathlist* pred = NULL;
		while (tmp != NULL)
		{
			int minweight = minelem->ourpath.weight;
			int tmpweight = tmp->ourpath.weight;
			if (tmpweight < minweight)
			{
				pred = currpred;
				minelem = tmp;
			}
			currpred = tmp;
			tmp = tmp->next;
		}
		if (minelem != startelem)
		{
			if (pred == startelem)
			{
				struct node* buffer = NULL;
				predstart->next = minelem;
				startelem->next = minelem->next;
				minelem->next = startelem;
				predstart = minelem;
				startelem = minelem->next;
			}
			else
			{
				struct node* buffer = NULL;
				buffer = startelem->next;
				pred->next = startelem;
				startelem->next = minelem->next;
				predstart->next = minelem;
				minelem->next = buffer;
				predstart = minelem;
				startelem = buffer;
			}
		}
		else
		{
			predstart = minelem;
			startelem = startelem->next;
		}
	}
}
void center(struct list* mas[])
{
	int A[6][6];//матрица цен
	for (int i = 0;i < 6;i++)
	{
		for (int j = 0;j < 6;j++)
		{
			if (i == j)
			{
				A[i][j] = 0;
			}
			else
			{
				int flag = 0;
				struct list* temp = mas[i]->next;
				while (temp != NULL && flag == 0)
				{ 
					if (temp->number == j + 1)
					{
						A[i][j] = temp->weight;
						flag = 1;
					}
					temp = temp->next;
				}
				if (flag == 0)
				{
					A[i][j] = 99;
				}
				
			}
		}
	}
	for (int k = 0;k < 6;k++)
	{
		for (int i = 0;i < 6;i++)
		{
			for (int j = 0;j < 6;j++)
			{
				if ((A[i][k] + A[k][j]) < A[i][j])
				{
					A[i][j] = A[i][k] + A[k][j];
				}
			}
		}
	}
	printf("Floid algorithm matrix:\n");
	for (int i = 0;i < 6;i++)
	{
		for (int j = 0;j < 6;j++)
		{
			printf("%3d", A[i][j]);
		}
		printf("\n");
	}
	int exs[6];
	for (int i = 0;i < 6;i++)
	{
		exs[i] = 0;
	}
	for (int i = 0;i < 6;i++)
	{
		for (int j = 0;j < 6;j++)
		{
			if (A[j][i] > exs[i] && i!=j)
			{
				exs[i] = A[j][i];
		  }
		}
	}
	printf("Escentricities:\n");

	int min = 0;
	for (int i = 0;i < 6;i++)
	{
		printf("%3d", exs[i]);
		if (exs[i] < exs[min])
		{
			min = i;
		}
	}
	for (int i = 0;i < 6;i++)
	{
		if (exs[i] == exs[min])
		{
			printf("\nCenter of graf is:ver number %d", i + 1);
		}
	}
}