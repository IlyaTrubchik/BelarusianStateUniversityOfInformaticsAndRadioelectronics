#include <windows.h>
#include "pch.h"
#include <iostream>
#include <windows.h>
#include <stdio.h>
#include <vector>
#include <fstream>
#include "dll.h"
#include <iostream>
#include <tlhelp32.h>
#include <conio.h>
#include <wchar.h>
using namespace std;

	__declspec(dllexport) void ReplaceString(params* fParams)
	{
		HANDLE process = GetCurrentProcess();
		size_t destLength = strlen(fParams->dest);
		size_t sourceLength = strlen(fParams->source);

		if (process)
		{
			SYSTEM_INFO sysInfo;
			GetSystemInfo(&sysInfo);

			MEMORY_BASIC_INFORMATION info;
			char* p = 0;
			std::vector<char> bytesRead;
			while (p <= sysInfo.lpMaximumApplicationAddress)
			{
				if (VirtualQueryEx(process, p, &info, sizeof(info)) != 0)
				{
					if (info.State == MEM_COMMIT && info.Protect == PAGE_READWRITE)
					{
						p = (char*)info.BaseAddress;
						bytesRead.resize(info.RegionSize);
						size_t countOfBytes;
							if (ReadProcessMemory(process, p, &bytesRead[0], info.RegionSize, &countOfBytes))
							{
								for (size_t i = 0;i < (countOfBytes - destLength);i++)
								{
									if (memcmp(fParams->dest, &bytesRead[i], destLength)==0)
									{
										char* ref = (char*)p + i;
										for (size_t j = 0;j < sourceLength;j++)
										{
											ref[j] = fParams->source[j];
										}
										ref[sourceLength] = 0;
									}
								}
							}
					}
					p += info.RegionSize;
				}
			}
		}
	}
	__declspec(dllexport) void PrintString(char text[])
	{
		cout << text << endl;
	}