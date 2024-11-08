unit ValueConverterProg;

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants,
  System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs, ValueCOnverterFunctions, Vcl.StdCtrls,
  Vcl.ExtCtrls, Vcl.Buttons, System.Actions, Vcl.ActnList, Vcl.Menus,
  datecalculator, historyunit;

type
  Tvalueconverterform = class(TForm)
    panelCurrent: TPanel;
    panelResult: TPanel;
    cmbbCurrent: TComboBox;
    cmbbLast: TComboBox;
    labelCurrent: TLabel;
    Label1: TLabel;
    panelButtons1: TPanel;
    panelButtons2: TPanel;
    btnLength: TBitBtn;
    btnVolume: TBitBtn;
    btnWeight: TBitBtn;
    btnTemperature: TBitBtn;
    btnSquare: TBitBtn;
    BtnEnergy: TBitBtn;
    btnTime: TBitBtn;
    editCurrent: TEdit;
    editResult: TEdit;
    labelCurrentValue: TLabel;
    Panel1: TPanel;
    btnNumber1: TBitBtn;
    btnNumber2: TBitBtn;
    btnNumber3: TBitBtn;
    btnNumber4: TBitBtn;
    btnNumber5: TBitBtn;
    btnNumber6: TBitBtn;
    btnNumber7: TBitBtn;
    btnNumber8: TBitBtn;
    btnNumber9: TBitBtn;
    btnNumber0: TBitBtn;
    btnDot: TBitBtn;
    btnClear: TBitBtn;
    btnClearOne: TBitBtn;
    InputButtons: TActionList;
    Number1: TAction;
    Number2: TAction;
    number3: TAction;
    number4: TAction;
    number5: TAction;
    number6: TAction;
    number7: TAction;
    Number8: TAction;
    number9: TAction;
    number0: TAction;
    Clear: TAction;
    ClearOne: TAction;
    Dot: TAction;
    mmValueConverter: TMainMenu;
    N1: TMenuItem;
    ValueConverter: TMenuItem;
    WorkWithDate: TMenuItem;
    MainCalculator: TMenuItem;
    btnConvert: TBitBtn;
    btnHistory: TBitBtn;
    btnMinus: TBitBtn;
    actMinus: TAction;
    ActionExit: TAction;
    procedure editCurrentClick(Sender: TObject);
    procedure editResultClick(Sender: TObject);
    procedure number0Execute(Sender: TObject);
    procedure FormCreate(Sender: TObject);
    procedure ClearOneExecute(Sender: TObject);
    procedure ClearExecute(Sender: TObject);
    procedure btnLengthClick(Sender: TObject);
    procedure btnVolumeClick(Sender: TObject);
    procedure btnWeightClick(Sender: TObject);
    procedure btnTemperatureClick(Sender: TObject);
    procedure btnSquareClick(Sender: TObject);
    procedure BtnEnergyClick(Sender: TObject);
    procedure btnTimeClick(Sender: TObject);
    procedure WorkWithDateClick(Sender: TObject);
    procedure MainCalculatorClick(Sender: TObject);
    procedure FormClose(Sender: TObject; var Action: TCloseAction);
    procedure btnConvertClick(Sender: TObject);
    procedure btnHistoryClick(Sender: TObject);
    procedure actMinusExecute(Sender: TObject);
    procedure cmbbCurrentKeyPress(Sender: TObject; var Key: Char);
    procedure ActionExitExecute(Sender: TObject);
    // procedure N1Click(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  ConverterForm: Tvalueconverterform;
  CurrentChosen: boolean;
  startindex: integer;
  historystring: string;

implementation

{$R *.dfm}

procedure Tvalueconverterform.ActionExitExecute(Sender: TObject);
begin
(application).mainform.close;
end;

procedure Tvalueconverterform.actMinusExecute(Sender: TObject);
begin
  if CurrentChosen = true then
    editCurrent.text := editCurrent.text + '-'
  else
    editResult.text := editResult.text + '-';
end;

// ���������� ������� �� ������ '��������������'
procedure Tvalueconverterform.btnConvertClick(Sender: TObject);
var
  bufs: string;
  num: extended;
