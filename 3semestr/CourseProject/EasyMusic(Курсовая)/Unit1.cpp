//---------------------------------------------------------------------------

#include <vcl.h>
#pragma hdrstop

#include "Unit1.h"
#include "Unit6.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
TCreatePlaylistUnit *CreatePlaylistUnit;
//---------------------------------------------------------------------------
__fastcall TCreatePlaylistUnit::TCreatePlaylistUnit(TComponent* Owner)
	: TForm(Owner)
{
}
//---------------------------------------------------------------------------
void __fastcall TCreatePlaylistUnit::AddbtnClick(TObject *Sender)
{
	playlistfile->Execute();
	int bracketposition = 0;
	std::string FileName = (AnsiString(playlistfile->FileName).c_str());
	bracketposition = FileName.rfind("\\");
	std::string trackname = FileName.substr(bracketposition+1);
	pltracksbox->AddItem(AnsiString(trackname.c_str()),0);
	addelem(tracks,FileName);
}
//---------------------------------------------------------------------------
void __fastcall TCreatePlaylistUnit::FormCreate(TObject *Sender)
{
	tracks  = new dynlist();
    TBitmap *bmp = new Graphics::TBitmap;
	bmp->LoadFromFile("C:\\Users\\Ilya\\Playlists\\StandartPlaylist\\StandartPlaylist.bmp");
	Image1->Canvas->StretchDraw(Rect(0,0,220,220),bmp);
}
//---------------------------------------------------------------------------
 void __fastcall TCreatePlaylistUnit::addelem(dynlist* head,std::string value)
{
	dynlist* tmp  = new dynlist();
	tmp->value = value;
	while(head->next != NULL)
	{
		head = head->next;
	}
	head->next = tmp;
}
void __fastcall TCreatePlaylistUnit::deleteelem(dynlist* head)
{
	std::string value;
	value  = AnsiString(pltracksbox->Items->Strings[pltracksbox->ItemIndex]).c_str();
	dynlist* tmp = head->next;
	dynlist* prev = head;
	  int bracketposition;
	std::string FileName  = tmp->value;
	bracketposition = FileName.rfind("\\");
	std::string trackname = FileName.substr(bracketposition+1);
	while(tmp != NULL && value != trackname)
	{
		prev = prev->next;
		tmp = tmp->next;
		FileName  = tmp->value;
		bracketposition = FileName.rfind("\\");
		trackname = FileName.substr(bracketposition+1);
	}
	if(trackname == value)
	{
	   prev->next = tmp->next;
       free(tmp);
	}
}
void __fastcall TCreatePlaylistUnit::freelist(dynlist* head)
{
	dynlist* tmp = head;
	while (head != NULL)
	{
		tmp = head->next;
		free(head);
		head = tmp;
    }
}
void __fastcall TCreatePlaylistUnit::CreatePlaylist(){
	if(!DirectoryExists("C:\\Users\\Ilya\\Playlists\\"+PlaylistName->Text)){

	dynlist* tmp  = tracks->next;
	int  i =0;
	if (PlaylistName->Text != "")
	{
			   createplaylistdir(PlaylistName->Text);
			   while (tmp != NULL)
			   {
				copytrack(tmp,i);
				tmp =  tmp->next;
				i++;
			   }
	}
	freelist(tracks);
	Image1->Picture->SaveToFile("C:\\Users\\Ilya\\Playlists\\"+PlaylistName->Text+"\\"+PlaylistName->Text+".bmp");
	CreatePlaylistUnit->Close();
	CreatePlaylistUnit->DoDestroy();
	}
	else
	{
		ShowMessage("�������� � ����� ������ ��� ����������!");
	}
}
void __fastcall TCreatePlaylistUnit::CreateplbtnClick(TObject *Sender)
{

	CreatePlaylist();
}
 void __fastcall TCreatePlaylistUnit::createplaylistdir(String Name)
 {
	CreateDir("C:\\Users\\Ilya\\Playlists\\"+Name);
 }
 void __fastcall TCreatePlaylistUnit::copytrack(dynlist* elem,int currindex)
 {
		String customplaylistdir = "C:\\Users\\Ilya\\Playlists\\" + PlaylistName->Text+"\\";
        const char* To = (AnsiString(customplaylistdir.c_str()) + pltracksbox->Items->Strings[currindex]).c_str();
		const char* From =AnsiString(elem->value.c_str()).c_str();
		CopyFileA(From,To,true);
		From = "";
        To = "";
 }

void __fastcall TCreatePlaylistUnit::CancelplbtnClick(TObject *Sender)
{
	freelist(tracks);
	CreatePlaylistUnit->Close();
	CreatePlaylistUnit->DoDestroy();
}
//---------------------------------------------------------------------------

void __fastcall TCreatePlaylistUnit::picturebtnClick(TObject *Sender)
{
	 TJPEGImage *jpg = new TJPEGImage();
	 Playlistpicturedialog->Execute();
	 jpg->LoadFromFile(Playlistpicturedialog->FileName);
	 TBitmap *bmp = new Graphics::TBitmap;
	 bmp->Assign(jpg);
	 Image1->Canvas->StretchDraw(Rect(0,0,220,220),bmp);
	 IsPictureChoosen = true;
}
//---------------------------------------------------------------------------


void __fastcall TCreatePlaylistUnit::DeletebtnClick(TObject *Sender)
{
			if(pltracksbox->ItemIndex!=-1)
			{
			deleteelem(tracks);
			pltracksbox->Items->Delete(pltracksbox->ItemIndex);
			}
}
//---------------------------------------------------------------------------

