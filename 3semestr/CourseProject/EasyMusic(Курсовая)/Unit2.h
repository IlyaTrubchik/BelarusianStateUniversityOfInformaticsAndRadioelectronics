//---------------------------------------------------------------------------
#include <System.Classes.hpp>
#include <Vcl.Buttons.hpp>
#include <Vcl.Controls.hpp>
#include <Vcl.StdCtrls.hpp>
#ifndef Unit2H
#define Unit2H
//---------------------------------------------------------------------------
#include <System.Classes.hpp>
#include <Vcl.Controls.hpp>
#include <Vcl.StdCtrls.hpp>
#include <Vcl.Forms.hpp>
#include <Vcl.Buttons.hpp>
//---------------------------------------------------------------------------
class TChoosePlaylistUnit : public TForm
{
__published:	// IDE-managed Components
	TListBox *playlistslb;
	TLabel *Label1;
	TBitBtn *chooseplbtn;
	TBitBtn *cancelchoosebtn;
	TBitBtn *DeletePlaylistbtn;
	void __fastcall FormCreate(TObject *Sender);
	void __fastcall cancelchoosebtnClick(TObject *Sender);
	void __fastcall chooseplbtnClick(TObject *Sender);
	void __fastcall DeletePlaylistbtnClick(TObject *Sender);
private:	// User declarations
public:		// User declarations
	__fastcall TChoosePlaylistUnit(TComponent* Owner);
    bool __fastcall  DeleteDir(AnsiString DirName);
};
//---------------------------------------------------------------------------
extern PACKAGE TChoosePlaylistUnit *ChoosePlaylistUnit;
//---------------------------------------------------------------------------
#endif
