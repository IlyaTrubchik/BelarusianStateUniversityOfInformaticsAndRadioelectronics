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
  ConverterArray: Converter = ((Name: 'Метр'; Value: 1), (Name: 'Нанометр';
    Value: 1000000000), (Name: 'Километр'; Value: 0.001), (Name: 'Сантиметр';
    Value: 100), (Name: 'Миллиметр'; Value: 1000), (Name: 'Дюйм';
    Value: 39.37008), (Name: 'Фут'; Value: 3.28084), (Name: 'Ярд';
    Value: 1.093613), (Name: 'Миля'; Value: 0.000621), (Name: 'Микрон';
    Value: 1000000), (Name: 'Миллилитр'; Value: 1),
    (Name: 'Кубический Сантиметр'; Value: 1), (Name: 'Литр'; Value: 0.001),
    (Name: 'Кубический метр'; Value: 0.000001), (Name: 'Кубический Дюйм';
    Value: 0.061024), (Name: 'Кубический Фут'; Value: 0.000035),
    (Name: 'Кубический Ярд'; Value: 0.000001), (Name: 'Кубический Дециметр';
    Value: 0.001), (Name: 'Галлон(США)'; Value: 0.000264), (Name: 'Кварт(США)';
    Value: 0.001057), (Name: 'Килограмм'; Value: 1), (Name: 'Карат';
    Value: 5000), (Name: 'Миллиграмм'; Value: 1000000), (Name: 'Сантиграмм';
    Value: 100000), (Name: 'Дециграмм'; Value: 10000), (Name: 'Грамм';
    Value: 1000), (Name: 'Декаграмм'; Value: 100), (Name: 'Гектограмм';
    Value: 10), (Name: 'Метрическая Тонна'; Value: 0.001), (Name: 'Фунт';
    Value: 2.204623), (Name: 'Шкала Цельсия'; Value: 0),
    (Name: 'Шкала Фаренгейта'; Value: 32), (Name: 'Шкала Кельвина';
    Value: 273.15), (Name: 'Квадратный метр'; Value: 1),
    (Name: 'Квадратный сантиметр'; Value: 10000), (Name: 'Квадратный миллиметр';
    Value: 1000000), (Name: 'Гектар'; Value: 0.0001),
    (Name: 'Квадратный километр'; Value: 0.000001), (Name: 'Квадратный дюйм';
    Value: 1550.003), (Name: 'Квадратный фут'; Value: 10.76391),
    (Name: 'Квадратный ярд'; Value: 1.19599), (Name: 'Акр'; Value: 0.000247),
    (Name: 'Квадратная миля'; Value: 0.000000386102), (Name: 'Джоуль';
    Value: 1), (Name: 'Килоджоуль'; Value: 0.001), (Name: 'Калории';
    Value: 0.239006), (Name: 'Пищевые Калории'; Value: 0.000239),
    (Name: 'Фут-фунт'; Value: 0.737562), (Name: 'Британска тепловая единица';
    Value: 0.000948), (Name: 'Электрон-вольт'; Value: 6241509000000000000),
    (Name: 'Микросекунда'; Value: 1), (Name: 'Миллисекунда'; Value: 0.001),
    (Name: 'Секунда'; Value: 0.000001), (Name: 'Минута'; Value: 0.0000000166),
    (Name: 'Час'; Value: 0.000000000277), (Name: 'День';
    Value: 0.000000000011574), (Name: 'Неделя'; Value: 0.000000000001653),
    (Name: 'Год'; Value: 0.000000000000032));

  {
    N:
    Длина:1;
    Объем:11;
    Масса:21;
    Температура:31;
    Площадь:34;
    Энергия:44;
    Время:51;
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
  // Помещаем экземплярное значение первой единицы измерения
  examplevalue2 := quantity[finalindex].Value;
  // Помещаем экземплярное значение второй единицы измерения
  if (currindex >= TemperatureIndex) and (currindex <= TemperatureIndex + 2)
  then // Если величина измерения- температура
  begin // ИСпользования специальных формул для перевода температур
    case currindex - TemperatureIndex of
      0: // цельсий
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
      1: // Фаренгейт
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
      2: // кельвин
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
  else // В ином случае
  begin
    finalvalue := (currvalue / examplevalue1) * examplevalue2;
    // Получаем значение исходного числа в новой величине измерения
    result := floattostr(finalvalue);
  end;
end;

function checkstr(s: string; index: integer): boolean;
// Проверка строки на корректность
var
  i: integer;
  dotcount: integer;
begin
  result := true;
  dotcount := 0;
  // Если первый символ минус и выбранная величина- температура
  if (s[1] = '-') and (index >= TemperatureIndex) and
    (index <= TemperatureIndex + 2) then
    delete(s, 1, 1); // Удаление минуса из строки
  i := 1;
  while (i <= length(s)) and (result = true) do
  begin
    if pos(s[i], '0123456789,') = 0 then // Если символ не является числом
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
        // Если количество точек в строке больше 1,то результат-false
          result := false
        else
          inc(i);
      end;
    end;
  end;
  // Если длина строки меньше 3 или перед/после точки нет числа,то результат-false
  if (result = true) and (dotcount = 1) and
    ((length(s) < 3) or (pos(s[pos(',', s) - 1], '0123456789') = 0) or
    (pos(s[pos(',', s) + 1], '0123456789') = 0)) then
  begin
    result := false;
  end;
end;

end.
