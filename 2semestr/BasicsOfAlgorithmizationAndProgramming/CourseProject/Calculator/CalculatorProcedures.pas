unit CalculatorProcedures;

interface

uses System.SysUtils;

type
  Tstack = ^Sstack;

  Sstack = record
    data: string;
    next: Tstack;
    priority: integer;
  end;

  TROperation = record
    data: string;
    priority: integer;
  end;

  shale = procedure(var s: string); // ��� ��� �������� ������������� �������
  // ������� ��� �������� ������ �� 10-����� � 2-���� �������������
function MathInDecNew(s: string; n: integer; snum, sop: Tstack;
  var corflag: boolean): string;
// ������� ��� �������� ������ �� 18-����� � 2-���� �������������
function MathInOCt(s: string; n: integer; snum, sop: Tstack;
  var corflag: boolean): string;
// ������� ��� �������� ������ �� 16-����� � 2-���� �������������
function MathInHEx(s: string; n: integer; var corflag: boolean): string;
// ��������� ������ ������ � �������� �������
procedure reversestr(var s: string);
// ������� ��������� �������� ��� ��������, ����������� ����� � �������� �������������
function sor(s1, s2: string): string;
function sxor(s1, s2: string): string;
function sand(s1, s2: string): string;
/// /////////////////////////////////////
/// ������� �������� ������ � 10-���� c������ ���������
function indec(a: string; curr: integer): integer;
// ������� ����������� ������ �� ����� � ����� ������� ���������
function convertss(a: string; curr: integer; n: integer): string;
// ������� ��� ���������� �������� ����� ������� � �������� �������������, ������������� � ������
function diff(s1, s2: string; n: integer): string; // s1-s2
// ��������� ��� ���������� ����� ����� ������ ��� �������� �����������
procedure preparenumber(var s: string; n: integer);
// ��������� ��� �������������� ������
procedure convertnumber(var s: string; n: integer);
// ������� ���������, �������, ����� ��� �����, ���������� ����� � �������� �������������
function sum(s1, s2: string; n: integer): string;
function divbin(s1, s2: string; n: integer): string;
function binmul(s1, s2: string; n: integer): string;
/// ///////////////////////////////////////////////
/// ��������� ��� ���������� ������ ������
procedure clear(var s: string);
// ��������� ��� ������������� �������
procedure LogicShr(var s: string); // ����������  ������
procedure LogicShl(var s: string); // ����������  �����
procedure ArifmShl(var s: string); // �������������� �����
procedure ArifmShr(var s: string); // �������������� ������
procedure LoopShr(var s: string); // ����������� ������
procedure LoopShl(var s: string); // ����������� �����
// ������������ ��� ������ �� ������
procedure Push(var a: Tstack; b: TROperation);
procedure Pop(var a: Tstack; var b: TROperation);
/// ////////////////////////////////////////
/// ���������, ������� ������� �� ����� ������ ���������� �����, ������� �������� � ��������� ����� �� ����
procedure Mul(var snum, sop: Tstack; s: TROperation; n: integer);
procedure division(var snum, sop: Tstack; s: TROperation; n: integer);
procedure Pnot(var snum, sop: Tstack; s: TROperation);
procedure PShale(var snum, sop: Tstack; s: TROperation; sh_f: shale);
procedure Pand(var snum, sop: Tstack; s: TROperation);
procedure Psum(var snum, sop: Tstack; s: TROperation; n: integer);
procedure PSub(var snum, sop: Tstack; s: TROperation; n: integer);
procedure Por(var snum, sop: Tstack; s: TROperation);
procedure Pxor(var snum, sop: Tstack; s: TROperation);
/// /////////////////////////////////////////////////////
// ��������� ���������� ������ ������������ ��� �������� ������ ��������
procedure Count(s: TROperation; var snum, sop: Tstack; n: integer;
  shl_f, shr_f: shale);
// ��������� ,������� ���������� : ����� �� ������ �������� �� ���� ��� ������� ���������� ��������
procedure PlaceOrCount(var snum, sop: Tstack; current: TROperation; n: integer;
  shl_f, shr_f: shale);
// �������, ���������� �� �������� �������� �������� ���������
function GetResult(var snum, sop: Tstack; var s: string; n: integer;
  shl_f, shr_f: shale; countsys: integer): string;
// �������, ��� �������� ������������ ������ � �������� �������������
function CheckString(var s: string; n: integer): boolean;
// ��������� ��� ������������ ������,������� ����������� � ����� ����������
procedure disposestack(a: Tstack);

{ priority table
  not:4;
  *,/,div,mod,and,shl,shr:3;
  +,-,or,xor:2;
  =,<>,<=,>=:1; }

var
  snum, sop: Tstack;
  n, CountSystem: integer;
  shl_f: shale;
  shr_f: shale;
  bufs: string;
  zerodivideflag: boolean;
  outofshale: boolean;

