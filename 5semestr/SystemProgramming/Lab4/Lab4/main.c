#include <Windows.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdbool.h>

#define  FILENAME   "dafaf"
#define THREADPOOL_SIZE 5


typedef struct task {
    char** data;
    int numStrings;
    char** (*functionPtr)(char** data,int numStrings);
}task;

typedef struct Node {
    task* threadTask;
    struct Node* next;
}Node;

typedef struct{
    Node* first;
    Node* last;
}ThreadSafeQueue;
char** SortStrings(char** strings, int numStrings);
void initTasks();
void addToQueue(int numStrings, char** data, char** (*functionPtr)(char** data,int numStrings), ThreadSafeQueue* q);
char** MergeTwo(char** sortedPart1, int size1, char** sortedPart2, int size2, int* resultSize);
void printStrings(char** Strings, int count);
void mergeThreadFunction();
void initThreadPool();
task* takeFromQueue(ThreadSafeQueue* q);
void threadFunction();
int taskQueueSize = 0;
int sortedQueueSize = 0;

CRITICAL_SECTION section;
ThreadSafeQueue taskQueue;
ThreadSafeQueue sortedChunksQueue;
CONDITION_VARIABLE taskQueueNotEmpty;
CONDITION_VARIABLE resultQueueFull;
HANDLE threadPool[THREADPOOL_SIZE];

int resultNum;
HANDLE fileHandle;
HANDLE mappingHandle;
LPVOID fileData;
DWORD fileSize;
bool stopState;
char** sortedFile;

void initQueue(ThreadSafeQueue q)
{
    EnterCriticalSection(&section);
	q.first = NULL;
	q.last = NULL;
    LeaveCriticalSection(&section);
}

void main() {
	InitializeCriticalSection(&section);
    InitializeConditionVariable(&taskQueueNotEmpty);
    InitializeConditionVariable(&resultQueueFull);
    DWORD id;
    HANDLE mergeThread = CreateThread(NULL, 0, &mergeThreadFunction, NULL, 0, &id);
    initThreadPool();
    initTasks();
    WaitForSingleObject(mergeThread, INFINITE);
    char* currentPosition = fileData;
    for (int i = 0; i < resultNum;i++) {
        char boba = ((char*)fileData)[0];
        int length = strlen(sortedFile[i]);
        memcpy_s(currentPosition, length, sortedFile[i], length);
        currentPosition += length;
        *currentPosition = '\n';
        currentPosition += 1;
    }
    int a = FlushViewOfFile(fileData, fileSize);
    UnmapViewOfFile(fileData);
    CloseHandle(mappingHandle);
    CloseHandle(fileHandle);
    WaitForMultipleObjects(THREADPOOL_SIZE, &threadPool[0], TRUE, INFINITE);
    for (int i = 0;i < resultNum;i++)
    {
        free(sortedFile[i]);
    }
    free(sortedFile);
    for (int i = 0;i < THREADPOOL_SIZE;i++)
    {
        CloseHandle(threadPool[i]);
    }
    CloseHandle(mergeThread);
}

void mergeThreadFunction() {
    while (true)
    {   
        EnterCriticalSection(&section);
        if (stopState == false)
        {
            SleepConditionVariableCS(&resultQueueFull, &section, INFINITE);
        }
        stopState = true;
        LeaveCriticalSection(&section);
        task* task1 = takeFromQueue(&sortedChunksQueue);
        sortedFile = task1->data;
        resultNum = task1->numStrings;
        task* task2;
        while ((task2 = takeFromQueue(&sortedChunksQueue)) != NULL) {
            char** secondStrings = task2->data;
            int numStrings = task2->numStrings;
            sortedFile = MergeTwo(sortedFile, resultNum, secondStrings, numStrings, &resultNum);
        }
        WakeAllConditionVariable(&taskQueueNotEmpty);  
        printf_s("result:\n\n");
       // printStrings(sortedFile, resultNum);
        break;
    }
  
}
void printStrings(char** Strings, int count)
{
    for (int i = 0;i < count;i++)
    {
        printf_s("%s\n",Strings[i]);
    }
}

