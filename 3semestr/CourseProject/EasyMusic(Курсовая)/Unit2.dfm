object ChoosePlaylistUnit: TChoosePlaylistUnit
  Left = 0
  Top = 0
  Caption = #1042#1099#1073#1088#1072#1090#1100' '#1087#1083#1077#1081#1083#1080#1089#1090
  ClientHeight = 424
  ClientWidth = 335
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -12
  Font.Name = 'Segoe UI'
  Font.Style = []
  OnCreate = FormCreate
  TextHeight = 15
  object Label1: TLabel
    Left = 112
    Top = 8
    Width = 116
    Height = 26
    Caption = #1055#1083#1101#1081#1083#1080#1089#1090#1099
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -20
    Font.Name = 'Showcard Gothic'
    Font.Style = [fsBold]
    ParentFont = False
  end
  object playlistslb: TListBox
    Left = 32
    Top = 40
    Width = 281
    Height = 289
    ItemHeight = 15
    TabOrder = 0
  end
  object chooseplbtn: TBitBtn
    Left = 32
    Top = 359
    Width = 89
    Height = 41
    Caption = #1042#1099#1073#1088#1072#1090#1100
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -12
    Font.Name = 'Segoe UI'
    Font.Style = [fsBold]
    ParentFont = False
    TabOrder = 1
    OnClick = chooseplbtnClick
  end
  object cancelchoosebtn: TBitBtn
    Left = 233
    Top = 359
    Width = 80
    Height = 41
    Caption = #1054#1090#1084#1077#1085#1072
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -12
    Font.Name = 'Segoe UI'
    Font.Style = [fsBold]
    ParentFont = False
    TabOrder = 2
    OnClick = cancelchoosebtnClick
  end
  object DeletePlaylistbtn: TBitBtn
    Left = 137
    Top = 359
    Width = 80
    Height = 41
    Caption = #1059#1076#1072#1083#1080#1090#1100
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -12
    Font.Name = 'Segoe UI'
    Font.Style = [fsBold]
    ParentFont = False
    TabOrder = 3
    OnClick = DeletePlaylistbtnClick
  end
end
