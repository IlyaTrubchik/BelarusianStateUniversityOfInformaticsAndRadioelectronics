#include "main.h"
void DrawMenuButton(button drawBtn, HDC compatDC, HDC hdc);
void DrawMenu(HDC hdc, HDC compatibleDC);
BOOL checkOnBtn(button btn, int xPos, int yPos);
void intToCharStr(int num, char* str);
void backOneMove();
void load(int currentBoardSize);
void save(int currentBoardSize);
BOOL generatenew = false;
HINSTANCE hInst;
int tileWidth = DEFAULT_TILE_WIDTH;
int tileHeight = DEFAULT_TILE_HEIGHT;
Tile board[MAX_BOARD_SIZE][MAX_BOARD_SIZE];;
Tile previousBoard[MAX_BOARD_SIZE][MAX_BOARD_SIZE];
int between;
int currentScore = 0;
int previousScore = 0;
int messageHeight = 155;
BOOL isWin = false;
BOOL isLose = false;
button exitBtn;
button retryBtn;
button play4x4Btn;
button play5x5Btn;
button play6x6Btn;
button backMoveBtn;
button gameRetryBtn;
button saveGameBtn;
button loadGameBtn;
int buttonWeight = 700;
HWND hwnd;
BOOL isAnimate = false;
BOOL isInMenu = true;
int animateDiff = 5;
int animateCount = 20;
int boardWidth = DEFAULT_BOARD_WIDTH;
int boardHeight = DEFAULT_BOARD_HEIGHT;
int currentBoardSize = LITTLE_BOARD;
int countOfBack = 1;
BOOL gameJustStarted;

