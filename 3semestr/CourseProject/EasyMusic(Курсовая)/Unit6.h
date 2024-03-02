//---------------------------------------------------------------------------

#ifndef Unit6H
#define Unit6H
//---------------------------------------------------------------------------
#include <System.Classes.hpp>
#include <Vcl.Controls.hpp>
#include <Vcl.StdCtrls.hpp>
#include <Vcl.Forms.hpp>
#include <Vcl.Buttons.hpp>
#include <Vcl.MPlayer.hpp>
#include <Vcl.Dialogs.hpp>
#include <Vcl.ComCtrls.hpp>
#include <Vcl.ExtCtrls.hpp>
#include <Unit1.h>
#include <Vcl.Graphics.hpp>
#include <System.ImageList.hpp>
#include <Vcl.ImgList.hpp>
//---------------------------------------------------------------------------
class TMainUnit : public TForm
{
__published:	// IDE-managed Components
	TBitBtn *playbtn;
	TMediaPlayer *MediaPlayer;
	TBitBtn *prevbtn;
	TBitBtn *nextbtn;
	TFileOpenDialog *openfile;
	TBitBtn *addmusicbtn;
	TListBox *MusicList;
	TLabel *CurrentTrack;
	TBitBtn *DeleteMusicBtn;
	TBitBtn *RandomBtn;
	TImage *Image2;
	TEdit *FindTrackEdit;
	TBitBtn *FindBtn;
	TTrackBar *Musicprogress;
	TTrackBar *VolumeTrackBar;
	TBitBtn *RepeatBtn;
	TButton *ChoosePlbtn;
	TButton *AddPlbtn;
	TTimer *tracktimer;
	TSpeedButton *WithoutVolume;
	TListBox *FoundedTrack;
	TButton *editplaylistbtn;
	TImageList *ImageList1;
	TLabel *PlayListNameLabel;
	TTimer *Timer1;
	TLabel *FullTimeLabel;
	TLabel *CurrTimeLabel;
	void __fastcall addmusicbtnClick(TObject *Sender);
	void __fastcall playmusic();
	void __fastcall playbtnClick(TObject *Sender);
	void __fastcall nextbtnClick(TObject *Sender);
	void __fastcall prevbtnClick(TObject *Sender);
	void __fastcall updatetrackname();
	void __fastcall SetVolume(DWORD Volume,HWAVEOUT hwo);
	void __fastcall VolumeTrackBarChange(TObject *Sender);
	void __fastcall tracktimerTimer(TObject *Sender);
	void __fastcall MusicprogressChange(TObject *Sender);
	void __fastcall WithoutVolumeClick(TObject *Sender);
	void __fastcall AddPlbtnClick(TObject *Sender);
	void __fastcall FormCreate(TObject *Sender);
   	void __fastcall GetMusicFromDir(String dir);
	void __fastcall ChoosePlbtnClick(TObject *Sender);
	void __fastcall RepeatBtnClick(TObject *Sender);
	void __fastcall RandomBtnClick(TObject *Sender);
	void __fastcall MusicListDblClick(TObject *Sender);
	void __fastcall FindBtnClick(TObject *Sender);
	void __fastcall FoundedTrackDblClick(TObject *Sender);
	void __fastcall editplaylistbtnClick(TObject *Sender);
	void __fastcall DeleteMusicBtnClick(TObject *Sender);
	void __fastcall CurrentTrackMouseEnter(TObject *Sender);
	void __fastcall PlayListNameLabelMouseEnter(TObject *Sender);


private:	// User declarations
public: std::string filename;
		std::_Bool IsPlaying = false;
		int currindex = 0;
		int maxindex = 0;
		HWAVEOUT hwo;
		__fastcall TMainUnit(TComponent* Owner);
		String playlistdir;
		String playlistname;
        String a;
		bool OnRepeat;
		void __fastcall playnext();
        void __fastcall playprev();
		void __fastcall RandomizeTracks() ;
		bool __fastcall checkfiletype(std::string FileName);
        void __fastcall settime();
        int FoundedIndex = -1;
};

//---------------------------------------------------------------------------
extern PACKAGE TMainUnit *MainUnit;
extern PACKAGE TCreatePlaylistUnit *CreatePlaylistUnit;
//---------------------------------------------------------------------------
#endif
