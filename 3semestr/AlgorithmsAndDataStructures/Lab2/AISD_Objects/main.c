#include <stdio.h>
#include <stdlib.h>
#include <string.h>
void edittermin(struct node* mainhead, int level);
void findtermbysub(struct node* first, char subterm[50]);
void add_page(struct node* ourtermin);
void deletetermin(struct node* ourtermin);
void freepages(struct node* ourtermin);
void sortbypages(struct node* mainhead);
struct pages_node {
	char number[10];
	struct pages_node* nextpage;
};
struct node {
	char  termin[50];
	struct node* subtermin;
	struct node* nextterm;
	struct pages_node* pages;
};
struct node* inithead()
{
	struct node* head;
	head = (struct node*)malloc(sizeof(struct node));
	head->nextterm = NULL;
	head->pages = NULL;
	head->subtermin = NULL;
	head->termin[0] = 0;
	return head;
}
void add_inittermin(struct node* mainhead, char termin[50],char pagenumber[10])
{
	while (mainhead->nextterm != NULL)
	{
		mainhead = mainhead->nextterm;
	}
	struct node* tmp = (struct node*)malloc(sizeof(struct node));
	for (int i = 0;i <= strlen(termin);i++)
	{
		tmp->termin[i] = termin[i];
	}
	mainhead->nextterm = tmp;
	tmp->nextterm = NULL;
	tmp->subtermin = NULL;
	struct pages_node* page = (struct pages_node*)malloc(sizeof(struct pages_node));
	tmp->pages = page;
	page->nextpage = NULL;
	{
		//char number[10] = "12";
		for (int i = 0;i < strlen(pagenumber);i++)
		{
			page->number[i] = pagenumber[i];
			page->number[i + 1] = 0;
		}
	}
}
void enter_termin(char termin[50])
{
	char newtermin[50];
	
	rewind(stdin);
	fgets(newtermin,50,stdin);
	for (int i = 0;i < strlen(newtermin);i++)
	{
		termin[i] = newtermin[i];
	}
	termin[strlen(newtermin)-1] = 0;
}
struct node* add_termin(struct node* lastelem)
{
	struct node* tmp;
	tmp = (struct node*)malloc(sizeof(struct node));
	tmp->nextterm = NULL;
	printf("Add Termin:\n");
	enter_termin(tmp->termin);
	char pagenumber[10];
	tmp->pages = NULL;
	int counter = 0;
	printf("Enter number of pages:\n");
	scanf_s("%d", &counter);
	for (int i = 0; i < counter;i++)
	{
		printf("enter page number\n");
		rewind(stdin);
		add_page(tmp);
	}
	tmp->subtermin = NULL;
	lastelem->nextterm = tmp;
	return tmp;
	
}
void add_subterm(struct node* ourtermin)
{
	struct node* subtermin;
	subtermin = (struct node*)malloc(sizeof(struct node));
	subtermin->nextterm = NULL;
	printf("Enter Subterm:\n");
	enter_termin(subtermin->termin);
	subtermin->pages = NULL;
	int counter = 0;
	printf("Enter number of pages:\n");
	scanf_s("%d", &counter);
	for (int i = 0; i < counter;i++)
	{
		printf("enter page number\n");
		rewind(stdin);
		add_page(subtermin);
	}
	subtermin->subtermin = NULL;
	if (ourtermin->subtermin == NULL)
	{
		struct node* head;
		head = (struct node*)malloc(sizeof(struct node));
		ourtermin->subtermin = head;
		head->nextterm = subtermin;
		head->subtermin = NULL;
		head->pages = NULL;
	}
	else
	{
		struct node* lasttermin;
		lasttermin = ourtermin->subtermin;
		while (lasttermin->nextterm != NULL)
		{
			lasttermin = lasttermin->nextterm;
		}
		lasttermin->nextterm = subtermin;
	}
}
void newinitlist(struct node* mainhead)
{
	add_inittermin(mainhead, "Table", "25");
	add_inittermin(mainhead, "Smartphone", "15");
	add_inittermin(mainhead, "Professions", "13");
	struct node* tmp = (struct node*)malloc(sizeof(struct node));
	tmp->subtermin = NULL;
	tmp->nextterm = NULL;
	tmp->pages = NULL;
	mainhead->nextterm->subtermin = tmp;
	struct node* first_subhead = tmp;
	add_inittermin(mainhead->nextterm->subtermin, "Wooden", "143");
	add_inittermin(mainhead->nextterm->subtermin, "Metalic", "120");
	tmp = (struct node*)malloc(sizeof(struct node));
	tmp->subtermin = NULL;
	tmp->nextterm = NULL;
	tmp->pages = NULL;
	first_subhead->nextterm->subtermin = tmp;
	add_inittermin(first_subhead->nextterm->subtermin, "Oak", "12");
	add_inittermin(first_subhead->nextterm->subtermin, "Maple", "16");
	add_inittermin(first_subhead->nextterm->subtermin, "Birch", "12");
	tmp = (struct node*)malloc(sizeof(struct node));
	tmp->subtermin = NULL;
	tmp->nextterm = NULL;
	tmp->pages = NULL;
	mainhead->nextterm->nextterm->subtermin = tmp;
	struct node* second_sub = tmp;
	add_inittermin(tmp, "IOS", "45");
	add_inittermin(tmp, "Android", "55");
	tmp = (struct node*)malloc(sizeof(struct node));
	tmp->subtermin = NULL;
	tmp->nextterm = NULL;
	tmp->pages = NULL;
	second_sub->nextterm->subtermin = tmp;
	add_inittermin(tmp, "Google", "16");
	add_inittermin(tmp, "Xiaomi", "5");
	add_inittermin(tmp, "Huawei", "2");
	tmp = (struct node*)malloc(sizeof(struct node));
	tmp->subtermin = NULL;
	tmp->nextterm = NULL;
	tmp->pages = NULL;
	second_sub->nextterm->nextterm->subtermin = tmp;
	add_inittermin(tmp, "Apple", "23");
	tmp = (struct node*)malloc(sizeof(struct node));
	tmp->nextterm = NULL;
	tmp->subtermin = NULL;
	tmp->pages = NULL;
	mainhead->nextterm->nextterm->nextterm->subtermin = tmp;
	add_inittermin(tmp, "Physical", "234");
	add_inittermin(tmp, "Intellectual", "54");
	add_inittermin(mainhead, "Computer", "55");
}
void initlist(struct node* mainhead)
{
	struct node* tmp = NULL;
	struct node* last = mainhead;
	struct pages_node* page = NULL;
	tmp = (struct node*)malloc(sizeof(struct node));
	last->nextterm = tmp;
	tmp->nextterm = NULL;
	tmp->subtermin = NULL;
	page = (struct pages_node*)malloc(sizeof(struct pages_node));
	tmp->pages = page;
	page->nextpage = NULL;
	{
		char number[10] = "234";
		for (int i = 0;i < strlen(number);i++)
		{
			page->number[i] = number[i];
			page->number[i + 1] = 0;
		}
	}
	{
		char term[50] = "YOUTUBE";
		for (int i = 0;i < strlen(term);i++)
		{
			tmp->termin[i] = term[i];
			tmp->termin[i + 1] = 0;
		}
	}
	last = last->nextterm;

	tmp = (struct node*)malloc(sizeof(struct node));
	last->nextterm = tmp;
	tmp->nextterm = NULL;
	tmp->subtermin = NULL;
	page = (struct pages_node*)malloc(sizeof(struct pages_node));
	tmp->pages = page;
	page->nextpage = NULL;
	{
		char number[10] = "123";
		for (int i = 0;i < strlen(number);i++)
		{
			page->number[i] = number[i];
			page->number[i + 1] = 0;
		}
	}

	{
		char term[50] = "TABLE";
		for (int i = 0;i < strlen(term);i++)
		{
			tmp->termin[i] = term[i];
			tmp->termin[i + 1] = 0;
		}
	}
	last = last->nextterm;
	tmp = (struct node*)malloc(sizeof(struct node));
	last->nextterm = tmp;
	tmp->nextterm = NULL;
	tmp->subtermin = NULL;
	page = (struct pages_node*)malloc(sizeof(struct pages_node));
	tmp->pages = page;
	page->nextpage = NULL;
	{
		char number[10] = "126";
		for (int i = 0;i < strlen(number);i++)
		{
			page->number[i] = number[i];
			page->number[i + 1] = 0;
		}
	}

	{
		char term[50] = "THING";
		for (int i = 0;i < strlen(term);i++)
		{
			tmp->termin[i] = term[i];
			tmp->termin[i + 1] = 0;
		}
		last = last->nextterm;
	}
	tmp = (struct node*)malloc(sizeof(struct node));
	last->nextterm = tmp;
	tmp->nextterm = NULL;
	tmp->subtermin = NULL;
	page = (struct pages_node*)malloc(sizeof(struct pages_node));
	tmp->pages = page;
	page->nextpage = NULL;
	{
		char number[10] = "15";
		for (int i = 0;i < strlen(number);i++)
		{
			page->number[i] = number[i];
			page->number[i + 1] = 0;
		}
	}

	{
		char term[50] = "Computer";
		for (int i = 0;i < strlen(term);i++)
		{
			tmp->termin[i] = term[i];
			tmp->termin[i + 1] = 0;
		}
		last = last->nextterm;
	}
	tmp = (struct node*)malloc(sizeof(struct node));
	last->subtermin = tmp;
	last->subtermin->subtermin = NULL;
	last->subtermin->pages = NULL;
	tmp = (struct node*)malloc(sizeof(struct node));
	last->subtermin->nextterm = tmp;
	tmp->nextterm = NULL;
	tmp->subtermin = NULL;
	page = (struct pages_node*)malloc(sizeof(struct pages_node));
	tmp->pages = page;
	page->nextpage = NULL;
	{
		char number[10] = "16";
		for (int i = 0;i < strlen(number);i++)
		{
			page->number[i] = number[i];
			page->number[i + 1] = 0;
		}
	}

	{
		char term[50] = "Processor";
		for (int i = 0;i < strlen(term);i++)
		{
			tmp->termin[i] = term[i];
			tmp->termin[i + 1] = 0;
		}
		last = last->subtermin->nextterm;
	}
	tmp = (struct node*)malloc(sizeof(struct node));
	last->nextterm = tmp;
	tmp->nextterm = NULL;
	tmp->subtermin = NULL;
	page = (struct pages_node*)malloc(sizeof(struct pages_node));
	tmp->pages = page;
	page->nextpage = NULL;
	{
		char number[10] = "13";
		for (int i = 0;i < strlen(number);i++)
		{
			page->number[i] = number[i];
			page->number[i + 1] = 0;
		}
	}
	{
		char term[50] = "GPU";
		for (int i = 0;i < strlen(term);i++)
		{
			tmp->termin[i] = term[i];
			tmp->termin[i + 1] = 0;
		}
		last = last->nextterm;
	}
	tmp = (struct node*)malloc(sizeof(struct node));
	last->subtermin = tmp;
	last->subtermin->subtermin = NULL;
	last->subtermin->pages = NULL;
	tmp = (struct node*)malloc(sizeof(struct node));
	last->subtermin->nextterm = tmp;
	tmp->nextterm = NULL;
	tmp->subtermin = NULL;
	page = (struct pages_node*)malloc(sizeof(struct pages_node));
	tmp->pages = page;
	page->nextpage = NULL;
	{
		char number[10] = "18";
		for (int i = 0;i < strlen(number);i++)
		{
			page->number[i] = number[i];
			page->number[i + 1] = 0;
		}
	}
	{
		char term[50] = "Diamond";
		for (int i = 0;i < strlen(term);i++)
		{
			tmp->termin[i] = term[i];
			tmp->termin[i + 1] = 0;
		}
		last = last->subtermin->nextterm;
	}
	last = mainhead->nextterm->nextterm;
	tmp = (struct node*)malloc(sizeof(struct node));
	last->subtermin = tmp;
	last->subtermin->subtermin = NULL;
	last->subtermin->pages = NULL;
	tmp = (struct node*)malloc(sizeof(struct node));
	last->subtermin->nextterm = tmp;
	tmp->nextterm = NULL;
	tmp->subtermin = NULL;
	page = (struct pages_node*)malloc(sizeof(struct pages_node));
	tmp->pages = page;
	page->nextpage = NULL;
	{
		char number[10] = "19";
		for (int i = 0;i < strlen(number);i++)
		{
			page->number[i] = number[i];
			page->number[i + 1] = 0;
		}
	}
	{
		char term[50] = "WOOD";
		for (int i = 0;i < strlen(term);i++)
		{
			tmp->termin[i] = term[i];
			tmp->termin[i + 1] = 0;
		}
		last = last->subtermin->nextterm;
	}
	tmp = (struct node*)malloc(sizeof(struct node));
	last->nextterm = tmp;
	tmp->nextterm = NULL;
	tmp->subtermin = NULL;
	page = (struct pages_node*)malloc(sizeof(struct pages_node));
	tmp->pages = page;
	page->nextpage = NULL;
	{
		char number[10] = "20";
		for (int i = 0;i < strlen(number);i++)
		{
			page->number[i] = number[i];
			page->number[i + 1] = 0;
		}
	}
	{
		char term[50] = "METAL";
		for (int i = 0;i < strlen(term);i++)
		{
			tmp->termin[i] = term[i];
			tmp->termin[i + 1] = 0;
		}
	}
	last = last->nextterm;
}
void print_termins(struct node* head)
{
	head = head->nextterm;
	printf("Termins:\t");
	printf("\n");
	while (head != NULL)
	{
		puts(head->termin);
		head = head->nextterm;
	}
}
void add_page(struct node* ourtermin)
{
	char number[10];
	fgets(number, 10, stdin);
	number[strlen(number) - 1] = 0;
	struct pages_node* page;
	if (ourtermin->pages == NULL)
	{
		page = (struct pages_node*)malloc(sizeof(struct pages_node*));
		ourtermin->pages = page;
	
		for (int i = 0;i <= strlen(number);i++)
		{
			page->number[i] = number[i];
		}

		page->nextpage = NULL;
	}
	else
	{
		page = (struct pages_node*)malloc(sizeof(struct pages_node*));
		for (int i = 0;i <= strlen(number);i++)
		{
			page->number[i] = number[i];
		}
		page->nextpage = NULL;
		struct pages_node* lastpage;
		lastpage = ourtermin->pages;
		while (lastpage->nextpage != NULL)
		{
			lastpage = lastpage->nextpage;
		}
		lastpage->nextpage = page;
	}
}
void printfstring(char a[50])
{
	for (int i = 0;i < strlen(a); i++)
	{
		printf("%c", a[i]);
	}
}
void printallterm(struct node* first,int level)
{
	int startlevel = level;
	while (first != NULL)
	{
		for (int i = 1;i <= level; i++)
		{
			printf("\t");
		}
		printfstring(first->termin);
		printf(",pages:");
		struct pages_node* page;
		page = first->pages;
		while (page != NULL)
		{
			for ( int i = 0; i < strlen(page->number); i++)
			{
				printf("%c", page->number[i]);
			}
			printf(";");
			page = page->nextpage;
		}
		
		if (first->subtermin == NULL)
		{
			first = first->nextterm;
			printf("\n");
		}
		else
		{
			printf("\n");
			level++;
			struct node* lastsubterm;
			printallterm(first->subtermin->nextterm,level);
			first = first->nextterm;
		}
		level = startlevel;
		
	}
}
void printfonlytermins(struct node* first)
{
	first = first->nextterm;
	while (first != NULL)
	{
		printfstring(first->termin);
		printf(",pages:");
		struct pages_node* page;
		page = first->pages;
		while (page != NULL)
		{
			for (int i = 0; i < strlen(page->number); i++)
			{
				printf("%c", page->number[i]);
			}
			printf(";");
			page = page->nextpage;
		}
		printf("\n");
		first = first->nextterm;
	}
}
struct node* chooseterm(char text[50], struct node* first)
{
	struct node* pred = first;
	first = first->nextterm;
	int flag = 0;
	while (first != NULL && flag == 0)
	{
		int lettercount = 0;
		for (int i = 0; i < strlen(text); i++)
		{
			if (text[i] == first->termin[i])
			{
				lettercount++;
			}
		}
		if (lettercount == strlen(text))
		{
			flag = 1;
			return pred;
		}
		pred = first;
		first = first->nextterm;
	}
}
void sortbyalphabet(struct node* mainhead)
{
	struct node* predstart = mainhead;
	struct node* startelem = mainhead->nextterm;
	struct node* minelem = NULL;
	while (startelem->nextterm != NULL)
	{
		minelem = startelem;
		struct node* tmp = minelem->nextterm;
		struct node* currpred = minelem;
		struct node* pred = NULL;
		while (tmp != NULL)
		{
			if (strcmp(minelem->termin, tmp->termin) > 0)
			{
				pred = currpred;
				minelem = tmp;
			}
			currpred = tmp;
			tmp = tmp->nextterm;
		}
		if (minelem != startelem)
		{
			if (pred == startelem)
			{
				struct node* buffer = NULL;
				predstart->nextterm = minelem;
				startelem->nextterm = minelem->nextterm;
				minelem->nextterm = startelem;
				predstart = minelem;
				startelem = minelem->nextterm;
			}
			else
			{
				struct node* buffer = NULL;
				buffer = startelem->nextterm;
				pred->nextterm = startelem;
				startelem->nextterm = minelem->nextterm;
				predstart->nextterm = minelem;
				minelem->nextterm = buffer;
				predstart = minelem;
				startelem = buffer;
			}
		}
		else
		{
			predstart = minelem;
			startelem = startelem->nextterm;
		}
	}
}
struct node* Foradding(struct node* first,int level, int delflag)
{
	int errorflag = 0;
	struct node* choosen = NULL;
	struct node* pred = NULL;
	while (level != 0)
	{
		if (delflag == 0)
		{
			printf("Choose termin for adding subtermin\n");
		}
		else
		{
			if (delflag == 1)
			{
				printf("Choose deleting termin\n");
			}
			else
			{
				printf("Choose termin for sorting subtermins\n");
			}
		}
		printfonlytermins(first);
		char text[60];
		rewind(stdin);
		fgets(text, 50, stdin);
		text[strlen(text) - 1] = 0;
		choosen = chooseterm(text, first);
		if (level != 1 && choosen->nextterm->subtermin != NULL)
		{

			first = choosen->nextterm->subtermin;
			
		}
		else 
			if (level != 1 && choosen->subtermin == NULL)
			{
				errorflag = 1;
				level = 1;
			}
		level--;
	}
	if (errorflag == 0 &&  choosen != NULL)
	{
		if (delflag == 0)
		{
			add_subterm(choosen->nextterm);
		}
		else
		{
			return(choosen);
		}
	}
	else
	{
		if (delflag == 0)
		{
			printf("Subtermin cannot be added\n");
		}
		else
		{
			if (delflag == 1)
			{
				printf("Cannot be deleted\n");

			}
			else
			{
				printf("Cannot be sorted\n");
			}
		}
	}
}
void freememory(struct node* ourtermin)
{
	struct node* pred = NULL;
	struct node* start = ourtermin;
		while (ourtermin->nextterm != NULL)
		{
			if (ourtermin->subtermin != NULL)
			{
				freememory(ourtermin->subtermin);
			}
			else
			{
				pred = ourtermin;
				ourtermin = ourtermin->nextterm;
			}
		}
		while (start->nextterm != NULL)
		{
			freepages(ourtermin);
			free(ourtermin);
			
			pred->nextterm = NULL;
			ourtermin = start;
			while (ourtermin->nextterm != NULL)
			{
				pred = ourtermin;
				ourtermin = ourtermin->nextterm;
			}

		}
		if (start->pages != NULL)
		{
			freepages(start);
		}
		free(start);
		
}
void freepages(struct node* ourtermin)
{
	struct pages_node* pages_list = ourtermin->pages;
	struct pages_node* pred = NULL;
	struct pages_node* pages_list_start = pages_list;
	while (pages_list_start->nextpage != NULL)
	{
		pages_list = pages_list_start;
		while (pages_list->nextpage != NULL)
		{
			pred = pages_list;
			pages_list = pages_list->nextpage;
		}
		free(pages_list);
		pred->nextpage = NULL;
	}
	free(pages_list_start);
}
void deletetermin(struct node* ourtermin, struct node* mainhead,struct node* pred)
{
	
	if (ourtermin->subtermin != NULL)
	{
		freememory(ourtermin->subtermin);
	}
	pred->nextterm = ourtermin->nextterm;
	freepages(ourtermin);
	free(ourtermin);
	
}
void menu()
{
	struct node* mainhead = inithead();
	struct node* lastelem = mainhead;
	initlist(mainhead);
	//newinitlist(mainhead);
	while (lastelem->nextterm != NULL)
	{
		lastelem = lastelem->nextterm;
	}
	int key = 0;
	struct node* first = mainhead->nextterm;

	printf("1. View termins\n");
	printf("2. Add termin\n");
	printf("3. Find termin by subterm\n");
	printf("4. Add subtermin\n");
	printf("5. Delete termin\n");
	printf("6. Sort termins by alphabet\n");
	printf("7. SortByPages\n");
	printf("8. Edit termin\n");
	printf("9. End programm\n");
	printf("Enter number:\n");
	rewind(stdin);
	scanf_s("%d", &key);
	while (key != 9)
	{
		switch (key)
		{
			case 4:
			{
				struct node* tmp = NULL;
				printf("Choose level of subterm\n");
				int level = 0;
				scanf_s("%d", &level);
				Foradding(mainhead, level, 0);
				break;
			}
			case 1:
			{
				printallterm(mainhead->nextterm, 0);
				break;
			}
			case 2:
			{
				lastelem = add_termin(lastelem);
				break;
			}
			case 3:
			{
				char subterm[50];
				printf("enter subterm for serching:\n");
				rewind(stdin);
				fgets(subterm, 50, stdin);
				subterm[strlen(subterm) - 1] = 0;
				struct node* lastfoundedterm;
				first = mainhead->nextterm;
				findtermbysub(first, subterm);
				break;
			}
			case 5:
			{
				int level = 0;
				printf("Enter level of deleting termin(0-maintermins list)\n");
				rewind(stdin);
				scanf_s("%d", &level);
				level++;
				first = Foradding(mainhead, level, 1);
				lastelem = first;
				level--;
				deletetermin(first->nextterm, level, first);
				break;
			}
			case 6:
			{

				printf("Choose level of subterm:\n");
				int level = 0;
				scanf_s("%d", &level);
				if (level == 0)
				{
					sortbyalphabet(mainhead);
				}
				else
				{
					struct node* head = NULL;
					head = Foradding(mainhead, level, 2);
					head = head->nextterm;
					if (head->subtermin != NULL)
					{
						sortbyalphabet(head->subtermin);
					}
				}
				lastelem = mainhead;
				while (lastelem->nextterm != NULL)
				{
					lastelem = lastelem->nextterm;
				}
				break;
			}
			case 7:
			{
				printf("Choose level of subterm:\n");
				int level = 0;
				scanf_s("%d", &level);
				if (level == 0)
				{
					sortbypages(mainhead);
				}
				else
				{
					struct node* head = NULL;
					head = Foradding(mainhead, level, 2);
					head = head->nextterm;
					if (head->subtermin != NULL)
					{
						sortbypages(head->subtermin);
					}
				}
				lastelem = mainhead;
				while (lastelem->nextterm != NULL)
				{
					lastelem = lastelem->nextterm;
				}
				break;
			}
			case 8:
			{
				int level = 0;
				printf("Enter level of termin:\n");
				rewind(stdin);
				scanf_s("%d", &level);
				edittermin(mainhead, level);
				break;
			}
		
		}
		printf("1. View termins\n");
		printf("2. Add termin\n");
		printf("3. Find termin by subterm\n");
		printf("4. Add subtermin\n");
		printf("5. Delete termin\n");
		printf("6. Sort termins by alphabet\n");
		printf("7. SortByPages\n");
		printf("8. Edit termin\n");
		printf("9. End programm\n");
		printf("Enter number:\n");
		scanf_s("%d", &key);
	}
}
struct node* findtermbypage(struct node* first,int pagenum)
{
		
}
int strtoint(char number[10])
{
	int k = 1;
	char zero[1] = "0";
	int result = 0;
	for (int i = strlen(number) - 1;i >= 0;i--)
	{
		result = result + (number[i] - zero[0])*k;
		k = k * 10;
	}
	return result;
}
int findminpage(struct pages_node* page)
{
	int min;
	min = strtoint(page->number);
	page = page->nextpage;
	while (page != NULL)
	{
		if (strtoint(page->number) < min)
		{
			min = strtoint(page->number);
		}
		page = page->nextpage;
	}
	return min;
}
void sortbypages(struct node* mainhead)
{
	struct node* predstart = mainhead;
	struct node* startelem = mainhead->nextterm;
	struct node* minelem = NULL;
	while (startelem->nextterm != NULL)
	{
		minelem = startelem;
		struct node* tmp = minelem->nextterm;
		struct node* currpred = minelem;
		struct node* pred = NULL;
		while (tmp != NULL)
		{
			int minelempage = findminpage(minelem->pages);
			int tmppage = findminpage(tmp->pages);
			if (tmppage < minelempage)
			{
				pred = currpred;
				minelem = tmp;
			}
			currpred = tmp;
			tmp = tmp->nextterm;
		}
		if (minelem != startelem)
		{
			if (pred == startelem)
			{
				struct node* buffer = NULL;
				predstart->nextterm = minelem;
				startelem->nextterm = minelem->nextterm;
				minelem->nextterm = startelem;
				predstart = minelem;
				startelem = minelem->nextterm;
			}
			else
			{
				struct node* buffer = NULL;
				buffer = startelem->nextterm;
				pred->nextterm = startelem;
				startelem->nextterm = minelem->nextterm;
				predstart->nextterm = minelem;
				minelem->nextterm = buffer;
				predstart = minelem;
				startelem = buffer;
			}
		}
		else
		{
			predstart = minelem;
			startelem = startelem->nextterm;
		}
	}
}
int checkterm(struct node* subtermhead,char term[50])
{
	struct node* subtermin = subtermhead->nextterm;
	while (subtermin != NULL)
	{
		int lettercount = 0;
		for (int i = 0;i < strlen(term);i++)
		{
			if (term[i] == subtermin->termin[i])
			{
				lettercount++;
			}
		}
		if (lettercount == strlen(term))
		{
			subtermin = NULL;
			return 1;
		}
		else
		{
			if (subtermin->subtermin != NULL)
			{
				if (checkterm(subtermin->subtermin, term) == 1)
				{
					subtermin = NULL;
					return 1;
				}
			}
			subtermin = subtermin->nextterm;
		}
	}
}
void findtermbysub(struct node* first, char subterm[50])
{
	printf("Founded termins:\n");
	while (first != NULL)
	{
		if (first->subtermin != NULL)
		{
			if (checkterm(first->subtermin, subterm) == 1)
			{
				printfstring(first->termin);
				printf("\n");
			}	
		}
		first = first->nextterm;
	}
}
void edittermin(struct node* mainhead, int level)
{
	struct node* choosen = NULL;
	while (level != 0 && level != -1)
	{
		printf("Choose termin:\n");
		print_termins(mainhead);
		char termin[50];
		rewind(stdin);
		fgets(termin, 50, stdin);
		termin[strlen(termin) - 1] = 0;
		choosen = chooseterm( termin,mainhead);
		choosen = choosen->nextterm;
		if (choosen->subtermin != NULL)
		{
			mainhead = choosen->subtermin;
			level--;
		}
		else
		{
			level = -1;
		}
		printf("\n");
	}
	if (level == 0)
	{
		printf("Choose termin you want to edit:\n");
		print_termins(mainhead);
		printf("\n");
		char termin[50];
		rewind(stdin);
		fgets(termin, 50, stdin);
		termin[strlen(termin) - 1] = 0;
		choosen = chooseterm(termin,mainhead);
		choosen = choosen->nextterm;
		printf("Enter new termin\n");
		rewind(stdin);
		fgets(termin, 50, stdin);
		for (int i = 0;i < strlen(termin);i++)
		{
			choosen->termin[i] = termin[i];
		}
		choosen->termin[strlen(termin) - 1] = 0;
		int newpages;
		printf("Enter new number of pages:\n");
		rewind(stdin);
		scanf_s("%d", &newpages);
		freepages(choosen);
		choosen->pages = NULL;
		for (int i = 0;i < newpages;i++)
		{
			printf("enter page number\n");
			rewind(stdin);
			add_page(choosen);
		}
	}
	else 
	{
		printf("Current termin hasnt entered level subtermin\n");
	}

}
void main()
{
	menu();
	return 0;
}