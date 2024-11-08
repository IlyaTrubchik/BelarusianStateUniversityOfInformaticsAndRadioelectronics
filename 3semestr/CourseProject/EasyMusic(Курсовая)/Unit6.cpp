//---------------------------------------------------------------------------

#include <vcl.h>
#include <stdlib.h>
#include <time.h>
#pragma hdrstop
#include <winbase.h>
#include "Unit6.h"
#include "Unit2.h"
#include "Unit4.h"
#include <String>
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
TMainUnit *MainUnit;
//---------------------------------------------------------------------------
__fastcall TMainUnit::TMainUnit(TComponent* Owner)
	: TForm(Owner)
{
}
//Standart directory C:\\Users\\Ilya\\Playlists\\StandartPlaylist\\
//---------------------------------------------------------------------------
bool __fastcall TMainUnit::checkfiletype(std::string FileName)
{
	std::string postfix;
	postfix.insert(0,FileName,FileName.length()-4,4);
	if (postfix == ".bmp" || postfix == ".mp3") {
			 return true;
	}
	return false;
}
void __fastcall TMainUnit::addmusicbtnClick(TObject *Sender)
{

	int bracketposition = 0;
	openfile->Execute();
	std::string FileName = (AnsiString(openfile->FileName).c_str());
	if (checkfiletype(FileName)) {
	bracketposition = FileName.rfind("\\");
	std::string trackname = FileName.substr(bracketposition+1);
	MusicList->AddItem(AnsiString(trackname.c_str()),0);
	CopyFileA(AnsiString(openfile->FileName.c_str()).c_str(), (AnsiString(playlistdir.c_str()) + MusicList->Items->Strings[MusicList->Items->Count-1]).c_str(),true);
	MediaPlayer->FileName  = AnsiString(playlistdir.c_str()) + MusicList->Items->Strings[currindex];
	MediaPlayer->Open();

	}
	else
	{
        ShowMessage("Unsupported type!,You can add only *.mp3 or *.wav music files");
    }

}
void  __fastcall TMainUnit::settime(){

		   String seconds  = (MediaPlayer->Length / 1000)% 60;
		   String minutes  = (MediaPlayer->Length/1000)/60;
		   if(atoi(AnsiString(seconds).c_str())< 10){
		   FullTimeLabel->Caption  = minutes+":0"+seconds;
		   }
		   else
		   {
			   FullTimeLabel->Caption  = minutes+":"+seconds;
		   }
		   seconds  = (MediaPlayer->Position / 1000)% 60;
		   minutes  = (MediaPlayer->Position/1000)/60;
		   if(atoi(AnsiString(seconds).c_str()) < 10){
				CurrTimeLabel->Caption  = minutes+":0"+seconds;
		   }
		   else
		   {
                CurrTimeLabel->Caption  = minutes+":"+seconds;
           }

}
//---------------------------------------------------------------------------void __fastcall TForm6::playmusic()
void __fastcall TMainUnit::playmusic()
{
  if(MediaPlayer->FileName != "")
  {
	  if (IsPlaying == false)
	  {
			MediaPlayer->Play();
			IsPlaying  = true;
			Musicprogress->Min = MediaPlayer->StartPos;
			Musicprogress->Max = MediaPlayer->Length;
			playbtn->ImageIndex = 3;
	  }
	  else
	  {
		  MediaPlayer->Pause();
		  IsPlaying = false;
          	playbtn->ImageIndex =2;
	  }

	  tracktimer->Enabled = true;
  }
  else
  {
	  ShowMessage("There are no tracks!Add music");
      playbtn->ImageIndex = 2;
  }
}

//---------------------------------------------------------------------------
void __fastcall TMainUnit::playbtnClick(TObject *Sender)
{
	updatetrackname();
	playmusic();
}
void __fastcall TMainUnit::playnext()
{
  if(MusicList->Items->Count != 0){
  if (currindex == MusicList->Items->Count-1) {
	   currindex = 0;
   }
   else
   {
	   currindex++;
   }
	   MediaPlayer->FileName = AnsiString(playlistdir.c_str()) + MusicList->Items->Strings[currindex];
	   updatetrackname();
	   MediaPlayer->Open();
	   IsPlaying = false;
   //		settime();
	   playmusic();
  }
}
void __fastcall TMainUnit::playprev()
{
        if(MusicList->Items->Count != 0)
   {
   if (currindex == 0) {
	   currindex = MusicList->Items->Count-1;
   }
   else
   {
	   currindex--;
   }
	   MediaPlayer->FileName = AnsiString(playlistdir.c_str()) + MusicList->Items->Strings[currindex];
	   updatetrackname();
	   MediaPlayer->Open();
	   IsPlaying = false;
	   //	settime();
	   playmusic();
   }
}
//---------------------------------------------------------------------------
void __fastcall TMainUnit::nextbtnClick(TObject *Sender)
{
		playnext();
}
//---------------------------------------------------------------------------