begin
  if CurrentChosen = true then
  // ���� ���������� �������� � ���� ��� �������� ��������
    bufs := trim(editCurrent.text)
  else // ���� ���������� �������� � ���� ��� �������� ��������
    bufs := trim(editResult.text);
  if bufs <> '' then
  begin
    if checkstr(bufs, startindex + cmbbCurrent.ItemIndex) = true then
    // �������� ������ �� ������������
    begin
      num := strtofloat(bufs);
      // ���������, ����� � ������ � ������� ���������� �����������
      if CurrentChosen = true then
      begin
        bufs := convertQuantity(startindex + cmbbCurrent.ItemIndex,
          startindex + cmbbLast.ItemIndex, num,
          ValueCOnverterFunctions.converterarray);
        editResult.text := bufs;
      end
      else
      begin
        bufs := convertQuantity(startindex + cmbbLast.ItemIndex,
          startindex + cmbbCurrent.ItemIndex, num,
          ValueCOnverterFunctions.converterarray);
        editCurrent.text := bufs;
      end;
      historystring := '(' + cmbbCurrent.items[cmbbCurrent.ItemIndex] + ') ' +
        editCurrent.text + '==>' + bufs + ' (' + cmbbLast.items
        [cmbbLast.ItemIndex] + ')';
      HistoryForm.lbHistory.items.add(historystring);
      HistoryForm.lbHistory.items.add('');
    end
    else
    begin
      if CurrentChosen = true then
        editResult.text := 'Uncorrect Data'
      else
        editCurrent.text := 'Uncorrect Data';
    end;
  end
  else
  begin
    if CurrentChosen = true then
      editResult.text := 'Uncorrect Data'
    else
      editCurrent.text := 'Uncorrect Data';
  end;
end;

// ��������� �������� �� ������� � ��������� ������� ������ ���������
procedure Tvalueconverterform.BtnEnergyClick(Sender: TObject);
var
  i: integer;
begin
  for i := 0 to 6 do
  begin
    cmbbCurrent.items[i] := converterarray[EnergyIndex + i].name;
    cmbbLast.items[i] := converterarray[EnergyIndex + i].name;
  end;
  for i := 7 to 9 do
  begin
    cmbbCurrent.items.Delete(7);
    cmbbLast.items.Delete(7);
  end;
  cmbbCurrent.ItemIndex := 0;
  cmbbLast.ItemIndex := 0;
  startindex := EnergyIndex;
  labelCurrentValue.Caption := '�������';
  actMinus.Enabled := false;
end;

// ������� �� �������� �������
procedure Tvalueconverterform.btnHistoryClick(Sender: TObject);
begin
  ConverterForm.Enabled := false;
  ConverterForm.visible := false;
  HistoryForm.visible := true;
  HistoryForm.Show;
end;

// ��������� �������� �� ����� � ��������� ������� ������ ���������
procedure Tvalueconverterform.btnLengthClick(Sender: TObject);
var
  i: integer;
begin
  for i := 0 to 9 do
  begin
    cmbbCurrent.items[i] := converterarray[LengthIndex + i].name;
    cmbbLast.items[i] := converterarray[LengthIndex + i].name;
  end;
  cmbbCurrent.ItemIndex := 0;
  cmbbLast.ItemIndex := 0;
  startindex := LengthIndex;
  labelCurrentValue.Caption := '�����';
  actMinus.Enabled := false;

end;

// ��������� �������� �� ������� � ��������� ������� ������ ���������
procedure Tvalueconverterform.btnSquareClick(Sender: TObject);
var
  i: integer;
begin
  for i := 0 to 9 do
  begin
    cmbbCurrent.items[i] := converterarray[SquareIndex + i].name;
    cmbbLast.items[i] := converterarray[SquareIndex + i].name;
  end;
  cmbbCurrent.ItemIndex := 0;
  cmbbLast.ItemIndex := 0;
  startindex := SquareIndex;
  labelCurrentValue.Caption := '�������';
  actMinus.Enabled := false;
end;

// ��������� �������� �� ����������� � ��������� ������� ������ ���������
procedure Tvalueconverterform.btnTemperatureClick(Sender: TObject);
var
  i: integer;
begin
  for i := 0 to 2 do
  begin
    cmbbCurrent.items[i] := converterarray[TemperatureIndex + i].name;
    cmbbLast.items[i] := converterarray[TemperatureIndex + i].name;
  end;
  for i := 3 to 9 do
  begin
    cmbbCurrent.items.Delete(3);
    cmbbLast.items.Delete(3);
  end;
  cmbbCurrent.ItemIndex := 0;
  cmbbLast.ItemIndex := 0;
  startindex := TemperatureIndex;
  labelCurrentValue.Caption := '�����������';
  actMinus.Enabled := true;