LRESULT CALLBACK WndProc(HWND hWnd, UINT message,WPARAM wParam, LPARAM lParam)
{
    RECT rc;
    GetClientRect(hWnd, &rc);
    switch (message) {
    case WM_CREATE:
    {
        RECT rect;
        between = (boardWidth - (4 * DEFAULT_TILE_WIDTH)) / 5;
        rect.left = MARGIN_LEFTRIGHT+between + tileWidth / 2;
        rect.right = MARGIN_LEFTRIGHT+ 2 * between + 2 * tileWidth + between / 2;
        rect.top = between * 4 + tileHeight * 3;
        rect.bottom = rect.top + tileHeight / 2;
        retryBtn.rect = rect;
        retryBtn.text = L"Retry";
        rect.left = rect.right + between / 2;
        rect.right = retryBtn.rect.right - retryBtn.rect.left + rect.left;
        exitBtn.rect = rect;
        exitBtn.text = L"Exit";
        exitBtn.weight = 300;
        retryBtn.weight = 300;
        rect.top = MARGIN_TOP + DEFAULT_BOARD_HEIGHT + 25;
        rect.bottom = rect.top + 30;
        rect.left = MARGIN_LEFTRIGHT;
        rect.right = rect.left+ (DEFAULT_BOARD_WIDTH- between) / 2;
        gameRetryBtn.rect = rect;
        rect.top = rect.bottom + 10;
        rect.bottom = rect.top + 30;
        saveGameBtn.rect = rect;
       
        rect.top = MARGIN_TOP + DEFAULT_BOARD_HEIGHT + 25;
        rect.bottom = rect.top + 30;
        rect.left = rect.right + between;
        rect.right = rect.left + (DEFAULT_BOARD_WIDTH  - between) / 2;
        backMoveBtn.rect = rect;
        rect.top = rect.bottom + 10;
        rect.bottom = rect.top + 30;
        loadGameBtn.rect = rect;
     
        loadGameBtn.text = L"LoadGame";
        saveGameBtn.text = L"SaveGame";
        gameRetryBtn.text = L"Retry";
        backMoveBtn.text = L"Back move";
        loadGameBtn.weight = saveGameBtn.weight = backMoveBtn.weight = gameRetryBtn.weight = play4x4Btn.weight = play5x5Btn.weight = play6x6Btn.weight = 300;
        //load(currentBoardSize);
        break;
    }
   
    case WM_PAINT:
    {
        PAINTSTRUCT ps;
        HDC dc = BeginPaint(hWnd, &ps);
        HDC compatibleDC = CreateCompatibleDC(dc);
        HBITMAP compatBitmap = CreateCompatibleBitmap(dc, rc.right - rc.left, rc.bottom - rc.top);
        SelectObject(compatibleDC, compatBitmap);
        if (!isInMenu)
        {
                if (!isWin && !isLose)
                {
                    DrawBoard(dc, compatibleDC);
                    DrawMenuButton(gameRetryBtn,compatibleDC,dc);
                    DrawMenuButton(backMoveBtn, compatibleDC, dc );
                    DrawMenuButton(loadGameBtn, compatibleDC, dc);
                    DrawMenuButton(saveGameBtn, compatibleDC, dc);
                }
                else
                {
                    if (isWin)
                    {
                        DrawBoard(dc, compatibleDC);
                        DrawWin(compatibleDC);
                    }
                    else
                    {
                        DrawBoard(dc, compatibleDC);
                        DrawLoose(compatibleDC);
                    }
                }
                BitBlt(dc, 0, 0, rc.right - rc.left, rc.bottom - rc.top, compatibleDC, 0, 0, SRCCOPY);
        
            DeleteObject(compatBitmap);
            DeleteObject(compatibleDC);
            EndPaint(hWnd, &ps);
            if (isAnimate)
            {
                isAnimate = false;
                InvalidateRect(hWnd, &rc, FALSE);
            }
        }
        else
        {
            DrawMenu(dc, compatibleDC);
            BitBlt(dc, 0, 0, rc.right - rc.left, rc.bottom - rc.top, compatibleDC, 0, 0, SRCCOPY);
            DeleteObject(compatBitmap);
            DeleteObject(compatibleDC);
            EndPaint(hWnd, &ps);
        }
       
    }
    break;
    case WM_MOUSEMOVE:
    {
        if (!isInMenu)
        {
            if (isWin || isLose)
            {
                POINT cursorPos;
                GetCursorPos(&cursorPos);
                ScreenToClient(hWnd, &cursorPos);
                exitBtn.weight = 300;
                retryBtn.weight = 300;
                if (checkOnBtn(retryBtn,cursorPos.x,cursorPos.y))
                {
                    retryBtn.weight = 900;
                }
                else
                    if (checkOnBtn(exitBtn, cursorPos.x, cursorPos.y))
                    {
                        exitBtn.weight = 900;
                    }

                InvalidateRect(hWnd, &rc, FALSE);
            }
            else
            {
                POINT cursorPos;
                GetCursorPos(&cursorPos);
                ScreenToClient(hWnd, &cursorPos);
                gameRetryBtn.weight = 300;
                backMoveBtn.weight = 300;
                saveGameBtn.weight = 300;
                loadGameBtn.weight = 300;
                if (checkOnBtn(gameRetryBtn, cursorPos.x, cursorPos.y))
                {
                    gameRetryBtn.weight = 900;
                }
                else
                    if (checkOnBtn(backMoveBtn, cursorPos.x, cursorPos.y))
                    {
                        backMoveBtn.weight = 900;
                    }
                    else if (checkOnBtn(saveGameBtn, cursorPos.x, cursorPos.y))
                    {
                        saveGameBtn.weight = 900;
                    }
                    else if (checkOnBtn(loadGameBtn, cursorPos.x, cursorPos.y))
                    {
                        loadGameBtn.weight = 900;
                    }

                InvalidateRect(hWnd, &rc, FALSE);
            }
            
        }
        else {
            POINT cursorPos;
            GetCursorPos(&cursorPos);
            ScreenToClient(hWnd, &cursorPos);
            int xPos = cursorPos.x;
            int yPos = cursorPos.y;
            play4x4Btn.weight = 300;
            play5x5Btn.weight = 300;
            play6x6Btn.weight = 300;
            if (checkOnBtn(play4x4Btn, xPos, yPos))
            {
                play4x4Btn.weight = 900;
                //isInMenu = false;
            }
            else if (checkOnBtn(play5x5Btn, xPos, yPos))
            {
                play5x5Btn.weight = 900;
                //isInMenu = false;
            }
            else if (checkOnBtn(play6x6Btn, xPos, yPos))
            {
                play6x6Btn.weight = 900;
                //isInMenu = false;
            }
            InvalidateRect(hWnd, &rc, FALSE);
        }

    }
    break;
    case WM_LBUTTONDOWN:
    {
        if(!isInMenu)
        { 
            if(isLose || isWin)
            { 
                int xPos = LOWORD(lParam);
                int yPos = HIWORD(lParam);
                if (checkOnBtn(retryBtn,xPos,yPos))
                {
                    ResetBoard(hWnd);
                    gameJustStarted = true;
                    currentScore = 0;
                    isWin = false;
                    isLose = false;
                    InvalidateRect(hWnd, &rc, FALSE);
                }else 
                    if (checkOnBtn(exitBtn,xPos,yPos))
                {
                        isInMenu = true;
                        InvalidateRect(hWnd, &rc, true);
                }
            }
            else {
                int xPos = LOWORD(lParam);
                int yPos = HIWORD(lParam);
                if (checkOnBtn(gameRetryBtn, xPos, yPos))
                {
                    gameJustStarted = true;
                    ResetBoard(hWnd);
                    currentScore = 0;
                    isWin = false;
                    isLose = false;
                    InvalidateRect(hWnd, &rc, FALSE);
                }
                else if (checkOnBtn(backMoveBtn, xPos, yPos))
                {
                    backOneMove();
                    InvalidateRect(hWnd, &rc, FALSE);
                }
                else if (checkOnBtn(saveGameBtn, xPos, yPos))
                {
                    save(currentBoardSize);
                }
                else if (checkOnBtn(loadGameBtn, xPos, yPos))
                {
                    load(currentBoardSize);
                    gameJustStarted = false;
                    InvalidateRect(hWnd, &rc, FALSE);
                }
                  
            }
        }
        else
        {
            int xPos = LOWORD(lParam);
            int yPos = HIWORD(lParam);
            //play4x4Btn.weight = 300;
           // play5x5Btn.weight = 300;
            //play5x5Btn.weight = 300;
            if (checkOnBtn(play4x4Btn, xPos, yPos))
            {
                //play4x4Btn.weight = 900;
                tileWidth = DEFAULT_TILE_WIDTH;
                tileHeight = DEFAULT_TILE_HEIGHT;
                currentBoardSize = LITTLE_BOARD;
                ResetBoard(hWnd);
                gameJustStarted = true;
                isInMenu = false;
                
            }
            else if (checkOnBtn(play5x5Btn, xPos, yPos))
            {
                //play5x5Btn.weight = 900;
                tileWidth = MEDIUM_TILE_WIDTH;
                tileHeight = MEDIUM_TILE_HEIGHT;
                currentBoardSize = MEDIUM_BOARD;
                gameJustStarted = true;
                ResetBoard(hWnd);
                isInMenu = false;
            }
            else if (checkOnBtn(play6x6Btn, xPos, yPos)) 
            {
                //play5x5Btn.weight = 900;
                tileWidth = BIG_TILE_WIDTH;
                tileHeight = BIG_TILE_WIDTH;
                currentBoardSize = BIG_BOARD;
                gameJustStarted = true;
                ResetBoard(hWnd);
                isInMenu = false;
            }
            InvalidateRect(hWnd, &rc, FALSE);
        }
        break;
    }
    case WM_KEYDOWN:
        if(!isInMenu)
        {
            if(!isLose && !isWin)
            { 
                switch (wParam) {
                    case VK_LEFT:
    
                    case VK_RIGHT:
               
                    case VK_UP:
             
                    case VK_DOWN:
                        ProcessMove(wParam);
                        CheckForWin();
                        if (!isWin)
                        {
                            isLose = CheckForLoose();
                        }
                        break;

                    case VK_ESCAPE:
                        isInMenu = true;
                        InvalidateRect(hWnd, &rc, true);
                        break;
                    ////case VK_F1: {
                     //   load(currentBoardSize);
                      //  InvalidateRect(hWnd, &rc, false);
                      //  break;
                   // }
                   // case VK_F2: {
                     //   save(currentBoardSize);
                     //   break;
                   // }
                }
                
                break;
            }
        }
        else {
            switch (wParam) {
            case VK_ESCAPE:
                PostQuitMessage(0);
                break;
            }
            break;
        }
        break;
    case WM_DESTROY:
        PostQuitMessage(0);
        break;

    default:
        return DefWindowProc(hWnd, message, wParam, lParam);
    }
    return 0;
}
void backOneMove() {
    if (countOfBack != 0 && !gameJustStarted)
    {
        countOfBack--;
        for (int i = 0;i < currentBoardSize;i++)
        {
            for (int j = 0;j < currentBoardSize;j++)
            {
                board[i][j] = previousBoard[i][j];
            }
        }
        currentScore = previousScore;
    }
}
void load(int currentBoardSize)
{
    ResetBoard(hwnd);
    HANDLE fileHandle = NULL;
    switch (currentBoardSize)
    {
        case 4:{
            fileHandle = CreateFile(
                L"E:\\SP\\Kurs_project\\2048_third\\2048_sec_try(without_anim)\\2048_first_try\\save\\4x4.txt",                   // Имя файла
                GENERIC_READ | GENERIC_WRITE,               // Режим доступа (чтение/запись)
                FILE_SHARE_READ,            // Режим совместного доступа (чтение)
                NULL,                       // Атрибуты защиты (по умолчанию)
                OPEN_EXISTING,              // Действие при открытии (открыть существующий файл)
                FILE_ATTRIBUTE_NORMAL,      // Атрибуты файла (обычный файл)
                NULL                        // Хэндл шаблона файла (не используется)
            );
           
            break;
        }
        case 5: {
            fileHandle = CreateFile(
                L"E:\\SP\\Kurs_project\\2048_third\\2048_sec_try(without_anim)\\2048_first_try\\save\\5x5.txt",                   // Имя файла
                GENERIC_READ | GENERIC_WRITE,               // Режим доступа (чтение/запись)
                FILE_SHARE_READ,            // Режим совместного доступа (чтение)
                NULL,                       // Атрибуты защиты (по умолчанию)
                OPEN_EXISTING,              // Действие при открытии (открыть существующий файл)
                FILE_ATTRIBUTE_NORMAL,      // Атрибуты файла (обычный файл)
                NULL                        // Хэндл шаблона файла (не используется)
            );

            break;
        }
        case 6: {
            fileHandle = CreateFile(
                L"E:\\SP\\Kurs_project\\2048_third\\2048_sec_try(without_anim)\\2048_first_try\\save\\6x6.txt",                   // Имя файла
                GENERIC_READ | GENERIC_WRITE,               // Режим доступа (чтение/запись)
                FILE_SHARE_READ,            // Режим совместного доступа (чтение)
                NULL,                       // Атрибуты защиты (по умолчанию)
                OPEN_EXISTING,              // Действие при открытии (открыть существующий файл)
                FILE_ATTRIBUTE_NORMAL,      // Атрибуты файла (обычный файл)
                NULL                        // Хэндл шаблона файла (не используется)
            );

            break;
        }
    }
    if(fileHandle != INVALID_HANDLE_VALUE)
    { 
        char buffer[500];
        OVERLAPPED ol = { 0 };
        DWORD  numBytes= 0;
        BOOL isRead = ReadFile(fileHandle, buffer, 500,&numBytes, &ol);
        int t = 0;
        int nums[MAX_BOARD_SIZE * MAX_BOARD_SIZE*2+3];
        if (numBytes > 0)
        {
            char currentNum[5];
            int j = 0;
            int  i = 0;
            while(i<numBytes)
            {
                if (buffer[i] != '\r') {
                    currentNum[j] = buffer[i];
                    j++;
               
                }
                else
                {
                    currentNum[j] = '\0';
                    int number = strToNum(currentNum);
                    nums[t] = number;
                    t++;
                    j = 0;
                    i++;
                }
                i++;
            }
        }
        t = 0;
        for (int i = 0;i < currentBoardSize;i++)
        {
            for (int j = 0;j < currentBoardSize;j++) {
                board[i][j].value = nums[t];
                previousBoard[i][j].value = nums[t + currentBoardSize * currentBoardSize];
                t++;
            }
        }
        currentScore = nums[t+ currentBoardSize * currentBoardSize];
        previousScore = nums[t + currentBoardSize * currentBoardSize + 1];
        countOfBack= nums[t + currentBoardSize * currentBoardSize + 2];
        CloseHandle(fileHandle);
    }
}
int strToNum(char* str)
{
    int length = strlen(str);
    int number = 0;
    int i = 1;
    while (length != 0)
    {
      
        number += (str[length - 1] - '0')*i;
        i = i*10;
        length--;
    }
    return number;
}
void save(int currentBoardSize) {
    HANDLE fileHandle = NULL;
    switch (currentBoardSize)
    {
    case 4: {
        fileHandle = CreateFile(
            L"E:\\SP\\Kurs_project\\2048_third\\2048_sec_try(without_anim)\\2048_first_try\\save\\4x4.txt",                   // Имя файла
            GENERIC_READ | GENERIC_WRITE,               // Режим доступа (чтение/запись)
            FILE_SHARE_READ,            // Режим совместного доступа (чтение)
            NULL,                       // Атрибуты защиты (по умолчанию)
            OPEN_ALWAYS,              // Действие при открытии (открыть существующий файл)
            FILE_ATTRIBUTE_NORMAL,      // Атрибуты файла (обычный файл)
            NULL                        // Хэндл шаблона файла (не используется)
        );

        break;
    }
    case 5: {
        fileHandle = CreateFile(
            L"E:\\SP\\Kurs_project\\2048_third\\2048_sec_try(without_anim)\\2048_first_try\\save\\5x5.txt",                   // Имя файла
            GENERIC_READ | GENERIC_WRITE,               // Режим доступа (чтение/запись)
            FILE_SHARE_READ,            // Режим совместного доступа (чтение)
            NULL,                       // Атрибуты защиты (по умолчанию)
            OPEN_ALWAYS,              // Действие при открытии (открыть существующий файл)
            FILE_ATTRIBUTE_NORMAL,      // Атрибуты файла (обычный файл)
            NULL                        // Хэндл шаблона файла (не используется)
        );

        break;
    }
    case 6: {
        fileHandle = CreateFile(
            L"E:\\SP\\Kurs_project\\2048_third\\2048_sec_try(without_anim)\\2048_first_try\\save\\6x6.txt",                   // Имя файла
            GENERIC_READ | GENERIC_WRITE,               // Режим доступа (чтение/запись)
            FILE_SHARE_READ,            // Режим совместного доступа (чтение)
            NULL,                       // Атрибуты защиты (по умолчанию)
            OPEN_ALWAYS,              // Действие при открытии (открыть существующий файл)
            FILE_ATTRIBUTE_NORMAL,      // Атрибуты файла (обычный файл)
            NULL                        // Хэндл шаблона файла (не используется)
        );

        break;
    }
    }
    char numBuffer[6];
    char bufferToWrite[500];
    int currPosition = 0;
    for (int i = 0;i < currentBoardSize;i++)
    {
        for (int j = 0;j < currentBoardSize;j++) {
            intToCharStr(board[i][j].value, numBuffer);
            int length = strlen(numBuffer);
            numBuffer[length] = '\r';
            numBuffer[length + 1] = '\n';
            for (int k = 0;k <= length + 1;k++)
            {
                bufferToWrite[currPosition] = numBuffer[k];
                currPosition++;
            }
        }
    }
    for (int i = 0;i < currentBoardSize;i++)
    {
        for (int j = 0;j < currentBoardSize;j++) {
            intToCharStr(previousBoard[i][j].value, numBuffer);
            int length = strlen(numBuffer);
            numBuffer[length] = '\r';
            numBuffer[length + 1] = '\n';
            for (int k = 0;k <= length + 1;k++)
            {
                bufferToWrite[currPosition] = numBuffer[k];
                currPosition++;
            }
        }
    }
    intToCharStr(currentScore, numBuffer);
    int length = strlen(numBuffer);
    numBuffer[length] = '\r';
    numBuffer[length + 1] = '\n';
    for (int k = 0;k <= length + 1;k++)
    {
        bufferToWrite[currPosition] = numBuffer[k];
        currPosition++;
    }
    intToCharStr(previousScore, numBuffer);
    numBuffer[length] = '\r';
    numBuffer[length + 1] = '\n';
    for (int k = 0;k <= length + 1;k++)
    {
        bufferToWrite[currPosition] = numBuffer[k];
        currPosition++;
    }
    intToCharStr(countOfBack, numBuffer);
    numBuffer[length] = '\r';
    numBuffer[length + 1] = '\n';
    for (int k = 0;k <= length + 1;k++)
    {
        bufferToWrite[currPosition] = numBuffer[k];
        currPosition++;
    }
    BOOL bErrorFlag = FALSE;
    DWORD dwBytesWritten = 0;
    bErrorFlag = WriteFile(
        fileHandle,           // open file handle
        bufferToWrite,      // start of data to write
        500,  // number of bytes to write
        &dwBytesWritten, // number of bytes that were written
        NULL);
    CloseHandle(fileHandle);
}

