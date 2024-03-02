package com.example.lab1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloController {

    public static boolean isValid =true;

    @FXML
    private Button decimaciiBtn;

    @FXML
    private Button decryptButton;

    @FXML
    private Button encryptButton;

    @FXML
    private Button loadFromFileButton;

    @FXML
    private TextArea enterTF;

    @FXML
    private TextField keyField;

    @FXML
    private Button keyBtn;


    @FXML
    private TextArea returnTF;

    @FXML
    private Button vizhinerBtn;

    @FXML
    private Label resultLabel;

    @FXML
    public void onDecimaciiClick(){
        HelloApplication.mode =0;
        decimaciiBtn.setDisable(true);
        vizhinerBtn.setDisable(false);
    }

    @FXML
    public void onVizhinerCLick(){
        HelloApplication.mode =1;
        decimaciiBtn.setDisable(false);
        vizhinerBtn.setDisable(true);
    }
    @FXML
    public void onKeyBtn(){
        HelloApplication.key=keyField.getText();
    }
    @FXML
    public void onEncypt() throws IOException {
        if(HelloApplication.mode == 0) {
            String str = cleanStr(HelloApplication.key);
            if(str !="") {
                DeciCypher a = new DeciCypher("", enterTF.getText().toLowerCase(), Integer.parseInt(str));
                String result =a.getCypher();
                returnTF.setText(result);
                FileWriter fw = new FileWriter("C:\\Users\\Ilya\\Desktop\\result.txt");
                fw.write(result);
                fw.close();
                if (isValid == false) {
                    HelloApplication.alert.show();
                }
            }else HelloApplication.alert.show();
        }
        else{
            VizhenCyphr a = new VizhenCyphr(enterTF.getText().toLowerCase(),"" , HelloApplication.key.toLowerCase());
            String result = a.getCypher();
            returnTF.setText(result);
            FileWriter fw = new FileWriter("C:\\Users\\Ilya\\Desktop\\result.txt");
            fw.write(result);
            fw.close();
            if(isValid == false)
            {
                HelloApplication.alert.show();
            }
        }
    }
    @FXML
    public void onDecrypt() throws IOException {
        if(HelloApplication.mode == 0) {
            String str = cleanStr(HelloApplication.key);
            if(str !="") {
                DeciCypher a = new DeciCypher(enterTF.getText().toLowerCase(), "",Integer.parseInt(str));
                String result = a.decypher();
                FileWriter fw = new FileWriter("C:\\Users\\Ilya\\Desktop\\result.txt");
                fw.write(result);
                fw.close();
                returnTF.setText(result);
                if(isValid == false)
                {
                    HelloApplication.alert.show();
                }
            }
            else HelloApplication.alert.show();

        }else
        {

            VizhenCyphr a = new VizhenCyphr("",enterTF.getText().toLowerCase() , HelloApplication.key.toLowerCase());
            String result =a.getDecypher();
            returnTF.setText(result);
            FileWriter fw = new FileWriter("C:\\Users\\Ilya\\Desktop\\result.txt");
            fw.write(result);
            fw.close();
            if(isValid == false)
            {
                HelloApplication.alert.show();
            }

        }
    }
    @FXML
    public void onOpenFile()
    {
        FileChooser fOpen = new FileChooser();
        File file = fOpen.showOpenDialog(HelloApplication.stage);
        if(file !=null) {
            try {
                BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
                String s;
                StringBuilder sb = new StringBuilder();
                while ((s = br1.readLine()) != null) {
                    sb.append(s);
                    sb.append('\n');
                }
                br1.close();
                enterTF.setText(sb.toString());
            } catch (FileNotFoundException e) {

            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public String cleanStr(String str){
        char[] arr = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i< arr.length;i++)
        {
            if(arr[i]>='0' && arr[i] <='9') sb.append(arr[i]);
        }
        return sb.toString();
    }
}


