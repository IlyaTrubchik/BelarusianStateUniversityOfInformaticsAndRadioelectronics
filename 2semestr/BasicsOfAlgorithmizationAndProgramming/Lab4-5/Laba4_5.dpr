program Laba4_5;

{$APPTYPE CONSOLE}

uses
  SysUtils;

type
TVac = packed record
    index:integer;
    firm:string[60];
    spec:string[60];
    position:string[60];
    salary:integer;
    vacation:integer;
    high_ed:string[3];
    min_age:integer;
    max_age:integer;
end;
TCand = packed record
    index:integer;
    Name:string[60];
    born_date:string[8];
    spec:string[60];
    high_ed:string[3];
    want_position:string[60];
    min_salary:integer;
end;
TlVac = ^TRVac;
TRVac =  record
   vac:TVac;
   address:TlVac;
end;
TLCand= ^TRCand;
 TRCand =  record
   cand:TCand;
   address:TLCand;
end;
TFVac = file of Tvac;
TFCand = file of TCand;
var headvac:TLVac;
headcand,candidates:TLCand;
check:integer;
temp:string;
n:integer;
vacanciesindexcounter,candidatesindexcounter:integer;
Procedure DisposeSpFirm(var head:TLVAC);
var temp:TLVac;
begin
temp:=head;
    while head<>nil do
    begin
        temp:=temp^.address;
        dispose(head);
        head:=temp;
    end;
end;
procedure IsHighEd(var s:string);
begin
s:=trim(s);
  if (ansilowercase(s)<>'да') and (ansilowercase(s)<>'нет') then
  begin
     repeat
       writeln('Введено некорректное значение,введите ДА или НЕТ') ;
       readln(s);
     until (ansilowercase(s)='да') or (ansilowercase(s)='нет') ;
  end;
end;
procedure DisposeSpCand(var head:TLCand);
var temp:TLcand;
begin
temp:=head;
    while head<>nil do
    begin
        temp:=temp^.address;
        dispose(head);
        head:=temp;
    end;
end;

procedure IsNumber(var temp:string);
var i:integer;
begin
repeat
if temp<>'' then
begin
  i:=1;
  while (i<=length(temp)) and (pos(temp[i],'0123456789')<>0)  do
  begin
    inc(i);
  end;
  if i<=length(temp) then
  begin
    repeat
        writeln('Введите числовое значение');
        readln(temp);
        i:=1;
        while (i<=length(temp)) and (pos(temp[i],'0123456789')<>0)  do
        begin
           inc(i);
        end;
    until i>length(temp) ;
  end;
end
else
begin
  writeln('Введите числовое значение');
  readln(temp);
  i:=0;
end;
until (temp<>'') and (i<>0);
end;
procedure IsCheck(n:integer;var check:string);
begin
isnumber(check);
if strtoint(check)>n  then
begin
    repeat
      writeln('Введите одно из чисел на экране');
      readln(check);
      isnumber(check);
    until strtoint(check)<=n;
end;
end;
function IsData(s:shortstring):boolean;
var i:integer;
bufs:shortstring;
bufint:integer;
begin
result:=true;
bufs:='0123456789';
  i:=1;
  if length(s)<8 then
      result:=false
  else
  begin
    while (i<=length(s)) and (pos(s[i],bufs)<>0) do
      inc(i);
    if i<=length(s) then
      result:=false
    else
    begin
      bufs:=s[1]+s[2];
      bufint:=strtoint(string(bufs));
      if bufint>31 then
          result:=false
      else
      begin
          bufs:=s[3]+s[4];
          bufint:=strtoint(string(bufs));
          if bufint>12 then
          result:=false
      end;
    end;
  end;
end;
function GetLastName(s:shortstring):shortstring;
var i:integer;
begin
   i:=1;
   while s[1]=' ' do
   delete(s,1,1);
   while (i<=length(s)) and (s[i]<>' ') do
   begin
     inc(i);
   end;
   result:=copy(s,1,i-1);
end;
procedure new_cand_elem(var head:TLCand;var n:integer);
var last_adr:TLCand;
bufs:string;
begin
 last_adr:=head;
  while last_adr^.address<>nil do
  last_adr:=last_adr^.address;

  new(last_adr^.address);
  Last_adr:=last_adr^.address;
  last_adr^.address:=nil;
  last_adr^.cand.index:=n;
  inc(n);
  writeln('Заполните данными список');
  writeln('Введите ФИО');
  readln(last_adr^.cand.name);
  writeln('Введите дату рождения формата(ddmmyyyy)');
  readln(last_adr^.cand.born_date);
  if isdata(last_adr^.cand.born_date)=false then
  begin
    repeat
       writeln('Дата введена некорректно,введите ее в формате ddmmyyyy') ;
       readln(last_adr^.cand.born_date);
    until isdata(last_adr^.cand.born_date)=true;
  end;
  writeln('Введите специальность');
  readln(last_adr^.cand.spec);
  writeln('Введите наличие высшего образования(да/Нет)');
  readln(bufs);
  ishighed(bufs);
  last_adr^.cand.high_ed:=shortstring(bufs);
  writeln('Введите желаемую должность');
  readln(last_adr^.cand.want_position);
  writeln('Введите минимальный оклад');
  readln(bufs);
  IsNumber(bufs);
  last_adr^.cand.min_salary:=strtoint(bufs);