implementation

procedure reversestr(var s: string);
var
  k: string;
  i, j: integer;
begin
  i := 0;
  k := copy(s, 1, length(s));
  for j := length(s) downto 1 do
  begin
    inc(i);
    s[i] := k[j];
  end;
end;

function sor(s1, s2: string): string;
var
  bufs: string;
  i: integer;
begin
  bufs := '';
  preparenumber(s1, n);
  preparenumber(s2, n);
  for i := 1 to length(s1) do
  begin
    if (s1[i] = '0') and (s2[i] = '0') then
    // ���� ������� ����� '0', �� � ���������� �������� '0'
      bufs := bufs + '0'
    else
      bufs := bufs + '1';
  end;
  result := bufs;
end;

function sxor(s1, s2: string): string;
var
  bufs: string;
  i: integer;
begin
  preparenumber(s1, n);
  preparenumber(s2, n);
  bufs := '';
  for i := 1 to length(s1) do
  begin
    if (s1[i] = s2[i]) then // ���� ������� �����, �� �������� � ���������� '0'
      bufs := bufs + '0'
    else // � ���� ������ �������� '1'
      bufs := bufs + '1';
  end;
  result := bufs;
end;

function sand(s1, s2: string): string;
var
  i: integer;
  bufs: string;
begin
  bufs := '';
  preparenumber(s1, n);
  preparenumber(s2, n);
  for i := 1 to length(s1) do
  begin
    if (s1[i] = '1') and (s2[i] = '1') then
    // ���� ������� '1' � '1',�� ���������� �������� '1'
      bufs := bufs + '1'
    else // � ���� ������ �������� '0'
      bufs := bufs + '0';
  end;
  result := bufs;
end;

function indec(a: string; curr: integer): integer;
var
  i: integer;
  x, y: integer;
  res: string;
begin
  x := 0;
  y := 1;
  a := uppercase(a);
  if curr <= 10 then
  begin
    res := a;
    // ������ �� ����� ������ ������ � ��������� ������� ����� �� �������� ��������� �������� ������� ��������� � ������� ������ ������ �����-1
    // ������ ����� ����������� � ����������
    for i := length(res) - 1 downto 0 do
    begin
      x := x + strtoint(res[i + 1]) * y;
      y := y * curr;
    end;
  end
  else // ����� ���������� ��������� �� ���� �� ��������,������
  begin // �����������, ��� � �������� ������� ��������� ����� �������������� �����
    res := a;
    for i := length(res) - 1 downto 0 do
    begin
      if (ord(res[i + 1]) >= ord('0')) and (ord(res[i + 1]) <= ord('9')) then
      // ���� ��������� �����
      begin
        x := x + strtoint(res[i + 1]) * y;
        y := y * curr;
      end
      else // ����� ��������� �����, ������������� ������ �����,� ����� ���������� ����������
      begin
        x := x + (ord(res[i + 1]) - ord('A') + 10) * y;
        y := y * curr;
      end;
    end;
  end;
  result := x;
end;

function convertss(a: string; curr: integer; n: integer): string;
var
  x: integer;
  k: boolean;
begin
if n=curr then
begin
  result:=a;
end
else
begin
  result := '';
  x := indec(a, curr);
  // ������� ���������� �������� � ���������� ������� ���������
  if x < n then
    k := true
  else
    k := false;
  if n < 10 then
  // ���� ��������� �������� ������� ��������� ������ 10,�� ������� ����� ������� �� ��������� � ������ �������
  begin
    while ((x div n) <> 0) or (k = true) do
    begin
      result := result + inttostr(x mod n);
      x := x div n;
      k := false;
    end;
    if (x mod n <> 0) then
    begin
      result := result + inttostr(x mod n);
      reversestr(result);
    end
    else
      reversestr(result);
  end
  else
  begin
    while (x >= n - 1) or (k = true) do
    // ������������ ������� ��� �� �������, ������ ����������� ����������� ��������� ����
    begin
      if ((x mod n) <= 9) then
      begin
        result := result + inttostr(x mod n);
        x := x div n;
      end
      else
      begin
        result := result + chr(ord('A') - 1 + ((x mod n) - 9));
        // � ���������� ����������� �����, �������������� ���������� �����
        x := x div n;
      end;
      k := false;
    end;
    if (x mod n <> 0) then
    begin
      if ((x mod n) <= 9) then
      begin
        result := result + inttostr(x mod n);
      end
      else
      begin
        result := result + chr(ord('A') - 1 + ((x mod n) - 9));

      end;

      reversestr(result);
    end
    else
      reversestr(result);
  end;
end;
end;

function diff(s1, s2: string; n: integer): string; // s1-s2
var
  bufs: string;
  i, j: integer;
