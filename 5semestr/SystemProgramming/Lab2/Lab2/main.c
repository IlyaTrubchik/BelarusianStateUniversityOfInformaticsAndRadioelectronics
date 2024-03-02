#include "windows.h"
#include "tchar.h"

#define COLUMN_COUNT 10
#define ROW_COUNT 10
#define TEXT_BUFFER 1024
#define DEFAULT_TEXT L"ายฮศ"
#define PADDING 2
LRESULT WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam);
void initColumns(HWND hWnd);
void WinDraw(HWND hWnd, HDC dc);
void CalculateCells(HWND hWnd, HDC dc);
void recalculateWidth();


typedef struct tableCell {
	TCHAR text[TEXT_BUFFER];
	RECT rectangle;
	double cellWidth;
	double cellHeight;
	BOOL edit;
}tableCell;

double TableHeight;
const int columnCount = 10;
const int rowCount = 10;
double defaultRectWidth = 10;
double defaultRectHeight = 10;
tableCell enteredCell;
SCROLLINFO scrollInfo;
tableCell* editCell;
static int yMinScroll;       // minimum vertical scroll value 
static int yCurrentScroll;   // current vertical scroll value 
static int yMaxScroll;       // maximum vertical scroll value 
BOOL fScroll;
SCROLLINFO si;
tableCell cells[COLUMN_COUNT * ROW_COUNT];
HDC hdcScreenCompat;