void __fastcall TMainUnit::prevbtnClick(TObject *Sender)
{
	   playprev();
}
void __fastcall TMainUnit::updatetrackname()
{
	std::string FileName(AnsiString(MediaPlayer->FileName).c_str());
	int	bracketposition = FileName.rfind("\\");
	std::string trackname =   FileName.substr(bracketposition+1);
	CurrentTrack->Caption =  AnsiString(trackname.c_str());
}
//---------------------------------------------------------------------------
void __fastcall TMainUnit::SetVolume(DWORD Volume,HWAVEOUT hwo)
{
	waveOutSetVolume(hwo,DWORD(Volume*65535)/100);

}
//---------------------------------------------------------------------------

void __fastcall TMainUnit::VolumeTrackBarChange(TObject *Sender)
{
	SetVolume(VolumeTrackBar->Position,hwo);
	if(VolumeTrackBar->Position == 0){
		WithoutVolume->Down = true;
	}
	else
	{
        WithoutVolume->Down = false;
    }
}
//---------------------------------------------------------------------------

void __fastcall TMainUnit::tracktimerTimer(TObject *Sender)
{
    Musicprogress->Position = MediaPlayer->Position;
   if (MediaPlayer->Position == MediaPlayer->Length)
   {
	 if (OnRepeat)
	 {
	  MediaPlayer->Position = 0;
	  Musicprogress->Position = MediaPlayer->Position;
      MediaPlayer->Play();
	 }
	 else
	 {
		MediaPlayer->Position = 0;
		playnext();
	 }
   }
   settime();

  // tracktimer->Enabled =false;
}
//---------------------------------------------------------------------------

void __fastcall TMainUnit::MusicprogressChange(TObject *Sender)
{
	if(MediaPlayer->FileName !="")
	{

	if ((MediaPlayer->Position>Musicprogress->Position+10) || (MediaPlayer->Position<Musicprogress->Position-10))
	{
	  MediaPlayer->Pause();
	  MediaPlayer->Position = Musicprogress->Position;
	  MediaPlayer->Play();

	  playbtn->ImageIndex =  3;
	}
	}
	else
	{
		if(Musicprogress->Position !=0)
		{
		ShowMessage("Please,Choose track!");
		}
        Musicprogress->Position = 0;
    }
}
//---------------------------------------------------------------------------



void __fastcall TMainUnit::WithoutVolumeClick(TObject *Sender)
{
   if(WithoutVolume->Down == true)
   {
	   SetVolume(0,0);
   }
   else
   {
	   SetVolume(VolumeTrackBar->Position,hwo);
   }
}
//---------------------------------------------------------------------------



void __fastcall TMainUnit::AddPlbtnClick(TObject *Sender)
{
	 Application->CreateForm(__classid(TCreatePlaylistUnit), &CreatePlaylistUnit);
	 CreatePlaylistUnit->Visible = true;
}
//---------------------------------------------------------------------------


void __fastcall TMainUnit::FormCreate(TObject *Sender)
{
VolumeTrackBar->Position = 50;
SetVolume(VolumeTrackBar->Position,hwo);
  OnRepeat = false;
  TBitmap *bmp = new Graphics::TBitmap;
  playlistdir = "C:\\Users\\Ilya\\Playlists\\StandartPlaylist\\";
  if (DirectoryExists("C:\\Users\\Ilya\\Playlists"))
   {
		if(DirectoryExists("C:\\Users\\Ilya\\Playlists\\StandartPlaylist"))
		{
			bmp->LoadFromFile("C:\\Users\\Ilya\\Playlists\\StandartPlaylist\\StandartPlaylist.bmp");
			Image2->Canvas->StretchDraw(Rect(0,0,270,270),bmp);
			GetMusicFromDir("C:\\Users\\Ilya\\Playlists\\StandartPlaylist");
		}
		else
		{
			CreateDir("C:\\Users\\Ilya\\Playlists\\StandartPlaylist");
			bmp->Assign(Image2->Picture);
			Image2->Canvas->StretchDraw(Rect(0,0,270,270),bmp);
			bmp->Assign(Image2->Picture);
			bmp->Height = 270;
			bmp->Width  = 270;
			bmp->SaveToFile("C:\\Users\\Ilya\\Playlists\\StandartPlaylist\\StandartPlaylist.bmp")   ;

		}
   }
   else
   {
		CreateDir("C:\\Users\\Ilya\\Playlists");
		CreateDir("C:\\Users\\Ilya\\Playlists\\StandartPlaylist");
		bmp->Assign(Image2->Picture);
		Image2->Canvas->StretchDraw(Rect(0,0,270,270),bmp);
		bmp->Assign(Image2->Picture);
		bmp->Height = 270;
        bmp->Width  = 270;
		bmp->SaveToFile("C:\\Users\\Ilya\\Playlists\\StandartPlaylist\\StandartPlaylist.bmp")   ;
		tracktimer->Enabled = false;
   }
   playlistname = "StandartPlaylist";
   PlayListNameLabel->Caption  =playlistname;

}
void __fastcall TMainUnit::GetMusicFromDir(String dir)
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
			MusicList->AddItem(AnsiString(trackname.c_str()),0);
		}
		while (FindNextFileA(hf,&FindFileData)!=0);
		FindClose(hf);
	}
 //	 playlistdir = playlistdir + "\\";
     currindex = 0;
	 if (MusicList->Items->Count != 0) {
        MediaPlayer->FileName = AnsiString(playlistdir.c_str()) + MusicList->Items->Strings[currindex];
		updatetrackname();
		MediaPlayer->Open();
        settime();
	 }
	 else
	 {
         tracktimer->Enabled = false;
     }


}
//---------------------------------------------------------------------------

