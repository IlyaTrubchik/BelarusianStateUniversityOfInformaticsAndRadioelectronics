#include <windows.h>
#include <stdbool.h>
#ifndef UNICODE
#define UNICODE
#endif
#define DIR_NONE 0
#define DIR_LEFT 1
#define DIR_RIGHT 2
#define DIR_DOWN 3
#define DIR_UP 4

LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam);
bool isKeyPressed(int ikey);
bool checkCollisions(HWND hWnd);
void innertionMove(int direction, HWND hWnd);
void WinDraw(HDC dc, HWND hWnd);
void WinMove();
void StateUpdate(HWND hWnd);
void DrawBitMap(HDC dc);

float x = 0;
float y = 0;
int width = 50;
int height = 50;
float speedX = 0;
float speedY = 0;
int currInnertion = 0;
float innertionIncrease = 0.5;
float innertionDecrease = 0.5;
int isMoving = DIR_NONE;
int isStopping = DIR_NONE;
HBITMAP BmpSrc;
HBITMAP BmpMask;
BITMAP bi;


LRESULT CALLBACK WndProc(HWND hWnd, UINT message,
	WPARAM wParam, LPARAM lParam)
{
	HDC hdc;
	switch (message)
	{
	case WM_PAINT:
	{
		PAINTSTRUCT ps;
		HDC dc = BeginPaint(hWnd, &ps);;
		GetObject(BmpSrc, sizeof(BITMAP), &bi);
		HDC memDC = CreateCompatibleDC(dc);

		SelectObject(memDC, BmpMask);
		BitBlt(dc, 0, 0, bi.bmWidth, bi.bmHeight, memDC, 0, 0, SRCAND);

		SelectObject(memDC, BmpSrc);
		BitBlt(dc, 0, 0, bi.bmWidth, bi.bmHeight, memDC, 0, 0, SRCPAINT);
     
		DeleteDC(memDC);
	
		EndPaint(hWnd, &ps);
		break;
	}
	case WM_KEYDOWN:
		break;
	case WM_DESTROY:
		PostQuitMessage(0);
		break;
	default:
		return DefWindowProc(hWnd, message, wParam, lParam);
	}
	return 0;
}
int WINAPI wWinMain(HINSTANCE hInstance,
	HINSTANCE hPrevInstance, LPTSTR lpCmdLine, int nCmdShow)
{
	HBRUSH wndBrush = CreateSolidBrush(RGB(2, 181, 47));
	WNDCLASSEX wcex; HWND hWnd; MSG msg = { 0 };
	wcex.cbSize = sizeof(WNDCLASSEX);
	wcex.style = CS_DBLCLKS;
	wcex.lpfnWndProc = WndProc;
	wcex.cbClsExtra = 0;
	wcex.cbWndExtra = 0;
	wcex.hInstance = hInstance;
	wcex.hIcon = LoadIcon(NULL, IDI_APPLICATION);
	wcex.hCursor = LoadCursor(NULL, IDC_ARROW);
	wcex.hbrBackground = wndBrush;
	wcex.lpszMenuName = NULL;
	wcex.lpszClassName = L"HelloWorldClass";
	wcex.hIconSm = wcex.hIcon;
	RegisterClassEx(&wcex);
	hWnd = CreateWindow(L"HelloWorldClass", L"Hello, World!",
		WS_OVERLAPPEDWINDOW, CW_USEDEFAULT, 0,
		CW_USEDEFAULT, 0, NULL, NULL, hInstance, NULL);
	HDC dc = GetDC(hWnd);
	BmpSrc = (HBITMAP)LoadImage(hInstance, L"E:\\SP\\Lab1\\Bitmaps\\pudge(mini2).bmp", IMAGE_BITMAP, 0, 0, LR_LOADFROMFILE);
	BmpMask = (HBITMAP)LoadImage(hInstance, L"E:\\SP\\Lab1\\Bitmaps\\pudge(mini_mask2).bmp", IMAGE_BITMAP, 0, 0, LR_LOADFROMFILE);
	ShowWindow(hWnd, nCmdShow);
	UpdateWindow(hWnd);
	while (WM_QUIT != msg.message)
	{
		if (GetMessage(&msg, NULL, 0, 0, PM_REMOVE))
		{
			TranslateMessage(&msg);
			DispatchMessage(&msg);
		}
		DrawBitMap(dc);
	}
	return (int)msg.wParam;
}







void DrawBitMap(HDC dc) {
}