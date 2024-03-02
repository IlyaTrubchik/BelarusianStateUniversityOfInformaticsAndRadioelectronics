#include <stdio.h>
float  var16()
{
	int i;
	float result = 0;
	for (i = 1; i <= 100; i++)
	{
		result = result + ((float)1 / (float)(i * i));
	}
	return result;
}
float var17()
{
	int i;
	float result = 0;
	for (i = 1; i <= 50; i++)
	{
		result = result + ((float)1 / (float)(i * i * i));
	}
	return result;

}
 int var11b()
{
	int a;
	int n;
	printf("Enter number a\n");
	scanf_s("%d", &a);
	printf("Enter number n\n");
	scanf_s("%d", &n);

	/*var11b*/;
	int i;
	int res = 1;
	for (i = 1; i <= n;i++)
	{
		res = res * (a + i - 1);
	}
	return res;

}
int var10()
{
	int n;
	printf("Enter number n\n");
	scanf_s("%d", &n);
	int i;
	int res = 1;
	for (i = 1;i <= n;i++)
	{
		res = res * 2;
	}
	return res;
}
int var11a()
{
	int a, n;
	printf("Enter number a\n");
	scanf_s("%d", &a);
	printf("Enter number n\n");
	scanf_s("%d", &n);
	int res = 1;
	int i;
	for (i = 1;i <= n;i++)
	{
		res = res * a;
	}
	return res;
}
int main()
{
	printf("-----11(a)------\n");
	printf("Result:\n%d\n", var11a());
	printf("-----11(b)------\n");
	printf("Result:\n%d\n", var11b());
	printf("-----10------\n");
	printf("Result:\n%d\n", var10());
	printf("-----16------\n");
	printf("Result:\n%f\n", var16());
	printf("-----17------\n");
	printf("Result:\n%f\n", var17());
	return 0;
	
}