void __fastcall TMainUnit::ChoosePlbtnClick(TObject *Sender)
{
	Application->CreateForm(__classid(TChoosePlaylistUnit), &ChoosePlaylistUnit);
	ChoosePlaylistUnit->Visible = True;
}
//---------------------------------------------------------------------------

void __fastcall TMainUnit::RepeatBtnClick(TObject *Sender)
{
  OnRepeat = !OnRepeat;
  RepeatBtn->ImageIndex = 6;
  if(OnRepeat)
  {
	  RepeatBtn->ImageIndex =  7;
  }

}
//---------------------------------------------------------------------------
void __fastcall TMainUnit::RandomizeTracks()
{
	srand(time(NULL));
	for (int i = MusicList->Items->Count-1;i>=1; i--)
	{
		int k = rand()%(i);

		AnsiString tmp = MusicList->Items->Strings[i];
		MusicList->Items->Strings[i] = MusicList->Items->Strings[k];
		MusicList->Items->Strings[k] = tmp;
	}
	currindex = -1;
    playnext();
}
void __fastcall TMainUnit::RandomBtnClick(TObject *Sender)
{
 RandomizeTracks();
 playbtn->ImageIndex =3;
}
//---------------------------------------------------------------------------

//---------------------------------------------------------------------------

void __fastcall TMainUnit::MusicListDblClick(TObject *Sender)
{
		 if(MusicList->ItemIndex != -1)
		 {
		 currindex = MusicList->ItemIndex-1;
		 playnext();
		 }
}
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------


void __fastcall TMainUnit::FindBtnClick(TObject *Sender)
{
	std::string  TrackName  =AnsiString(FindTrackEdit->Text).c_str();
	bool IsFound = false;
	int i = 0;
	while (i<MusicList->Items->Count && IsFound == false)
	{
		if (MusicList->Items->Strings[i] == TrackName.c_str()) {
			  FoundedTrack->AddItem(AnsiString(TrackName.c_str()),0);
			  IsFound = true;
			  FoundedIndex = i;
		}
        i++;
	}
	if(IsFound == false)
	{
        ShowMessage("There is no such track!");
    }

}
//---------------------------------------------------------------------------


void __fastcall TMainUnit::FoundedTrackDblClick(TObject *Sender)
{
	currindex = FoundedIndex-1;
    playnext();
}
//---------------------------------------------------------------------------

void __fastcall TMainUnit::editplaylistbtnClick(TObject *Sender)
{
Application->CreateForm(__classid(TEditPlaylistUnit), &EditPlaylistUnit);
EditPlaylistUnit->Visible = true;
MainUnit->Enabled = false;
}
//---------------------------------------------------------------------------


void __fastcall TMainUnit::DeleteMusicBtnClick(TObject *Sender)
{
if(MusicList->ItemIndex != -1)
{
char* Deleting = AnsiString(playlistdir+MusicList->Items->Strings[MusicList->ItemIndex]).c_str();
MediaPlayer->Pause();
DeleteFileA(Deleting);
currindex=currindex-1;
MusicList->Items->Delete(MusicList->ItemIndex);
if(MusicList->Items->Count != 0){
playnext();
}
else
{
	MediaPlayer->FileName="";
}
 }
}
//---------------------------------------------------------------------------

void __fastcall TMainUnit::CurrentTrackMouseEnter(TObject *Sender)
{
 CurrentTrack->Hint = CurrentTrack->Caption;
}
//---------------------------------------------------------------------------

void __fastcall TMainUnit::PlayListNameLabelMouseEnter(TObject *Sender)
{
PlayListNameLabel->Hint = PlayListNameLabel->Caption;
}
//---------------------------------------------------------------------------


//---------------------------------------------------------------------------



