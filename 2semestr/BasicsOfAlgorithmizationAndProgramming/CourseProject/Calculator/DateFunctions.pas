unit DateFunctions;

interface

uses
  System.SysUtils;

type
  TRDiffresult = record
    days: integer;
    weeks: integer;
    years: integer;
    AllDays: integer;
  end;

const
  tdays: array [1 .. 12] of integer = (31, 28, 31, 30, 31, 30, 31, 31, 30, 31,
    30, 31); // ������ ��������� ���� � �������

const
  �namemonths: array [1 .. 12] of string[10] = ('������', '�������', '�����',
    '������', '���', '����', '����', '�������', '��������', '�������', '������',
    '�������');

var
  date_1: string;
  date_2: string;
  diffresult: TRDiffresult;
function checknum(x: string): boolean; // ������� ��� �������� ��������� ������
procedure fordate(a: integer; var s: string);
// ��������� ��� �������� ���� � ������� � ������ ������� (ddmm)
procedure prepare(var a, b, c: integer; k: string);
procedure ConvToDate(a, b, c: integer; var s: string);
// ��������� ��� �������� ����,�������,��� � ������ ����
procedure obm(var a, b: integer); // ��������� ������ �������� ���� ����������
function difference(date_1, date_2: string): TRDiffresult;
// ������� ������� ����� ������
function SUmdays(x: integer; months, years: integer; date_1: string): string;
// ������� ���������� ����,�������,��� � ����
function Subtractdays(x: integer; months, years: integer; date_1: string)
  : string; // ������� ��������� ����,�������,��� �� ����
function leapyearcheck(y: integer): boolean;
// ������� �������� ���� �� ������������
function ResultOutput(date: string): string;
// ������� ������� ���� ������� ddmmyyyy  � ������ "10 ����� 2010 ����"

implementation

function ResultOutput(date: string): string;
var
  d, m, y: integer;
begin
  if date = '���� ������� �� �������!' then
    result := date
  else
  begin
    d := strtoint(copy(date, 1, 2));
    m := strtoint(copy(date, 4, 2));
    y := strtoint(copy(date, 7, length(date) - 6));
    result := inttostr(d) + ' ' + string(�namemonths[m]) + ' ' + inttostr(y) +
      ' ' + '����';
  end;
end;

function checknum(x: string): boolean;
var
  ch: string;
  i: integer;
begin
  ch := '0123456789';
  i := 1;
  while (i <= length(x)) and (pos(x[i], ch) <> 0) do
  // ������ �� ������ � �������� ���� ��������
  begin
    inc(i);
  end;
  if (i <= length(x)) or (x = '') then
  // ���� ������ ������������ ������,�� ���������-false, ����� ���������-true
  begin
    result := false;
  end
  else
    result := true;
end;

procedure fordate(a: integer; var s: string);
begin
  if a <= 9 then // ���� ����� ������ 10,�� ����� ��� ����� �������� 0
  begin
    s := s + '0';
    s := s + inttostr(a); // ���������� � ����� ������
  end
  else
    s := s + inttostr(a); // ���������� � ����� ������
end;

procedure ConvToDate(a, b, c: integer; var s: string);
begin
  s := '';
  fordate(a, s); // ������� ����������� ����
  fordate(b, s); // ����� �������
  case c of // ����������� ������ ����
    0 .. 9:
      s := s + '000';
      // ���� ��� ����������� �����,�� ����� �������� ����� ��� "000"
    10 .. 99:
      s := s + '00'; // ���� �����������,�� ����� �������� "00"
    100 .. 999:
      s := s + '0'; // ���� �����������,�� ����� �������� "0"
  end;
  insert('.', s, 3); // ���������� ����� � ������
  insert('.', s, 6);
  s := s + inttostr(c); // ���������� ������ ���� � ������
end;

procedure obm(var a, b: integer);
var
  tmp: integer;
begin
  tmp := a;
  a := b;
  b := tmp;
end;

// ��������� �������� � ���������� ��������� ������ ���, ������,���� �� ���� ������� "dd.mm.yyyy"
procedure prepare(var a, b, c: integer; k: string);
var
  i: integer;
