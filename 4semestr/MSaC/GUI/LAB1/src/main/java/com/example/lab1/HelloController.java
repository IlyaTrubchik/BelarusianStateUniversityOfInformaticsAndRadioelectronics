package com.example.lab1;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.lab1.Main.init_tokens;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    private List<Token> tok;
    @FXML
    private Button analyzeButton;

    @FXML
    private TextField programDictionary;
    @FXML
    private TextField programVolume;
    @FXML
    private TextField programLength;
    @FXML
    private TextField operandDictionary;
    @FXML
    private TextField operatorDictionary;
    @FXML
    private TextField operatorCount;
    @FXML
    private TextField operandCount;
    @FXML
    private Button loadButton;

    @FXML
    private TableView operandTable;

    @FXML
    private TableView operatorTable;

    @FXML
    private TextArea programText;
    @FXML
    protected  void analyzeClick(){
        init_tokens();
        tok = new ArrayList<>();
        String code = "";
        ArrayList<String> codeArr = new ArrayList<>();
        String text = programText.getText();
        int  index = 0;
        while(text.contains("\n")!=false)
        {
            index = text.indexOf("\n");
            codeArr.add(text.substring(0,index));
            text = new String(text.substring(index+1));
        };
        codeArr.add(text);
        int x =0;
    }

    @FXML
    protected  void loadFileClick(){

    }
}