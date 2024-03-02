//---------------------------------------------------------------------------

#include <vcl.h>
#pragma hdrstop

#include "Unit6.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
TForm6 *Form6;
//---------------------------------------------------------------------------
__fastcall TForm6::TForm6(TComponent* Owner)
	: TForm(Owner)
{
}
//---------------------------------------------------------------------------
void __fastcall TForm6::addmusicbtnClick(TObject *Sender)
{
	openfile->Execute();
	//MediaPlayer->FileName = openfile->FileName;
	//MediaPlayer->Open();
	//MusicList->
	CurrentTrack->Caption = MediaPlayer->FileName;

}
//---------------------------------------------------------------------------void __fastcall TForm6::playmusic()
void __fastcall TForm6::playmusic()
{
  if (IsPlaying == false)
  {
		MediaPlayer->Play();
		IsPlaying  = true;
  }
  else
  {
	  MediaPlayer->Pause();
	  IsPlaying = false;
  }
}

//---------------------------------------------------------------------------
void __fastcall TForm6::playbtnClick(TObject *Sender)
{
    playmusic();
}
//---------------------------------------------------------------------------