void PaintTile( HDC hdc, HDC compatibleDC, int number, int i, int j,BOOL isAnimate)
{
    HBITMAP bitmap, oldbitmap;
    int diff = 0;
    if (isAnimate) diff = animateDiff;
    HBITMAP bmpMask = LoadImage(hInst, L".\\bmps\\pole_mask.bmp",IMAGE_BITMAP, tileWidth+diff, tileHeight + diff, LR_LOADFROMFILE); ;
    switch (number)
    {
    case 0: 
        DeleteObject(bmpMask);
        bitmap = LoadImage(hInst, L".\\bmps\\bitmap1.bmp", IMAGE_BITMAP, tileWidth + diff, tileHeight + diff, LR_LOADFROMFILE); 
        bmpMask = LoadImage(hInst, L".\\bmps\\bitmap1_mask.bmp", IMAGE_BITMAP, tileWidth + diff, tileHeight + diff, LR_LOADFROMFILE);
        break;
    case 2:     bitmap = LoadImage(hInst, L".\\bmps\\pole_2.bmp", IMAGE_BITMAP, tileWidth + diff, tileHeight + diff, LR_LOADFROMFILE); break;
    case 4:     bitmap = LoadImage(hInst, L".\\bmps\\pole_4.bmp", IMAGE_BITMAP, tileWidth + diff, tileHeight + diff, LR_LOADFROMFILE); break;
    case 8:     bitmap = LoadImage(hInst, L".\\bmps\\pole_8.bmp", IMAGE_BITMAP, tileWidth + diff, tileHeight + diff, LR_LOADFROMFILE); break;
    case 16:     bitmap = LoadImage(hInst, L".\\bmps\\pole_16.bmp", IMAGE_BITMAP, tileWidth + diff, tileHeight + diff, LR_LOADFROMFILE); break;
    case 32:     bitmap = LoadImage(hInst, L".\\bmps\\pole_32.bmp", IMAGE_BITMAP, tileWidth + diff, tileHeight + diff, LR_LOADFROMFILE); break;
    case 64:     bitmap = LoadImage(hInst, L".\\bmps\\pole_64.bmp", IMAGE_BITMAP, tileWidth + diff, tileHeight + diff, LR_LOADFROMFILE); break;
    case 128:     bitmap = LoadImage(hInst, L".\\bmps\\pole_128.bmp", IMAGE_BITMAP, tileWidth + diff, tileHeight + diff, LR_LOADFROMFILE); break;
    case 256:     bitmap = LoadImage(hInst, L".\\bmps\\pole_256.bmp", IMAGE_BITMAP, tileWidth + diff, tileHeight + diff, LR_LOADFROMFILE); break;
    case 512:     bitmap = LoadImage(hInst, L".\\bmps\\pole_512.bmp", IMAGE_BITMAP, tileWidth + diff, tileHeight + diff, LR_LOADFROMFILE); break;
    case 1024:    bitmap = LoadImage(hInst, L".\\bmps\\pole_1024.bmp", IMAGE_BITMAP, tileWidth + diff, tileHeight + diff, LR_LOADFROMFILE); break;
    case 2048:     bitmap = LoadImage(hInst, L".\\bmps\\pole_2048.bmp", IMAGE_BITMAP, tileWidth + diff, tileHeight + diff, LR_LOADFROMFILE); break;
    default:    return;
    }
    HDC memDC = CreateCompatibleDC(hdc);
    oldbitmap = (HBITMAP)SelectObject(memDC, bitmap);
    BITMAP bi;
    GetObject(bitmap, sizeof(BITMAP), &bi);
    SelectObject(memDC, bmpMask);
    BitBlt(compatibleDC, i, j, bi.bmWidth,bi.bmHeight, memDC, 0, 0, SRCAND);

    SelectObject(memDC, bitmap);
    BitBlt(compatibleDC, i, j, bi.bmWidth, bi.bmHeight, memDC, 0, 0, SRCPAINT);
   
    //BitBlt(compatibleDC, i, j, bi.bmWidth, bi.bmHeight, memDC, 0, 0, SRCCOPY);

    SelectObject(memDC, oldbitmap);
    DeleteObject(bitmap);
    DeleteObject(bmpMask);
    DeleteObject(memDC);
}
int WINAPI wWinMain(HINSTANCE hInstance,
	HINSTANCE hPrevInstance, LPTSTR lpCmdLine, int nCmdShow)
{
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
    wc.lpszClassName = L"MessageHandler";
    wc.hIcon = NULL;
    wc.hIconSm = wc.hIcon;

    RegisterClassExW(&wc);

    HWND hWnd = CreateWindow(L"MessageHandler", L"Lab 2", WS_OVERLAPPEDWINDOW | WS_VISIBLE, CW_USEDEFAULT, CW_USEDEFAULT, DEFAULT_WND_WIDTH, DEFAULT_WND_HEIGHT, NULL, NULL, hInstance, NULL);
    hwnd = hWnd;
    ResetBoard(hWnd);
    ShowWindow(hWnd, nCmdShow);
    UpdateWindow(hWnd);
    MSG message;
    hInst = hInstance;

    while (GetMessageW(&message, NULL, 0, 0)) {
        TranslateMessage(&message);
        DispatchMessageW(&message);
    }

    return (int)message.wParam;

}
void intToStr(int num, TCHAR* str)
{
    int i = 0;
    if (num == 0)
    {
        str[i] = '0';
        i++;
    }else
    { 
        while (num > 0)
        {
            str[i] = num % 10 + '0';
            num = num / 10;
            i++;
        }
        int len = i;
        for (int j = 0; j < len / 2; j++) {
            char temp = str[j];
            str[j] = str[len - j - 1];
            str[len - j - 1] = temp;
        }
    }
    str[i] = '\0';
}
void intToCharStr(int num, char* str)
{
    int i = 0;
    if (num == 0)
    {
        str[i] = '0';
        i++;
    }
    else
    {
        while (num > 0)
        {
            str[i] = num % 10 + '0';
            num = num / 10;
            i++;
        }
        int len = i;
        for (int j = 0; j < len / 2; j++) {
            char temp = str[j];
            str[j] = str[len - j - 1];
            str[len - j - 1] = temp;
        }
    }
    str[i] = '\0';
}
void DrawMenu(HDC hdc, HDC compatibleDC)
{
    RECT tlo;
    tlo.bottom = tlo.right = 1000;
    tlo.left = tlo.top = 0;
    HBRUSH brush = CreateSolidBrush(RGB(250, 247, 238));
    FillRect(compatibleDC, &tlo, brush);
    DeleteObject(brush);
    RECT rect;
    GetClientRect(hwnd, &rect);
    int buttonAreaMargin = MARGIN_LEFTRIGHT;
    int buttonAreaWidth = rect.right-rect.left-2*MARGIN_LEFTRIGHT;
    int buttonAreaHeight = rect.bottom - rect.top;
    int buttonAreaBetween = buttonAreaHeight / 16;
    int buttonHeight = buttonAreaBetween * 4;
    rect.left = MARGIN_LEFTRIGHT;
    rect.right = MARGIN_LEFTRIGHT + buttonAreaWidth;
    rect.top = buttonAreaBetween;
    rect.bottom = buttonAreaBetween + buttonHeight;
    play4x4Btn.rect = rect;
    rect.top = rect.top + buttonAreaBetween + buttonHeight;
    rect.bottom = rect.top + buttonHeight;
    play5x5Btn.rect = rect;
    rect.top = rect.top + buttonAreaBetween + buttonHeight;
    rect.bottom = rect.top + buttonHeight;
    play6x6Btn.rect = rect;
    play4x4Btn.text = L"Play 4x4 mode";
    play5x5Btn.text = L"Play 5x5 mode";
    play6x6Btn.text = L"Play 6x6 mode";
    DrawMenuButton(play4x4Btn, compatibleDC, hdc);
    DrawMenuButton(play5x5Btn, compatibleDC, hdc);
    DrawMenuButton(play6x6Btn, compatibleDC, hdc);
}
BOOL checkOnBtn(button btn, int xPos, int yPos)
{
    if (xPos >= btn.rect.left && xPos <= btn.rect.right
        && yPos >= btn.rect.top && yPos <= btn.rect.bottom)
    {
        return true;
    }
    return false;
}
void DrawMenuButton(button drawBtn,HDC compatDC,HDC hdc) {
    HFONT hFont = CreateFont(36, 0, 0, 0, drawBtn.weight, FALSE, FALSE, FALSE, DEFAULT_CHARSET, OUT_OUTLINE_PRECIS,
        CLIP_DEFAULT_PRECIS, CLEARTYPE_QUALITY, VARIABLE_PITCH, L"Times New Roman");
    HDC memDC = CreateCompatibleDC(hdc);
    HBITMAP bitmap;
    int diff = 0;
    if (drawBtn.weight == 900)
    {
        diff = 10;
    }
    bitmap = LoadImage(hInst, L".\\bmps\\menuBtn.bmp", IMAGE_BITMAP, drawBtn.rect.right-drawBtn.rect.left+diff, drawBtn.rect.bottom-drawBtn.rect.top+diff, LR_LOADFROMFILE);
    HBITMAP oldBitMap = SelectObject(memDC, bitmap);
    BitBlt(compatDC, drawBtn.rect.left-diff/2, drawBtn.rect.top-diff/2, drawBtn.rect.right - drawBtn.rect.left+diff, drawBtn.rect.bottom - drawBtn.rect.top+diff, memDC, 0, 0, SRCCOPY);
    SelectObject(memDC, oldBitMap);
    DeleteObject(bitmap);
    DeleteObject(memDC);
    HFONT oldFont = SelectObject(compatDC, hFont);
    HBRUSH newBrush = CreateSolidBrush(RGB(204, 192, 174));
    HBRUSH oldBrush = SelectObject(compatDC, newBrush);
    HPEN newPen = CreatePen(PS_SOLID, 5, RGB(204, 192, 174));
    HPEN oldPen = SelectObject(compatDC, newPen);
    SetTextColor(compatDC, RGB(119, 110, 101));
    SetTextAlign(compatDC, TA_LEFT);
    SetBkMode(compatDC, TRANSPARENT);
    DrawTextW(compatDC, drawBtn.text, -1, &drawBtn.rect, DT_NOCLIP | DT_CENTER | DT_VCENTER | DT_SINGLELINE);
    SelectObject(compatDC, oldBrush);
    SelectObject(compatDC, oldFont);
    SelectObject(compatDC, newPen);
    DeleteObject(newBrush);
    DeleteObject(hFont);
    DeleteObject(newPen);
}
void DrawScoreBoard(HDC hdc, HDC compatDC) {
    HBITMAP bitmap, oldbitmap;
    RECT rect;
    bitmap = LoadImage(hInst, L".\\bmps\\scoreBoard.bmp", IMAGE_BITMAP, 0, 0, LR_LOADFROMFILE);
    HDC memDC = CreateCompatibleDC(hdc);
    oldbitmap = (HBITMAP)SelectObject(memDC, bitmap);
    BitBlt(compatDC, between+MARGIN_LEFTRIGHT, between, between + DEFAULT_BOARD_WIDTH, between + 70, memDC, 0, 0, SRCCOPY);
    HFONT hFont = CreateFont(48, 0, 0, 0, FW_BOLD, FALSE, FALSE, FALSE, DEFAULT_CHARSET, OUT_OUTLINE_PRECIS,
        CLIP_DEFAULT_PRECIS, CLEARTYPE_QUALITY, VARIABLE_PITCH, L"Times New Roman");
    HFONT oldFont = SelectObject(compatDC, hFont);
    SetTextColor(compatDC, RGB(255, 255, 255));
    SetTextAlign(compatDC, TA_LEFT);
    SetRect(&rect, between + MARGIN_LEFTRIGHT, between, between + DEFAULT_BOARD_WIDTH, between + 70);
    SetBkColor(compatDC, RGB(204, 192, 174));
    TCHAR text[15] = L"Score:";
    TCHAR* score = (TCHAR*)malloc(sizeof(TCHAR*));
    intToStr(currentScore, score);
    wcscat_s(text,15, score);
    DrawTextW(compatDC, text, -1, &rect, DT_NOCLIP | DT_CENTER | DT_VCENTER | DT_SINGLELINE);
    SelectObject(compatDC, oldFont);
    SelectObject(memDC, oldbitmap);
    DeleteObject(hFont);
    DeleteObject(bitmap);
    DeleteObject(memDC);
}
void DrawBoard(HDC hdc,HDC compatibleDC)
{
    PAINTSTRUCT ps;
    RECT tlo;
    tlo.bottom = tlo.right = 1000;
    tlo.left = tlo.top = 0;
    HBRUSH brush = CreateSolidBrush(RGB(250, 247, 238));
    FillRect(compatibleDC, &tlo, brush);
    DeleteObject(brush);
    HPEN pen = CreatePen(PS_SOLID, 1, RGB(187, 173, 160));
    HPEN oldPen = SelectObject(compatibleDC, pen);
    brush = CreateSolidBrush(RGB(187, 173, 160));
    HBRUSH oldBrush = SelectObject(compatibleDC, brush);
    Rectangle(compatibleDC, MARGIN_LEFTRIGHT, MARGIN_TOP, MARGIN_LEFTRIGHT + DEFAULT_BOARD_WIDTH, MARGIN_TOP + DEFAULT_BOARD_HEIGHT);
    SelectObject(compatibleDC, oldBrush);
    SelectObject(compatibleDC, oldPen);
    DeleteObject(pen);
    DeleteObject(brush);
    DrawScoreBoard(hdc,compatibleDC);
   
    for (int i = 0; i < currentBoardSize; i++)
    {
        for (int j = 0; j < currentBoardSize; j++)
        {
            PaintTile( hdc, compatibleDC, board[i][j].value, board[i][j].rect.left, board[i][j].rect.top, board[i][j].isAnimate);
            board[i][j].isAnimate = false;
        }
    }
}
void CheckForWin()
{
    for (int i = 0;i < currentBoardSize;i++)
    {
        for (int j = 0;j < currentBoardSize;j++)
        {
            if (board[i][j].value == 2048)
            {
                isWin = true;
            }
        }
    }
}
BOOL CheckForLoose() {
    for (int i = 0;i < currentBoardSize;i++)
    {
        for (int j = 0;j < currentBoardSize;j++)
        {
            if (board[i][j].value == 0 || board[i][j].value==WIN_VALUE) return false;
        }
    }
    BOOL isHaveToMerge = false;;
    for (int i = 0;i < currentBoardSize;i++)
    {
        for (int j = 0;j < currentBoardSize;j++)
        {
            if (j != currentBoardSize - 1)
            {
                if (board[i][j].value == board[i][j + 1].value) {
                    isHaveToMerge = true;
                }
            }
            if (i != currentBoardSize - 1)
            {
                if (board[i][j].value == board[i + 1][j].value)
                {
                    isHaveToMerge = true;
                }
            }
        }
    }
    if (isHaveToMerge)
    {
        return false;
    }
    return true;
}

