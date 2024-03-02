#include <stdio.h>
#include <time.h>
#include <stdlib.h>
void switch_element();
void replace();
void print_matrix();
void exercise();
void print_second();

void main()
{
	srand(time(NULL));
	int Matrix[6][6];
	int i;


	for (i = 0;i <= 5;i++)
	{
		int j;
		for (j = 0;j <= 5;j++)
		{
			Matrix[i][j] = rand() % 100;
		}
	}
	printf("Start matrix:\n");
	print_matrix(6,6,Matrix);
	for (i = 0;i <= 5;i++)
	{
		switch_element(6,6,Matrix, i);
	}
	printf("Result:\n");
	print_matrix(6,6,Matrix);
	printf("Exercise27:\n\n");
	int matrix2[5][5];
	for (i = 0;i <= 4;i++)
	{
		int j;
		for (j = 0;j <= 4;j++)
		{
			matrix2[i][j] = rand() % 100;
		}
	}
	printf("Start Matrix:\n");
	print_second(5,5,matrix2);
	exercise(5,5,matrix2);
	printf("Result:\n");
	print_second(5,5,matrix2);
};

void switch_element (int ncol,int nlines,int  a[], int b)
{
	int i;
	int max_index;
	max_index = 0;
	int min_index = 0;
	for (i = 0; i <= 5;i++)
	{
		if (a[b*ncol+i] >= a[b*ncol+max_index])
		{
			max_index = i;
		}
		if (a[b*ncol+i] <= a[b*ncol+min_index])
		{
			min_index = i;
		}
	}
	replace(6,6,min_index, max_index, a, b);
}
void replace(int ncol,int nlines,int min, int max, int mat[], int b)
{
	int tmp;
	tmp = mat[b*ncol+min];
	mat[b*ncol+min] = mat[b*ncol+max];
	mat[b*ncol+max] = tmp;
}
void print_matrix(int ncol,int nlines,int  mat[])
{
	for (int i = 0;i <= 5;i++)
	{
		int j;
		for (j = 0;j <= 5;j++)
		{
			printf("%3d", mat[i*ncol+j]);
			printf(" ");

		}
		printf("\n");
	}
}
void print_second(int ncol,int nlines,int  mat[])
{
	for (int i = 0;i <= 4;i++)
	{
		int j;
		for (j = 0;j <= 4;j++)
		{
			printf("%3d", mat[i*ncol+j]);
			printf(" ");

		}
		printf("\n");
	}
}
void exercise(int ncol,int nlines,int mat[])
{
	for (int i = 0; i <= 4;i++)
	{
		int min_index = 0;
		for (int j = 0;j <= 4;j++)
		{
			if (mat[j*ncol+i] <= mat[min_index*ncol+i])
			{
				min_index = j;
			}
		}
		int tmp;
		tmp = mat[(4-i)*ncol+i];
		mat[(4-i)*ncol+i] = mat[min_index*ncol+i];
		mat[min_index*ncol+i] = tmp;
	}
}