end;
procedure new_vac_elem(var head:TLVac;var n:integer);
var last_adr:TLVac;
bufs:string;
begin
 last_adr:=head;
  while last_adr^.address<>nil do
  last_adr:=last_adr^.address;

  new(last_adr^.address);
  Last_adr:=last_adr^.address;
  last_adr^.address:=nil;
  last_adr^.vac.index:=n;
  inc(n);
  writeln('Введите название фирмы');
  readln(last_adr^.vac.firm);
  writeln('Введите специальность');
  readln(last_adr^.vac.spec);
  writeln('Введите должность');
  readln(last_adr^.vac.position);
  writeln('Введите оклад');
  readln(bufs);
  IsNumber(bufs);
  last_adr^.vac.salary:=strtoint(bufs);
  writeln('Введите количество дней отпуска');
  readln(bufs);
  IsNumber(bufs);
  last_adr^.vac.vacation:=strtoint(bufs);
  writeln('Введите наличие высшего образования(да/нет)');
  readln(bufs);
  ishighed(bufs);
  last_adr^.vac.high_ed:=shortstring(bufs);
  writeln('Введите минимальный возраст');
  readln(bufs);
  IsNumber(bufs);
  last_adr^.vac.min_age:=strtoint(bufs);
  writeln('Введите максимальный возраст');
  readln(bufs);
  IsNumber(bufs);
  last_adr^.vac.max_age:=strtoint(bufs);

end;
procedure sortbylastname(h:TLCand);
var i,k,pred,ending:TLCand;
index2:string[60];
  begin
     ending:=h^.address;
    if ending<>nil then
    begin
     k:=h^.address^.address;
    while (k<>nil) do
    begin
      pred:=h;
      i:=h^.address;
      index2:=GetLastName(k^.cand.name);
      while (i<>k) and (GetLastName(i^.cand.name)<index2)  do
       begin
         pred:=pred^.address;
         i:=i^.address;
       end;
       if i<>k then
       begin
          ending^.address:=k^.address;;
          pred^.address:=k;
          k^.address:=i;
       end
       else
          if (i<>k) and (k^.address=nil) then
          begin
              ending^.address:=nil;
              pred^.address:=k;
              k^.address:=i;
          end
       else
       ending:=k;
       k:=ending^.address;
    end;
  end;
end;
procedure sortvacancysalary(h:TLVac);
var i,k,pred,ending:TLVac;
index2:integer;
  begin
  ending:=h^.address;
    if ending<>nil then
    begin
     k:=h^.address^.address;
    while (k<>nil) do
    begin
      pred:=h;
      i:=h^.address;
      index2:=k^.vac.salary;
      while (i<>k) and (i^.vac.salary>index2)  do
       begin
         pred:=pred^.address;
         i:=i^.address;
       end;
       if i<>k then
       begin
          ending^.address:=k^.address;;
          pred^.address:=k;
          k^.address:=i;
       end
       else
          if (i<>k) and (k^.address=nil) then
          begin
              ending^.address:=nil;
              pred^.address:=k;
              k^.address:=i;
          end
       else
       ending:=k;
       k:=ending^.address;
    end;
  end;
end;
procedure print_sp_vacancy(h:TLVac);
var t:TLVac;
begin
  t:=h^.address;
  if t=nil then
    writeln('Список пуст') ;

   while t<>nil do
   begin
      writeln(t^.vac.index,' ',t^.vac.firm,' ',t^.vac.spec,' ',t^.vac.position,' ',t^.vac.salary,' ',t^.vac.vacation,' ',t^.vac.high_ed,' ',t^.vac.min_age,' ',t^.vac.max_age);
      t:=t^.address;
   end;
end;
procedure print_sp_cand(h:TLCand);
var t:TLCand;
begin
    t:=h^.address;
    if t=nil then
      writeln('Список пуст');
    while t<>nil do
    begin
        writeln(t^.cand.name,' ',t^.cand.born_date,' ',t^.cand.spec,' ',t^.cand.high_ed,' ',t^.cand.want_position,' ',t^.cand.min_salary);
        t:=t^.address;
    end;
end;
function  searchvacancy(t:TLVac;name:string;k:integer;p:boolean):TLvac;
var pred:TLVac;
begin
 pred:=t;
 t:=t^.address;
 name:=ansilowercase(name);
  case k of
   0:
   begin
       while (t<>nil) and (t^.vac.index<>strtoint(name)) do
       begin
         pred:=t;
         t:=t^.address;
       end;
       result:=t;
   end;
   1:
    begin
      while (t<>nil) and (ansilowercase(string(t^.vac.firm))<>name) do
       begin
         pred:=t;
         t:=t^.address;
       end;
        result:=t;
     end;
   2:
   begin
      while (t<>nil) and (ansilowercase(string(t^.vac.spec))<>name) do
       begin
         pred:=t;
         t:=t^.address;
       end;
        result:=t;
     end;

   3:
    begin
      while (t<>nil) and (ansilowercase(string(t^.vac.position))<>name) do
       begin
         pred:=t;
         t:=t^.address;
       end;
        result:=t;
     end;
   4:
    begin
      while (t<>nil) and (t^.vac.salary<>strtoint(name)) do
       begin
         pred:=t;
         t:=t^.address;
       end;
        result:=t;
     end;
   5:
    begin
      while (t<>nil) and (t^.vac.vacation<>strtoint(name)) do
       begin
         pred:=t;
         t:=t^.address;
       end;
        result:=t;
     end;
   6:
     begin
      while (t<>nil) and (ansilowercase(string(t^.vac.high_ed))<>name) do
       begin
         pred:=t;
         t:=t^.address;
       end;
        result:=t;
     end;
   7:
     begin
      while (t<>nil) and (t^.vac.min_age<>strtoint(name)) do
       begin
         pred:=t;
         t:=t^.address;
       end;
        result:=t;
     end;
   8:
      begin
      while (t<>nil) and (t^.vac.max_age<>strtoint(name)) do
       begin
         pred:=t;
         t:=t^.address;
       end;
        result:=t;
     end
     else
     result:=nil;
 end;
 if p=true then
 begin
   result:=pred;
 end;
