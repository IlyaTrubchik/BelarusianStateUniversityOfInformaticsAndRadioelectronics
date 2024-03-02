package com.example.lab2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class HelloController {
    Path from;
    Path to;
    @FXML
    private Label welcomeText;
    @FXML
    private TextArea plainTextArea;
    @FXML
    private TextArea keyTextArea;
    @FXML
    private TextArea cypherTextArea;
    @FXML
    private Button openFileButton;
    @FXML
    private Button cypherButton;

    @FXML
    private TextField registerTextField;

    @FXML
    private Button chooseFileButton;
    @FXML
    private void onChooseFile(){
        FileChooser fOpen = new FileChooser();
        File file = fOpen.showSaveDialog(HelloApplication.mainStage);
        if(file !=null) {
            this.to = Path.of(file.getPath());
        }
    }
    private boolean checkRegister(){
        String register = registerTextField.getText();
        if(register.length()!=39) return false;
        else{
            for(int i = 0;i<39;i++)
            {
                if(register.charAt(i)!='0' && register.charAt(i)!='1') return false;
            }
        }
        return true;
    }
    @FXML
    private void onCypher(){
        if(checkRegister()) {
            LFSR reg = new LFSR(registerTextField.getText());
            try {
                reg.cypherFile(this.from,this.to);
                SeekableByteChannel plain = Files.newByteChannel(this.from);
                SeekableByteChannel cypher = Files.newByteChannel(this.to);
                SeekableByteChannel key = Files.newByteChannel(Path.of("C:\\Users\\Ilya\\Desktop\\key.txt"));
                StringBuilder plainSb =  new StringBuilder();
                StringBuilder cypherSb =  new StringBuilder();
                StringBuilder keySb = new StringBuilder();
                ByteBuffer plainByte = ByteBuffer.allocate(500);
                ByteBuffer cypherByte = ByteBuffer.allocate(500);
                ByteBuffer keyByte = ByteBuffer.allocate(500);
                plain.read(plainByte);
                cypher.read(cypherByte);
                key.read(keyByte);
                for(int i=0;i<plainByte.position();i++)
                {
                    plainSb.append(String.format("%8s",Integer.toBinaryString(0xFF & (plainByte.get(i)))).replaceAll(" ", "0"));
                    cypherSb.append(String.format("%8s",Integer.toBinaryString(0xFF & cypherByte.get(i))).replaceAll(" ","0"));
                    keySb.append(String.format("%8s",Integer.toBinaryString(0xFF & keyByte.get(i))).replace(" ","0"));
                }
                cypherTextArea.setText(cypherSb.toString());
                plainTextArea.setText(plainSb.toString());
                keyTextArea.setText(keySb.toString());
            } catch (IOException e) {
                System.out.println("Error!!");
            }
        }
        else HelloApplication.alert.show();
    }
        @FXML
        public void onOpenFile()
        {
            FileChooser fOpen = new FileChooser();
            File file = fOpen.showOpenDialog(HelloApplication.mainStage);
            if(file !=null) {
                StringBuilder from = new StringBuilder();
                from.append( file.getPath());
             this.from = Path.of(from.toString());
             int index =  from.indexOf(".");
             String to =  from.insert(index-1,"1").toString();
             this.to = Path.of(to);
            }
        }
}