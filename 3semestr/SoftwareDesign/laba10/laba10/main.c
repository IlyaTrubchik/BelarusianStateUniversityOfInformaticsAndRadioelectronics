#include <stdio.h>
#include <stdlib.h>
#include <time.h>
typedef struct stack_node {
	struct stack_node* next;
	int value;
}stack_node;
void push(stack_node** head, int value)
{
	stack_node* tmp = (stack_node*)malloc(sizeof(stack_node));
	tmp->next = (*head);
	tmp->value = value;
	*head = tmp;
}

int pop(stack_node** head)
{
	if (*head == NULL)
	{
		printf("\nStack is empty\n");
		return 0;
	}
	else
	{
		stack_node* tmp = *head;
		*head = tmp->next;
		int value = tmp->value;
		free(tmp);
		return value;
	}

}
void free_stack(stack_node** head)
{
	while (*head != NULL)
	{
		pop(head);
	}
}
void print_stack(stack_node* head)
{
	printf("\nOur Stack:\n");
	while (head != NULL)
	{
		printf("%3d", head->value);
		head = head->next;
	}
}
void init_stack(int count, stack_node** head)
{
	srand(time(NULL));
	for (int i = 0;i < count;i++)
	{
		int value = rand() % 100;
		push(head, value);
	}
}
void transform_stack(stack_node** head)
{
	stack_node* tmp_head = NULL;
	while (*head != NULL)
	{
		int value = pop(head);
		if (value % 2 != 0)
		{
			push(&tmp_head,value);
		}
	}
	*head = tmp_head;
}
int main() {
	stack_node* head = NULL;
	init_stack(30, &head);
	print_stack(head);
	transform_stack(&head);
	print_stack(head);
	free_stack(&head);
	return 0;
}