void initThreadPool() {
    DWORD id;
    for (int i = 0;i < THREADPOOL_SIZE;i++)
    {
        threadPool[i] = CreateThread(NULL, 0, &threadFunction, NULL, 0, &id);
    }
}

void addToQueue(int numStrings,char** data,char** (*functionPtr)(char** data,int numStrings), ThreadSafeQueue* q)
{
    EnterCriticalSection(&section);
    Node* newNode = (Node*)malloc(sizeof(task*)+sizeof(Node*));
    task* pTask = (task*)malloc(sizeof(char**)+sizeof(char** (*)(char**,int)));
    pTask->data = data;
    pTask->numStrings = numStrings;
    pTask->functionPtr = functionPtr;
    newNode->threadTask = pTask;
    newNode->next = NULL;
    if (q->first == NULL)
    {
        q->first = newNode;
        q->last = newNode;
    }else
    {
        q->last->next = newNode;
        q->last = newNode;
    }
    LeaveCriticalSection(&section);
}
task* takeFromQueue(ThreadSafeQueue* q)
{
    EnterCriticalSection(&section);
    Node* firstNode = q->first;
    task* result = NULL;
    if (firstNode != NULL)
    { 
        q->first = q->first->next;
        result = firstNode->threadTask;
    }
    LeaveCriticalSection(&section);
    return result;
}
void initTasks() {
    initQueue(taskQueue);

    LPCWSTR fileName = L"E:\\SP\\test.txt";
    // ��������� ����

     fileHandle = CreateFile(
        fileName,                   // ��� �����
        GENERIC_READ | GENERIC_WRITE,               // ����� ������� (������/������)
        FILE_SHARE_READ,            // ����� ����������� ������� (������)
        NULL,                       // �������� ������ (�� ���������)
        OPEN_EXISTING,              // �������� ��� �������� (������� ������������ ����)
        FILE_ATTRIBUTE_NORMAL,      // �������� ����� (������� ����)
        NULL                        // ����� ������� ����� (�� ������������)
    );
    // �������� ������ �����
    DWORD fileSizeHigh;
    DWORD fileSizeLow = GetFileSize(fileHandle, &fileSizeHigh);

    fileSize =  fileSizeLow;
     mappingHandle = CreateFileMapping(
        fileHandle,                 // ����� �����
        NULL,                       // �������� ������ (�� ���������)
        PAGE_READWRITE,              // ����� ������� � ����������� (������,������)
        0,                          // ������ ������������ ������� (0 - ���� ����)
        0,                          // ������������ ������ ������������ ������� (0 - ������ �����)
        NULL                        // ��� ����������� (�� ������������)
    );

    fileData = MapViewOfFile(
        mappingHandle,              // ����� ����������� �����
         FILE_MAP_ALL_ACCESS,              // ����� ������� � ����������� (������ ������)
        0,                          // �������� � ����� (������ �����)
        0,                          // �������� � ����������� (0 - �� ������ �����������)
        fileSize                    // ������ ������������ ������� (���� ����)
    );
    
    int chunkSize = fileSize / THREADPOOL_SIZE; 
    int numLines = 0;

    char** lines = (char**)malloc(1 * sizeof(char*));
    char* file = (char*)fileData;
     
    char* nPos = strchr(file, L'\n');
    int size = nPos - file;
    char* line = (char*)malloc(sizeof(char)*size);
    int newsize = sizeof(line);
    if(nPos!=NULL)
    {
        strncpy_s(line, sizeof(char) *(size+1),file , size);
    } 
    int i = 0;
    while (nPos!=NULL) {
        i++;
        file = nPos + 1;
        // �������� ������ ��� ������ � �������� ��
        lines = (char**)realloc(lines, i * sizeof(char*));
        lines[numLines] = (char*)malloc((strlen(line) + 1) * sizeof(char)+sizeof(char*));
        strcpy_s(lines[numLines],sizeof(char)*(strlen(line)+1), line);
        numLines++;
        // ��������� � ��������� ������
        nPos = strchr(file, '\n');
        if (nPos != NULL)
        {
            int size = nPos - file;
            line = (char*)malloc(sizeof(char) * size+sizeof(char*));
            strncpy_s(line, sizeof(char)*(size+1), file, size);
        }
    }
    int countPerThread = numLines / THREADPOOL_SIZE;
    int offset = 0;
    for (int i = 0;i < THREADPOOL_SIZE;i++)
    {
        int currentSize = countPerThread;
        if (i == THREADPOOL_SIZE - 1)
        {
            currentSize = numLines - countPerThread*(THREADPOOL_SIZE-1);
        }
        char** threadLines = (char**)malloc(currentSize * sizeof(char*)+sizeof(char**));
        for (int j = 0;j < currentSize;j++)
        {
            threadLines[j] = lines[j + offset];
        }
        offset = offset + currentSize;
        addToQueue(currentSize,threadLines,&SortStrings, &taskQueue);
        taskQueueSize++;
        WakeAllConditionVariable(&taskQueueNotEmpty);
    }
    WakeAllConditionVariable(&taskQueueNotEmpty); 
}
void threadFunction() {
    while (true)
    {
        EnterCriticalSection(&section);
        while (taskQueueSize == 0 && stopState == false)
        {
            SleepConditionVariableCS(&taskQueueNotEmpty, &section, INFINITE);
        }
        if (stopState == true && taskQueueSize == 0)
        {
            LeaveCriticalSection(&section);
            break;
        }
        taskQueueSize--;
        LeaveCriticalSection(&section);
        task* pTask = takeFromQueue(&taskQueue);
        char** result = pTask->functionPtr(pTask->data,pTask->numStrings);
        addToQueue(pTask->numStrings,result, NULL, &sortedChunksQueue);
        sortedQueueSize++;
        if (sortedQueueSize == THREADPOOL_SIZE)
        {
            WakeConditionVariable(&resultQueueFull);
        }
       
    }
}

