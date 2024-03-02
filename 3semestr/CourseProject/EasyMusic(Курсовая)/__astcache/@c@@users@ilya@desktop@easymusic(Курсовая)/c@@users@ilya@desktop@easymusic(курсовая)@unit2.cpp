//---------------------------------------------------------------------------

#include <vcl.h>
#pragma hdrstop
#include <System.IOUtils.hpp>
#include "Unit6.h"
#include "Unit2.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
TForm2 *Form2;
//---------------------------------------------------------------------------
__fastcall TForm2::TForm2(TComponent* Owner)
	: TForm(Owner)
{
}
//---------------------------------------------------------------------------
void __fastcall TForm2::FormCreate(TObject *Sender)
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
void __fastcall TForm2::cancelchoosebtnClick(TObject *Sender)
{
	Form2->Close();
	Form2->DoDestroy();
}
//---------------------------------------------------------------------------
void __fastcall TForm2::chooseplbtnClick(TObject *Sender)
{
	playlistdir = "C:\\Users\\Ilya\\Playlists\\"  + playlistslb->Items->Strings[playlistslb->ItemIndex] +"\\" ;
	Form2->Close();
	Form2->DoDestroy();
}
//---------------------------------------------------------------------------
