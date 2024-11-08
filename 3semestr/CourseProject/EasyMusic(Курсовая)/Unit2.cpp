//---------------------------------------------------------------------------

#include <vcl.h>
#pragma hdrstop
#include <System.IOUtils.hpp>
#include "Unit6.h"
#include "Unit2.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
TChoosePlaylistUnit *ChoosePlaylistUnit;
//---------------------------------------------------------------------------
__fastcall TChoosePlaylistUnit::TChoosePlaylistUnit(TComponent* Owner)
	: TForm(Owner)
{
}
//---------------------------------------------------------------------------
void __fastcall TChoosePlaylistUnit::FormCreate(TObject *Sender)
{
	  const TStringDynArray dirs = TDirectory::GetDirectories(L"C:\\Users\\Ilya\\Playlists");
	  for (int i = 0; i < dirs.Length; i++)
	  {
		  String directory = dirs[i];
		  std::string dirname = AnsiString(directory).c_str() ;
		  int bracketposition = dirname.rfind("\\");
          std::string  playlistname  = dirname.substr(bracketposition+1);
		  playlistslb->AddItem(AnsiString(playlistname.c_str()),0);
      }
}
//---------------------------------------------------------------------------
void __fastcall TChoosePlaylistUnit::cancelchoosebtnClick(TObject *Sender)
{
	ChoosePlaylistUnit->Close();
	ChoosePlaylistUnit->DoDestroy();
}
//---------------------------------------------------------------------------
void __fastcall TChoosePlaylistUnit::chooseplbtnClick(TObject *Sender)
{
	MainUnit->playlistdir = "C:\\Users\\Ilya\\Playlists\\"  + playlistslb->Items->Strings[playlistslb->ItemIndex] +"\\"  ;
	MainUnit->playlistname =playlistslb->Items->Strings[playlistslb->ItemIndex];
	MainUnit->MusicList->Clear();
	MainUnit->GetMusicFromDir(MainUnit->playlistdir);
	MainUnit->IsPlaying = false;
	TBitmap *bmp = new Graphics::TBitmap;
	bmp->LoadFromFile("C:\\Users\\Ilya\\Playlists\\"  + playlistslb->Items->Strings[playlistslb->ItemIndex]+"\\"+playlistslb->Items->Strings[playlistslb->ItemIndex]+".bmp");
	MainUnit->Image2->Canvas->StretchDraw(Rect(0,0,270,270),bmp);
	ChoosePlaylistUnit->Close();
	ChoosePlaylistUnit->DoDestroy();
	MainUnit->PlayListNameLabel->Caption=MainUnit->playlistname;
}
//---------------------------------------------------------------------------
bool __fastcall  TChoosePlaylistUnit::DeleteDir(AnsiString DirName)
{
TSearchRec sr;
if (DirName.Length())
 {
 if (!FindFirst(DirName+"\\*.*",faAnyFile,sr))
 do
  {
	  if (!(sr.Name=="." || sr.Name==".."))
	  {
	   if (((sr.Attr & faDirectory) == faDirectory ) ||
	   (sr.Attr == faDirectory))// ������� �����
		{
		FileSetAttr(DirName+"\\"+sr.Name, faDirectory );// ����� ������ read-only
		DeleteDir(DirName+"\\"+sr.Name);//���������� ������� ����������
		RemoveDir(DirName + "\\"+sr.Name);// ������� ������ ��� ������ �����
		}
		else// ����� ������ ����
		{
		FileSetAttr(DirName+"\\"+sr.Name, 0);// ����� ������ read-only
		DeleteFile(DirName+"\\"+sr.Name);// ������� ����
		}
	  }
  }
 while (!FindNext(sr));// ���� �����, ���� �� ������ ���
 FindClose(sr);
 }
RemoveDir(DirName);
return true;
}
void __fastcall TChoosePlaylistUnit::DeletePlaylistbtnClick(TObject *Sender)
{
		  AnsiString plname =  playlistslb->Items->Strings[playlistslb->ItemIndex];
		  AnsiString pldir =  "C:\\Users\\Ilya\\Playlists\\"  + playlistslb->Items->Strings[playlistslb->ItemIndex];
		  if(pldir == "C:\\Users\\Ilya\\Playlists\\StandartPlaylist")
		  {
			  ShowMessage("����������� �������� �� ����� ���� ������");
		  }
		  else if(plname == MainUnit->playlistname){
              ShowMessage("������� �������� �� ����� ���� ������");
		  }
		  else
		  {
				playlistslb->Items->Delete(playlistslb->ItemIndex);
				if(DeleteDir(pldir))
				{
					ShowMessage("�������� ������� ������");
				}
				else
				{
					playlistslb->AddItem(plname,0);
                    ShowMessage("��������� ������!!!");
                }
		  }
}
//---------------------------------------------------------------------------

