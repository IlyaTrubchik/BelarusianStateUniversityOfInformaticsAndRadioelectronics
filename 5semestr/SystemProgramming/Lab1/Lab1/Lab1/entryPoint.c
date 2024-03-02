#include <windows.h>
#include <stdbool.h>
#define IN_DEC -1
#define IN_INC 1


LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam);
bool isKeyPressed(int ikey);
bool checkCollisions(HWND hWnd);
void WinDraw(HWND hWnd);
void WinMove();
void StateUpdate(HWND hWnd);
void OnTimer(HWND hWnd, WPARAM wParam);
bool IsShiftPressed();

float x = 0;
float y = 0;
float unMinimizeX = 0;
float unMinimizeY = 0;
int width = 50;
int height = 50;
float speedX = 0;
float speedY = 0;
float innertionXIncrease = 1;
float innertionYIncrease = 1;
float innertionDecrease = 1;
HBITMAP BmpSrc;
HBITMAP BmpMask;
BITMAP bi;
bool isKeyXDown = 0;
bool isKeyYDown = 0;
int TIMER_MOVE_ID = 2022;
int wheelDelta = 0;
bool isScrollingX = 0;
bool isScrollingY = 0;
bool isMinimized = 0;

LRESULT CALLBACK WndProc(HWND hWnd, UINT message,
	WPARAM wParam, LPARAM lParam)
{
	HDC hdc;
	switch (message)
	{
	case WM_PAINT:
		WinDraw(hWnd);
		return 0;
	case WM_MOUSEWHEEL:
		wheelDelta += GET_WHEEL_DELTA_WPARAM(wParam);

		if (IsShiftPressed())
		{
			if (!isScrollingX && wheelDelta > 0) speedX = 2;
			else if (!isScrollingX && wheelDelta < 0) speedX = -2;
			isKeyXDown = true;
			isScrollingX = true;
		}
		else
		{
			if (!isScrollingY && wheelDelta > 0) speedY = -2;
			else if (!isScrollingY && wheelDelta < 0) speedY = 2;
			isKeyYDown = true;
			isScrollingY = true;
		}
		return 0;
	case WM_SIZE: {
		if (wParam == SIZE_MINIMIZED) {
			unMinimizeX = x;
			unMinimizeY = y;
			isMinimized = 1;
		}
		else
		{
			isMinimized = 0;
		}
		return 0;
	}
	case WM_KEYDOWN:
		switch (wParam)
		{
			case VK_DOWN:
				isKeyYDown = 1;
				innertionYIncrease = IN_INC;
				if(speedY==0)
				{ 
				speedY = 2;
				}
				break;
			case VK_UP:
				if (speedY == 0)
				{
					speedY =- 2;
				}
				innertionYIncrease = IN_DEC;
				isKeyYDown = 1;
				break;
			case VK_LEFT:
				isKeyXDown = 1;
				innertionXIncrease = IN_DEC;
				if (speedX == 0)
				{
					speedX = -2;
				}
				break;
			case VK_RIGHT:
				isKeyXDown = 1;
				innertionXIncrease = IN_INC;
				if (speedX == 0)
				{
					speedX = 2;
				}
				break;
		}
		return 0;
	case WM_TIMER:
		if (wParam == TIMER_MOVE_ID)
		{
			StateUpdate(hWnd);
			WinMove(hWnd);
			InvalidateRect(hWnd, NULL, FALSE);
		}
		return 0;
	case WM_KEYUP:
		switch (wParam)
		{
		case VK_DOWN:
			isKeyYDown = 0;
			break;
		case VK_UP:
			isKeyYDown = 0;
			break;
		case VK_LEFT:
			isKeyXDown = 0;
			break;
		case VK_RIGHT:
			isKeyXDown = 0;
			break;
		}
		return 0;
	case WM_DESTROY:
		PostQuitMessage(0);
		return 0;
	}
	return DefWindowProc(hWnd, message, wParam, lParam);
}
int WINAPI wWinMain(HINSTANCE hInstance,
	HINSTANCE hPrevInstance, LPTSTR lpCmdLine, int nCmdShow)
{
	HBRUSH wndBrush = CreateSolidBrush(RGB(255, 0, 0));
	WNDCLASSEX wcex; HWND hWnd; MSG msg = {0};
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
	wcex.lpszClassName = L"PudgeClass";
	wcex.hIconSm = wcex.hIcon;
	RegisterClassEx(&wcex);
	hWnd = CreateWindow(L"PudgeClass", L"Fresh meat!",
		WS_OVERLAPPEDWINDOW, CW_USEDEFAULT, 0,
		CW_USEDEFAULT, 0, NULL, NULL, hInstance, NULL);
	HDC dc = GetDC(hWnd);
	SetTimer(hWnd, TIMER_MOVE_ID, 25, (TIMERPROC)NULL);
	BmpSrc = (HBITMAP)LoadImage(hInstance, L"E:\\SP\\Lab1\\Bitmaps\\pudge(mini2).bmp", IMAGE_BITMAP, 0, 0, LR_LOADFROMFILE);
	BmpMask = (HBITMAP)LoadImage(hInstance, L"E:\\SP\\Lab1\\Bitmaps\\pudge(mini_mask2).bmp", IMAGE_BITMAP, 0, 0, LR_LOADFROMFILE);
	ShowWindow(hWnd, nCmdShow);
	UpdateWindow(hWnd);
	while(GetMessage(&msg,NULL,0,0)>0)
	{
		TranslateMessage(&msg);
		DispatchMessage(&msg);	
	}
	KillTimer(hWnd, TIMER_MOVE_ID);
	return 0;
}