end;
procedure IndexVacCheck(var temp:string;name:string;n:integer;head:Tlvac;var t:TLVac;p:boolean);
var correctflag:boolean;
begin
correctflag:=false;
repeat
 if (t=nil) or (t^.address=nil )then
 begin
 writeln('Введен неверный индекс');
 readln(temp);
 IsNumber(temp);
 t:=searchvacancy(head,temp,0,p);
 end
 else
 begin
   case n of
      0:correctflag:=true;
      1:
      begin
      if ((p=true) and (ansilowercase(string(t^.address.vac.firm))<>name)) or (((p=false) and (ansilowercase(string(t^.vac.firm))<>name))) then
       begin
         writeln('Введен неверный индекс');
         readln(temp);
         IsNumber(temp);
         t:=searchvacancy(head,temp,0,p);
       end
       else
       correctflag:=true;
      end;
      2:
      begin
      if ((p=true) and (ansilowercase(string(t^.address.vac.spec))<>name)) or (((p=false) and (ansilowercase(string(t^.vac.spec))<>name))) then
       begin
         writeln('Введен неверный индекс');
         readln(temp);
         IsNumber(temp);

         t:=searchvacancy(head,temp,0,p);
       end
       else
       correctflag:=true;
      end;

      3:
      begin
      if ((p=true) and (ansilowercase(string(t^.address.vac.position))<>name)) or (((p=false) and (ansilowercase(string(t^.vac.position))<>name))) then
       begin
         writeln('Введен неверный индекс');
         readln(temp);
         IsNumber(temp);

         t:=searchvacancy(head,temp,0,p);
       end
       else
       correctflag:=true;
      end;
      4:
       begin
         if ((p=true) and (t^.address.vac.salary<>strtoint(name))) or ((p=false) and (t^.vac.salary<>strtoint(name))) then
       begin
         writeln('Введен неверный индекс');
         readln(temp);
         IsNumber(temp);

         t:=searchvacancy(head,temp,0,p);
       end
       else
       correctflag:=true;
       end;
      5:
       begin
         if ((p=true) and (t^.address.vac.vacation<>strtoint(name))) or ((p=false) and (t^.vac.vacation<>strtoint(name))) then
       begin
         writeln('Введен неверный индекс');
         readln(temp);
         IsNumber(temp);

         t:=searchvacancy(head,temp,0,p);
       end
       else
       correctflag:=true;
       end;
      6:
      begin
             if ((p=true) and (ansilowercase(string(t^.address.vac.high_ed))<>name)) or (((p=false) and (ansilowercase(string(t^.vac.high_ed))<>name))) then
       begin
         writeln('Введен неверный индекс');
         readln(temp);
         IsNumber(temp);

         t:=searchvacancy(head,temp,0,p);
       end
       else
       correctflag:=true;
      end;
      7:
      begin
            if ((p=true) and (t^.address.vac.min_age<>strtoint(name))) or ((p=false) and (t^.vac.min_age<>strtoint(name))) then
       begin
         writeln('Введен неверный индекс');
         readln(temp);
         IsNumber(temp);

         t:=searchvacancy(head,temp,0,p);
       end
       else
       correctflag:=true;
      end;
      8:
      begin
             if ((p=true) and (t^.address.vac.max_age<>strtoint(name))) or ((p=false) and (t^.vac.max_age<>strtoint(name))) then
       begin
         writeln('Введен неверный индекс');
         readln(temp);
         IsNumber(temp);

         t:=searchvacancy(head,temp,0,p);
       end
       else
       correctflag:=true;
      end;
   end;
 end;
until correctflag=true;
end;

procedure FilterSearchVacancy(n:integer;head:TLVac);
var res:TLVac;
counter:integer;
temp:string;
p:boolean;
begin
p:=false;
counter:=0;
readln(temp);
res:=searchvacancy(head,temp,n,p);
while (res<>nil) do
  begin
    inc(counter);
    if counter=1 then
    writeln('Подходящие:');
    writeln(res^.vac.firm,' ',res^.vac.spec,' ',res^.vac.position,' ',res^.vac.salary,' ',res^.vac.vacation,' ',res^.vac.high_ed,' ',res^.vac.min_age,' ',res^.vac.max_age);
    res:=searchvacancy(res,temp,n,p);
  end;
if counter=0 then
writeln('Подходящих не найдено');
end;
function  searchcand(t:TLCand;name:string;k:integer;p:boolean):Tlcand;
var pred:TLCand;
begin
 pred:=t;
 t:=t^.address;
 name:=ansilowercase(name);
  case k of
   0:
   begin
      while (t<>nil) and (t^.cand.index<>strtoint(name)) do
       begin
          pred:=t;
         t:=t^.address;
       end;
        result:=t;
   end;
   1:
    begin
      while (t<>nil) and (ansilowercase(string(t^.cand.name))<>name) do
       begin
          pred:=t;
         t:=t^.address;
       end;
        result:=t;
     end;
   2:
   begin
      while (t<>nil) and (ansilowercase(string(t^.cand.born_date))<>name) do
       begin
         pred:=t;
         t:=t^.address;
       end;
        result:=t;
     end;

   3:
    begin
      while (t<>nil) and (ansilowercase(string(t^.cand.spec))<>name) do
       begin
         pred:=t;
         t:=t^.address;
       end;
        result:=t;
     end;
   4:
    begin
      while (t<>nil) and (ansilowercase(string(t^.cand.high_ed))<>name) do
       begin
         pred:=t;
         t:=t^.address;
       end;
        result:=t;
     end;
   5:
    begin
      while (t<>nil) and (ansilowercase(string(t^.cand.want_position))<>name) do
       begin
         pred:=t;
         t:=t^.address;
       end;
        result:=t;
     end;
   6:
     begin
      while (t<>nil) and (t^.cand.min_salary<>strtoint(name)) do
       begin
         pred:=t;
         t:=t^.address;
       end;
        result:=t;
     end
     else
     result:=nil;
 end;
 if p=true then
 result:=pred;
