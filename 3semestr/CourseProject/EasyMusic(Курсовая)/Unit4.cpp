//---------------------------------------------------------------------------

#include <vcl.h>
#pragma hdrstop

#include "Unit4.h"
#include "Unit6.h"
#include <System.IOUtils.hpp>
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
TEditPlaylistUnit *EditPlaylistUnit;
//---------------------------------------------------------------------------
__fastcall TEditPlaylistUnit::TEditPlaylistUnit(TComponent* Owner)
	: TForm(Owner)
{
}
//---------------------------------------------------------------------------
void __fastcall TEditPlaylistUnit::AddbtnClick(TObject *Sender)
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
 void __fastcall TEditPlaylistUnit::addelem(dynlist* head,std::string value)
{
	dynlist* tmp  = new dynlist();
	tmp->value = value;
	while(head->next != NULL)
	{
		head = head->next;
	}
	head->next = tmp;
}
void __fastcall TEditPlaylistUnit::DeletebtnClick(TObject *Sender)
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
		 int bracketposition = dirname.rfind("\\");
         dirname = dirname.erase(bracketposition);
		 std::string TrackPath = dirname + "\\" +playlistname+"\\"+ value;
	   const char* DeletingItem = (AnsiString(TrackPath.c_str())).c_str();
       pltracksbox->Items->Delete(pltracksbox->ItemIndex);
	   DeleteFileA(DeletingItem);
}
}
//---------------------------------------------------------------------------

void __fastcall TEditPlaylistUnit::CancelplbtnClick(TObject *Sender)
{
	freelist(tracks);
	EditPlaylistUnit->Close();
	EditPlaylistUnit->DoDestroy();
	MainUnit->Enabled  = true;
}
//---------------------------------------------------------------------------

void __fastcall TEditPlaylistUnit::picturebtnClick(TObject *Sender)
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

void __fastcall TEditPlaylistUnit::FormCreate(TObject *Sender)
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
void __fastcall TEditPlaylistUnit::GetMusic(String dir)
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
            addelem(tracks,FileName);
			int	bracketposition = FileName.rfind("\\");
			std::string trackname = FileName.substr(bracketposition+1);
			pltracksbox->AddItem(AnsiString(trackname.c_str()),0);
		}
		while (FindNextFileA(hf,&FindFileData)!=0);
		FindClose(hf);
	}
}
void __fastcall TEditPlaylistUnit::choosebtnClick(TObject *Sender)
{
      freelist(tracks);
	  pltracksbox->Clear();
      tracks = new dynlist();
	  GetMusic("C:\\Users\\Ilya\\Playlists\\"+playlists->Items->Strings[playlists->ItemIndex]);
	  PlaylistName->Text = playlists->Items->Strings[playlists->ItemIndex];
	  playlistname = AnsiString(playlists->Items->Strings[playlists->ItemIndex]).c_str();

	  TBitmap *bmp = new Graphics::TBitmap;
	  bmp->LoadFromFile("C:\\Users\\Ilya\\Playlists\\"+playlists->Items->Strings[playlists->ItemIndex]+"\\"+playlists->Items->Strings[playlists->ItemIndex]+".bmp");
	  Image1->Canvas->StretchDraw(Rect(0,0,220,220),bmp);
}
//---------------------------------------------------------------------------
bool TEditPlaylistUnit::CheckDirectNames()
{
		 for (int  i =0; i < playlists->Items->Count; i++) {
			 if (playlists->Items->Strings[i] == PlaylistName->Text && AnsiString(playlists->Items->Strings[i]).c_str()!=playlistname) {
				   return  false;
			 }
		 }
         return true;
}
void __fastcall TEditPlaylistUnit::savebtnClick(TObject *Sender)
{

	   char* OldName= AnsiString(("C:\\Users\\Ilya\\Playlists\\"+playlistname).c_str()).c_str();
	   //����������������� ������� �����,������ ���������� OldName � NewName ,� ����� �������� ���������� �������
	   if (MainUnit->playlistname == AnsiString(playlistname.c_str())) {
			ShowMessage("You cant edit currently playing playlist,please choose another one");
	   }
	   else
	   {
	   Save(OldName);
	   EditPlaylistUnit->Enabled =false;
	   EditPlaylistUnit->Close();
	   MainUnit->Focused();
	   MainUnit->Enabled = true;
       }
}
void __fastcall TEditPlaylistUnit::Save(char* OldName)
{
	if (CheckDirectNames()) {

		DeleteFileA((AnsiString(("C:\\Users\\Ilya\\Playlists\\"+playlistname+"\\"+playlistname+".bmp").c_str())).c_str());
      Image1->Picture->SaveToFile((AnsiString(("C:\\Users\\Ilya\\Playlists\\"+playlistname+"\\"+AnsiString(PlaylistName->Text).c_str()+".bmp").c_str())).c_str());
	  // char* NewName = AnsiString(("C:\\Users\\Ilya\\Playlists\\"+PlaylistName->Text).c_str()).c_str();

	   RenameFile(AnsiString(("C:\\Users\\Ilya\\Playlists\\"+playlistname).c_str()).c_str(),AnsiString(("C:\\Users\\Ilya\\Playlists\\"+PlaylistName->Text).c_str()).c_str());
	   playlistname = AnsiString(PlaylistName->Text).c_str();

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
	else
	{
        ShowMessage("Playlist with the same name is exists");
    }

}
//---------------------------------------------------------------------------
  void __fastcall TEditPlaylistUnit::copytrack(dynlist* elem,int currindex)
 {
		String customplaylistdir = "C:\\Users\\Ilya\\Playlists\\" + PlaylistName->Text+"\\";
        const char* To = (AnsiString(customplaylistdir.c_str()) + pltracksbox->Items->Strings[currindex]).c_str();
		const char* From =AnsiString(elem->value.c_str()).c_str();
		CopyFileA(From,To,true);
		From = "";
        To = "";
 }
 void __fastcall TEditPlaylistUnit::freelist(dynlist* head)
{
	dynlist* tmp = head;
	while (head != NULL)
	{
		tmp = head->next;
		free(head);
		head = tmp;
    }
}
void __fastcall TEditPlaylistUnit::FormClose(TObject *Sender, TCloseAction &Action)
{
MainUnit->Enabled = true;
}
//---------------------------------------------------------------------------

