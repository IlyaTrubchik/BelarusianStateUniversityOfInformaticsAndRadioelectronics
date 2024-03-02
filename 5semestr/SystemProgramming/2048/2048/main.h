#include <windows.h>
#include <stdbool.h>
#include <string.h>
#define RETURN_TO_ZERO 2
#define MULTIPLY 1
#define NO_ACTION 0
#define RESET_TILE 3
#define TIMER_ID 1250
#define WIN_VALUE 2048
#define MAX_BOARD_SIZE 6
#define LITTLE_BOARD 4
#define MEDIUM_BOARD 5
#define BIG_BOARD 6
#define DEFAULT_WND_WIDTH 421
#define DEFAULT_WND_HEIGHT 594
#define DEFAULT_TILE_WIDTH 70
#define DEFAULT_TILE_HEIGHT 70
#define DEFAULT_BOARD_WIDTH 355
#define DEFAULT_BOARD_HEIGHT 355
#define MARGIN_LEFTRIGHT 25
#define MARGIN_BOTTOM 95
#define MARGIN_TOP 105
#define MEDIUM_TILE_WIDTH  53
#define MEDIUM_TILE_HEIGHT  53
#define BIG_TILE_WIDTH  41
#define BIG_TILE_HEIGHT 41
#define SAVE4x4_PATH L".\\save\\4x4.txt"
#define SAVE5x5_PATH L".\\save\\5x5.txt"
#define SAVE6x6_PATH L".\\save\\6x6.txt"

typedef struct {
    int value;
    RECT rect;
    BOOL isAnimate;
    BOOL isMerged;
} Tile;
typedef struct {
    TCHAR* text;
    RECT rect;
    int weight;
}button;
LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam);
void UpdateTiles(HWND hWnd);
void DrawBoard(HDC dc, HDC compatibleDC);
void ResetBoard(HWND hWnd);
void GenerateNewNumber();
void ProcessMove(int direction);
void moveTile(Tile* tile);
void checkMoving();
void ResetTile(HWND hWnd, Tile* tile, int row, int column);
void DrawScoreBoard(HDC hdc, HDC compatDC);
void intToStr(int num, TCHAR* str);
void DrawWin(HDC compatDC);
void DrawLoose(HDC compatDC);
void CheckForWin();
BOOL CheckForLoose();
void DrawButton(button btn, HDC compatDC);
void PaintTile(HDC hdc, HDC compatibleDC, int number, int i, int j, BOOL isAnimate);