end;
procedure IndexCandCheck(var temp:string;name:string;n:integer;head:Tlcand;var t:TLcand;p:boolean);
var correctflag:boolean;
begin
correctflag:=false;
repeat
 if (t=nil) or (t^.address=nil )then
 begin
 writeln('Введен неверный индекс');
 readln(temp);
 IsNumber(temp);
 t:=searchcand(head,temp,0,p);
 end
 else
 begin
   case n of
      0:correctflag:=true;
      1:
      begin
      if ((p=true) and (ansilowercase(string(t^.address.cand.name))<>name)) or (((p=false) and (ansilowercase(string(t^.cand.name))<>name))) then
       begin
         writeln('Введен неверный индекс');
         readln(temp);
         IsNumber(temp);
         t:=searchcand(head,temp,0,p);
       end
       else
       correctflag:=true;
      end;
      2:
      begin
      if ((p=true) and (t^.address.cand.born_date<>name)) or (((p=false) and ((t^.cand.born_date)<>name))) then
       begin
         writeln('Введен неверный индекс');
         readln(temp);
         IsNumber(temp);
         t:=searchcand(head,temp,0,p);
       end
       else
       correctflag:=true;
      end;

      3:
      begin
      if ((p=true) and (ansilowercase(string(t^.address.cand.spec))<>name)) or (((p=false) and (ansilowercase(string(t^.cand.spec))<>name))) then
       begin
         writeln('Введен неверный индекс');
         readln(temp);
         IsNumber(temp);
         t:=searchcand(head,temp,0,p);
       end
       else
       correctflag:=true;
      end;
      4:
       begin
        if ((p=true) and (ansilowercase(string(t^.address.cand.high_ed))<>name)) or (((p=false) and (ansilowercase(string(t^.cand.high_ed))<>name))) then
       begin
         writeln('Введен неверный индекс');
         readln(temp);
         IsNumber(temp);
         t:=searchcand(head,temp,0,p);
       end
       else
       correctflag:=true;
       end;
      5:
       begin
         if ((p=true) and (ansilowercase(string(t^.address.cand.want_position))<>name)) or (((p=false) and (ansilowercase(string(t^.cand.want_position))<>name))) then
       begin
         writeln('Введен неверный индекс');
         readln(temp);
         IsNumber(temp);
         t:=searchcand(head,temp,0,p);
       end
       else
       correctflag:=true;
       end;
      6:
      begin
            if ((p=true) and (t^.address.cand.min_salary<>strtoint(name))) or ((p=false) and (t^.cand.min_salary<>strtoint(name))) then
       begin
         writeln('Введен неверный индекс');
         readln(temp);
         IsNumber(temp);
         t:=searchcand(head,temp,0,p);
       end
       else
       correctflag:=true;
      end
   end;
 end;
until correctflag=true;
end;
procedure filtersearchcand(n:integer;head:TLCand);
var res:TLCand;
counter:integer;
temp:string;
p:boolean;
begin
 p:=false;
counter:=0;
readln(temp);
res:=searchcand(head,temp,n,p);
while (res<>nil) do
  begin
    inc(counter);
    if counter=1 then
    writeln('Подходящие:');
    writeln(res^.cand.name,' ',res^.cand.born_date,' ',res^.cand.spec,' ',res^.cand.high_ed,' ',res^.cand.want_position,' ',res^.cand.min_salary);
    res:=searchcand(head,temp,n,p);
  end;
if counter=0 then
writeln('Подходящий не найдено');
end;
procedure editvac(head:TLVac;n:integer);
var check:string;
counter:integer;
temp:TLVac;
firstfind:TLVac;
temp2:string;
p:boolean ;
begin
p:=false;
  firstfind:=nil;
  counter:=0;
  writeln('Введите значение этого поля');
  readln(check);
  temp:=head;
  while  temp<>nil do
  begin
     temp:=searchvacancy(temp,check,n,p);
     if temp<>nil then
     begin
       inc(counter);
       firstfind:=temp;
       writeln(temp^.vac.index,' ',temp^.vac.firm,' ',temp^.vac.spec,' ',temp^.vac.position,' ',temp^.vac.salary,' ',temp^.vac.vacation,' ',temp^.vac.high_ed,' ',temp^.vac.min_age,' ',temp^.vac.max_age);
     end;
  end;
  if counter=0 then
  writeln('Элемент не найден');
  if counter>=1 then
  begin
    if counter>1 then
    begin
      writeln('Введите индекс записи');
      readln(temp2);
      IsNumber(temp2);
      firstfind:=searchvacancy(head,temp2,0,p);
    end;
    indexvaccheck(temp2,check,n,head,firstfind,p);
    writeln('Введите поле для редактирования');
    writeln('1.Название фирмы');
    writeln('2.Специальность');
    writeln('3.Должность');
    writeln('4.Оклад');
    writeln('5.Количество дней отпуска');
    writeln('6.Наличие высшего образования');
    writeln('7.Минимальный возраст');
    writeln('8.Максимальный возраст');
    readln(temp2);
    IsCheck(8,temp2);
    n:=strtoint(temp2);
    case n of
         1:
         begin
            writeln('Введите отредактированное поле');
            readln(firstfind^.vac.firm);
         end;
         2:
         begin
            writeln('Введите отредактированное поле');
            readln(firstfind^.vac.spec);
         end;
         3:
         begin
            writeln('Введите отредактированное поле');
            readln(firstfind^.vac.position);
         end;
         4:
         begin
            writeln('Введите отредактированное поле');
            readln(check);
            IsNumber(check);
            firstfind^.vac.salary:=strtoint(check);
         end;
         5:
         begin
            writeln('Введите отредактированное поле');
            readln(check);
            IsNumber(check);
            firstfind^.vac.vacation:=strtoint(check);
         end;
         6:
         begin
            writeln('Введите отредактированное поле');
             readln(check);
             ishighed(check);
             firstfind^.vac.high_ed:=shortstring(check);
         end;
         7:
         begin
            writeln('Введите отредактированное поле');
            readln(check);
            IsNumber(check);
            firstfind^.vac.min_age:=strtoint(check);
         end;
         8:
         begin
            writeln('Введите отредактированное поле');
            readln(check);
            IsNumber(check);
            firstfind^.vac.max_age:=strtoint(check);
         end;
    end;
   end;