LRESULT CALLBACK WndProc(HWND hWnd, UINT message,
	WPARAM wParam, LPARAM lParam)
{
	RECT clientRect;
	switch (message)
	{
	case WM_CREATE:
	{
		yMinScroll = 0;
		yCurrentScroll = 0;
		yMaxScroll = 0;
		return  0;
	}
	case WM_PAINT:
	{
		PAINTSTRUCT ps;
		HDC dc = BeginPaint(hWnd, &ps);
		CalculateCells(hWnd, dc);
		WinDraw(hWnd, dc);
		PRECT prect;
		if (fScroll)
		{
			prect = &ps.rcPaint;
			BitBlt(dc,
				prect->left, prect->top,
				(prect->right - prect->left),
				(prect->bottom - prect->top),
				hdcScreenCompat,
				prect->left,
				prect->top + yCurrentScroll,
				SRCCOPY);
			fScroll = FALSE;
		}
		else {
			prect = &ps.rcPaint;
			BitBlt(dc,
				prect->left, prect->top,
				(prect->right - prect->left),
				(prect->bottom - prect->top),
				hdcScreenCompat,
				prect->left,
				prect->top + yCurrentScroll,
				SRCCOPY);
		}
		DeleteObject(hdcScreenCompat);
		EndPaint(hWnd, &ps);
		return 0;
	}
	case WM_GETMINMAXINFO:
	{
		MINMAXINFO* windowInfo;
		windowInfo = (MINMAXINFO*)lParam;
		windowInfo->ptMinTrackSize.x = 500;
		windowInfo->ptMinTrackSize.y = 200;
	}
	break;
	case WM_LBUTTONDBLCLK:
	{
		int x = LOWORD(lParam);
		int y = HIWORD(lParam);
		for (int i = 0;i < ROW_COUNT;i++)
		{
			for (int j = 0; j < COLUMN_COUNT;j++)
			{
				tableCell currCell = cells[i * COLUMN_COUNT + j];
				if (currCell.rectangle.left <= x
					&& currCell.rectangle.right >= x
					&& currCell.rectangle.bottom >= y + yCurrentScroll
					&& currCell.rectangle.top <= y + yCurrentScroll) {
					if (editCell != NULL) {
						editCell->edit = FALSE;
					}
					editCell = &cells[i * COLUMN_COUNT + j];
					editCell->edit = TRUE;
					GetClientRect(hWnd, &clientRect);
					InvalidateRect(hWnd, &clientRect, FALSE);
					return  0;
				}
			}
		}

		return 0;
	}
	case WM_SIZE:
	{
		int yNewSize;
		double xNewSize = LOWORD(lParam);
		defaultRectWidth = xNewSize / columnCount;
		yNewSize = HIWORD(lParam);

		yMaxScroll = max(TableHeight - yNewSize, 0);
		yCurrentScroll = min(yCurrentScroll, yMaxScroll);
		si.cbSize = sizeof(si);
		si.fMask = SIF_RANGE | SIF_PAGE | SIF_POS;
		si.nMin = yMinScroll;
		si.nMax = TableHeight;
		si.nPage = yNewSize;
		si.nPos = yCurrentScroll;
		SetScrollInfo(hWnd, SB_VERT, &si, TRUE);

		GetClientRect(hWnd, &clientRect);
		InvalidateRect(hWnd, &clientRect, FALSE);
		break;
	}
	case WM_VSCROLL:
	{
		int xDelta = 0;
		int yDelta;     // yDelta = new_pos - current_pos 
		int yNewPos;    // new position 

		switch (LOWORD(wParam))
		{
			// User clicked the scroll bar shaft above the scroll box. 
		case SB_PAGEUP:
			yNewPos = yCurrentScroll - 50;
			break;

			// User clicked the scroll bar shaft below the scroll box. 
		case SB_PAGEDOWN:
			yNewPos = yCurrentScroll + 50;
			break;

			// User clicked the top arrow. 
		case SB_LINEUP:
			yNewPos = yCurrentScroll - 5;
			break;

			// User clicked the bottom arrow. 
		case SB_LINEDOWN:
			yNewPos = yCurrentScroll + 5;
			break;

			// User dragged the scroll box. 
		case SB_THUMBPOSITION:
			yNewPos = HIWORD(wParam);
			break;
		case SB_THUMBTRACK:
			yNewPos = HIWORD(wParam);
			break;
		default:
			yNewPos = yCurrentScroll;
		}

		// New position must be between 0 and the screen height. 
		yNewPos = max(0, yNewPos);
		yNewPos = min(yMaxScroll, yNewPos);

		// If the current position does not change, do not scroll.
		if (yNewPos == yCurrentScroll)
			break;

		// Set the scroll flag to TRUE. 
		fScroll = TRUE;

		// Determine the amount scrolled (in pixels). 
		yDelta = yNewPos - yCurrentScroll;

		// Reset the current scroll position. 
		yCurrentScroll = yNewPos;

		ScrollWindowEx(hWnd, -xDelta, -yDelta, (CONST RECT*) NULL,
			(CONST RECT*) NULL, (HRGN)NULL, (PRECT)NULL,
			SW_INVALIDATE);
		UpdateWindow(hWnd);

		// Reset the scroll bar. 
		si.cbSize = sizeof(si);
		si.fMask = SIF_POS;
		si.nPos = yCurrentScroll;
		SetScrollInfo(hWnd, SB_VERT, &si, TRUE);

		break;
	}

	case WM_CHAR:
	{
		if (editCell != NULL)
		{
			RECT rc = editCell->rectangle;
			wchar_t inputChar = (wchar_t)(wParam);
			switch (inputChar)
			{
			case VK_RETURN:
			{
				if (editCell != NULL)
				{
					editCell->edit = FALSE;
					editCell = NULL;
					GetClientRect(hWnd, &clientRect);
					InvalidateRect(hWnd, &clientRect, FALSE);
				}
				break;
			}
			case VK_ESCAPE:
			{
				if (editCell != NULL)
				{
					editCell->edit = FALSE;
					editCell = NULL;
					GetClientRect(hWnd, &clientRect);
					InvalidateRect(hWnd, &clientRect, FALSE);
				}
				break;
			}
			case VK_BACK:
			{
				if (editCell != NULL)
				{
					if (wcslen((editCell->text)) - 1 >= 0) {
						editCell->text[wcslen((editCell->text)) - 1] = L'\0';
						GetClientRect(hWnd, &clientRect);
						InvalidateRect(hWnd, &clientRect, FALSE);
					}
				}
				break;
			}
			default: {
				if (strlen(editCell->text) < TEXT_BUFFER)
				{
					wchar_t str[2] = { inputChar,L'\0' };
					wcscat_s(editCell->text, sizeof(editCell->text) / sizeof(wchar_t), str);
					GetClientRect(hWnd, &clientRect);
					InvalidateRect(hWnd, &clientRect, FALSE);
				}
			}
			}
		}
		return 0;
	}
	case WM_DESTROY:
	{
		PostQuitMessage(0);
		return 0;
	}
	}
	return DefWindowProc(hWnd, message, wParam, lParam);
}

