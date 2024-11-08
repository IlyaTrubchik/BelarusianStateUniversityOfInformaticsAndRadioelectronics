//---------------------------------------------------------------------------

#ifndef Unit1H
#define Unit1H
//---------------------------------------------------------------------------
#include <System.Classes.hpp>
#include <Vcl.Controls.hpp>
#include <Vcl.StdCtrls.hpp>
#include <Vcl.Forms.hpp>
#include <Vcl.Buttons.hpp>
#include <Vcl.Dialogs.hpp>
#include <Vcl.ExtCtrls.hpp>
#include <Vcl.ExtDlgs.hpp>
#include <Vcl.Imaging.jpeg.hpp>
#include <Vcl.Graphics.hpp>
//---------------------------------------------------------------------------
class TCreatePlaylistUnit : public TForm
{
__published:	// IDE-managed Components
	TFileOpenDialog *playlistfile;
	TImage *Image1;
	TListBox *pltracksbox;
	TBitBtn *picturebtn;
	TBitBtn *Addbtn;
	TBitBtn *Deletebtn;
	TEdit *PlaylistName;
	TBitBtn *Createplbtn;
	TBitBtn *Cancelplbtn;
	TLabel *Label1;
	TLabel *Label2;
	TLabel *Label3;
	TOpenPictureDialog *Playlistpicturedialog;
	void __fastcall AddbtnClick(TObject *Sender);
	void __fastcall FormCreate(TObject *Sender);
	void __fastcall CreateplbtnClick(TObject *Sender);
	void __fastcall CancelplbtnClick(TObject *Sender);
	void __fastcall picturebtnClick(TObject *Sender);
	void __fastcall DeletebtnClick(TObject *Sender);
private:	// User declarations
public:		// User declarations
	__fastcall TCreatePlaylistUnit(TComponent* Owner);
	typedef struct dynlist{
		std::string value;
		dynlist* next;
	}dynlist;
    dynlist*  tracks;
	void __fastcall addelem(dynlist* head,std::string value);
	void __fastcall deleteelem(dynlist* head);
	void __fastcall freelist(dynlist* head);
	void __fastcall createplaylistdir(String Name);
	void __fastcall copytrack(dynlist* elem,int currindex);
    void __fastcall CreatePlaylist();
	TBitmap *bmp;
    bool IsPictureChoosen = false;
};
//---------------------------------------------------------------------------
extern PACKAGE TCreatePlaylistUnit *CreatePlaylistUnit;
//---------------------------------------------------------------------------
#endif
