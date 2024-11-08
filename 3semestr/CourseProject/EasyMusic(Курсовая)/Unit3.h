//---------------------------------------------------------------------------

#ifndef Unit3H
#define Unit3H
//---------------------------------------------------------------------------
#include <System.Classes.hpp>
#include <Vcl.Controls.hpp>
#include <Vcl.StdCtrls.hpp>
#include <Vcl.Forms.hpp>
#include <Vcl.Buttons.hpp>
#include <Vcl.Dialogs.hpp>
#include <Vcl.ExtCtrls.hpp>
#include <Vcl.ExtDlgs.hpp>
#include <Vcl.Graphics.hpp>
//---------------------------------------------------------------------------
class TForm3 : public TForm
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
	TBitBtn *SaveBtn;
	TBitBtn *Cancelplbtn;
	TFileOpenDialog *playlistfile;
	TOpenPictureDialog *Playlistpicturedialog;
	void __fastcall AddbtnClick(TObject *Sender);
	void __fastcall addelem(dynlist* head,std::string value)
private:
public:		// User declarations
	__fastcall TForm3(TComponent* Owner);

};
//---------------------------------------------------------------------------
extern PACKAGE TForm3 *Form3;
//---------------------------------------------------------------------------
#endif