int WINAPI wWinMain(HINSTANCE hInstance, HINSTANCE prevInstance, PWSTR pCmdLine, int nCmdShow) {
	WNDCLASSEX wc;
	wc.cbSize = sizeof(WNDCLASSEX);
	wc.style = CS_DBLCLKS;
	wc.lpfnWndProc = WndProc;
	wc.cbClsExtra = 0;
	wc.cbWndExtra = 0;
	wc.hInstance = hInstance;
	wc.hIcon = NULL;
	wc.hCursor = LoadCursorA(NULL, IDC_ARROW);
	wc.hbrBackground = (HBRUSH)(COLOR_WINDOW + 1);
	wc.lpszMenuName = NULL;
	wc.lpszClassName = L"Text";
	wc.hIcon = NULL;
	wc.hIconSm = wc.hIcon;

	RegisterClassExW(&wc);

	HWND hWnd = CreateWindowW(L"Text", L"Lab 2", WS_OVERLAPPEDWINDOW | WS_VSCROLL | SBS_VERT | WS_VISIBLE, CW_USEDEFAULT, 0, CW_USEDEFAULT, 0, NULL, NULL, hInstance, NULL);
	fScroll = FALSE;;
	yCurrentScroll = 0;
	yMinScroll = 0;
	yMaxScroll = 0;
	initColumns(hWnd);

	ShowWindow(hWnd, nCmdShow);
	UpdateWindow(hWnd);



	MSG message;

	while (GetMessageW(&message, NULL, 0, 0)) {
		TranslateMessage(&message);
		DispatchMessageW(&message);
	}

	return (int)message.wParam;

}

