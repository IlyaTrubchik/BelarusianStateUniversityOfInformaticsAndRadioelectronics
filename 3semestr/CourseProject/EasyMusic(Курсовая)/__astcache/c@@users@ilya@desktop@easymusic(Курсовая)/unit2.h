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
class TForm2 : public TForm
{
__published:	// IDE-managed Components
	TListBox *playlistslb;
	TLabel *Label1;
	TBitBtn *chooseplbtn;
	TBitBtn *cancelchoosebtn;
	void __fastcall FormCreate(TObject *Sender);
	void __fastcall cancelchoosebtnClick(TObject *Sender);
	void __fastcall chooseplbtnClick(TObject *Sender);
private:	// User declarations
public:		// User declarations
	__fastcall TForm2(TComponent* Owner);
};
//---------------------------------------------------------------------------
extern PACKAGE TForm2 *Form2;
extern String playlistdir;
//---------------------------------------------------------------------------
#endif