begin
  preparenumber(s1, n);
  preparenumber(s2, n);
  bufs := '';
  for i := 1 to n do
    bufs := bufs + '0';
  i := n;
  while i >= 1 do
  begin
    if (s1[i] = '0') and (s2[i] = '1') then
    begin
      j := i;
      while (j >= 1) and (s1[j] <> '1') do
      begin
        dec(j);
      end;
      if j = 0 then
      begin
        for j := 1 to i - 1 do
        begin
          s1[j] := '1';
        end;
        bufs[i] := '1';
      end
      else
      begin
        s1[j] := '0';
        inc(j);
        while j <> i do
        begin
          s1[j] := '1';
          inc(j);
        end;
        bufs[i] := '1';
      end;
    end
    else if (s1[i] = '1') and (s2[i] = '0') then
    begin
      bufs[i] := '1';
    end
    else
      bufs[i] := '0';
    dec(i);
  end;
  result := bufs;
end;

procedure preparenumber(var s: string; n: integer);
var
  i: integer;
begin
  if length(s) < n then
  begin
    i := n - length(s);
    while i <> 0 do // ���������� ������� ���������� ����� � ������
    begin
      s := '0' + s;
      dec(i);
    end;
  end;
end;

procedure convertnumber(var s: string; n: integer);
var
  i: integer;
begin
  preparenumber(s, n);
  for i := 1 to length(s) do // ������ �� ������ � ����������� ��������
  begin
    if s[i] = '0' then
      s[i] := '1'
    else
      s[i] := '0';
  end;
end;

function sum(s1, s2: string; n: integer): string;
var
  bufs: string;
  i: integer;
  memory: integer;
begin
  preparenumber(s1, n);
  preparenumber(s2, n);
  bufs := '';
  for i := 1 to n do
    bufs := bufs + '0';
  memory := 0;
  for i := length(s1) downto 1 do
  // ������ �� ������� � �������� ����� �� �������� �������� �������� �����
  begin
    if (s1[i] = '0') and (s2[i] = '0') then
    begin
      if memory = 0 then
        bufs[i] := '0'
      else
      begin
        bufs[i] := '1';
        dec(memory);
      end;
    end
    else if ((s1[i] = '1') and (s2[i] = '0')) or
      ((s2[i] = '1') and (s1[i] = '0')) then
    begin
      if memory = 0 then
        bufs[i] := '1'
      else
      begin
        bufs[i] := '0';
      end;
    end
    else
    begin
      if memory = 1 then
        bufs[i] := '1'
      else
        bufs[i] := '0';
      memory := 1;
    end;
  end;
  result := bufs;
end;

function divbin(s1, s2: string; n: integer): string;
var
  bufs: string;
  n1, n2, res: integer;
begin
  res := 0;
  preparenumber(s1, n);
  preparenumber(s2, n);
  // ������� � ���������� ������� ���������
  if s1[1] = '0' then
    n1 := indec(s1, 2)
  else
  begin
    s1 := diff(s1, '00000001', n);
    convertnumber(s1, n);
    n1 := -indec(s1, 2);
  end;
  if s2[1] = '0' then
    n2 := indec(s2, 2)
  else
  begin
    s2 := diff(s2, '00000001', n);
    convertnumber(s2, n);
    n2 := -indec(s2, 2);
  end;
  if n2 = 0 then
  // ���� ������ ������� ����� 0, �� ������ ��������� � ������� �� ����
    bufs := 'zerodivide'
  else
    res := n1 div n2;
  if bufs = 'zerodivide' then
    result := bufs
  else
  begin
    if res >= 0 then
      result := convertss(inttostr(res), 10, 2)
      // ����������� � �������� ������� �������������� �����
    else
    begin // ����������� � �������������� ��� �������������� �����
      res := abs(res);
      result := convertss(inttostr(res), 10, 2);
      convertnumber(result, n);
      result := sum(result, '1', n);
    end;
  end;
end;

procedure clear(var s: string);
var
  j: integer;
begin
  for j := 1 to length(s) do
    s[j] := '0';
end;

function binmul(s1, s2: string; n: integer): string;
var
  i, j: integer;
  bufs, ch, resstr, number1, number2: string;