end;
procedure editcand(head:TLCand;n:integer);
var check:string;
counter:integer;
temp:TLCand;
firstfind:TLCand;
temp2:string;
p:boolean;
begin
p:=false;
  firstfind:=nil;
  counter:=0;
  writeln('Введите значение этого поля');
  readln(check);
  temp:=head;
  while  temp<>nil do
  begin
     temp:=searchcand(temp,check,n,p);
     if temp<>nil then
     begin
       inc(counter);
       firstfind:=temp;
       writeln(temp^.cand.index,' ',temp^.cand.name,' ',temp^.cand.born_date,' ',temp^.cand.spec,' ',temp^.cand.high_ed,' ',temp^.cand.want_position,' ',temp^.cand.min_salary);
     end;
  end;
  if counter=0 then
    writeln('Элемент не найден');
  if counter>=1 then
  begin
    if counter>1 then
    begin
      writeln('Введите индекс записи');
      readln(temp2);
      isnumber(temp2);
      firstfind:=searchcand(head,temp2,0,p);
    end;
    indexcandcheck(temp2,check,n,head,firstfind,p);
    writeln('Введите поле для редактирования');
    writeln('1.ФИО');
    writeln('2.Дата рождения');
    writeln('3.Специальность');
    writeln('4.Наличие высшего образования');
    writeln('5.Желаемая должность');
    writeln('6.Минимальный оклад');
    readln(temp2);
    IsCheck(6,temp2);
    n:=strtoint(temp2);
    case n of
        1:
        begin
            writeln('Введите отредактированное поле');
            readln(firstfind^.cand.name);
        end;
        2:
        begin
           writeln('Введите отредактированное поле');
           readln(check);
           firstfind^.cand.born_date:=shortstring(check);
           if isdata(firstfind^.cand.born_date)=false then
           begin
              repeat
                  writeln('Дата введена некорректно,введите ее в формате ddmmyyyy') ;
                  readln(firstfind^.cand.born_date);
              until isdata(firstfind^.cand.born_date)=true;
           end;
        end;
        3:
        begin
            writeln('Введите отредактированное поле');
            readln(firstfind^.cand.spec);
        end;
        4:
        begin
            writeln('Введите отредактированное поле');
            readln(check);
            ishighed(check);
            firstfind^.cand.high_ed:=shortstring(check);
        end;
        5:
        begin
           writeln('Введите отредактированное поле');
           readln(firstfind^.cand.want_position);
        end;
        6:
        begin
            writeln('Введите отредактированное поле');
            readln(check);
            IsNumber(check);
            firstfind^.cand.min_salary:=strtoint(check);
        end;
    end;
   end;
end;
procedure savewith(head:TLVac;name:string;head2:TLCand;name2:string);
var firmfile:TFVac;
candfile:TFCand;
t:TLVac;
t2:TLCand;
begin
    t:=head^.address;
    assign(firmfile,name);
    reset(firmfile);
    rewrite(firmfile);
    while t<>nil do
    begin
      write(firmfile,t^.vac);
      t:=t^.address;
    end;
    close(firmfile);
    t2:=head2^.address;
    assign(candfile,name2);
    reset(candfile);
    rewrite(candfile);
    while t2<>nil do
    begin
      write(candfile,t2^.cand);
      t2:=t2^.address;
    end;
    close(candfile);
end;
procedure readfromf(var head:TLVac;name:string;var n:integer);
var fir:TFVac;
t:TLVac;
begin
n:=0;
     new(head);
     t:=head;
     assign(Fir,name);
     reset(fir);
     while not eof(fir) do
     begin
       new(t^.address);
       t:=t^.address;
       read(fir,t^.vac);
       if t^.vac.index>n then
          n:=t^.vac.index;
     end;
     inc(n);
     t^.address:=nil;
     close(fir);
end;
procedure readfromf_c(var head:TLCand;name:string;var n:integer);
var CandF:TFCand;
t:TLCand;
begin
     new(head);
     t:=head;
     assign(CAndf,name);
     reset(candf);
     N:=0;
     while not eof(candf) do
     begin
       new(t^.address);
       t:=t^.address;
       read(candf,t^.cand);
       if t^.cand.index>n then
       n:=t^.cand.index
     end;
     inc(n);
     t^.address:=nil;
     close(candf);
