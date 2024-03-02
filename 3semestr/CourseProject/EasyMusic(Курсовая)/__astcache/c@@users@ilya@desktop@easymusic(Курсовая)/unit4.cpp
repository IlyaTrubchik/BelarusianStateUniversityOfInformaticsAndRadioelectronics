//---------------------------------------------------------------------------

#include <vcl.h>
#pragma hdrstop

#include "Unit4.h"
#include "Unit6.h"
#include <System.IOUtils.hpp>
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
TForm4 *Form4;
//---------------------------------------------------------------------------
__fastcall TForm4::TForm4(TComponent* Owner)
	: TForm(Owner)
{
}
//---------------------------------------------------------------------------
void __fastcall TForm4::AddbtnClick(TObject *Sender)
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
 void __fastcall TForm4::addelem(dynlist* head,std::string value)
{
	dynlist* tmp  = new dynlist();
	tmp->value = value;
	while(head->next != NULL)
	{
		head = head->next;
	}
	head->next = tmp;
}
void __fastcall TForm4::DeletebtnClick(TObject *Sender)
{
	dynlist* tmp = tracks->next;
	dynlist* prev = tracks;
	std::string value = AnsiString(pltracksbox->Items->Strings[pltracksbox->ItemIndex]).c_str();
	while(tmp != NULL && value != tmp->value)
	{
		prev = prev->next;
		tmp = tmp->next;
	}
	if(tmp->value == value)
	{
	   prev->next = tmp->next;
       free(tmp);
	}
	else
	{
	   std::string TrackPath = dirname + "\\" + value;
	   const char* DeletingItem = (AnsiString(TrackPath.c_str())).c_str();
	   DeleteFileA(DeletingItem);
    }
}
//---------------------------------------------------------------------------

void __fastcall TForm4::CancelplbtnClick(TObject *Sender)
{
	freelist(tracks);
	Form4->Close();
	Form4->DoDestroy();
	Form6->Enabled  = true;
}
//---------------------------------------------------------------------------

void __fastcall TForm4::picturebtnClick(TObject *Sender)
{
	 TJPEGImage *jpg = new TJPEGImage();
	 Playlistpicturedialog->Execute();
	 jpg->LoadFromFile(Playlistpicturedialog->FileName);
	 TBitmap *bmp = new Graphics::TBitmap;
	 bmp->Assign(jpg);
	 Image1->Canvas->StretchDraw(Rect(0,0,220,220),bmp);
     IsImage = true;
}
//---------------------------------------------------------------------------

void __fastcall TForm4::FormCreate(TObject *Sender)
{

      tracks  = new dynlist();
	  const TStringDynArray dirs = TDirectory::GetDirectories(L"C:\\Users\\Ilya\\Playlists");
	  for (int i = 0; i < dirs.Length; i++)
	  {
		  String directory = dirs[i];
		  dirname = AnsiString(directory).c_str() ;
		  int bracketposition = dirname.rfind("\\");
		  playlistname  = dirname.substr(bracketposition+1);
		  playlists->AddItem(AnsiString(playlistname.c_str()),0);
	  }
}
//---------------------------------------------------------------------------
void __fastcall TForm4::GetMusic(String dir)
{
	WIN32_FIND_DATAA FindFileData;
	HANDLE hf;
	String postfix = "\\*.mp3";
	hf=FindFirstFileA(AnsiString(dir+postfix).c_str(), &FindFileData);
	if (hf!=INVALID_HANDLE_VALUE)
	{
		do
		{
			std::string FileName = (AnsiString(	FindFileData.cFileName).c_str());
			int	bracketposition = FileName.rfind("\\");
			std::string trackname = FileName.substr(bracketposition+1);
			pltracksbox->AddItem(AnsiString(trackname.c_str()),0);
		}
		while (FindNextFileA(hf,&FindFileData)!=0);
		FindClose(hf);
	}
}
void __fastcall TForm4::choosebtnClick(TObject *Sender)
{
	  GetMusic("C:\\Users\\Ilya\\Playlists\\"+playlists->Items->Strings[playlists->ItemIndex]);
	  PlaylistName->Text = playlists->Items->Strings[playlists->ItemIndex];
	  playlistname = AnsiString(playlists->Items->Strings[playlists->ItemIndex]).c_str();

	  TBitmap *bmp = new Graphics::TBitmap;
	  bmp->LoadFromFile("C:\\Users\\Ilya\\Playlists\\"+playlists->Items->Strings[playlists->ItemIndex]+"\\"+playlists->Items->Strings[playlists->ItemIndex]+".bmp");
	  Image1->Canvas->StretchDraw(Rect(0,0,220,220),bmp);
}
//---------------------------------------------------------------------------

void __fastcall TForm4::savebtnClick(TObject *Sender)
{
       char* OldName= AnsiString(("C:\\Users\\Ilya\\Playlists\\"+playlistname).c_str()).c_str();
	   char* DeletingItem = (AnsiString(("C:\\Users\\Ilya\\Playlists\\"+playlistname+"\\"+playlistname+".bmp").c_str())).c_str();
	   Save(OldName,DeletingItem);
	   Form4->Close();
	   Form4->DoDestroy();
	   Form6->Enabled = true;
}
void __fastcall TForm4::Save(char* OldName,char* Deleting)
{
	   DeleteFileA(Deleting);
  // char* NewName = AnsiString(("C:\\Users\\Ilya\\Playlists\\"+PlaylistName->Text).c_str()).c_str();

   RenameFile(OldName,AnsiString(("C:\\Users\\Ilya\\Playlists\\"+PlaylistName->Text).c_str()).c_str());
   playlistname = AnsiString(PlaylistName->Text).c_str();
   Image1->Picture->SaveToFile((AnsiString(("C:\\Users\\Ilya\\Playlists\\"+playlistname+"\\"+playlistname+".bmp").c_str())).c_str());
	   int i = 0;
	   dynlist* tmp  = tracks->next;
	   tmp = tracks->next;
	   while (tmp != NULL)
			   {
				copytrack(tmp,i);
				tmp =  tmp->next;
				i++;
			   }
}
//---------------------------------------------------------------------------
  void __fastcall TForm4::copytrack(dynlist* elem,int currindex)
 {
		String customplaylistdir = "C:\\Users\\Ilya\\Playlists\\" + PlaylistName->Text+"\\";
        const char* To = (AnsiString(customplaylistdir.c_str()) + pltracksbox->Items->Strings[currindex]).c_str();
		const char* From =AnsiString(elem->value.c_str()).c_str();
		CopyFileA(From,To,true);
		From = "";
        To = "";
 }
 void __fastcall TForm4::freelist(dynlist* head)
{
	dynlist* tmp = head;
	while (head != NULL)
	{
		tmp = head->next;
		free(head);
		head = tmp;
    }
}
void __fastcall TForm4::FormClose(TObject *Sender, TCloseAction &Action)
{
Form6->Enabled = true;
}
//---------------------------------------------------------------------------

