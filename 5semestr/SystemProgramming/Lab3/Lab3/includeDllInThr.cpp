#include <iostream>
#include <windows.h>
#include <tlhelp32.h>
#include <conio.h>
#include <wchar.h>
using namespace std;
struct params {
	char dest[100];
	char source[100];
};
DWORD GetPidByProcessName(wchar_t processName[])
{
	DWORD processId = 0;
	HANDLE hSnapshot = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);
	PROCESSENTRY32 processEntry;
	processEntry.dwSize = sizeof(processEntry);

	bool isFound = false;
	while (Process32Next(hSnapshot, &processEntry) && !isFound)
	{
		if (!wcscmp(processName, processEntry.szExeFile))
		{
			processId = processEntry.th32ProcessID;
			isFound = true;
		}
	}

	return processId;
}

int main() {
	const char* pathToDll = "./dll.dll";
	const char* pathToDllRemote = "./dll.dll";
	wchar_t processName[100] = L"TestRemote.exe";
	params funcParams;
	strcpy_s(funcParams.dest, "Studio");
	strcpy_s(funcParams.source, "Code");
	DWORD processId = GetPidByProcessName(processName);
	HANDLE procHandle = OpenProcess(PROCESS_ALL_ACCESS, FALSE, processId);
	if (procHandle != NULL)
	{
		LPVOID remoteString = VirtualAllocEx(procHandle, NULL, strlen(pathToDllRemote) + 1, MEM_COMMIT, PAGE_READWRITE);
		LPVOID pParam = VirtualAllocEx(procHandle, NULL, sizeof(funcParams), MEM_COMMIT, PAGE_READWRITE);
		
		if(pParam != NULL && remoteString != NULL)
		{
			WriteProcessMemory(procHandle, remoteString, pathToDllRemote, strlen(pathToDllRemote) + 1, NULL);
			WriteProcessMemory(procHandle, pParam, &funcParams, sizeof(funcParams), NULL);

			LPVOID loadAddr = (LPVOID)GetProcAddress(GetModuleHandle(L"kernel32.dll"), "LoadLibraryA");

			HANDLE hRemoteThread = CreateRemoteThread(procHandle, NULL, 0, (LPTHREAD_START_ROUTINE)loadAddr, remoteString, 0, NULL);
			if (hRemoteThread != NULL) {
				HMODULE currProcLib = (HMODULE)LoadLibraryA(pathToDll);
				HANDLE funcHandle = (HANDLE)GetProcAddress(currProcLib, "ReplaceString");
				HANDLE hRemoteThread2 = CreateRemoteThread(procHandle, NULL, 0, (LPTHREAD_START_ROUTINE)funcHandle, (LPVOID)pParam, 0, NULL);
				if (hRemoteThread2 != NULL)
				{
					cout << "Remote thread complete.";
					WaitForSingleObject(hRemoteThread2, INFINITE);
									}
				else {
					cout << "Remote thread doesnt work";
				}
				WaitForSingleObject(hRemoteThread, INFINITE);
				CloseHandle(hRemoteThread);
				VirtualFreeEx(procHandle, remoteString, 0, MEM_RELEASE);
				VirtualFreeEx(procHandle, pParam, 0, MEM_RELEASE);
				CloseHandle(procHandle);
			}
			else {
				cout << "Remote thread doesnt work" << endl;
			}
				_getch();
		}
		else {
			cout << "Cant allocate memory" << endl;
		}
	}
}