end;
procedure deletevacancy(head:TLVac;n:integer;name:string);
var t,pred,temps:TLVac;
counter:integer;
temp:string;
p:boolean;
begin
p:=true;
      temps:=nil;
      counter:=0;
      t:=head;                          //в t адрес элемента,который находится перед удаляемым
      writeln('Подходящие записи:');
      t:=searchvacancy(t,name,n,p);
      while t^.address<>nil do
      begin
          inc(counter);
          temps:=t;
          writeln(t^.address^.vac.index,' ',t^.address^.vac.firm,' ',t^.address^.vac.spec,' ',t^.address^.vac.position,' ',t^.address^.vac.salary,' ',t^.address^.vac.vacation,' ',t^.address^.vac.high_ed,' ',t^.address^.vac.min_age,' ',t^.address^.vac.max_age);
          t:=t^.address;
          t:=searchvacancy(t,name,n,p);
      end;
    if counter=1 then
    begin
      if temps^.address^.address=nil then
      begin
        dispose(temps^.address);
        temps^.address:=nil;
      end
      else
      begin
        pred:=temps;
        temps:=temps^.address;
        pred^.address:=temps^.address;
        dispose(temps);
      end;
    end;
    if  ((t=nil) or (t^.address=nil))  and (counter=0)  then
    begin
      writeln('Элемент не найден');
    end;
    if counter>1 then
    begin
      writeln('Введите индекс записи');
      readln(temp);
      IsNumber(temp);

      temps:=searchvacancy(head,temp,0,p);
      indexvaccheck(temp,name,n,head,temps,p);
      if temps^.address^.address=nil then
      begin
        dispose(temps^.address);
        temps^.address:=nil;
      end
      else
      begin
        pred:=temps;
        temps:=temps^.address;
        pred^.address:=temps^.address;
        dispose(temps);
      end;
    end;
end;
procedure deletecandidate(head:TLCand;n:integer;name:string);
var t,pred,temps:TLCand;
counter:integer;
temp:string;
p:boolean;
begin
      p:=true;
      temps:=nil;
      counter:=0;
      t:=head;
      writeln('Подходящие записи:');
       t:=searchcand(t,name,n,p);
      while t^.address<>nil do
      begin
          temps:=t;
          inc(counter);
          writeln(t^.address^.cand.index,' ',t^.address^.cand.name,' ',t^.address^.cand.born_date,' ',t^.address^.cand.spec,' ',t^.address^.cand.high_ed,' ',t^.address^.cand.want_position,' ',t^.address^.cand.min_salary);
          t:=t^.address;
          t:=searchcand(t,name,n,p);
      end;
    if counter=1 then
    begin
     begin
      if temps^.address^.address=nil then
      begin
        dispose(temps^.address);
        temps^.address:=nil;
      end
      else
      begin
        pred:=temps;
        temps:=temps^.address;
        pred^.address:=temps^.address;
        dispose(temps);
      end;
    end;
    end;
    if  ((t=nil) or (t^.address=nil))  and (counter=0)  then
    begin
      writeln('Элемент не найден');
    end;
    if counter>1 then
    begin
      writeln('Введите индекс записи');
      readln(temp);
      IsNumber(temp);

      t:=searchcand(head,temp,0,p);
      indexcandcheck(temp,name,n,head,t,p);
      if t^.address^.address=nil then
        begin
        dispose(t^.address);
        t^.address:=nil;
       end
       else
       begin
        pred:=t;
        t:=t^.address;
        pred^.address:=t^.address;
        dispose(t);
       end;
      end;
end;
procedure JobSearch(Cand:TLCand;head:TLVac);
var age,counter:integer;
temp:string;
i,code:integer;
textf:text;
temp1:TLVac;
begin
assign(textf,'JobSearch.txt');
rewrite(textf);
if cand=nil then
begin
writeln('Список кандидатов  пуст');
writeln(textf,'Список кандидатов  пуст');
end
else
begin
temp1:=head;
while cand<>nil do
begin
    head:=temp1^.address;
    counter:=0;
    temp:=copy(string(cand^.cand.born_date),5,4);
    val(temp,i,code);
    age:=2022-i;
    while head<>nil do
    begin
      if (age>head^.vac.min_age) and (age<head^.vac.max_age) and (lowercase(string(cand^.cand.high_ed))=lowercase(string(head^.vac.high_ed))) and (cand^.cand.min_salary<=head^.vac.salary) and (lowercase(string(cand^.cand.spec))=lowercase(string(head^.vac.spec))) and (lowercase(string(cand^.cand.want_position))=lowercase(string(head^.vac.position))) then
      begin
          inc(counter);
          if counter=1 then
          begin
              writeln(textf,'Подходящие вакансии для  ',cand^.cand.name,':');
              writeln('Подходящие вакансии для  ',cand^.cand.name,':');
              writeln('|ID |                      Фирма                       |                  Специальность                   |                     Должность                    |Зарплата|Дни Отпуска|ВО|МИН|МАКС|');
              writeln(textf,'|ID |                      Фирма                       |                  Специальность                   |                     Должность                    |Зарплата|Дни Отпуска|ВО|МИН|МАКС|');
          end;
          writeln(textf,'|',head^.vac.index:3,'|',head^.vac.firm:50,'|',head^.vac.spec:50,'|',head^.vac.position:50,'|',head^.vac.salary:8,'|',head^.vac.vacation:12,'|',head^.vac.high_ed:2,'|',head^.vac.min_age:3,'|',head^.vac.max_age:3);
          writeln('|',head^.vac.index:3,'|',head^.vac.firm:50,'|',head^.vac.spec:50,'|',head^.vac.position:50,'|',head^.vac.salary:8,'|',head^.vac.vacation:12,'|',head^.vac.high_ed:2,'|',head^.vac.min_age:3,'|',head^.vac.max_age:3);
      end;
      head:=head^.address;
    end;
    if counter=0 then
    begin
        writeln(textf,'Подходящих для ',cand^.cand.name,' вакансий не найдено');
        writeln('Подходящих для ',cand^.cand.name,' вакансий не найдено');
    end;
    writeln;
    cand:=cand^.address;