bool checkCollisions(HWND hWnd) {
	RECT rc;
	GetClientRect(hWnd, &rc);
	if (x <= rc.left) {
		x = rc.left+2;
		speedX = -speedX;
		wheelDelta = 0;
		return true;
	}
	else if (x + width >= rc.right) {
		x = rc.right - width - 2;
		speedX = -speedX;
		wheelDelta = 0;
		return true;
	}
	else if (y + height >= rc.bottom) {
		y = rc.bottom - height - 2;
		speedY = -speedY;
		wheelDelta = 0;
		return true;
	}
	else if (y <= rc.top) {
		y = rc.top + 2;
		speedY = -speedY;
		wheelDelta = 0;
		return true;
	}
	return false;
	
}

void WinMove(HWND hWnd) {
	x = x + speedX*0.1;
	y = y + speedY*0.1;
	if (speedX != 0)
	{
		if (wheelDelta > 0) {
			innertionXIncrease = IN_INC;
			wheelDelta -= 10;
		}
		else if (wheelDelta < 0) {
			innertionXIncrease = IN_DEC;
			wheelDelta += 10;
		}
		else if (isScrollingX && wheelDelta == 0)
		{
			isScrollingX = 0;
			isKeyXDown = 0;
		}
		speedX = speedX + innertionXIncrease * isKeyXDown + ((speedX > 0 ? -innertionDecrease : innertionDecrease) * (!isKeyXDown));
	}
     if (speedY != 0)
	{
		 if (wheelDelta > 0) {
			 innertionYIncrease = IN_DEC;
			 wheelDelta -= 10;
		 }
		 else if (wheelDelta < 0) {
			 innertionYIncrease = IN_INC;
			 wheelDelta += 10;
		 }
		 else if (isScrollingY && wheelDelta == 0)
		 {
			 isScrollingY = 0;
			 isKeyYDown = 0;
		 }
		speedY = speedY + innertionYIncrease  * isKeyYDown + (speedY > 0 ? -innertionDecrease : innertionDecrease) * (!isKeyYDown);
	
	 }
	 if (speedY == 0 && isScrollingY)
	 {
		 isScrollingY = false;
		 wheelDelta = 0;
		 isKeyYDown = 0;
	 }
	 if (speedX == 0 && isScrollingX)
	 {
		 isScrollingX = false;
		 wheelDelta = 0;
		 isKeyXDown = 0;
	 }
		
}
void WinDraw(HWND hWnd)
{
	if (isMinimized)
	{
		x = unMinimizeX;
		y = unMinimizeY;
	}
	PAINTSTRUCT ps;
	HDC hdc = BeginPaint(hWnd, &ps);;
	GetObject(BmpSrc, sizeof(BITMAP), &bi);
	HDC memDC = CreateCompatibleDC(hdc);
	HDC buffDC = CreateCompatibleDC(hdc);
	HBRUSH wndBrush = CreateSolidBrush(RGB(0, 255, 0));
	RECT rc;
	GetClientRect(hWnd, &rc);
	
	HBITMAP memBM = CreateCompatibleBitmap(hdc, rc.right-rc.left,rc.bottom-rc.top);
	SelectObject(memDC, memBM);
	FillRect(memDC, &rc, wndBrush); 
	SelectObject(buffDC, BmpMask);
	BitBlt(memDC, x, y, bi.bmWidth, bi.bmHeight, buffDC, 0, 0, SRCAND);

	SelectObject(buffDC, BmpSrc);
	BitBlt(memDC, x, y, bi.bmWidth, bi.bmHeight, buffDC, 0, 0, SRCPAINT); 

	BitBlt(hdc, 0, 0, rc.right - rc.left, rc.bottom - rc.top, memDC, 0, 0, SRCCOPY);

	DeleteDC(buffDC);
	DeleteDC(memDC);
	DeleteObject(memBM);

	EndPaint(hWnd, &ps);
}
void StateUpdate(HWND hWnd) {
	checkCollisions(hWnd);
}
bool IsShiftPressed()
{
	short stateLeft = GetKeyState(VK_LSHIFT);
	short stateRight = GetKeyState(VK_RSHIFT);


	if ((stateLeft & 0x8000) || (stateRight & 0x8000))
	{
		return true; 
	}

	return false; 
}