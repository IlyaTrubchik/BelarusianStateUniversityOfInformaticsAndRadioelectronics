﻿#pragma checksum "..\..\..\MainWindow.xaml" "{ff1816ec-aa5e-4d10-87f7-6f4963833460}" "7F36B83767F85B76DFEFEBACBE34DF6AE06D46E0"
//------------------------------------------------------------------------------
// <auto-generated>
//     Этот код создан программой.
//     Исполняемая версия:4.0.30319.42000
//
//     Изменения в этом файле могут привести к неправильной работе и будут потеряны в случае
//     повторной генерации кода.
// </auto-generated>
//------------------------------------------------------------------------------

using Lab1Wpf;
using System;
using System.Diagnostics;
using System.Windows;
using System.Windows.Automation;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Controls.Ribbon;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Markup;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Media.Effects;
using System.Windows.Media.Imaging;
using System.Windows.Media.Media3D;
using System.Windows.Media.TextFormatting;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Shell;


namespace Lab1Wpf {
    
    
    /// <summary>
    /// MainWindow
    /// </summary>
    public partial class MainWindow : System.Windows.Window, System.Windows.Markup.IComponentConnector {
        
        
        #line 10 "..\..\..\MainWindow.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.Button OpenBtn;
        
        #line default
        #line hidden
        
        
        #line 11 "..\..\..\MainWindow.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.Image drawingImage;
        
        #line default
        #line hidden
        
        
        #line 26 "..\..\..\MainWindow.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.ComboBox modeBox;
        
        #line default
        #line hidden
        
        
        #line 37 "..\..\..\MainWindow.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.TextBox rBox;
        
        #line default
        #line hidden
        
        
        #line 38 "..\..\..\MainWindow.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.TextBox gBox;
        
        #line default
        #line hidden
        
        
        #line 39 "..\..\..\MainWindow.xaml"
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1823:AvoidUnusedPrivateFields")]
        internal System.Windows.Controls.TextBox bBox;
        
        #line default
        #line hidden
        
        private bool _contentLoaded;
        
        /// <summary>
        /// InitializeComponent
        /// </summary>
        [System.Diagnostics.DebuggerNonUserCodeAttribute()]
        [System.CodeDom.Compiler.GeneratedCodeAttribute("PresentationBuildTasks", "7.0.3.0")]
        public void InitializeComponent() {
            if (_contentLoaded) {
                return;
            }
            _contentLoaded = true;
            System.Uri resourceLocater = new System.Uri("/Lab3Net;component/mainwindow.xaml", System.UriKind.Relative);
            
            #line 1 "..\..\..\MainWindow.xaml"
            System.Windows.Application.LoadComponent(this, resourceLocater);
            
            #line default
            #line hidden
        }
        