begin
  delete(k, 3, 1); // �������� ����� �� ������
  delete(k, 5, 1);
  while k <> '' do
  begin
    for i := 1 to 3 do
    begin
      case i of
        1:
          begin // ��������� ������ ���
            if k[1] = '0' then
            begin
              delete(k, 1, 1);
              a := strtoint(k[1]);
              delete(k, 1, 1);
            end
            else
            begin
              a := strtoint(copy(k, 1, 2));
              delete(k, 1, 2);
            end;
          end;
        2: // ��������� ������ ������
          begin
            if k[1] = '0' then
            begin
              delete(k, 1, 1);
              b := strtoint(k[1]);
              delete(k, 1, 1);
            end
            else
            begin
              b := strtoint(copy(k, 1, 2));
              delete(k, 1, 2);
            end;
          end;
        3: // ��������� ������ ����
          begin
            while (k[1] = '0') do
            begin
              delete(k, 1, 1);
            end;
            c := strtoint(copy(k, 1, length(k)));
            delete(k, 1, length(k));
          end;
      end;
    end;
  end;
end;

function difference(date_1, date_2: string): TRDiffresult;
var
  d1, d2, m1, m2, y1, y2: integer;
  dy: integer;
  i, weeks, days, counter: integer;
  diff: integer;
  years: integer;
begin
  counter := 0;
  prepare(d1, m1, y1, date_1);
  prepare(d2, m2, y2, date_2);
  if y1 < y2 then
  begin
    obm(y1, y2);
    obm(d1, d2);
    obm(m1, m2);
  end;
  dy := y1 - y2; // ����� ������� � ����� ����� ������
  diff := (dy - 1) * 365; // ��������� � ������� ���������� ������ ��� � ����
  if (y1 - y2) > 1 then
  begin
    for i := y2 + 1 to y1 - 1 do
    // ���������� �� ����� � ���� ���� ���������� ��������� ��� �� ���
    begin
      if ((leapyearcheck(i) = true)) then
        inc(counter);
    end;
    diff := diff + counter;
  end;
  if (dy) > 0 then // ���� ���������� ������� ����� ������ ��� ���� ��� ������ 0
  begin
    for i := (m2 + 1) to 12 do
    // ���������� �� ������� ������� ���� � ������ ���������� �� ������� �� ����� ����
    begin // � ��������� � ������� ���������� ���� � ������ �� �������
      if (i = 2) and ((leapyearcheck(y2) = true)) then
        inc(diff); // ���� ���������� ��� � �������,�� ��������� ��� ����
      diff := diff + tdays[i];
    end;
    diff := diff + tdays[m2] - d2;
    // ��������� � ������� ���������� ���� ���������� �� ����� ������ � ������� ������ ������ ����
    if (m2 = 2) and ((leapyearcheck(y2) = true)) then
    // ���� ��� ���������� ���,�� ��������� ��� ����
      inc(diff);
    for i := 1 to m1 - 1 do
    // ���������� �� ������ ���� �� ������, ��������������� �������� ������ ������ ����
    begin
      if (i = 2) and (leapyearcheck(y1) = true) then
      // ���� ���������� ��� � �������,�� �������� ��� ����
        inc(diff);
      diff := diff + tdays[i] // ��������� � ������� ���������� ���� � ������
    end;
    diff := diff + d1;
  end
  else
  begin
    diff := 0;
    if m1 = m2 then
    // ���� ������ ��� ���������, �� ������� ����� ������� ����� �������� ����
      diff := abs(d1 - d2)
    else
    begin
      if m2 > m1 then // ���� ������ ���� ������, �� ������ �������
      begin
        obm(m1, m2);
        obm(d1, d2);
      end;
      diff := diff + abs(tdays[m2] - d2); // ��������� � ������� ���������� ����
      if ((leapyearcheck(y2) = true)) and (m2 = 2) then
      /// ����������� �� ������ ���� ��� �������� �� ��������� �����
        inc(diff);
      diff := diff + d1; // ��������� � ������� ����� ��� ������ ����
      if abs(m1 - m2) > 1 then // ���� � ����� �� ���������� �������� ������
      begin
        for i := m2 + 1 to m1 - 1 do // ������ �� ������� ����� ����� ������
        begin
          diff := diff + tdays[i];
          // ���������� � ������� ���������� ���� � ���� �������
          if (leapyearcheck(y2) = true) and (i = 2) then
          // ���� ������� ���������� ���  � 2 �����,�� ��� ���� ��������
            inc(diff);
        end;
      end;
    end;
  end;
  result.AllDays := diff; // ������� � ���� ����� ����
  years := diff div 365; // ������� ������� � ������� ����+������+���
  weeks := (diff - years * 365) div 7;
  days := diff - (weeks * 7) - years * 365;
  result.days := days;
  result.weeks := weeks;
  result.years := years;