void GenerateNewNumber()
{
    // Поиск пустой клетки
   // int emptyCells[BOARD_SIZE * BOARD_SIZE];
    int emptyCount = 0;
    for (int i = 0; i < currentBoardSize; i++) {
        for (int j = 0; j < currentBoardSize; j++) {
            if (board[i][j].value== 0 ) {
                emptyCount++;
            }
        }
    }
        if (emptyCount > 0)
    {
        int randomNum = rand() % emptyCount+1;
        int currNum = 0;
        for (int i = 0;i < currentBoardSize;i++)
        {
            for (int j = 0;j < currentBoardSize;j++)
            {
                if (board[i][j].value == 0)
                {
                    currNum++;
                    if (currNum == randomNum)
                    {
                        board[i][j].value = (rand() % 2 + 1) * 2; // Генерация числа 2 или 4
                    }
                }
            }
        }
    }

}

void ResetBoard(HWND hWnd)
{
    RECT rect;
    GetClientRect(hWnd, &rect);
    int width = rect.right - rect.left;
    int height = rect.bottom - rect.top;
    countOfBack = 1;

    for (int i = 0; i < currentBoardSize; i++) {
        for (int j = 0; j < currentBoardSize; j++) {
            Tile newTile;
            RECT currRect;
            currRect.left = j * tileWidth + between*(j+1)+MARGIN_LEFTRIGHT;
            currRect.top = i * tileHeight + between*(i+1)+MARGIN_TOP;
            currRect.right = currRect.left+tileWidth;
            currRect.bottom = currRect.top+tileHeight;
            newTile.rect = currRect;
            newTile.value = 0;
            newTile.isAnimate = false;
            newTile.isMerged = false;
            board[i][j] = newTile;
            previousBoard[i][j] = newTile;
            previousScore = 0;
        }
    }
    isLose = false;
    isWin = false;
    GenerateNewNumber();
    GenerateNewNumber();
}


