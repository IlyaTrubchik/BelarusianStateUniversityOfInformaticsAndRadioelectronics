#include <stdio.h>
#include <stdlib.h>
struct list {
	float number;
	struct list* next;
};
void add_elem(struct list* head, float num);
void insert(struct list* pred, float num);
float findfirstpositive(struct list* head);
void insertpositive(struct list* head, float num);
void printlist(struct list* head);
void initlist(struct list* head);


void main() {
	struct list* head;
	head = (struct list*)malloc(sizeof(struct list));
	head->next = NULL;
	initlist(head);
	printf("First list:\n");
	printlist(head);
	float num = findfirstpositive(head);
	if (num > 0)
	{
		insertpositive(head, num);
	}
	printf("New list:\n");
	printlist(head);
}
void add_elem(struct list* head, float num) {
	while (head->next != NULL)
	{
		head = head->next;
	}
	struct list* new;
	new = (struct list*)malloc(sizeof(struct list));
	new->next = NULL;
	new->number = num;
	head->next = new;
}
void insert(struct list* pred, float num)
{
	struct list* new;
	new = (struct list*)malloc(sizeof(struct list));
	new->next = pred->next;
	new->number = num;
	pred->next = new;
}
float findfirstpositive(struct list* head)
{
	head = head->next;
	int count = 0;
	while (head != NULL)
	{
		if (head->number > 0)
		{
			count++;
			return head->number;
			break;
		}
		head = head->next;
	}
	if (count == 0)
	{
		printf("There is no positive number\n");
		return -1;
	}
}
void insertpositive(struct list* head, float num)
{
	head = head->next;
	while (head != NULL)
	{
		if (head->number < 0)
		{
			insert(head, num);
		}
		head = head->next;
	}
}
void printlist(struct list* head)
{
	struct list* first = head->next;
	while (first != NULL)
	{
		printf("%.3f ", first->number);
		first = first->next;
	}
	printf("\n");
}
void initlist(struct list* head)
{
	add_elem(head,- 5.5);
	add_elem(head, -15.5);
	add_elem(head,- 5.64);
	add_elem(head,- 3.2);
	add_elem(head, 7.05);
	add_elem(head,- 1.25);
	add_elem(head,- 3.33);
	add_elem(head, 6.66);
	add_elem(head, 10.5);
	add_elem(head,- 64.5);
}

