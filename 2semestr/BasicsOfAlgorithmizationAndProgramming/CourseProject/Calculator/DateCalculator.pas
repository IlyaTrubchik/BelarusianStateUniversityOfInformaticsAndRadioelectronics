unit DateCalculator;

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants,
  System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs, DateFunctions, Vcl.StdCtrls,
  Vcl.ExtCtrls,
  Vcl.ComCtrls, Vcl.WinXPickers, Vcl.Menus, historyunit, Vcl.Buttons,
  System.Actions, Vcl.ActnList;

type
  Tworkwithdateform = class(TForm)
    panelfunctions: TPanel;
    panelOutputDateAddFunc: TPanel;
    labelOutput: TLabel;
    cmbbDateFuncs: TComboBox;
    panelProcessAddFunc: TPanel;
    rbtnAdd: TRadioButton;
    rbtSubtract: TRadioButton;
    editDays: TEdit;
    editMonths: TEdit;
    editYears: TEdit;
    Label3: TLabel;
    Label4: TLabel;
    Label5: TLabel;
    labelResult: TLabel;
    panelInputDateAddFunc: TPanel;
    Label1: TLabel;
    panelCurrDate: TPanel;
    panelDiffDates: TPanel;
    Panel2: TPanel;
    labelLast: TLabel;
    dtpLast: TDateTimePicker;
    panelDiffDatesResult: TPanel;
    labelDiff: TLabel;
    labelDiffResult: TLabel;
    Panel3: TPanel;
    labelCurrent: TLabel;
    dtpCurrent: TDateTimePicker;
    labelAlldays: TLabel;
    mmWorkwithdate: TMainMenu;
    N1: TMenuItem;
    valueconverter: TMenuItem;
    workwithdate: TMenuItem;
    calculatorprogrammer: TMenuItem;
    btnHistory: TBitBtn;
    btnComputeAddorSub: TBitBtn;
    dtpInput: TDateTimePicker;
    aclistExit: TActionList;
    ActionExit: TAction;
    procedure FormCreate(Sender: TObject);
    procedure cmbbDateFuncsChange(Sender: TObject);
    procedure valueconverterClick(Sender: TObject);
    procedure calculatorprogrammerClick(Sender: TObject);
    procedure FormClose(Sender: TObject; var Action: TCloseAction);
    procedure btnHistoryClick(Sender: TObject);
    procedure dtpCurrentCloseUp(Sender: TObject);
    procedure dtpLastCloseUp(Sender: TObject);
    procedure btnComputeAddorSubClick(Sender: TObject);
    procedure cmbbDateFuncsKeyPress(Sender: TObject; var Key: Char);
    procedure ActionExitExecute(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  workwithdateform: Tworkwithdateform;
  days, years, months: integer;
  number: string;
  historystring: string;

implementation

{$R *.dfm}

uses valueconverterprog;

// ����� ���������� ������� ��������/������� ��� , � ����� ����� ���������� � �������

procedure Tworkwithdateform.ActionExitExecute(Sender: TObject);
begin
   (application).mainform.close;
end;

procedure Tworkwithdateform.btnComputeAddorSubClick(Sender: TObject);
var
  maxyears: integer;
begin
  maxyears := strtoint(copy(datetostr(dtpInput.date), 7, 4));
  days := 0;
  months := 0;
  years := 0;
  number := editDays.text;
  if (checknum(editDays.text) = true) and (checknum(editYears.text) = true) and
    (checknum(editMonths.text) = true) then
  begin
    days := strtoint(editDays.text);
    months := strtoint(editMonths.text);
    years := strtoint(editYears.text);
    if rbtnAdd.checked = true then
    begin
      labelResult.Caption := ResultOutput(DateFunctions.Sumdays(days, months,
        years, datetostr(dtpInput.date)));
      historystring := datetostr(dtpInput.date) + '+' + inttostr(days) + ' ����'
        + ' + ' + inttostr(months) + ' ������� ' + ' + ' + inttostr(years) +
        ' ��� =' + labelResult.Caption;
      HistoryForm.lbHistory.Items.add(historystring);
      HistoryForm.lbHistory.Items.add('');
    end
    else
    begin
      if years > maxyears then
        labelResult.Caption := '���� ������� �� �������!'
      else
      begin
        labelResult.Caption := ResultOutput(DateFunctions.Subtractdays(days,
          months, years, datetostr(dtpInput.date)));
        historystring := datetostr(dtpInput.date) + '-' + inttostr(days) +
          ' ����' + ' - ' + inttostr(months) + ' ������� ' + ' - ' +
          inttostr(years) + ' ��� =' + labelResult.Caption;
        HistoryForm.lbHistory.Items.add(historystring);
        HistoryForm.lbHistory.Items.add('');
      end;
    end;
  end
  else
    labelResult.Caption := '������!';
end;

// ������� �� �������
procedure Tworkwithdateform.btnHistoryClick(Sender: TObject);
begin
  workwithdateform.Enabled := false;
  workwithdateform.Visible := false;
  HistoryForm.Visible := true;
  HistoryForm.Show;
end;

// ������� �� �������� ������������-������������
procedure Tworkwithdateform.calculatorprogrammerClick(Sender: TObject);
begin
  (application).MainForm.Visible := true;
  (application).MainForm.Enabled := true;
  workwithdateform.Enabled := false;
  workwithdateform.Visible := false;
end;

// ������� ����� ��������� ����������/��������� � �������� ����� ������
procedure Tworkwithdateform.cmbbDateFuncsChange(Sender: TObject);
begin
  if cmbbDateFuncs.ItemIndex = 1 then
  begin
    panelInputDateAddFunc.Visible := false;
    labelOutput.Visible := false;
    panelProcessAddFunc.Visible := false;
    labelResult.Visible := false;
    panelCurrDate.Visible := true;
    panelDiffDates.Visible := true;
    panelDiffDatesResult.Visible := true;
  end
  else
  begin
    panelInputDateAddFunc.Visible := true;
    panelOutputDateAddFunc.Visible := true;
    labelOutput.Visible := true;
    labelResult.Visible := true;
    panelProcessAddFunc.Visible := true;
    panelCurrDate.Visible := false;
    panelDiffDates.Visible := false;
    panelDiffDatesResult.Visible := false;
  end;
end;

procedure Tworkwithdateform.cmbbDateFuncsKeyPress(Sender: TObject;
  var Key: Char);
begin
Key := #0;
end;

// ����� ���������� ������� ����� ������ � ������ ��� � �������
procedure Tworkwithdateform.dtpCurrentCloseUp(Sender: TObject);
begin
  if dtpCurrent.date <> dtpLast.date then
  begin
    DateFunctions.Diffresult := DateFunctions.Difference
      (datetostr(dtpCurrent.date), datetostr(dtpLast.date));
    labelDiffResult.Caption := inttostr(DateFunctions.Diffresult.days) +
      ' ���� ' + inttostr(DateFunctions.Diffresult.weeks) + ' ������ ' +
      inttostr(DateFunctions.Diffresult.years) + ' ���';
    labelAlldays.Caption := inttostr(DateFunctions.Diffresult.AllDays)
      + ' ���� ';
    historystring := '������� �����:' + datetostr(dtpCurrent.date) + ' � ' +
      datetostr(dtpLast.date) + ' = ' + labelDiffResult.Caption + '(' +
      inttostr(DateFunctions.Diffresult.AllDays) + ' ���� ' + ')';
    HistoryForm.lbHistory.Items.add(historystring);
    HistoryForm.lbHistory.Items.add('');
  end
  else
  begin
    labelDiffResult.Caption := '���������� ����';
    labelAlldays.Caption := '';
  end;
end;

// ����� ���������� ������� ����� ������ � ������ ��� � �������
procedure Tworkwithdateform.dtpLastCloseUp(Sender: TObject);
begin
  DateFunctions.Diffresult := DateFunctions.Difference
    (datetostr(dtpCurrent.date), datetostr(dtpLast.date));
  if dtpCurrent.date <> dtpLast.date then
  begin
    DateFunctions.Diffresult := DateFunctions.Difference
      (datetostr(dtpCurrent.date), datetostr(dtpLast.date));
    labelDiffResult.Caption := inttostr(DateFunctions.Diffresult.days) +
      ' ���� ' + inttostr(DateFunctions.Diffresult.weeks) + ' ������ ' +
      inttostr(DateFunctions.Diffresult.years) + ' ���';
    labelAlldays.Caption := inttostr(DateFunctions.Diffresult.AllDays)
      + ' ���� ';
    historystring := '������� �����:' + datetostr(dtpCurrent.date) + ' � ' +
      datetostr(dtpLast.date) + ' = ' + labelDiffResult.Caption + '(' +
      inttostr(DateFunctions.Diffresult.AllDays) + ' ���� ' + ')';
    HistoryForm.lbHistory.Items.add(historystring);
    HistoryForm.lbHistory.Items.add('');
  end
  else
  begin
    labelDiffResult.Caption := '���������� ����';
    labelAlldays.Caption := '';
  end;
end;

// �������� ������������ ��������
procedure Tworkwithdateform.FormClose(Sender: TObject;
  var Action: TCloseAction);
begin
  (application).MainForm.close;
end;

procedure Tworkwithdateform.FormCreate(Sender: TObject);
var
  date: string;
begin
  date := datetostr(dtpInput.date);
  labelResult.Caption := ResultOutput(date);
end;


// ������� �� �������� ��������������� �������
procedure Tworkwithdateform.valueconverterClick(Sender: TObject);
begin
  converterform.Visible := true;
  converterform.Enabled := true;
  workwithdateform.Visible := false;
  workwithdateform.Enabled := false;
end;

end.
