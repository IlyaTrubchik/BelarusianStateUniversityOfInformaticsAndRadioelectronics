#include <windows.h>
#include <stdio.h>
#include <conio.h>
struct params {
	char dest[100];
	char source[100];
};
typedef void Replace(params* fParams);

int main() {
	char txt[100] = "Visual Studio\n";
	HMODULE lib = LoadLibraryA("./Lib/dll.dll");
	if (lib != NULL) {
		Replace* pFunc = (Replace*)GetProcAddress(lib, "ReplaceString");
		printf_s("Dynamic dll import:\n");
		printf_s("%s", &txt);
		params fParams;
		strcpy_s(fParams.dest, "Studio");
		strcpy_s(fParams.source, "Code");
		pFunc(&fParams);
		printf_s("%s", &txt);
		FreeLibrary(lib);
	}
	else {
		printf_s("Cannot load DLL lib");
	}
	_getch();
}