end;

// ��������� �������� �� ����� � ��������� ������� ������ ���������
procedure Tvalueconverterform.btnTimeClick(Sender: TObject);
var
  i: integer;
begin

  for i := 0 to 7 do
  begin
    cmbbCurrent.items[i] := converterarray[TimeIndex + i].name;
    cmbbLast.items[i] := converterarray[TimeIndex + i].name;
  end;
  for i := 8 to 9 do
  begin
    cmbbCurrent.items.Delete(8);
    cmbbLast.items.Delete(8);
  end;
  cmbbCurrent.ItemIndex := 0;
  cmbbLast.ItemIndex := 0;
  startindex := TimeIndex;
  labelCurrentValue.Caption := '�����';
  actMinus.Enabled := false;
end;

// ��������� �������� �� ����� � ��������� ������� ������ ���������
procedure Tvalueconverterform.btnVolumeClick(Sender: TObject);
var
  i: integer;
begin
  for i := 0 to 9 do
  begin
    cmbbCurrent.items[i] := converterarray[VOlumeindex + i].name;
    cmbbLast.items[i] := converterarray[VOlumeindex + i].name;
  end;
  cmbbCurrent.ItemIndex := 0;
  cmbbLast.ItemIndex := 0;
  startindex := VOlumeindex;
  labelCurrentValue.Caption := '�����';
  actMinus.Enabled := false;
end;

// ��������� �������� �� ����� � ��������� ������� ������ ���������
procedure Tvalueconverterform.btnWeightClick(Sender: TObject);
var
  i: integer;
begin
  for i := 0 to 9 do
  begin
    cmbbCurrent.items[i] := converterarray[Weightindex + i].name;
    cmbbLast.items[i] := converterarray[Weightindex + i].name;
  end;
  cmbbCurrent.ItemIndex := 0;
  cmbbLast.ItemIndex := 0;
  startindex := Weightindex;
  labelCurrentValue.Caption := '�����';
  actMinus.Enabled := false;
end;

// ���������� ������� ��� ������ '��������'
procedure Tvalueconverterform.ClearExecute(Sender: TObject);
begin
  if CurrentChosen = true then
    editCurrent.text := ''
  else
    editResult.text := '';
end;

procedure Tvalueconverterform.ClearOneExecute(Sender: TObject);
var
  bufs: string;
begin
  if CurrentChosen = true then
  begin
    bufs := editCurrent.text;
    Delete(bufs, length(bufs), 1);
    editCurrent.text := bufs;
    editcurrent.SelStart:=length(editcurrent.text);
  end
  else
  begin
    bufs := editResult.text;
    Delete(bufs, length(bufs), 1);
    editResult.text := bufs;
    editresult.SelStart:=length(editresult.text);
  end;

end;

procedure Tvalueconverterform.cmbbCurrentKeyPress(Sender: TObject;
  var Key: Char);
begin
Key := #0;
end;

procedure Tvalueconverterform.editCurrentClick(Sender: TObject);
begin
  CurrentChosen := true;

end;

procedure Tvalueconverterform.editResultClick(Sender: TObject);
begin
  CurrentChosen := false;
end;

// �������� ������������ ��������
procedure Tvalueconverterform.FormClose(Sender: TObject;
  var Action: TCloseAction);
begin
  (application).MainForm.close;
end;

procedure Tvalueconverterform.FormCreate(Sender: TObject);
begin
  CurrentChosen := true;
  startindex := LengthIndex;
end;

// ������� �� �������� ������������-������������
procedure Tvalueconverterform.MainCalculatorClick(Sender: TObject);
begin
  (application).MainForm.visible := true;
  ConverterForm.visible := false;
  ConverterForm.Enabled := false;
  (application).MainForm.Enabled := true;
end;

procedure Tvalueconverterform.number0Execute(Sender: TObject);
begin
  if CurrentChosen = true then
    editCurrent.text := editCurrent.text + (Sender as TAction).Caption
  else
    editResult.text := editResult.text + (Sender as TAction).Caption;
end;

// ������� �� �������� ������ � ������
procedure Tvalueconverterform.WorkWithDateClick(Sender: TObject);
begin
  ConverterForm.visible := false;
  ConverterForm.Enabled := false;
  workwithdateform.visible := true;
  workwithdateform.Enabled := true;
end;

end.
