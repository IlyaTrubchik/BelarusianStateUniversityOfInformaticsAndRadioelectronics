unit HistoryProcedures;

interface
uses HistoryUnit;
    type
Thistory= string[200];
TlHIstory = ^TRHistory;
TRHistory =  record
   hitem:THistory;
   address:TlHIstory;
end;
    procedure addtohistory(var h:TLHistory;item:string);
    procedure WriteIntoList(s:string);
implementation

 procedure WriteIntoList(s:string);
begin
end;
procedure addtohistory(var h:TLHistory;item:string);
var t:tlhistory;
begin
 t:=h;
  if h<>nil then
  begin
     while t^.address<>nil do
      t:=t^.address;
      new(t^.address);
      t:=t^.address;
      t^.address:=nil;
      t^.hitem:=shortstring(item);
  end
  else
  begin
    new(h);
    new(h^.address);
    t:=h^.address;
    t^.hitem:=shortstring(item);
  end;
end;
end.