begin
  bufs := '';
  preparenumber(s1, n);
  preparenumber(s2, n);
  for i := 1 to n do
    bufs := bufs + '0';
  resstr := '';
  // ����� ���������� �������� ��������� �������� ����� '� �������'
  // � ���������� ��������� ������ ����������, �������� ������� ��������� ���������
  for i := length(s1) downto 1 do
  begin
    if s1[i] = '1' then
    begin
      for j := 1 to length(s2) do
      begin
        bufs[j] := s2[j];
      end;
      resstr := resstr + bufs + '+';
      clear(bufs);
      bufs := bufs + '0';
    end
    else
    begin
      clear(bufs);
      resstr := resstr + bufs + '+';
      bufs := bufs + '0';
    end;
  end;
  delete(resstr, length(resstr), 1);
  // ������� ����� ���������
  while pos('+', resstr) <> 0 do
  begin
    resstr := resstr + '+';
    number1 := '';
    number2 := '';
    i := 1;
    while resstr[i] <> ('+') do
    begin
      number1 := number1 + resstr[i];
      inc(i);
    end;
    inc(i);
    while resstr[i] <> ('+') do
    begin
      number2 := number2 + resstr[i];
      inc(i);
    end;
    delete(resstr, 1, i - 1);
    ch := sum(number1, number2, 2 * n);
    resstr := ch + resstr;
    delete(resstr, length(resstr), 1);
  end;
  delete(resstr, 1, n);
  result := resstr;
end;

procedure LogicShr(var s: string);
var
  i: integer;
begin
  preparenumber(s, n);
  // ������ �� ������ � ����� ��������
  for i := length(s) downto 2 do
  begin
    s[i] := s[i - 1];
  end;
  s[1] := '0';
end;

procedure LogicShl(var s: string);
var
  i: integer;
begin
  preparenumber(s, n);
  // ������ �� ������ � ����� ��������
  for i := 1 to length(s) - 1 do
  begin
    s[i] := s[i + 1];
  end;
  s[length(s)] := '0';
end;

procedure ArifmShl(var s: string);
var
  i: integer;
begin
  // ������ �� ������ � ����� ��������
  for i := 1 to length(s) - 1 do
  begin
    s[i] := s[i + 1];
  end;
  s[length(s)] := '0';
end;

procedure ArifmShr(var s: string);
var
  i: integer;
begin
  preparenumber(s, n);
  // ������ �� ������ � ����� ��������
  for i := length(s) downto 2 do
  begin
    s[i] := s[i - 1];
  end;
end;

procedure LoopShr(var s: string);
var
  i: integer;
  last: char;
begin
  preparenumber(s, n);
  last := s[length(s)];
  // ������ �� ������ � ����� ��������
  for i := length(s) downto 2 do
  begin
    s[i] := s[i - 1];
  end;
  s[1] := last;
end;

procedure LoopShl(var s: string);
var
  i: integer;
  first: char;
begin
  preparenumber(s, n);
  first := s[1];
  // ������ �� ������ � ����� ��������
  for i := 1 to length(s) - 1 do
  begin
    s[i] := s[i + 1];
  end;
  s[length(s)] := first;
end;

procedure Push(var a: Tstack; b: TROperation);
var
  new_elem: Tstack;
begin
  // ���������� ������ ��� ����� �������
  // ����� � �������� ���� ������ �������� ���������� ����� ������� �����
  // ������� ����� ������������� ����� ������ ��������
  // ����� ���������� ����� ������� �������
  new(new_elem);
  new_elem^.next := a;
  a := new_elem;
  a^.data := b.data;
  a^.priority := b.priority;
end;

procedure Pop(var a: Tstack; var b: TROperation);
var
  temp: Tstack;
begin

  temp := a;
  b.data := a^.data; // ��������� ������ � ����������
  b.priority := a^.priority;
  a := a^.next; // ��������� ������ ������� �����
  dispose(temp); // ������������ ������
end;

procedure Mul(var snum, sop: Tstack; s: TROperation; n: integer);
var
  x_s, y_s: TROperation;
  res: TROperation;
begin
  Pop(sop, res);
  Pop(snum, y_s);
  Pop(snum, x_s);
  res.data := binmul(x_s.data, y_s.data, n);
  Push(snum, res);
end;

procedure division(var snum, sop: Tstack; s: TROperation; n: integer);
var
  x_s, y_s: TROperation;
  res: TROperation;
begin
  zerodivideflag := false;
  Pop(sop, res);
  Pop(snum, y_s);
  Pop(snum, x_s);
  res.data := divbin(x_s.data, y_s.data, n);
  if res.data = 'zerodivide' then
    zerodivideflag := true;
  Push(snum, res);
end;

procedure Pnot(var snum, sop: Tstack; s: TROperation);
var
  y_s: TROperation;
  res: TROperation;
begin

  Pop(sop, res);
  Pop(snum, y_s);
  convertnumber(y_s.data, n);
  Push(snum, y_s);
end;

procedure PShale(var snum, sop: Tstack; s: TROperation; sh_f: shale);
var
  x_s, y_s: TROperation;
  y: integer;
  res: TROperation;
begin
  Pop(sop, res);
  Pop(snum, y_s);
  Pop(snum, x_s);
  y := strtoint(convertss(y_s.data, 2, 10));
  if y >= n then
    outofshale := true;
  while y <> 0 do
  begin
    sh_f(x_s.data);
    dec(y);
  end;

  res := x_s;
  Push(snum, res);
