#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

typedef struct node {
	char value;
	struct node* left;
	struct node* right;
	struct node* parent;
}node;
char treearr[1000];
void addnode(node **head,char value)
{
	if (*head == NULL)
	{
		*head = (node*)malloc(sizeof(node));
		(*head)->left = NULL;
		(*head)->right = NULL;
		(*head)->parent = NULL;
	}
	else
	{
		if (value == '(')
		{
			node* tmp = (node*)malloc(sizeof(node));
			tmp->left = NULL;
			tmp->right = NULL;
			tmp->parent = *head;
			(*head)->left = tmp;
			*head = tmp;
		}
		else 
			if (value == '+' || value == '-' || value == '/' || value == '*' || value == '^')
			{
				(*head)->value = value;
				node* tmp = (node*)malloc(sizeof(node));
				(*head)->right = tmp;
				tmp->parent = *head;
				tmp->left = NULL;
				tmp->right = NULL;
				(*head) = tmp;
			}
			else 
				if (value == ')')
				{
					(*head) = (*head)->parent;
				}
				else
				{
					(*head)->value = value;
					(*head) = (*head)->parent;
				}
	}
}
void postorder(node* tree,int i )
{
	int starti = i;
	if (tree->left != NULL) {
		i = i * 2 + 1;
		treearr[i] = tree->left->value;
			postorder(tree->left,i);
			
	}
	if (tree->right != NULL) {
		i = starti * 2 + 2;
		treearr[i] = tree->right->value;
		postorder(tree->right, i);
					
	}
	printf("%c", tree->value);
}
int show(node* tree, int lvl, int rank) {
	node* curr = tree;
	int curr_lvl = 0;
	//array borders to get elements
	int st = 0;
	int nd = 0;
	//tabs count from left corner and other elements
	int tbs_l = 0;
	int tbs_c = 3;


	//counting tabs
	for (int i = 0; i < lvl - 1; i++) {
		tbs_l = tbs_c;
		tbs_c =2* (tbs_c + 1) + 1;
	}
	tbs_c = tbs_l;
	node* el = NULL;
	while (curr_lvl < lvl) {
		st = pow(2, curr_lvl) - 1;
		nd = 2 * st;
		for (int j = st; j <= nd; j++) {
			if (j == st) {
				for (int i = 0; i < tbs_l; i++) {
					printf(" ");
				}
			}



			if (treearr[j] == 0) {
				for (int i = 0; i < rank; i++) {
					printf_s(" ");
				}

			}
			else
			{
				printf_s("%3c", treearr[j]);

			}
			for (int i = 0; i < tbs_c; i++) {
				printf(" ");
			}
		}

		printf("\n");
		tbs_c = tbs_l;
		tbs_l = (tbs_l - 1) / 2 - 1;
		printf("\n");

		curr_lvl++;
	}
}
int getmaxdepth(node* tree)
{
	int ret = 0;
	if (tree)
	{
		int lDepth = getmaxdepth(tree->left);
		int rDepth = getmaxdepth(tree->right);
		ret = max(lDepth + 1, rDepth + 1);
	}
	return ret;
}
int main() {
	char inf[30] = "((a+b)+(c*((y-(g*d))^n)))";
	//char inf[60] = "(p*((d+(g+(h/c)))-(t*j)))";
	node* head = NULL;
	addnode(&head, '0');
	node* firsthead = head;
	for (int i = 0;i <= strlen(inf);i++)
	{
		addnode(&head, inf[i]);
	}
	treearr[0] = firsthead->value;
	postorder(firsthead,0);
	printf("\n\n\n");
	int level = 0;
	level = getmaxdepth(firsthead);
	show(firsthead, level, 3);
	return 0;
}