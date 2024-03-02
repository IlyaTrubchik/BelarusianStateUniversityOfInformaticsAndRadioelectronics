#pragma once
#include <string>

using namespace std;

#ifdef __cplusplus 
extern "C" {
#endif
	__declspec(dllexport) void PrintString(char text[]);
	struct params {
		char dest[100];
		char source[100];
	};
	__declspec(dllexport) void __cdecl ReplaceString(params* fParams);
#ifdef __cplusplus 
}
#endif
#pragma once