end;

procedure Pand(var snum, sop: Tstack; s: TROperation);
var
  x_s, y_s: TROperation;
  res: TROperation;
begin
  Pop(sop, res);
  Pop(snum, y_s);
  Pop(snum, x_s);
  res.data := sand(x_s.data, y_s.data);
  Push(snum, res);
end;

procedure Psum(var snum, sop: Tstack; s: TROperation; n: integer);
var
  x_s, y_s: TROperation;
  res: TROperation;
begin
  Pop(sop, res);
  Pop(snum, y_s);
  Pop(snum, x_s);
  res.data := sum(x_s.data, y_s.data, n);
  Push(snum, res);
end;

procedure PSub(var snum, sop: Tstack; s: TROperation; n: integer);
var
  x_s, y_s: TROperation;
  res: TROperation;
begin
  Pop(sop, res);
  Pop(snum, y_s);
  Pop(snum, x_s);
  res.data := diff(x_s.data, y_s.data, n);
  Push(snum, res);
end;

procedure Por(var snum, sop: Tstack; s: TROperation);
var
  x_s, y_s: TROperation;
  res: TROperation;
begin
  Pop(sop, res);
  Pop(snum, y_s);
  Pop(snum, x_s);
  res.data := sor(x_s.data, y_s.data);
  Push(snum, res);
end;

procedure Pxor(var snum, sop: Tstack; s: TROperation);
var
  x_s, y_s: TROperation;
  res: TROperation;
begin
  Pop(sop, res);
  Pop(snum, y_s);
  Pop(snum, x_s);
  res.data := sxor(x_s.data, y_s.data);
  Push(snum, res);
end;

procedure Count(s: TROperation; var snum, sop: Tstack; n: integer;
  shl_f, shr_f: shale);
begin
  // ���������� ������ ������ ��������
  if sop^.data = '*' then
    Mul(snum, sop, s, n)
  else if sop^.data = '/' then
    division(snum, sop, s, n)
  else if sop^.data = 'NOT' then
    Pnot(snum, sop, s)
  else if sop^.data = 'AND' then
    Pand(snum, sop, s)
  else if sop^.data = '+' then
    Psum(snum, sop, s, n)
  else if sop^.data = '-' then
    PSub(snum, sop, s, n)
  else if Lowercase(sop^.data) = 'or' then
    Por(snum, sop, s)
  else if Lowercase(sop^.data) = 'xor' then
    Pxor(snum, sop, s)
  else if Lowercase(sop^.data) = 'shl' then
  begin
    PShale(snum, sop, s, shl_f);
  end
  else if Lowercase(sop^.data) = 'shr' then
  begin
    PShale(snum, sop, s, shr_f);
  end;
  // ����� ������������: ������� �� ��������� �������� �� ����� ��� ��������� �� ���� �����
  PlaceOrCount(snum, sop, s, n, shl_f, shr_f);
end;

procedure PlaceOrCount(var snum, sop: Tstack; current: TROperation; n: integer;
  shl_f, shr_f: shale);
var
  pred: TROperation;
begin
  // ���������� ��������� ����������� ������� �������� � �������� �� ������� �����
  // � ����� ������������ ���������� �������� �� ����� ����������
  if (sop <> nil) and (sop.data <> '(') and (sop.data <> ')') then
  begin
    pred.priority := sop^.priority;
    if pred.priority >= current.priority then
    begin
      Count(current, snum, sop, n, shl_f, shr_f);
    end
    else
      Push(sop, current);
  end
  else
    Push(sop, current);
end;

function GetResult(var snum, sop: Tstack; var s: string; n: integer;
  shl_f, shr_f: shale; countsys: integer): string;
var
  i: integer;
  current, bufs, res: TROperation;
  correctflag: boolean;
const
  ForthPriorityS = 'NOT'; // �������� ������� ����������
  ThirdPriorityS = '*/ANDSHLSHR'; // �������� ������ �� ����������
  SecondPriorityS = '+-XOR'; // �������� ��������� �� ����������
