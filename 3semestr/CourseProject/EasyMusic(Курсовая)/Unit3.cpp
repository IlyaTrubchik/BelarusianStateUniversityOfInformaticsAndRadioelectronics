//---------------------------------------------------------------------------

#include <vcl.h>
#pragma hdrstop

#include "Unit3.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
TForm3 *Form3;
//---------------------------------------------------------------------------
__fastcall TForm3::TForm3(TComponent* Owner)
	: TForm(Owner)
{
}
//---------------------------------------------------------------------------
void __fastcall TForm3::AddbtnClick(TObject *Sender)
{
	playlistfile->Execute();
	int bracketposition = 0;
	std::string FileName = (AnsiString(playlistfile->FileName).c_str());
	bracketposition = FileName.rfind("\\");
	std::string trackname = FileName.substr(bracketposition+1);
	pltracksbox->AddItem(AnsiString(trackname.c_str()),0);
	addelem(tracks,FileName);
}
 void __fastcall TForm3::addelem(dynlist* head,std::string value)
{
	dynlist* tmp  = new dynlist();
	tmp->value = value;
	while(head->next != NULL)
	{
		head = head->next;
	}
	head->next = tmp;
}
//---------------------------------------------------------------------------

