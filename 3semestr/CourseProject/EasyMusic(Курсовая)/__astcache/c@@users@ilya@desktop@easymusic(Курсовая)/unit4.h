﻿//---------------------------------------------------------------------------

#ifndef Unit4H
#define Unit4H
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
class TForm4 : public TForm
{
__published:	// IDE-managed Components
	TImage *Image1;
	TLabel *Label1;
	TLabel *Label2;
	TLabel *Label3;
	TListBox *pltracksbox;
	TBitBtn *picturebtn;
	TBitBtn *Addbtn;
	TBitBtn *Deletebtn;
	TEdit *PlaylistName;
	TBitBtn *savebtn;
	TBitBtn *Cancelplbtn;
	TFileOpenDialog *playlistfile;
	TOpenPictureDialog *Playlistpicturedialog;
	TListBox *playlists;
	TBitBtn *choosebtn;
	TLabel *Label4;
	void __fastcall AddbtnClick(TObject *Sender);
	void __fastcall DeletebtnClick(TObject *Sender);
	void __fastcall CancelplbtnClick(TObject *Sender);
	void __fastcall picturebtnClick(TObject *Sender);
	void __fastcall FormCreate(TObject *Sender);
	void __fastcall choosebtnClick(TObject *Sender);
	void __fastcall savebtnClick(TObject *Sender);
	void __fastcall FormClose(TObject *Sender, TCloseAction &Action);
private:	// User declarations
public:		// User declarations
	typedef struct dynlist{
		std::string value;
		dynlist* next;
	}dynlist;
	dynlist*  tracks;
	void __fastcall addelem(dynlist* head,std::string value);
	std::string dirname;
    std::string playlistname;
	__fastcall TForm4(TComponent* Owner);
	bool IsImage =  false;
	void __fastcall copytrack(dynlist* elem,int currindex);
	void __fastcall freelist(dynlist* head);
	void __fastcall GetMusic(String dir);
    void __fastcall Save(char* OldName,char* DeletingItem) ;
};
//---------------------------------------------------------------------------
extern PACKAGE TForm4 *Form4;
//---------------------------------------------------------------------------
#endif
