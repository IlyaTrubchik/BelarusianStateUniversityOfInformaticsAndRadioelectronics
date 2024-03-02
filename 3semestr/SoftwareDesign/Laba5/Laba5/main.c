#include <stdio.h>
;
int o1, s1, o2, s2;
int sortvybor(int[], int);
int sortvstav(int[], int);
int sortreverse(int[], int);
int main()
{
	int MAS[2000], examplemas[2000];
	int i;
	i = 0;
	int n = 2000;
	while (i <= n - 1)
	{
		examplemas[i] = rand() % 101;
		i++;
	}
	for (i = 0; i <= n - 1;i++)
	{
		MAS[i] = examplemas[i];
	}
	printf("----------------------------------------------------------\n");
	printf("|     Тип    |       Выбор        |      Вставки       |\n");
	printf("|   Массива  |____________________|____________________|\n");
	printf("|            |Сравнений  |Обменов |Сравнений  |Обменов |\n");
	printf("|____________|___________|________|___________|________|\n");
	printf("|10 элементов|           |        |           |        |\n");
	//////////////////////////////////////////////////////////
	n = 10;
	sortvybor(MAS, n);
	printf("|Неотсорт.   |%11d", s1);
	printf("|%8d", o1);
	printf("|");
	for (i = 0; i <= n - 1;i++)
	{
		MAS[i] = examplemas[i];
	}
	sortvstav(MAS, n);
	printf("%11d", s2);
	printf("|%8d", o2);
	printf("|\n");

	printf("|______________________________________________________|\n");
////////////////////////////////////////////////////////////
	sortvybor(MAS, n);
	printf("|10 элементов|           |        |           |        |\n");
	printf("|отсорт.     |%11d", s1);
	printf("|%8d", o1);
	printf("|");
	sortvstav(MAS, n);
	printf("%11d", s2);
	printf("|%8d", o2);
	printf("|\n");

	printf("|______________________________________________________|\n");
	//////////////////////////////
	
	sortreverse(MAS, n);
	sortvybor(MAS, n);
	printf("|10 элементов|           |        |           |        |\n");
	printf("|отсорт.в обр|%11d", s1);
	printf("|%8d", o1);
	printf("|");
	sortreverse(MAS, n);
	sortvstav(MAS, n);
	printf("%11d", s2);
	printf("|%8d", o2);
	printf("|\n");

	printf("|______________________________________________________|\n");
	//////////////////////////////
	printf("|100 элем.   |           |        |           |        |\n");
	//////////////////////////////////////////////////////////
	n = 100;
	sortvybor(MAS, n);
	printf("|Неотсорт.   |%11d", s1);
	printf("|%8d", o1);
	printf("|");
	for (i = 0; i <= n - 1;i++)
	{
		MAS[i] = examplemas[i];
	}
	sortvstav(MAS, n);
	printf("%11d", s2);
	printf("|%8d", o2);
	printf("|\n");

	printf("|______________________________________________________|\n");
	////////////////////////////////////////////////////////////
	sortvybor(MAS, n);
	printf("|100 элем.   |           |        |           |        |\n");
	printf("|отсорт.     |%11d", s1);
	printf("|%8d", o1);
	printf("|");
	sortvstav(MAS, n);
	printf("%11d", s2);
	printf("|%8d", o2);
	printf("|\n");

	printf("|______________________________________________________|\n");
	//////////////////////////////

	sortreverse(MAS, n);
	sortvybor(MAS, n);
	printf("|100 элем.   |           |        |           |        |\n");
	printf("|отсорт.в обр|%11d", s1);
	printf("|%8d", o1);
	printf("|");
	sortreverse(MAS, n);
	sortvstav(MAS, n);
	printf("%11d", s2);
	printf("|%8d", o2);
	printf("|\n");

	printf("|______________________________________________________|\n");
	//////////////////////////////
	printf("|2000 элем.  |           |        |           |        |\n");
	//////////////////////////////////////////////////////////
	n = 2000;
	sortvybor(MAS, n);
	printf("|Неотсорт.   |%11d", s1);
	printf("|%8d", o1);
	printf("|");
	for (i = 0; i <= n - 1;i++)
	{
		MAS[i] = examplemas[i];
	}
	sortvstav(MAS, n);
	printf("%11d", s2);
	printf("|%8d", o2);
	printf("|\n");

	printf("|______________________________________________________|\n");
	////////////////////////////////////////////////////////////
	sortvybor(MAS, n);
	printf("|2000 элем.  |           |        |           |        |\n");
	printf("|отсорт.     |%11d", s1);
	printf("|%8d", o1);
	printf("|");
	sortvstav(MAS, n);
	printf("%11d", s2);
	printf("|%8d", o2);
	printf("|\n");

	printf("|______________________________________________________|\n");
	//////////////////////////////

	sortreverse(MAS, n);
	sortvybor(MAS, n);
	printf("|2000 элем.  |           |        |           |        |\n");
	printf("|отсорт.в обр|%11d", s1);
	printf("|%8d", o1);
	printf("|");
	sortreverse(MAS, n);
	sortvstav(MAS, n);
	printf("%11d", s2);
	printf("|%8d", o2);
	printf("|\n");

	printf("|______________________________________________________|\n");
	//////////////////////////////

	return 0;
}

int sortvybor(int MAS[], int count)
{
	int i, j;
	o1 = 0;
	s1 = 0;
	i = 0;
	j = 0;
	int min;
	while (i <= (count - 2))
	{
		min = i;
		j = i + 1;
		while (j <= count - 1)
		{
			s1++;
			if (MAS[j] <= MAS[min])
			{
				min = j;
			}
			j++;
		}
		o1++;
		int tmp = MAS[min];
		MAS[min] = MAS[i];
		MAS[i] = tmp;
		i++;
	}
	return 0;
}
int sortvstav(int MAS[], int count)
	{
		int i, j, k;
		o2 = 0;
		s2 = 0;
		i = 1;
		j = 0;
		while (i <= count - 1)
		{
			j = i - 1;
			k = MAS[i];
			while (j >= 0 && MAS[j] > k)
			{
				o2++;
				s2++;
				MAS[j + 1] = MAS[j];
				j--;
			}
			MAS[j + 1] = k;
			o2++;
			if (j >= 1 && MAS[j] <= k)
			{
				s2++;
			}
			i++;
		}
		return 0;
	}
int sortreverse(int MAS[], int count)
{
	int x = count / 2;
	int i;
	i = 0;
	while (i <= x - 1)
	{
		int tmp = MAS[i];
		MAS[i] = MAS[count - i];
		MAS[count - i] = tmp;
		i++;
	}
	return 0;
}