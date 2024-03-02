unit CalculatorForm;

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants,
  System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs, Vcl.StdCtrls, Vcl.Buttons, Vcl.ExtCtrls,
  Vcl.ControlList, Vcl.Menus, System.Actions, Vcl.ActnList,
  CalculatorProcedures,
  ValueConverterFunctions,
  datecalculator, datefunctions, ValueConverterProg, System.ImageList,
  Vcl.ImgList,
  Vcl.Imaging.jpeg, Vcl.ComCtrls;

type
  TMainCalculatorForm = class(TForm)
    manePanel: TPanel;
    panelKeyBoard: TPanel;
    btnNumber2: TBitBtn;
    btnNumber3: TBitBtn;
    btnNumber4: TBitBtn;
    btnNumber5: TBitBtn;
    btnNumber6: TBitBtn;
    btnNumber7: TBitBtn;
    btnNumber1: TBitBtn;
    btnNumber8: TBitBtn;
    btnNumber9: TBitBtn;
    btnNumber0: TBitBtn;
    btnLeftBracket: TBitBtn;
    btnRightBracket: TBitBtn;
    btnShl: TBitBtn;
    btnClear: TBitBtn;
    btnShr: TBitBtn;
    btnErase: TBitBtn;
    btnDiv: TBitBtn;
    btnMul: TBitBtn;
    btnSum: TBitBtn;
    btnDifference: TBitBtn;
    btnGetResult: TBitBtn;
    btnLetterA: TBitBtn;
    btnLetterB: TBitBtn;
    btnLetterC: TBitBtn;
    btnLetterD: TBitBtn;
    btnLetterE: TBitBtn;
    btnLetterF: TBitBtn;
    panelcmbs: TPanel;
    cmbbBitOperations: TComboBox;
    cmbbShale: TComboBox;
    PanelInput: TPanel;
    PanelOutput: TPanel;
    rbtnHex: TRadioButton;
    rbtnDec: TRadioButton;
    rbtnOct: TRadioButton;
    rbtnBin: TRadioButton;
    editResult: TEdit;
    aclistButtons: TActionList;
    Number0: TAction;
    Number1: TAction;
    Number2: TAction;
    Number3: TAction;
    Number4: TAction;
    Number5: TAction;
    Number6: TAction;
    Number7: TAction;
    Number8: TAction;
    Number9: TAction;
    A: TAction;
    B: TAction;
    C: TAction;
    D: TAction;
    E: TAction;
    F: TAction;
    actionClear: TAction;
    Mul: TAction;
    Division: TAction;
    Sum: TAction;
    Diff: TAction;
    BracketOpen: TAction;
    BracketClose: TAction;
    Input: TAction;
    GetResult: TAction;
    btnbit: TBitBtn;
    Erase: TAction;
    neodate: TAction;
    mmCalculatorMain: TMainMenu;
    N1: TMenuItem;
    valueconverter: TMenuItem;
    workwithdates: TMenuItem;
    CalculatorProgrammer: TMenuItem;
    btnHistory: TBitBtn;
    OpenDialog1: TOpenDialog;
    ImageList1: TImageList;
    Image1: TImage;
    editInput: TRichEdit;
    ActionExit: TAction;
    // Процедуры для записи в строку допустимых цифр//букв
    procedure Number0Execute(Sender: TObject);
    procedure Number4Execute(Sender: TObject);
    procedure Number1Execute(Sender: TObject);
    procedure Number2Execute(Sender: TObject);
    procedure Number3Execute(Sender: TObject);
    procedure Number5Execute(Sender: TObject);
    procedure Number6Execute(Sender: TObject);
    procedure Number7Execute(Sender: TObject);
    procedure Number8Execute(Sender: TObject);
    procedure Number9Execute(Sender: TObject);
    procedure AExecute(Sender: TObject);
    /// /////////////////////////////////////
    procedure actionClearExecute(Sender: TObject);
    procedure rbtnHexClick(Sender: TObject);
    procedure rbtnDecClick(Sender: TObject);
    procedure rbtnOctClick(Sender: TObject);
    procedure rbtnBinClick(Sender: TObject);
    procedure BracketOpenExecute(Sender: TObject);
    procedure BracketCloseExecute(Sender: TObject);
    procedure MulExecute(Sender: TObject);
    procedure SumExecute(Sender: TObject);
    procedure DivisionExecute(Sender: TObject);
    procedure DiffExecute(Sender: TObject);
    procedure cmbbBitOperationsChange(Sender: TObject);
    procedure cmbbShaleChange(Sender: TObject);
    procedure GetResultExecute(Sender: TObject);
    procedure btnbitClick(Sender: TObject);
    procedure FormCreate(Sender: TObject);
    procedure btnShlClick(Sender: TObject);
    procedure btnShrClick(Sender: TObject);
    procedure EraseExecute(Sender: TObject);
    procedure valueconverterClick(Sender: TObject);
    procedure workwithdatesClick(Sender: TObject);
    procedure btnHistoryClick(Sender: TObject);
    procedure FormClose(Sender: TObject; var Action: TCloseAction);
    procedure btnOpenFileClick(Sender: TObject);
    procedure cmbbBitOperationsKeyPress(Sender: TObject; var Key: Char);
    procedure ActionExitExecute(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  MainCalculatorForm: TMainCalculatorForm;
  CorrectFl: boolean;
  historystring: string;
  inbin: string;

implementation

{$R *.dfm}

uses HistoryUnit, HistoryProcedures;

procedure TMainCalculatorForm.actionClearExecute(Sender: TObject);
begin
  editInput.text := '';
  editResult.text := '';
end;

procedure TMainCalculatorForm.BracketOpenExecute(Sender: TObject);
begin
  editInput.text := editInput.text + '(';
end;

// Обработчик для изменения разрядности
procedure TMainCalculatorForm.btnbitClick(Sender: TObject);
begin
  if btnbit.caption = 'WORD' then
  begin
    btnbit.caption := 'DWORD';
    CalculatorProcedures.n := 32;
  end
  else if btnbit.caption = 'BYTE' then
  begin
    btnbit.caption := 'WORD';
    CalculatorProcedures.n := 16;
  end
  else if btnbit.caption = 'DWORD' then
  begin
    btnbit.caption := 'BYTE';
    CalculatorProcedures.n := 8;
  end

end;

// Переход на страницу истории
procedure TMainCalculatorForm.btnHistoryClick(Sender: TObject);
begin
  (application).MainForm.Visible := false;
  (application).MainForm.Enabled := false;
  HistoryForm.Visible := true;
  HistoryForm.Show;
end;

procedure TMainCalculatorForm.btnShlClick(Sender: TObject);
begin
  editInput.text := editInput.text + 'shl';
end;

procedure TMainCalculatorForm.btnShrClick(Sender: TObject);
begin
  editInput.text := editInput.text + 'shr';
end;

// Обработчик нажатия на нужную побитовую операции в выпадающем списке
procedure TMainCalculatorForm.cmbbBitOperationsChange(Sender: TObject);
begin
  case cmbbBitOperations.itemindex of
    0:
      editInput.text := editInput.text + 'OR';
    1:
      editInput.text := editInput.text + 'XOR';
    2:
      editInput.text := editInput.text + 'AND';
    3:
      editInput.text := editInput.text + 'NOT';
  end;
end;

procedure TMainCalculatorForm.cmbbBitOperationsKeyPress(Sender: TObject;
  var Key: Char);
begin
  Key := #0;
end;

procedure TMainCalculatorForm.cmbbShaleChange(Sender: TObject);
begin
  case cmbbShale.itemindex of
    0:
      begin
        CalculatorProcedures.shl_f := CalculatorProcedures.Arifmshl;
        CalculatorProcedures.shr_f := CalculatorProcedures.Arifmshr;
      end;
    1:
      begin
        CalculatorProcedures.shl_f := CalculatorProcedures.LoopShl;
        CalculatorProcedures.shr_f := CalculatorProcedures.LoopShr;
      end;
    2:
      begin
        CalculatorProcedures.shl_f := CalculatorProcedures.LogicShl;
        CalculatorProcedures.shr_f := CalculatorProcedures.LogicShr;
      end;
  end;
end;

procedure TMainCalculatorForm.DiffExecute(Sender: TObject);
begin
  editInput.text := editInput.text + '-';
end;

procedure TMainCalculatorForm.DivisionExecute(Sender: TObject);
begin
  editInput.text := editInput.text + '/';
end;

procedure TMainCalculatorForm.EraseExecute(Sender: TObject);
var
  s: string;
begin
  s := editInput.text;
  delete(s, length(s), 1);
  editInput.text := s;
end;

procedure TMainCalculatorForm.FormClose(Sender: TObject;
  var Action: TCloseAction);
var
  i: integer;
begin
  rewrite(HistoryUnit.f);
  for i := 0 to HistoryForm.lbHistory.Items.Count - 1 do
  begin
    writeln(historyunit.f, HistoryForm.lbHistory.Items[i]);
  end;
  closefile(historyunit.f);
end;

procedure TMainCalculatorForm.FormCreate(Sender: TObject);
begin
  CalculatorProcedures.n := 16;
  CalculatorProcedures.shl_f := CalculatorProcedures.Arifmshl;
  CalculatorProcedures.shr_f := CalculatorProcedures.Arifmshr;
  CalculatorProcedures.CountSystem := 16;
  converterform := tvalueconverterform.Create(application);
  workwithdateform := tworkwithdateform.Create(application);
  HistoryForm := thistoryform.Create(application);
  editResult.font.color := rgb(82, 116, 114);
  editInput.font.color := rgb(82, 116, 114);
end;

// обработчки кнопки для получения результата
procedure TMainCalculatorForm.GetResultExecute(Sender: TObject);
begin
  inbin := trim(editInput.text);
  // Считается результат в нужной системе счисления
  // При некорректности выводится сообщение об ошибке
  /// При успешном посчете выводится результат и записывается в историю
  case CountSystem of
    2:
      begin
        inbin := CalculatorProcedures.GetResult(snum, sop, inbin,
          CalculatorProcedures.n, CalculatorProcedures.shl_f,
          CalculatorProcedures.shr_f, CountSystem);
        if pos('o', lowercase(inbin)) <> 0 then
        begin
          editResult.font.color := clred;
          editResult.text := inbin;
          inbin := '';
        end
        else
        begin
          editResult.font.color := rgb(82, 116, 114);
          editResult.text := inbin;
          historystring := '(' + 'BIN' + ') ' + editInput.text + '=' +
            editResult.text;
          HistoryForm.lbHistory.Items.add(historystring);
          HistoryForm.lbHistory.Items.add('');
        end;
      end;
    8:
      begin
        bufs := CalculatorProcedures.GetResult(snum, sop, inbin,
          CalculatorProcedures.n, CalculatorProcedures.shl_f,
          CalculatorProcedures.shr_f, CountSystem);
        if pos('o',lowercase(bufs)) <> 0 then
        begin
          editResult.font.color := clred;
          editResult.text := bufs;
          inbin := '';
        end
        else
        begin
          editResult.font.color := rgb(82, 116, 114);
          editResult.text := bufs;
          historystring := '(' + 'BIN' + ') ' + editInput.text + '=' +
            editResult.text;
          HistoryForm.lbHistory.Items.add(historystring);
          HistoryForm.lbHistory.Items.add('');
        end;
      end;
    10:
      begin
        bufs := CalculatorProcedures.GetResult(snum, sop, inbin,
          CalculatorProcedures.n, CalculatorProcedures.shl_f,
          CalculatorProcedures.shr_f, CountSystem);
        if pos('o', lowercase(bufs)) <> 0 then
        begin
          editResult.font.color := clred;
          editResult.text := bufs;
          inbin := '';
        end
        else
        begin
          editResult.font.color := rgb(82, 116, 114);
          editResult.text := bufs;
          historystring := '(' + 'BIN' + ') ' + editInput.text + '=' +
            editResult.text;
          HistoryForm.lbHistory.Items.add(historystring);
          HistoryForm.lbHistory.Items.add('');
        end;
      end;
    16:
      begin
        bufs := CalculatorProcedures.GetResult(snum, sop, inbin,
          CalculatorProcedures.n, CalculatorProcedures.shl_f,
          CalculatorProcedures.shr_f, CountSystem);
        if pos('o', lowercase(bufs)) <> 0 then
        begin
          editResult.font.color := clred;
          editResult.text := bufs;
          inbin := '';
        end
        else
        begin
          editResult.font.color := rgb(82, 116, 114);
          editResult.text := bufs;
          historystring := '(' + 'BIN' + ') ' + editInput.text + '=' +
            editResult.text;
          HistoryForm.lbHistory.Items.add(historystring);
          HistoryForm.lbHistory.Items.add('');
        end;
      end;
  end;

end;

// Обработка кнопки для открытия файла
procedure TMainCalculatorForm.btnOpenFileClick(Sender: TObject);
var
  text: string;
  inputfile: textfile;
begin
  if OpenDialog1.Execute then
  begin
    if ExtractFileExt(OpenDialog1.FileName) = '.txt' then
    begin
      assignfile(inputfile, OpenDialog1.FileName);
      reset(inputfile);
      readln(inputfile, text);
      editInput.text := uppercase(trim(text));
      closefile(inputfile);
    end
    else // Если недопустимых формат,то вывод соответсвующего сообщения
      editResult.text := 'Некорректный формат';
  end;
end;

procedure TMainCalculatorForm.BracketCloseExecute(Sender: TObject);
begin
editInput.text := editInput.text + ')';
end;

procedure TMainCalculatorForm.ActionExitExecute(Sender: TObject);
begin
(application).mainform.close;
end;

procedure TMainCalculatorForm.AExecute(Sender: TObject);
begin
   editinput.text:=editinput.text+(sender as taction).Caption;
end;

procedure TMainCalculatorForm.MulExecute(Sender: TObject);
begin
  editInput.text := editInput.text + '*';
end;

procedure TMainCalculatorForm.Number0Execute(Sender: TObject);
begin
  editInput.text := editInput.text + '0';
end;

procedure TMainCalculatorForm.Number1Execute(Sender: TObject);
begin
  editInput.text := editInput.text + '1';
end;

procedure TMainCalculatorForm.Number2Execute(Sender: TObject);
begin
  editInput.text := editInput.text + '2';
end;

procedure TMainCalculatorForm.Number3Execute(Sender: TObject);
begin
  editInput.text := editInput.text + '3';
end;

procedure TMainCalculatorForm.Number4Execute(Sender: TObject);
begin
  editInput.text := editInput.text + '4';
end;

procedure TMainCalculatorForm.Number5Execute(Sender: TObject);
begin
  editInput.text := editInput.text + '5';
end;

procedure TMainCalculatorForm.Number6Execute(Sender: TObject);
begin
  editInput.text := editInput.text + '6';
end;

procedure TMainCalculatorForm.Number7Execute(Sender: TObject);
begin
  editInput.text := editInput.text + '7';
end;

procedure TMainCalculatorForm.Number8Execute(Sender: TObject);
begin
  editInput.text := editInput.text + '8';
end;

procedure TMainCalculatorForm.Number9Execute(Sender: TObject);
begin
  editInput.text := editInput.text + '9';
end;

// Изменение системы счисления на 2-чную и выключение недопустимых кнопок
procedure TMainCalculatorForm.rbtnBinClick(Sender: TObject);
begin
  Number0.Enabled := true;
  Number9.Enabled := false;
  Number8.Enabled := false;
  Number7.Enabled := false;
  Number6.Enabled := false;
  Number5.Enabled := false;
  Number4.Enabled := false;
  Number3.Enabled := false;
  Number2.Enabled := false;
  Number1.Enabled := true;
  A.Enabled := false;
  B.Enabled := false;
  C.Enabled := false;
  D.Enabled := false;
  E.Enabled := false;
  F.Enabled := false;
  if editResult.text <> '' then
  begin
    if inbin <> '' then
    begin
      editResult.text := inbin;
      editInput.text := '';
    end;
  end;
  CalculatorProcedures.CountSystem := 2;

end;

// Изменение системы счисления на 10-чную и выключение недопустимых кнопок
procedure TMainCalculatorForm.rbtnDecClick(Sender: TObject);
var
  bufs: string;
begin
  Number0.Enabled := true;
  Number9.Enabled := true;
  Number8.Enabled := true;
  Number7.Enabled := true;
  Number6.Enabled := true;
  Number5.Enabled := true;
  Number4.Enabled := true;
  Number3.Enabled := true;
  Number2.Enabled := true;
  Number1.Enabled := true;
  A.Enabled := false;
  B.Enabled := false;
  C.Enabled := false;
  D.Enabled := false;
  E.Enabled := false;
  F.Enabled := false;
  CalculatorProcedures.CountSystem := 10;
  if editResult.text <> '' then
  begin
    bufs := inbin;
    if bufs <> '' then
    begin
      if (bufs[1] = '0') then
        editResult.text := inttostr(indec(bufs, 2))
      else
      begin
        bufs := CalculatorProcedures.Diff(bufs, '00000001',
          CalculatorProcedures.n);
        convertnumber(bufs, CalculatorProcedures.n);
        bufs := '-' + inttostr(indec(bufs, 2));
        editResult.text := bufs;
      end;
      editInput.text := '';
    end;
  end;

end;

// Изменение системы счисления на 16-чную и выключение недопустимых кнопок
procedure TMainCalculatorForm.rbtnHexClick(Sender: TObject);
var
  bufs: string;
begin
  Number0.Enabled := true;
  Number9.Enabled := true;
  Number8.Enabled := true;
  Number7.Enabled := true;
  Number6.Enabled := true;
  Number5.Enabled := true;
  Number4.Enabled := true;
  Number3.Enabled := true;
  Number2.Enabled := true;
  Number1.Enabled := true;
  A.Enabled := true;
  B.Enabled := true;
  C.Enabled := true;
  D.Enabled := true;
  E.Enabled := true;
  F.Enabled := true;
  CalculatorProcedures.CountSystem := 16;
  bufs := inbin;
  if editResult.text <> '' then
  begin
    if bufs <> '' then
    begin
      editResult.text := CalculatorProcedures.convertss(bufs, 2, CountSystem);
      editInput.text := '';
    end;
  end;

end;

// Изменение системы счисления на 8-чную и выключение недопустимых кнопок
procedure TMainCalculatorForm.rbtnOctClick(Sender: TObject);
var
  bufs: string;
begin
  Number0.Enabled := true;
  Number9.Enabled := false;
  Number8.Enabled := false;
  Number7.Enabled := true;
  Number6.Enabled := true;
  Number5.Enabled := true;
  Number4.Enabled := true;
  Number3.Enabled := true;
  Number2.Enabled := true;
  Number1.Enabled := true;
  A.Enabled := false;
  B.Enabled := false;
  C.Enabled := false;
  D.Enabled := false;
  E.Enabled := false;
  F.Enabled := false;
  CalculatorProcedures.CountSystem := 8;
  bufs := inbin;
  if editResult.text <> '' then
  begin
    if bufs <> '' then
    begin
      editResult.text := CalculatorProcedures.convertss(bufs, 2, CountSystem);
      editInput.text := '';
    end;
  end;
end;

// Нажатие на кнопку '+'
procedure TMainCalculatorForm.SumExecute(Sender: TObject);
begin
  editInput.text := editInput.text + '+';
end;

// Переход на страницу преобразователя величин
procedure TMainCalculatorForm.valueconverterClick(Sender: TObject);
begin
  (application).MainForm.Visible := false;
  (application).MainForm.Enabled := false;
  converterform.Visible := true;
  converterform.Enabled := true;
end;

// Переход на страницу работы с датами
procedure TMainCalculatorForm.workwithdatesClick(Sender: TObject);
begin
  workwithdateform.Visible := true;
  (application).MainForm.Visible := false;
  (application).MainForm.Enabled := false;
  workwithdateform.Enabled := true;
end;

end.