end;

function leapyearcheck(y: integer): boolean;
begin
  if (y mod 4 = 0) or (y mod 400 = 0) then
  begin
    if (y mod 100 = 0) then
      result := false
    else
      result := true;
  end
  else
    result := false;
end;

function SUmdays(x: integer; months, years: integer; date_1: string): string;
var
  tmp: integer;
  m, d, y: integer;
begin
  prepare(d, m, y, date_1); // ��������� � ���������� ������ ���, ������, ����
  tmp := tdays[m] - d + 1;
  // �������� ���������� ���� �� �������� �� ��������� �����
  if (leapyearcheck(y) = true) and (m = 2) then
  // �������� �� ������������ ���� � �� �������
    inc(tmp);
  if x < tmp then
  // ���� ����� ����������� ���� ������, ��� ����� ��� �������� �� ��������� �����
  begin
    d := d + x;
  end
  else
  begin
    repeat
      tmp := tdays[m] - d + 1;
      if (leapyearcheck(y) = true) and (m = 2) then
        inc(tmp);
      x := x - tmp;
      // �������� �� ����������� ���� ����� ����, ����������� ��� �������� �� ���� �����
      if m = 12 then
      // ���� ����� ������-12,�� ������� �� ������ ����� ���������� ����
      begin
        m := 1;
        y := y + 1;
      end
      else // ����� ������� �� ��������� ����� �������� ����
        m := m + 1;
      d := 1;
    until x < tdays[m];
    d := d + x; // ���������� � ������ ���, ���������� ����
  end;
  if months < 13 - m then
  // ���� ���������� ����������� ������� ������, ��� ����� ��� �������� �� ��������� ���
    m := m + months
  else
  begin // �����
    repeat
      y := y + 1; // ��������� ����
      months := months - (13 - m); // ��������� ����������� �������
      m := 1; // ������������ ������ ������ �������
    until months < 13 - m;
    // ���� ���������� ����������� ������� �� ������,��� ����� ��� �������� �� ��������� ���
    m := m + months;
  end;
  y := y + years;
  result := '';
  ConvToDate(d, m, y, result); // ����������� � ������ ����
end;

function Subtractdays(x: integer; months, years: integer;
  date_1: string): string;
var
  m, d, y: integer;
begin
  prepare(d, m, y, date_1); // �������� � ���������� ����� ���, ������, ����
  if x >= d then
  begin
    repeat
      x := x - d; // �������� ������� ����� ���� � ������
      if m = 1 then
      // ���� ����� ��� ������, �� ������ �� ��������� ����� ����������� ����
      begin
        m := 12;
        dec(y);
      end
      else
        dec(m); // � ���� ������� ������ ����� ������
      d := tdays[m];
      // �������� ����� ���� ����������� ���������� ����,������ ���� � ������
      if ((leapyearcheck(y) = true)) and (m = 2) then
      // �������� �� ���������� ��� � ����� �������
        inc(d);
    until x < d;
    // �� ��� ���,���� ���������� ���������� ���� �� ������ ������ �������� ����� ���� ������                                //�������� �� �������� ����� ���������� ���
  end;
  d := d - x;
  if months >= m then
  // ���������� ���������� ������� ������ ���� ����� ������ �������� ������
  begin
    repeat // ���� ��,�� ���������:
      months := months - m;
      // �� ����������� ����� ������ �������� ����� �������� ������
      m := 12; // ������� �� 12 ����� ����������� ����
      dec(y); //
    until months < m;
    // �� ��� ���, ���� ����� ���������� ������� �� ������ ������ ������ �������� ������
  end;
  m := m - months; // �������� ����� �������� ������
  y := y - years; // �������� ��� �������� ������
  result := '';
  if y < 0 then // ������� ���������� � ������ ������
    result := '���� ������� �� �������!'
  else
    ConvToDate(d, m, y, result);
end;

end.