begin
  correctflag := true;
  // ����������� � �������� ������
  case countsys of
    2:
      begin
        correctflag := CheckString(s, n);
      end;
    8:
      begin
        s := MathInOCt(s, n, snum, sop, correctflag);
        if correctflag = true then
          correctflag := CheckString(s, n);
      end;
    10:
      begin
        s := MathInDecNew(s, n, snum, sop, correctflag);
        if correctflag = true then
          correctflag := CheckString(s, n);
      end;
    16:
      begin
        s := MathInHEx(s, n, correctflag);
        if correctflag = true then
          correctflag := CheckString(s, n);
      end;
  end;
  if correctflag = true then
  begin
    snum := nil;
    sop := nil;
    i := 1;
    zerodivideflag := false;
    outofshale := false;
    while (i <= length(s)) and (zerodivideflag = false) do
    begin
      bufs.data := '';
      if pos(s[i], '0123456789') <> 0 then
      // ���� �����,�� ������� ������ ����� � �������� �� � ����
      begin
        while pos(s[i], '01') <> 0 do
        begin
          bufs.data := bufs.data + s[i];
          inc(i);
        end;
        preparenumber(bufs.data, n);
        Push(snum, bufs);
        dec(i);
      end
      else
      begin
        bufs.data := '';
        if (s[i] <> '(') and (s[i] <> ')') then
        begin
          if pos(s[i], '*/+-') = 0 then
          begin
            while (pos(s[i], '01()') = 0) do
            // ���� �������������,�� ������� �������� �� ������ �������� � �������� �� �  bufs
            begin
              bufs.data := bufs.data + s[i];
              inc(i);
            end;
            dec(i);
          end
          else
          begin
            bufs.data := s[i]; // ���� ���������� ��������,�� �������� �� � bufs
          end;
          current.data := bufs.data;
          if pos(uppercase(current.data), ThirdPriorityS) <> 0 then
          // ��������� ����� �������������� ��������
            current.priority := 3
          else if pos(uppercase(current.data), SecondPriorityS) <> 0 then
            current.priority := 2
          else if pos(uppercase(current.data), ForthPriorityS) <> 0 then
            current.priority := 4;
          PlaceOrCount(snum, sop, current, n, shl_f, shr_f);
          // ����� �������� � ���� ��� ������� ���������� �������� � ����� ����� ����� �����
        end
        else if (s[i] = '(') then // ���� ������������� ������
        begin
          bufs.data := '('; // ����� �� � ����
          bufs.priority := 5;
          Push(sop, bufs);
        end
        else if (s[i] = ')') then // ���� �������������, �� ������� ��� ��
        begin
          while sop^.data <> '(' do
          begin
            bufs.data := '';
            Count(bufs, snum, sop, n, shl_f, shr_f);
            Pop(sop, bufs);
          end;
          Pop(sop, bufs);
        end;
      end;
      inc(i);
    end;
    if zerodivideflag = false then // ���� �� ���� ���������� ������� �� ����
    begin
      while (sop <> nil) and (zerodivideflag = false) do
      begin
        bufs.data := '';
        Count(bufs, snum, sop, n, shl_f, shr_f); // ������� ���������
        Pop(sop, bufs);
      end;
      if zerodivideflag = false then // ���� �� ���� ���������� ������� �� ����
      begin
        Pop(snum, res);
        if length(res.data) < n then
        begin
          i := n - length(res.data);
          while i <> 0 do
          begin
            res.data := '0' + res.data;
            dec(i);
          end;
        end;
        result := (res.data);
        s := result;
        if countsys = 10 then
        begin
          if result[1] = '0' then
          begin
            result := inttostr(indec(result, 2));
          end
          else
          begin
            result := diff(result, '1', n);
            convertnumber(result, n);
            result := '-' + inttostr(indec(result, 2));
          end;
        end
        else
        begin
          result := convertss(result, 2, countsys);
        end;
      end
      else
        result := '������� �� 0 ���o�������';
      if (outofshale = true) and (result <> '������� �� 0 ���o�������') then
      // ���� ��� �������� ����� �� ������� ������� ���������� �����
      begin
        result := '��������� ��o��������';
      end;
    end
    else
      result := '������� �� 0 ���o�������';
  end
  else
    result := 'O�����!';

end;

/// //////////////////////////////////////////////////////////////
procedure disposestack(a: Tstack);
var
  t, next: Tstack;
begin
  t := a;
  while t <> nil do
  begin
    next := t^.next;
    dispose(t);
    t := next;
  end;
end;

/// /////////////////////////////////////////////////////////////
function CheckString(var s: string; n: integer): boolean;
var
  bracket: Tstack;
  bracketflag: boolean;
  i, j: integer;
  bufs: TROperation;
