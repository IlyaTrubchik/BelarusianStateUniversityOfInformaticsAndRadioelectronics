#include <stdlib.h>
#include <stdio.h>
#include <math.h>
typedef struct treenode {
	int value;
	struct treenode* leftlief;
	struct treenode* rightlief;
}treenode;
int treearr[100];
treenode* addnode(treenode* tree,int value,int  i)
{
	if (tree == NULL)
	{
		treearr[i] = value;
		tree = (treenode*)malloc(sizeof(treenode));
		tree->value = value;
		tree->leftlief = NULL;
		tree->rightlief = NULL;
		return tree;
	}
	else
	{
		if (value < tree->value)
		{
			i = 2 * i + 1;
			tree->leftlief = addnode(tree->leftlief, value,i);
			return tree;
		}
		else
		{
			i = 2 * i + 2;
			tree->rightlief = addnode(tree->rightlief, value,i);
			return tree;
		}
	}
}
void inittree(treenode** tree)
{
	*tree = addnode(*tree,8,0);
	addnode(*tree, 3,0);
	addnode(*tree, 10,0);
	addnode(*tree, 1,0);
	addnode(*tree, 6,0);
	addnode(*tree, 4,0);
	addnode(*tree, 7,0);
	addnode(*tree, 14,0);
	addnode(*tree, 13,0);
	addnode(*tree, 9, 0);
	//addnode(*tree, 15, 0);
}
int show(treenode* tree, int lvl, int rank) {
	treenode* curr = tree;
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
		tbs_c = 2 * (tbs_c + 1) + 1;
	}
	tbs_c = tbs_l;
	treenode* el = NULL;
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
				printf_s("%3d", treearr[j]);

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
int getmaxdepth(treenode* tree)
{
	int ret = 0;
	if (tree)
	{
		int lDepth = getmaxdepth(tree->leftlief);
		int rDepth = getmaxdepth(tree->rightlief);
		ret = max(lDepth + 1, rDepth + 1);
	}
	return ret;
}
int detour(treenode* root, int* count) {
	treenode* curr = root;
	if (curr->leftlief != NULL && curr->rightlief != NULL)
	{
		*count = *count +1;
	}
	if (curr->leftlief != NULL) {
		detour(curr->leftlief, count);
	}
	if (curr->rightlief != NULL) {
		detour(curr->rightlief, count);
	}
	
}
int main() {
	treenode* tree = NULL;
	inittree(&tree);
	int lvl = 0;
	lvl = getmaxdepth(tree, lvl);
	show(tree, lvl, 3);
	int count = 0;
	detour(tree, &count);
	printf("Count:%d\n",count);

	return 0;

}