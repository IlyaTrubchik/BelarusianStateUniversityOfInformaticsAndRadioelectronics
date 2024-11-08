unit HistoryUnit;

interface

uses
  Winapi.Windows, Winapi.Messages, System.SysUtils, System.Variants,
  System.Classes, Vcl.Graphics,
  Vcl.Controls, Vcl.Forms, Vcl.Dialogs, Vcl.StdCtrls, Vcl.Buttons, Vcl.ExtCtrls,
  Vcl.Menus, System.Actions, Vcl.ActnList;

type
  THistoryForm = class(TForm)
    lbHistory: TListBox;
    Panel1: TPanel;
    Button1: TButton;
    btnBack: TButton;
    popUpDeleteItem: TPopupMenu;
    N1: TMenuItem;
    ActionListHistory: TActionList;
    ActionExit: TAction;
    procedure FormClose(Sender: TObject; var Action: TCloseAction);
    procedure btnBackClick(Sender: TObject);
    procedure Button1Click(Sender: TObject);
    procedure FormCreate(Sender: TObject);
    procedure N1Click(Sender: TObject);
    procedure ActionExitExecute(Sender: TObject);


  private
    { Private declarations }

  public
    { Public declarations }

  end;

var
  HistoryForm: THistoryForm;
  counter: integer;
  f: textfile;

implementation

{$R *.dfm}

{ I- }
uses CalculatorForm, ValueConverterProg, DateCalculator;



procedure THistoryForm.ActionExitExecute(Sender: TObject);
begin
(application).mainform.close;
end;

procedure THistoryForm.btnBackClick(Sender: TObject);
begin
  HistoryForm.visible := false;
  (application).MainForm.show;
  (application).MainForm.enabled := true;
end;

procedure THistoryForm.Button1Click(Sender: TObject);
begin
  lbHistory.clear;
end;

procedure THistoryForm.FormClose(Sender: TObject; var Action: TCloseAction);
begin

  (application).MainForm.close;
end;

// ������ ������� �� ����� � listbox
procedure THistoryForm.FormCreate(Sender: TObject);
var
  bufs: string;
begin
  if (FileExists('History.txt') = false) then
  begin

    AssignFile(f, 'History.txt');
    ReWrite(f);
  end
  else
  begin
    AssignFile(f, 'History.txt');
    reset(f);
    while not eof(f) do
    begin
      readln(f, bufs);
      lbHistory.Items.add(bufs);
    end;
  end;

end;

// ����������  ������� �� ������ '�������' ���������� ��������
procedure THistoryForm.N1Click(Sender: TObject);
begin
if lbhistory.itemindex<>-1   then
begin
  if lbHistory.Items[lbHistory.ItemIndex] <> '' then
  begin
    lbHistory.Items.Delete(lbHistory.ItemIndex + 1);
    lbHistory.Items.Delete(lbHistory.ItemIndex);
  end;
end;
end;

end.
