#include <windows.h>
#include <stdio.h>
#include <conio.h>
#include "dllStaticImport.h"

int main() {
	char txt[100] = "Visual Studio\n";
	printf_s("%s", &txt);
	params fParams;
	strcpy_s(fParams.dest, "Studio");
	strcpy_s(fParams.source, "Code");
	ReplaceString(&fParams);
	printf_s("%s", &txt);
	_getch();
}