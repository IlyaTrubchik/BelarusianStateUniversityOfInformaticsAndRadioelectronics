program Calculator;

uses
  Vcl.Forms,
  CalculatorForm in 'CalculatorForm.pas' {MainCalculatorForm},
  CalculatorProcedures in 'CalculatorProcedures.pas',
  DateCalculator in 'DateCalculator.pas' {workwithdateform},
  DateFunctions in 'DateFunctions.pas',
  ValueConverterFunctions in 'ValueConverterFunctions.pas',
  ValueConverterProg in 'ValueConverterProg.pas' {valueconverterform},
  HistoryUnit in 'HistoryUnit.pas' {HistoryForm};

{$R *.res}
 var MainCalculatorForm: TMaincalculatorForm;


begin
  Application.Initialize;
  Application.CreateForm(TMainCalculatorForm, MainCalculatorForm);
  Application.Run;
end.