void initColumns(HWND hWnd) {
	RECT cRect;
	GetClientRect(hWnd, &cRect);
	defaultRectWidth = (cRect.right - cRect.left) / columnCount;
	defaultRectHeight = ((cRect.bottom - cRect.top) / rowCount) + 20;
	double xl = 0;
	double yl = 0;
	for (int i = 0;i < ROW_COUNT;i++)
	{
		for (int j = 0;j < COLUMN_COUNT;j++)
		{
			RECT rc;
			rc.left = xl;
			rc.top = yl;
			rc.right = xl + defaultRectWidth;
			rc.bottom = yl + defaultRectHeight;
			tableCell currentCell;
			wcscpy_s(currentCell.text, sizeof(currentCell.text) / sizeof(wchar_t), DEFAULT_TEXT);
			currentCell.rectangle = rc;
			currentCell.cellWidth = defaultRectWidth;
			currentCell.cellHeight = defaultRectHeight;
			cells[i * COLUMN_COUNT + j] = currentCell;
			xl += defaultRectWidth;
		}
		xl = 0;
		yl += defaultRectHeight;
	}
	TableHeight = yl;
}
void recalculateWidth() {
	double xl = 0;
	for (int i = 0;i < ROW_COUNT;i++)
	{
		for (int j = 0;j < COLUMN_COUNT;j++)
		{
			cells[i * COLUMN_COUNT + j].rectangle.left = xl;
			cells[i * COLUMN_COUNT + j].rectangle.right = xl + defaultRectWidth;
			cells[i * COLUMN_COUNT + j].cellWidth = defaultRectWidth;
			xl += defaultRectWidth;
		}
		xl = 0;
	}
}
void CalculateCells(HWND hWnd, HDC dc) {
	int tableHeight = 0;
	recalculateWidth();
	for (int i = 0;i < ROW_COUNT;i++)
	{
		int rowHeight = defaultRectHeight;
		for (int j = 0;j < COLUMN_COUNT;j++)
		{
			RECT paddingRect;
			paddingRect.left = cells[i].rectangle.left + PADDING;
			paddingRect.right = cells[i].rectangle.right - PADDING;
			paddingRect.top = cells[i].rectangle.top + PADDING;
			paddingRect.bottom = cells[i].rectangle.bottom - PADDING;
			DrawTextW(dc, cells[i * COLUMN_COUNT + j].text, -1, &paddingRect, DT_EDITCONTROL | DT_WORDBREAK | DT_CALCRECT);
			if (paddingRect.bottom - paddingRect.top >= cells[i].cellHeight) {
				cells[i * COLUMN_COUNT + j].cellHeight = paddingRect.bottom - paddingRect.top + 2 * PADDING;
				cells[i * COLUMN_COUNT + j].rectangle.bottom = cells[i * COLUMN_COUNT + j].rectangle.top + cells[i * COLUMN_COUNT + j].cellHeight;
			}
			if (paddingRect.bottom - paddingRect.top + 2 * PADDING <= defaultRectHeight)
			{
				cells[i * COLUMN_COUNT + j].cellHeight = defaultRectHeight;
			}
			if (cells[i * COLUMN_COUNT + j].cellHeight >= rowHeight)
			{
				rowHeight = cells[i * COLUMN_COUNT + j].cellHeight;
			}
		}
		for (int j = 0;j < COLUMN_COUNT;j++)
		{

			cells[i * COLUMN_COUNT + j].cellHeight = rowHeight;
			cells[i * COLUMN_COUNT + j].rectangle.bottom = cells[i * COLUMN_COUNT + j].rectangle.top + cells[i * COLUMN_COUNT + j].cellHeight;
			if (i != ROW_COUNT - 1)
			{
				cells[(i + 1) * COLUMN_COUNT + j].rectangle.top = cells[i * COLUMN_COUNT + j].rectangle.bottom;
				cells[(i + 1) * COLUMN_COUNT + j].rectangle.bottom = cells[(i + 1) * COLUMN_COUNT + j].rectangle.top + cells[(i + 1) * COLUMN_COUNT + j].cellHeight;
			}
		}
		tableHeight += rowHeight;
	}
	RECT clientZone;
	GetClientRect(hWnd, &clientZone);
	yMaxScroll = max(tableHeight - (clientZone.bottom - clientZone.top), 0);
	yCurrentScroll = min(yCurrentScroll, yMaxScroll);

	SCROLLINFO scrollInfo;
	TableHeight = tableHeight;
	yMaxScroll = max(TableHeight - (clientZone.bottom - clientZone.top), 0);
	yCurrentScroll = min(yCurrentScroll, yMaxScroll);
	si.cbSize = sizeof(si);
	si.fMask = SIF_RANGE | SIF_PAGE | SIF_POS;
	si.nMin = yMinScroll;
	si.nMax = TableHeight;
	si.nPage = clientZone.bottom - clientZone.top;
	si.nPos = yCurrentScroll;
	SetScrollInfo(hWnd, SB_VERT, &si, TRUE);
}
void WinDraw(HWND hWnd, HDC dc)
{
	hdcScreenCompat = CreateCompatibleDC(dc);
	RECT rc;
	GetClientRect(hWnd, &rc);
	HBITMAP bmp = CreateCompatibleBitmap(dc, rc.right - rc.left, TableHeight);
	SelectObject(hdcScreenCompat, bmp);
	for (int i = 0;i < COLUMN_COUNT * ROW_COUNT;i++)
	{
		RECT paddingRect;
		paddingRect.left = cells[i].rectangle.left + PADDING;
		paddingRect.right = cells[i].rectangle.right - PADDING;
		paddingRect.top = cells[i].rectangle.top + PADDING;
		paddingRect.bottom = cells[i].rectangle.bottom - PADDING;
		SelectObject(hdcScreenCompat, GetStockObject(DC_BRUSH));
		if (cells[i].edit == TRUE) {
			HBRUSH brush = CreateSolidBrush(RGB(116, 242, 228));
			SelectObject(hdcScreenCompat, brush);
			Rectangle(hdcScreenCompat, cells[i].rectangle.left, cells[i].rectangle.top, cells[i].rectangle.left + cells[i].cellWidth, cells[i].rectangle.bottom);
			DeleteObject(brush);
			SetBkColor(hdcScreenCompat, RGB(116, 242, 228));
		}
		else {
			Rectangle(hdcScreenCompat, cells[i].rectangle.left, cells[i].rectangle.top, cells[i].rectangle.left + cells[i].cellWidth, cells[i].rectangle.bottom);
			SetBkColor(hdcScreenCompat, RGB(255, 255, 255));
		}
		DrawTextW(hdcScreenCompat, cells[i].text, -1, &paddingRect, DT_EDITCONTROL | DT_WORDBREAK);
		DeleteObject(bmp);
	}
}