        [System.Diagnostics.DebuggerNonUserCodeAttribute()]
        [System.CodeDom.Compiler.GeneratedCodeAttribute("PresentationBuildTasks", "7.0.3.0")]
        [System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Never)]
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Design", "CA1033:InterfaceMethodsShouldBeCallableByChildTypes")]
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Maintainability", "CA1502:AvoidExcessiveComplexity")]
        [System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1800:DoNotCastUnnecessarily")]
        void System.Windows.Markup.IComponentConnector.Connect(int connectionId, object target) {
            switch (connectionId)
            {
            case 1:
            this.OpenBtn = ((System.Windows.Controls.Button)(target));
            
            #line 10 "..\..\..\MainWindow.xaml"
            this.OpenBtn.Click += new System.Windows.RoutedEventHandler(this.Button_Click);
            
            #line default
            #line hidden
            return;
            case 2:
            this.drawingImage = ((System.Windows.Controls.Image)(target));
            
            #line 11 "..\..\..\MainWindow.xaml"
            this.drawingImage.PreviewMouseWheel += new System.Windows.Input.MouseWheelEventHandler(this.Image_PreviewMouseWheel);
            
            #line default
            #line hidden
            
            #line 11 "..\..\..\MainWindow.xaml"
            this.drawingImage.MouseMove += new System.Windows.Input.MouseEventHandler(this.i_MouseMove);
            
            #line default
            #line hidden
            
            #line 11 "..\..\..\MainWindow.xaml"
            this.drawingImage.MouseLeftButtonDown += new System.Windows.Input.MouseButtonEventHandler(this.onLeftMouseDown);
            
            #line default
            #line hidden
            
            #line 11 "..\..\..\MainWindow.xaml"
            this.drawingImage.MouseLeftButtonUp += new System.Windows.Input.MouseButtonEventHandler(this.onLeftMouseUp);
            
            #line default
            #line hidden
            return;
            case 3:
            
            #line 12 "..\..\..\MainWindow.xaml"
            ((System.Windows.Controls.Button)(target)).Click += new System.Windows.RoutedEventHandler(this.PlusScaleClick);
            
            #line default
            #line hidden
            return;
            case 4:
            
            #line 13 "..\..\..\MainWindow.xaml"
            ((System.Windows.Controls.Button)(target)).Click += new System.Windows.RoutedEventHandler(this.MinusScaleClick);
            
            #line default
            #line hidden
            return;
            case 5:
            
            #line 14 "..\..\..\MainWindow.xaml"
            ((System.Windows.Controls.Button)(target)).Click += new System.Windows.RoutedEventHandler(this.changeMode);
            
            #line default
            #line hidden
            return;
            case 6:
            
            #line 15 "..\..\..\MainWindow.xaml"
            ((System.Windows.Controls.Button)(target)).Click += new System.Windows.RoutedEventHandler(this.changeNormals);
            
            #line default
            #line hidden
            return;
            case 7:
            
            #line 20 "..\..\..\MainWindow.xaml"
            ((System.Windows.Controls.Button)(target)).Click += new System.Windows.RoutedEventHandler(this.addXLight);
            
            #line default
            #line hidden
            return;
            case 8:
            
            #line 21 "..\..\..\MainWindow.xaml"
            ((System.Windows.Controls.Button)(target)).Click += new System.Windows.RoutedEventHandler(this.subtractXLight);
            
            #line default
            #line hidden
            return;
            case 9:
            
            #line 22 "..\..\..\MainWindow.xaml"
            ((System.Windows.Controls.Button)(target)).Click += new System.Windows.RoutedEventHandler(this.subtractYLight);
            
            #line default
            #line hidden
            return;
            case 10:
            
            #line 23 "..\..\..\MainWindow.xaml"
            ((System.Windows.Controls.Button)(target)).Click += new System.Windows.RoutedEventHandler(this.addYLight);
            
            #line default
            #line hidden
            return;
            case 11:
            
            #line 24 "..\..\..\MainWindow.xaml"
            ((System.Windows.Controls.Button)(target)).Click += new System.Windows.RoutedEventHandler(this.addZLight);
            
            #line default
            #line hidden
            return;
            case 12:
            
            #line 25 "..\..\..\MainWindow.xaml"
            ((System.Windows.Controls.Button)(target)).Click += new System.Windows.RoutedEventHandler(this.subtractZLight);
            
            #line default
            #line hidden
            return;
            case 13:
            this.modeBox = ((System.Windows.Controls.ComboBox)(target));
            
            #line 26 "..\..\..\MainWindow.xaml"
            this.modeBox.SelectionChanged += new System.Windows.Controls.SelectionChangedEventHandler(this.ComboBox_SelectionChanged);
            
            #line default
            #line hidden
            return;
            case 14:
            
            #line 35 "..\..\..\MainWindow.xaml"
            ((System.Windows.Controls.Button)(target)).Click += new System.Windows.RoutedEventHandler(this.Button_Click_1);
            
            #line default
            #line hidden
            return;
            case 15:
            this.rBox = ((System.Windows.Controls.TextBox)(target));
            
            #line 37 "..\..\..\MainWindow.xaml"
            this.rBox.TextChanged += new System.Windows.Controls.TextChangedEventHandler(this.rBox_TextChanged);
            
            #line default
            #line hidden
            return;
            case 16:
            this.gBox = ((System.Windows.Controls.TextBox)(target));
            
            #line 38 "..\..\..\MainWindow.xaml"
            this.gBox.TextChanged += new System.Windows.Controls.TextChangedEventHandler(this.gBox_TextChanged);
            
            #line default
            #line hidden
            return;
            case 17:
            this.bBox = ((System.Windows.Controls.TextBox)(target));
            
            #line 39 "..\..\..\MainWindow.xaml"
            this.bBox.TextChanged += new System.Windows.Controls.TextChangedEventHandler(this.bBox_TextChanged);
            
            #line default
            #line hidden
            return;
            }
            this._contentLoaded = true;
        }
    }
}