begin
  result := true;
  bracket := nil;
  i := 1;
  bracketflag := true;
  if s = ' ' then
    result := true
  else
  begin
    // ������ �� ������ � �������� �������� �� ����������� ����������
    while (i <= length(s)) and (result = true) do
    begin
      result := false;
      if ((i = 1) and ((s[i] = '+') or (s[i] = '-'))) or
        ((i > 1) and ((s[i] = '+') or (s[i] = '-')) and (s[i - 1] = '(')) then
      begin
        if length(s) = 1 then
        begin
          result := false;
        end
        else
        begin
          insert('0', s, i);
          inc(i);
          result := true;
        end;
      end;
      if (s[i] = '(') then
      begin
        bufs.data := ')';
        Push(bracket, bufs);
        result := true;
      end
      else if (s[i] = ')') then
      begin
       if s[i-1]='(' then
        result:=false
       else
       begin
        if bracket <> nil then
        begin
          result := true;
          Pop(bracket, bufs)
        end
        else
        begin
          result := true;
          bracketflag := false;
        end;
       end;
      end;
      if pos(s[i], '23456789') <> 0 then
        result := false;
      if (i <> length(s)) and (result = false) and (pos(s[i], '+-*/') <> 0) and
        ((pos(s[i + 1], '+-*/)') = 0) and (pos(s[i - 1], '+-*/(') = 0)) then
        result := true;
      if (result = false) and (pos(s[i], '01') <> 0) then
      begin
        j := 1;
        if (i > 1) and (s[i - 1] = ')') then
          result := false
        else
        begin
          while (i <= length(s)) and (pos(s[i], '01') <> 0) do
          begin
            inc(j);
            inc(i);
          end;
          dec(j);
          if j > n then
            result := false
          else
            result := true;
          if s[i] = '(' then
            result := false;
          dec(i);
        end;
      end;
      if (i <= length(s) - 3) and (result = false) and
        ((upcase(s[i]) = 'N') and (upcase(s[i + 1]) = 'O') and
        (upcase(s[i + 2]) = 'T')) then
      begin
        if (i <> 1) and (pos(s[i - 1], '(+-*/') <> 0) and
          (pos(s[i + 3], '(10') <> 0) then
        begin
          inc(i, 2);
          result := true;
        end
        else if (i = 1) and (pos(s[i + 3], '(10') <> 0) then
        begin
          inc(i, 2);
          result := true;
        end
        else
          result := false;
      end;
      if (i <= length(s) - 3) and (i <> 1) and (result = false) and
        ((upcase(s[i]) = 'X') and (upcase(s[i + 1]) = 'O') and
        (upcase(s[i + 2]) = 'R')) then
      begin
        if ((pos(s[i - 1], ')10') <> 0) or (pos(s[i + 3], '(10') <> 0)) then
        begin
          inc(i, 2);
          result := true;
        end
        else
          result := false;
      end;
      if (i <= length(s) - 2) and (i <> 1) and (result = false) and
        ((upcase(s[i]) = 'O') and (upcase(s[i + 1]) = 'R')) then
      begin
        if ((pos(s[i - 1], ')10') <> 0) and (pos(s[i + 2], '(10') <> 0)) then
        begin
          inc(i, 1);
          result := true;
        end
        else
          result := false;
      end;
      if (result = false) and (upcase(s[i]) = 'A') then
      begin
        if (i <= length(s) - 3) and (i <> 1) and (upcase(s[i + 1]) = 'N') and
          (upcase(s[i + 2]) = 'D') and (pos(s[i - 1], ')10') <> 0) and
          (pos(s[i + 3], '(10') <> 0) then
        begin
          result := true;
          inc(i, 2);
        end
        else
          result := false;
      end;
      if (i <= length(s) - 2) and (i <> 1) and (upcase(s[i]) = 'S') then
      begin
        if (upcase(s[i + 1]) = 'H') and
          ((upcase(s[i + 2]) = 'L') or (upcase(s[i + 2]) = 'R')) and
          (pos(s[i - 1], ')10') <> 0) then
        begin
          result := true;
          inc(i, 2);
        end;
      end;
      inc(i);
    end;
    if (bracket <> nil) or (bracketflag = false) then
    begin
      if (bracket <> nil) then
        disposestack(bracket);
      result := false
    end
    else
    begin
      if (pos('1', s) = 0) and (pos('0', s) = 0) then
        result := false;
    end;
  end;
end;

function MathInOCt(s: string; n: integer; snum, sop: Tstack;
  var corflag: boolean): string;
var
  i: integer;
  bufs, number: string;
begin
  corflag := true;
  bufs := '';
  i := 1;
  // �������� �������� ������ �� ������� ������������ ��������
  while (i <= length(s)) and
    ((pos(upcase(s[i]), '+()-*/01234567ANDXORTSHL') <> 0)) do
    inc(i);
  if (i <= length(s)) or (s = '') then
    corflag := false;
  i := 1;
  // ������ �� ������ � ������� �� � �������� ������� ���������
  while (corflag = true) and (i <= length(s)) do
  begin
    if pos(s[i], '01234567') <> 0 then
    begin
      number := '';
      while (i <= length(s)) and (pos(s[i], '01234567') <> 0) do
      begin
        number := number + s[i];
        inc(i)
      end;
      dec(i);
      if (length(number) > 5) then
      begin
        if number[1] = '1' then
          bufs := bufs + convertss(number, 8, 2)
        else
          corflag := false;
      end
      else
        bufs := bufs + convertss(number, 8, 2);
    end
    else
      bufs := bufs + s[i];
    inc(i);
  end;
  if corflag = true then
    result := bufs
  else
    result := 'false';