end;
end;
close(textf);
end;
procedure NotEnoughVacanciesVerseSecond(headf:tlvac;headc:tlcand);
var countercand,countervac:integer;
vac:tlvac;
cand:tlcand;
age,i,code:integer;
temp:string;
textf:textfile;
counter:integer;
begin
assign(textf,'NotEnoughVacancies.txt');
rewrite(textf);
vac:=headf^.address;
counter:=0;
while vac<>nil do
  begin
  countercand:=0;
  cand:=headc^.address;
  while cand<>nil do
  begin
      temp:=copy(string(cand^.cand.born_date),5,4);
      val(temp,i,code);
      age:=2022-i;
        if (age>vac^.vac.min_age) and (age<vac^.vac.max_age) and (lowercase(string(cand^.cand.high_ed))=lowercase(string(vac^.vac.high_ed))) and (cand^.cand.min_salary<=vac^.vac.salary) and (lowercase(string(cand^.cand.spec))=lowercase(string(vac^.vac.spec))) and (lowercase(string(cand^.cand.want_position))=lowercase(string(vac^.vac.position))) then
      begin
       inc(countercand);
      end;
    cand:=cand^.address;
  end;
  if countercand>1 then
  begin
   inc(counter);
   if counter=1 then
   begin
      writeln('Список дефицитных вакансий:');
      writeln(textf,'Список дефицитных вакансий:');
      writeln('|ID |                      Фирма                       |                  Специальность                   |                     Должность                    |Зарплата|Дни Отпуска|ВО|МИН|МАКС|');
      writeln(textf,'|ID |                      Фирма                       |                  Специальность                   |                     Должность                    |Зарплата|Дни Отпуска|ВО|МИН|МАКС|');
   end;
   writeln(textf,'|',vac^.vac.index:3,'|',vac^.vac.firm:50,'|',vac^.vac.spec:50,'|',vac^.vac.position:50,'|',vac^.vac.salary:8,'|',vac^.vac.vacation:12,'|',vac^.vac.high_ed:2,'|',vac^.vac.min_age:3,'|',vac^.vac.max_age:3);
   writeln('|',vac^.vac.index:3,'|',vac^.vac.firm:50,'|',vac^.vac.spec:50,'|',vac^.vac.position:50,'|',vac^.vac.salary:8,'|',vac^.vac.vacation:12,'|',vac^.vac.high_ed:2,'|',vac^.vac.min_age:3,'|',vac^.vac.max_age:3);
  end;
  vac:=vac^.address;
end;
close(textf);
end;
Procedure NotEnoughVacancies(headf:TLVac;headc:TLCand);
type SPos = ^pos;
   pos = record
   posname:string[60];
   next:spos;
 end;
var counterVac,counterCand:integer;
heads,temp,last:spos;
cand,loopcand:TLCand;
firm:TLVac;
meetfl:boolean;
resultf:textfile;
procedure DisposeSpPos(var head:Spos);
var temp:spos;
begin
temp:=head;
    while head<>nil do
    begin
        temp:=temp^.next;
        dispose(head);
        head:=temp;
    end;
end;
begin
assign(resultf,'NotEnoughVacancies.txt');
rewrite(resultf);
  if headc^.address=nil then
  begin
    writeln('Список кандиадатов пуст.Дефицитных вакансий нет');
    writeln(resultf,'Список кандиадатов пуст.Дефицитных вакансий нет');
  end
  else
  begin
      writeln('Дефицитные вакансии:');
      writeln(resultf,'Дефицитные вакансии:');
      new(heads);
      heads^.next:=nil;
      last:=heads;
      cand:=headc^.address;
      while cand<>nil do
      begin
          meetfl:=false;
          temp:=heads;
          while (temp^.next<>nil) and (meetfl=false) do
          begin
              temp:=temp^.next;
              if temp^.posname=cand^.cand.want_position then
                  meetfl:=true;
          end;
          if  meetfl=false then
          begin
              countervac:=0;
              countercand:=0;
              new(last^.next);
              last:=last^.next;
              last^.next:=nil;
              last^.posname:=cand^.cand.want_position;
              firm:=headf^.address;
              while firm<>nil do
              begin
                  if lowercase(string(firm^.vac.position))=lowercase(string(last^.posname)) then
                      inc(countervac);
                  firm:=firm^.address;
              end;
              loopcand:=headc^.address;
              while loopcand<>nil do
              begin
                  if lowercase(string(loopcand^.cand.want_position)) = lowercase(string(last^.posname)) then
                      inc(countercand);
                  loopcand:=loopcand^.address;
              end;
              if countercand>countervac then
              begin
                write(last^.posname,', ');
                write(resultf,last^.posname,',');
              end;
          end;
          cand:=cand^.address;
      end;
      DisposeSpPos(heads);
  end;
  close(resultf);