void ProcessMove(int direction)
{
    Tile tmpBoard[MAX_BOARD_SIZE][MAX_BOARD_SIZE];
    for (int i = 0;i < currentBoardSize;i++)
    {
        for (int j = 0;j < currentBoardSize;j++)
        {
            tmpBoard[i][j] = board[i][j];
        }
    }
    int moved = 0;
    switch (direction) {
    case VK_LEFT:
        for (int i = 0; i < currentBoardSize; i++) {
            for (int j = 1; j < currentBoardSize; j++) {
                if (board[i][j].value != 0) {
                    int k = j - 1;
                    while (k >= 0 && board[i][k].value == 0) {
                        k--;
                    }
                    if (k >= 0 && board[i][k].value == board[i][j].value &&  !board[i][k].isMerged) {
               
                        board[i][j].value = 0;
                        board[i][k].value *= 2;
                        board[i][k].isAnimate = true;
                        board[i][k].isMerged = true;
                        previousScore = currentScore;
                        currentScore += board[i][k].value;
                        isAnimate = true;
                        moved = 1;
                    }
                    else if (k + 1 != j) {
                        board[i][k + 1].value = board[i][j].value;
                        board[i][j].value = 0;
                        moved = 1;
                    }
                }
            }
        }
        break;

    case VK_RIGHT:
        for (int i = 0; i < currentBoardSize; i++) {
            for (int j = currentBoardSize - 2; j >= 0; j--) {
                if (board[i][j].value != 0) {
                    int k = j + 1;
                    while (k < currentBoardSize && board[i][k].value == 0) {
                        k++;
                    }
                    if (k < currentBoardSize && board[i][k].value == board[i][j].value && !board[i][k].isMerged) {
                        board[i][j].value = 0;
                        board[i][k].value *= 2;
                        board[i][k].isAnimate = true;
                        board[i][k].isMerged = true;
                        previousScore = currentScore;
                        currentScore += board[i][k].value;
                        isAnimate = true;
                        moved = 1;
                    }
                    else if (k - 1 != j) {
                        board[i][k - 1].value = board[i][j].value;
                        board[i][j].value = 0;
                        moved = 1;
                    }
                }
            }
        }
        break;

    case VK_UP:
        for (int i = 1; i < currentBoardSize; i++) {
            for (int j = 0; j < currentBoardSize; j++) {
                if (board[i][j].value != 0) {
                    int k = i - 1;
                    while (k >= 0 && board[k][j].value == 0) {
                        k--;
                    }
                    if (k >= 0 && board[k][j].value == board[i][j].value && !board[k][j].isMerged) {
                        board[k][j].value *= 2;
                        board[i][j].value = 0;
                        previousScore = currentScore;
                        currentScore += board[k][j].value;
                        board[k][j].isAnimate = true;
                        board[k][j].isAnimate = true;
                        isAnimate = true;
                        moved = 1;
                    }
                    else if (k + 1 != i) {
                        board[k + 1][j].value = board[i][j].value;
                        board[i][j].value = 0;
                        moved = 1;
                    }
                }
            }
        }
        break;

    case VK_DOWN:
        for (int i = currentBoardSize - 2; i >= 0; i--) {
            for (int j = 0; j < currentBoardSize; j++) {
                if (board[i][j].value != 0) {
                    int k = i + 1;
                    while (k < currentBoardSize && board[k][j].value == 0) {
                        k++;
                    }
                    if (k < currentBoardSize && board[k][j].value == board[i][j].value && !board[k][j].isMerged) {
                        board[k][j].value *= 2;
                        board[i][j].value = 0;
                        board[k][j].isAnimate = true;
                        board[k][j].isMerged= true;
                        isAnimate = true;
                        previousScore = currentScore;
                        currentScore += board[k][j].value;
                        moved = 1;
                    }
                    else if (k - 1 != i) {
                        board[k - 1][j].value = board[i][j].value;
                        board[i][j].value = 0;
                        moved = 1;
                    }
                }
            }
        }
        break;
    }
    
    if (moved) {
        for (int i = 0;i < currentBoardSize;i++)
        {
            for (int j = 0;j < currentBoardSize;j++)
            {
                gameJustStarted = false;
                previousBoard[i][j] = tmpBoard[i][j];
                board[i][j].isMerged = false;
            }
        }
        GenerateNewNumber();
        generatenew = true;
        
    }
    InvalidateRect(hwnd, NULL, FALSE);
}
void DrawLoose(HDC compatDC) {
    RECT rect;
    HFONT hFont = CreateFont(48, 0, 0, 0, FW_BOLD, FALSE, FALSE, FALSE, DEFAULT_CHARSET, OUT_OUTLINE_PRECIS,
        CLIP_DEFAULT_PRECIS, CLEARTYPE_QUALITY, VARIABLE_PITCH, L"Times New Roman");
    HFONT oldFont = SelectObject(compatDC, hFont);
    HPEN newPen = CreatePen(PS_SOLID,5,RGB(204, 192, 174));
    HPEN oldPen = SelectObject(compatDC, newPen);
    SetTextColor(compatDC, RGB(247, 144, 17));
    SetTextAlign(compatDC, TA_LEFT);
    SetRect(&rect, MARGIN_LEFTRIGHT, MARGIN_TOP + between + 70, MARGIN_LEFTRIGHT + DEFAULT_BOARD_WIDTH, MARGIN_TOP + between + 70 * 2);
    SetBkMode(compatDC, TRANSPARENT);
    TCHAR text[14] = L"Game Over!!!\0";
    HBRUSH newBrush = CreateSolidBrush(RGB(187, 173, 160));
    HBRUSH oldBrush = SelectObject(compatDC, newBrush);
    Rectangle(compatDC, rect.left, rect.top, rect.right, rect.bottom+70+between);
    SelectObject(compatDC, oldBrush);
    DeleteObject(newBrush);
    newBrush = CreateSolidBrush(RGB(204, 192, 174));
    oldBrush = SelectObject(compatDC, newBrush);
    DrawTextW(compatDC, text, -1, &rect, DT_NOCLIP | DT_CENTER | DT_VCENTER | DT_SINGLELINE);
    SelectObject(compatDC, oldFont);
    SelectObject(compatDC, oldBrush);
    SelectObject(compatDC, oldPen);
    DeleteObject(newBrush);
    DeleteObject(hFont);
    DeleteObject(newPen);
    DrawButton(retryBtn, compatDC);
    DrawButton(exitBtn, compatDC);
}
void DrawButton(button btn,HDC compatDC) {
    HFONT hFont = CreateFont(36, 0, 0, 0, btn.weight, FALSE, FALSE, FALSE, DEFAULT_CHARSET, OUT_OUTLINE_PRECIS,
        CLIP_DEFAULT_PRECIS, CLEARTYPE_QUALITY, VARIABLE_PITCH, L"Times New Roman");
    HFONT oldFont = SelectObject(compatDC, hFont);
    HBRUSH newBrush = CreateSolidBrush(RGB(204, 192, 174));
    HBRUSH oldBrush = SelectObject(compatDC, newBrush);
    HPEN newPen = CreatePen(PS_SOLID, 5, RGB(204, 192, 174));
    HPEN oldPen = SelectObject(compatDC, newPen);
    SetTextColor(compatDC, RGB(247, 144, 17));
    SetTextAlign(compatDC, TA_LEFT);
    SetBkMode(compatDC, TRANSPARENT);
    DrawTextW(compatDC, btn.text, -1, &btn.rect, DT_NOCLIP | DT_CENTER | DT_VCENTER | DT_SINGLELINE);
    SelectObject(compatDC, oldBrush);
    SelectObject(compatDC, oldFont);
    SelectObject(compatDC, newPen);
    DeleteObject(newBrush);
    DeleteObject(hFont);
    DeleteObject(newPen);
}
void DrawWin(HDC compatDC)
{
    RECT rect;
    HFONT hFont = CreateFont(48, 0, 0, 0, FW_BOLD, FALSE, FALSE, FALSE, DEFAULT_CHARSET, OUT_OUTLINE_PRECIS,
        CLIP_DEFAULT_PRECIS, CLEARTYPE_QUALITY, VARIABLE_PITCH, L"Times New Roman");
    HFONT oldFont = SelectObject(compatDC, hFont);
    HPEN newPen = CreatePen(PS_SOLID, 5, RGB(204, 192, 174));
    HPEN oldPen = SelectObject(compatDC, newPen);
    SetTextColor(compatDC, RGB(247, 144, 17));
    SetTextAlign(compatDC, TA_LEFT);
    SetRect(&rect, MARGIN_LEFTRIGHT, MARGIN_TOP + between + 70, MARGIN_LEFTRIGHT + DEFAULT_BOARD_WIDTH, MARGIN_TOP + between + 70 * 2);
    SetBkMode(compatDC, TRANSPARENT);
    TCHAR text[14] = L"You Win!!!\0";
    HBRUSH newBrush = CreateSolidBrush(RGB(187, 173, 160));
    HBRUSH oldBrush = SelectObject(compatDC, newBrush);
    Rectangle(compatDC, rect.left, rect.top, rect.right, rect.bottom + 70+ between);
    SelectObject(compatDC, oldBrush);
    DeleteObject(newBrush);
    newBrush = CreateSolidBrush(RGB(204, 192, 174));
    oldBrush = SelectObject(compatDC, newBrush);
    DrawTextW(compatDC, text, -1, &rect, DT_NOCLIP | DT_CENTER | DT_VCENTER | DT_SINGLELINE);
    SelectObject(compatDC, oldFont);
    SelectObject(compatDC, oldBrush);
    SelectObject(compatDC, oldPen);
    DeleteObject(newBrush);
    DeleteObject(hFont);
    DeleteObject(newPen);
    DrawButton(retryBtn, compatDC);
    DrawButton(exitBtn, compatDC);
}