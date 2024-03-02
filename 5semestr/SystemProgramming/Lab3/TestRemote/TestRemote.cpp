#include <iostream>
#include <windows.h>
#include <tlhelp32.h>
#include <conio.h>
#include <wchar.h>
using namespace std;

int main() {
	char txt[100] = "Visual Studio\n";
	printf_s("%s", &txt);
	_getch();
	printf_s("%s", &txt);
	_getch();
}