end;
begin
new(headvac);
headvac.address:=nil;
new(headcand);
headcand.address:=nil;
  repeat
    writeln('--------->Главное_Меню<---------');
    writeln('1.Чтение данных из файла');
    writeln('2.Вывести список');
    writeln('3.Сортировка списков');
    writeln('4.Поиск по фильтрам');
    writeln('5.Добавить элемент в список');
    writeln('6.Удалить элемент из списка');
    writeln('7.Редактировать список');
    writeln('8.Вывести дефицитные вакансии');
    writeln('9.Подобрать вакансии');
    writeln('10.Завершить работу без сохранения изменений');
    writeln('11.Завершить работу с сохранением изменений');
    readln(temp);
    ischeck(11,temp);
    check:=strtoint(temp);
   case check of
      1:
      begin
          disposespfirm(headvac);
          disposespcand(headcand);
          new(headvac);
          headvac.address:=nil;
          new(headcand);
          headcand.address:=nil;
          readfromf(headvac,'Firms',vacanciesindexcounter);
          readfromf_c(headcand,'Candidates',candidatesindexcounter);
      end;
      3:
      begin
         writeln('Выберите список');
          writeln('1.Список вакансий');
          writeln('2.Список кандидатов');
          readln(temp);
          IsCheck(2,temp);
          check:=strtoint(temp);
          if check=2 then
          begin
              sortbylastname(headcand);
              print_sp_cand(headcand);
          end
          else
          begin
                sortvacancysalary(headvac);
                print_sp_vacancy(headvac);
          end
      end;
      5:
      begin
          writeln('Выберите список');
          writeln('1.Список вакансий');
          writeln('2.Список кандидатов');
          readln(temp);
          IsCheck(2,temp);
          check:=strtoint(temp);
          case check of
             1:new_vac_elem(headvac,vacanciesindexcounter);
             2:new_cand_elem(headcand,candidatesindexcounter);
          end;
      end;
      6:
      begin
            writeln('Выберите список');
            writeln('1.Список вакансий');
            writeln('2.Список кандидатов');
            readln(temp);
            IsCheck(2,temp);
            check:=strtoint(temp);
            if check=1 then
            begin
              writeln('Выберите поле для поиска удаляемого элемента');
              writeln('0.ID');
              writeln('1.Название фирмы');
              writeln('2.Специальность');
              writeln('3.Должность');
              writeln('4.Оклад');
              writeln('5.Количество дней отпуска');
              writeln('6.Наличие высшего образования');
              writeln('7.Минимальный возраст');
              writeln('8.Максимальный возраст');
              readln(temp);
              IsCheck(8,temp);
              N:=strtoint(temp);
              Writeln('Введите значени этого поля');
              readln(temp);
              deletevacancy(headvac,n,temp);
            end
            else if check=2 then
            begin
              writeln('Выберите поле для поиска удаляемого элемента');
              writeln('0.ID');
              writeln('1.ФИО');
              writeln('2.Дата рождения(ddmmyyyy)');
              writeln('3.Специальность');
              writeln('4.Наличие высшего образования');
              writeln('5.Желаемая должность');
              writeln('6.Минимальный оклад');
              readln(temp);
              IsCheck(6,temp);
              N:=strtoint(temp);
              Writeln('Введите значени этого поля');
              readln(temp);
              deletecandidate(headcand,n,temp);
            end;
      end;
      2:
      begin
           writeln('Выберите список');
           writeln('1.Список вакансий');
           writeln('2.Список кандидатов');
           readln(temp);
           IsCheck(2,temp);
           check:=strtoint(temp);
           case check of
             1: print_sp_vacancy(headvac);
             2:print_sp_cand(headcand);
            end;
      end;
      7:
      begin
            writeln('Выберите список' );
            writeln('1.Список вакансий');
            writeln('2.Список кандидатов');
            readln(temp);
            IsCheck(2,temp);
            check:=strtoint(temp);
            if check=1 then
            begin
              writeln('Выберите поле для поиска редактируемой записи');
              writeln('0.ID');
              writeln('1.Название фирмы');
              writeln('2.Специальность');
              writeln('3.Должность');
              writeln('4.Оклад');
              writeln('5.Количество дней отпуска');
              writeln('6.Наличие высшего образования');
              writeln('7.Минимальный возраст');
              writeln('8.Максимальный возраст');
              readln(temp);
              IsCheck(8,temp);
              N:=strtoint(temp);
              editvac(headvac,n);
            end
            else
            begin
              writeln('Выберите поле для поиска редактируемой записи');
              writeln('0.ID');
              writeln('1.ФИО');
              writeln('2.Дата рождения(ddmmyyyy)');
              writeln('3.Специальность');
              writeln('4.Наличие высшего образования');
              writeln('5.Желаемая должность');
              writeln('6.Минимальный оклад');
              readln(temp);
              IsCheck(6,temp);
              N:=strtoint(temp);
              editcand(headcand,n);
            end;
      end;
      4:
      begin
           writeln('Выберите список' );
           writeln('1.Список вакансий');
           writeln('2.Список кандидатов');
           readln(temp);
           IsCheck(2,temp);
           check:=strtoint(temp);
           if check=1 then
           begin
              writeln('Выберите фильтр');
              writeln('0.ID');
              writeln('1.Название фирмы');
              writeln('2.Название специальности');
              writeln('3.Название должности');
              writeln('4.Размер оклада');
              writeln('5.Количество дней отпуска');
              writeln('6.Наличие высшего образования');
              writeln('7.Минимальный возраст');
              writeln('8.Максимальный возраст');
              readln(temp);
              IsCheck(8,temp);
              N:=strtoint(temp);
              writeln('Введите значение этого поля');
              FilterSearchVacancy(n,headvac);
           end
           else if check=2 then
           begin
             writeln('Выберите фильтр');
             writeln('0.ID');
             writeln('1.ФИО');
             writeln('2.Дата Рождения');
             writeln('3.Специальность');
             writeln('4.Наличие высшего образования');
             writeln('5.Желаемая должность');
             writeln('6.Минимальный оклад');
             readln(temp);
             IsCheck(6,temp);
             N:=strtoint(temp);
             writeln('Введите значение этого поля');
             filtersearchcand(n,headcand);
           end;
      end;
      9:
      begin
            candidates:=headcand^.address;
            JobSearch(candidates,headvac);
      end;
      8:
      begin
            notenoughvacanciesversesecond(headvac,headcand);
            writeln;
      end
      else
      begin
          if check=11 then
          begin
            savewith(headvac,'Firms',headcand,'Candidates');
          end;
          disposespfirm(headvac);
          disposespcand(headcand);
      end;
   end;
  until (check=10) or (check=11);
end.
