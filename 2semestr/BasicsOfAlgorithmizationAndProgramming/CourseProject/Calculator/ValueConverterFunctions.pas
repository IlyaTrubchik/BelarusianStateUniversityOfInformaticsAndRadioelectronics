unit ValueConverterFunctions;

interface

uses
  System.SysUtils;

type
  Quantities = record
    name: string;
    Value: extended;
  end;

  Converter = array [1 .. 58] of Quantities;

const
  ConverterArray: Converter = ((Name: '����'; Value: 1), (Name: '��������';
    Value: 1000000000), (Name: '��������'; Value: 0.001), (Name: '���������';
    Value: 100), (Name: '���������'; Value: 1000), (Name: '����';
    Value: 39.37008), (Name: '���'; Value: 3.28084), (Name: '���';
    Value: 1.093613), (Name: '����'; Value: 0.000621), (Name: '������';
    Value: 1000000), (Name: '���������'; Value: 1),
    (Name: '���������� ���������'; Value: 1), (Name: '����'; Value: 0.001),
    (Name: '���������� ����'; Value: 0.000001), (Name: '���������� ����';
    Value: 0.061024), (Name: '���������� ���'; Value: 0.000035),
    (Name: '���������� ���'; Value: 0.000001), (Name: '���������� ��������';
    Value: 0.001), (Name: '������(���)'; Value: 0.000264), (Name: '�����(���)';
    Value: 0.001057), (Name: '���������'; Value: 1), (Name: '�����';
    Value: 5000), (Name: '����������'; Value: 1000000), (Name: '����������';
    Value: 100000), (Name: '���������'; Value: 10000), (Name: '�����';
    Value: 1000), (Name: '���������'; Value: 100), (Name: '����������';
    Value: 10), (Name: '����������� �����'; Value: 0.001), (Name: '����';
    Value: 2.204623), (Name: '����� �������'; Value: 0),
    (Name: '����� ����������'; Value: 32), (Name: '����� ��������';
    Value: 273.15), (Name: '���������� ����'; Value: 1),
    (Name: '���������� ���������'; Value: 10000), (Name: '���������� ���������';
    Value: 1000000), (Name: '������'; Value: 0.0001),
    (Name: '���������� ��������'; Value: 0.000001), (Name: '���������� ����';
    Value: 1550.003), (Name: '���������� ���'; Value: 10.76391),
    (Name: '���������� ���'; Value: 1.19599), (Name: '���'; Value: 0.000247),
    (Name: '���������� ����'; Value: 0.000000386102), (Name: '������';
    Value: 1), (Name: '����������'; Value: 0.001), (Name: '�������';
    Value: 0.239006), (Name: '������� �������'; Value: 0.000239),
    (Name: '���-����'; Value: 0.737562), (Name: '��������� �������� �������';
    Value: 0.000948), (Name: '��������-�����'; Value: 6241509000000000000),
    (Name: '������������'; Value: 1), (Name: '������������'; Value: 0.001),
    (Name: '�������'; Value: 0.000001), (Name: '������'; Value: 0.0000000166),
    (Name: '���'; Value: 0.000000000277), (Name: '����';
    Value: 0.000000000011574), (Name: '������'; Value: 0.000000000001653),
    (Name: '���'; Value: 0.000000000000032));

  {
    N:
    �����:1;
    �����:11;
    �����:21;
    �����������:31;
    �������:34;
    �������:44;
    �����:51;
  }
const
  lengthIndex = 1;
  VolumeIndex = 11;
  WeightIndex = 21;
  TemperatureIndex = 31;
  SquareIndex = 34;
  EnergyIndex = 44;
  TimeIndex = 51;
function checkstr(s: string; index: integer): boolean;
function convertQuantity(currindex, finalindex: integer; currvalue: extended;
  const quantity: Converter): string;

var
  n: integer;
  check: string;
  currval, finalval: integer;
  Value: extended;

implementation

function convertQuantity(currindex, finalindex: integer; currvalue: extended;
  const quantity: Converter): string;
var
  finalvalue: extended;
  examplevalue1, examplevalue2: extended;
begin
  finalvalue := 0;
  examplevalue1 := quantity[currindex].Value;
  // �������� ������������ �������� ������ ������� ���������
  examplevalue2 := quantity[finalindex].Value;
  // �������� ������������ �������� ������ ������� ���������
  if (currindex >= TemperatureIndex) and (currindex <= TemperatureIndex + 2)
  then // ���� �������� ���������- �����������
  begin // ������������� ����������� ������ ��� �������� ����������
    case currindex - TemperatureIndex of
      0: // �������
        begin
          case finalindex - TemperatureIndex of
            0:
              finalvalue := currvalue;
            1:
              begin
                finalvalue := examplevalue2 + currvalue * 1.8;
              end;
            2:
              begin
                finalvalue := examplevalue2 + currvalue;
              end;
          end;
        end;
      1: // ���������
        begin
          case finalindex - TemperatureIndex of
            0:
              finalvalue := (currvalue - examplevalue1) * 5 / 9;
            1:
              begin
                finalvalue := currvalue;
              end;
            2:
              begin
                finalvalue := (currvalue - examplevalue1) * 5 / 9 +
                  examplevalue2;
              end;
          end;
        end;
      2: // �������
        begin
          case finalindex - TemperatureIndex of
            0:
              finalvalue := (currvalue - examplevalue1);
            1:
              begin
                finalvalue := (currvalue - examplevalue1) * 1.8 +
                  quantity[currindex - 1].Value;
              end;
            2:

              begin
                finalvalue := currvalue;
              end;
          end;
        end;
    end;
    result := floattostr(finalvalue);
  end
  else // � ���� ������
  begin
    finalvalue := (currvalue / examplevalue1) * examplevalue2;
    // �������� �������� ��������� ����� � ����� �������� ���������
    result := floattostr(finalvalue);
  end;
end;

function checkstr(s: string; index: integer): boolean;
// �������� ������ �� ������������
var
  i: integer;
  dotcount: integer;
begin
  result := true;
  dotcount := 0;
  // ���� ������ ������ ����� � ��������� ��������- �����������
  if (s[1] = '-') and (index >= TemperatureIndex) and
    (index <= TemperatureIndex + 2) then
    delete(s, 1, 1); // �������� ������ �� ������
  i := 1;
  while (i <= length(s)) and (result = true) do
  begin
    if pos(s[i], '0123456789,') = 0 then // ���� ������ �� �������� ������
      result := false
    else
    begin
      if pos(s[i], '0123456789,') <> 0 then
      begin
        result := true;
        inc(i);
      end
      else
      begin
        inc(dotcount);
        if dotcount > 1 then
        // ���� ���������� ����� � ������ ������ 1,�� ���������-false
          result := false
        else
          inc(i);
      end;
    end;
  end;
  // ���� ����� ������ ������ 3 ��� �����/����� ����� ��� �����,�� ���������-false
  if (result = true) and (dotcount = 1) and
    ((length(s) < 3) or (pos(s[pos(',', s) - 1], '0123456789') = 0) or
    (pos(s[pos(',', s) + 1], '0123456789') = 0)) then
  begin
    result := false;
  end;
end;

end.
