#pragma once
#include <string>
#ifndef _DLL_IMPORT
#define _DLL_IMPORT

#ifdef __cplusplus 
extern "C" {
#endif
	struct params {
		char dest[100];
		char source[100];
	};
	__declspec(dllimport) void __cdecl ReplaceString(params* fParams);
#ifdef __cplusplus 
}
#endif
#endif 