end;

function MathInHEx(s: string; n: integer; var corflag: boolean): string;
var
  i: integer;
  bufs, number: string;
  sindec: string;
  hex_n: integer;
  len: integer;
begin
  i := 1;
  corflag := true;
  // �������� �������� ������ �� ������� ������������ ��������
  while (i <= length(s)) and
    ((pos(upcase(s[i]), '()+-*/0123456789ANDXORBCETFSHL') <> 0)) do
    inc(i);
  if (i <= length(s)) or (s = '') then
    corflag := false;
  hex_n := n div 4;
  sindec := '';
  i := 1;
  // ������ �� ������ � ������� �� � �������� ������� ���������
  while (i <= length(s)) and (corflag = true) do
  begin
    if (s[i] = 'A') or (s[i] = 'N') then
    begin
      if (i < length(s) - 1) and (s[i + 1] = 'N') or (s[i + 1] = 'O') then
      begin
        if (s[i] = 'A') and (s[i + 1] = 'N') then
        begin
          sindec := sindec + 'AND';
          inc(i, 2);
        end
        else if (s[i] = 'N') and (s[i + 1] = 'O') then
        begin
          sindec := sindec + 'NOT';
          inc(i, 2);
        end;
      end
      else
      begin
        if s[i] = 'N' then
          corflag := false
        else
        begin
          number := '';
        //  len := length(s);
          s := s + '!';
          while (i <= length(s)) and (pos(s[i], '0123456789ABCDEF') <> 0) and
            (s[i + 1] <> 'N') do
          begin
            number := number + s[i];
            inc(i);
          end;
          delete(s, length(s), 1);
          if length(number) > hex_n then
            corflag := false;
          sindec := sindec + convertss(number, 16, 10);
          dec(i);
        end;
      end;
    end
    else if (pos(upcase(s[i]), '0123456789BCDEF') <> 0) then
    begin
      number := '';
      len := length(s);
      s := s + '!';
      while (i <= len) and (pos(s[i], '0123456789ABCDEF') <> 0)  do
      begin
      if ((s[i]='A') and (s[i+1]='N') and (s[i+2]='D')) or ((s[i]='N')) then
         break;
        number := number + s[i];
        inc(i);
      end;
      delete(s, length(s), 1);
      if length(number) > hex_n then
        corflag := false;
      sindec := sindec + convertss(number, 16, 10);
      dec(i);
    end
    else
      sindec := sindec + s[i];
    inc(i);
  end;
  i := 1;
  bufs := '';
  while (i <= length(sindec)) and (corflag = true) do
  begin
    if pos(sindec[i], '0123456789') <> 0 then
    begin
      number := '';
      while (i <= length(sindec)) and (pos(sindec[i], '0123456789') <> 0) do
      begin
        number := number + sindec[i];
        inc(i);
      end;
      dec(i);
      bufs := bufs + convertss(number, 10, 2)
    end
    else
      bufs := bufs + sindec[i];
    inc(i);
  end;
  if corflag = true then
    result := bufs
  else
    result := 'false';
end;

function MathInDecNew(s: string; n: integer; snum, sop: Tstack;
  var corflag: boolean): string;
var
  maxn, minn, i: integer;
  bufs, number: string;
begin
  maxn := 0;
  minn := 0;
  if n = 8 then
  begin
    maxn := 127;
    minn := -128;
  end;
  if n = 16 then
  begin
    maxn := 32767;
    minn := -32768;
  end;
  if n = 32 then
  begin
    maxn := 2147483647;
    minn := -2147483648;
  end;
  i := 1;
  bufs := '';
  corflag := true;
  // �������� �������� ������ �� ������� ������������ ��������
  while (i <= length(s)) and
    ((pos(upcase(s[i]), '()+-*/0123456789ANDXORTSHL') <> 0)) do
    inc(i);
  if (i <= length(s)) or (s = '') then
    corflag := false;
  i := 1;
  // ������ �� ������ � ������� �� � �������� ������� ���������
  while (i <= length(s)) and (corflag = true) do
  begin
    if pos(s[i], '0123456789') <> 0 then
    begin
      number := '';
      while (i <= length(s)) and (pos(s[i], '0123456789') <> 0) do
      begin
        number := number + s[i];
        inc(i);
      end;
      dec(i);
      if (strtofloat(number) <= maxn) and (strtofloat(number) >= minn) then
        bufs := bufs + convertss(number, 10, 2)
      else
        corflag := false;
    end
    else
      bufs := bufs + s[i];
    inc(i);
  end;
  if corflag = true then
    result := bufs
  else
    result := 'false';
end;

end.