// ������� ��� ������ ���� �����
void swapStrings(char* str1, char* str2) {
    char* temp = str1;
    str1 = str2;
    str2 = temp;
}

char** SortStrings(char** strings,int numStrings){
    int n = numStrings;
    int gap = n; // ������������� ���������� ���������� ����� ����������
    float shrink = 1.3; // ����������� ������
    bool sorted = false;
    while (!sorted) {
        gap = gap / shrink;
        if (gap <= 1) {
            gap = 1;
            sorted = true;
        }
        int i = 0;
        while (i + gap < n) {
            if (strcmp(strings[i], strings[i + gap]) > 0) {
                char** temp = strings[i];
                strings[i] = strings[i + gap];
                strings[i + gap] = temp;
                sorted = false;
            }
            i++;
        }
    }
    return strings;
}


char** MergeTwo(char** sortedPart1, int size1, char** sortedPart2, int size2, int* resultSize) {
    char** result = (char**)malloc((size1 + size2) * sizeof(char*));
    int index1 = 0, index2 = 0, resultIndex = 0;

    while (index1 < size1 && index2 < size2) {
        if (strcmp(sortedPart1[index1], sortedPart2[index2]) <= 0) {
            result[resultIndex] = sortedPart1[index1];
            index1++;
        }
        else {
            result[resultIndex] = sortedPart2[index2];
            index2++;
        }
        resultIndex++;
    }

    // ��������� ���������� �������� �� ������� �������, ���� ����
    while (index1 < size1) {
        result[resultIndex] = sortedPart1[index1];
        index1++;
        resultIndex++;
    }

    // ��������� ���������� �������� �� ������� �������, ���� ����
    while (index2 < size2) {
        result[resultIndex] = sortedPart2[index2];
        index2++;
        resultIndex++;
    }

    *resultSize = resultIndex;
    return result;
};

