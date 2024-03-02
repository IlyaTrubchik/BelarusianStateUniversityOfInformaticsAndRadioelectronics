import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
    int Spen;
    private final double PModifier = 1;
    private  final  double MModifier = 2;
    private  final  double CModifier = 3;
    private  final  double TModifier = 0.5;
    @FXML
    TableView spenTable;
    @FXML
    TableView chapinTable;
    @FXML
    TableView IOChapinTable;
    @FXML
    TextField spenTextField;
    @FXML
    Button metricButton;
    @FXML
    TextArea codeTextArea;
    @FXML
    Button openFileButton;
    @FXML
    TextField chapinTextField;
    @FXML
    TextField IOChapinTextField;
    @FXML
    TableColumn<tableObject,String> CCol;
    @FXML
    TableColumn<tableObject,String> PCol;
    @FXML
    TableColumn<tableObject,String> MCol;
    @FXML
    TableColumn<tableObject,String> TCol;
    @FXML
    TableColumn<tableObject,String> IOCCol;
    @FXML
    TableColumn<tableObject,String> IOPCol;
    @FXML
    TableColumn<tableObject,String> IOMCol;
    @FXML
    TableColumn<tableObject,String> IOTCol;
    @FXML
    TableColumn<tableSpenObject,String> nameCol;
    @FXML
    TableColumn<tableSpenObject,Integer> countCol;
    Tokenizer tokenizer;
    @FXML
    private void onOpenFile(){
        FileChooser fOpen = new FileChooser();
        File file = fOpen.showOpenDialog(LabApplication.mainStage);
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
                codeTextArea.setText(sb.toString());
            } catch (FileNotFoundException e) {

            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void onMetricButton(){
        Spen = 0;
        ArrayList<String> codeArr = new ArrayList<>();
        String text = codeTextArea.getText();
        int  index = 0;
        while(text.contains("\n")!=false)
        {
            index = text.indexOf("\n");
            codeArr.add(text.substring(0,index));
            text = new String(text.substring(index+1));
        };
        codeArr.add(text);
        tokenizer =new Tokenizer();
        for(int  i =0 ;i<codeArr.size();i++)
            tokenizer.tokenize(codeArr.get(i));
        tokenizer.checkOnParasitOperands();
        tokenizer.printOperands();
        updateChapinTable();
        updateSpenTable();
        updateIOChapinTable();
    }
    private void updateSpenTable(){
        ArrayList<tableSpenObject> tableObjects = new ArrayList<>();
        for (Token token:this.tokenizer.tok) {
            if(!token.isFunction && token.get_type().equals("ID"))
            {
                tableObjects.add(new tableSpenObject(token.get_name(), token.get_count()-1));
                this.Spen +=token.get_count()-1;
            }
        }
        countCol.setCellValueFactory(new PropertyValueFactory<tableSpenObject,Integer>("count"));
        nameCol.setCellValueFactory(new PropertyValueFactory<tableSpenObject,String>("name"));
        ObservableList<tableSpenObject> objects = FXCollections.observableArrayList(tableObjects);
        spenTable.setItems(objects);
        spenTextField.setText(Integer.toString(this.Spen));
    }
    private void updateIOChapinTable(){
        int CCount = 0;
        int PCount = 0;
        int TCount = 0;
        int MCount = 0;
        StringBuilder CStr = new StringBuilder();
        StringBuilder MStr = new StringBuilder();
        StringBuilder PStr = new StringBuilder();
        StringBuilder TStr = new StringBuilder();
        for (Token token:this.tokenizer.IOop) {
            if(this.tokenizer.opC.contains(token)) {
                CCount++;
                CStr.append(token.get_name());
                CStr.append(",\n");
            }
            if(this.tokenizer.opM.contains(token)) {
                MCount++;
                MStr.append(token.get_name());
                MStr.append(",\n");
            }
            if(this.tokenizer.opP.contains(token)) {
                PCount++;
                PStr.append(token.get_name());
                PStr.append(",\n");
            }
            if(this.tokenizer.opT.contains(token)) {
                TCount++;
                TStr.append(token.get_name());
                TStr.append(",\n");
            }
        }
        ArrayList<tableObject> Tobjects = new ArrayList<>();
        Tobjects.add(new tableObject(PStr.toString(),MStr.toString(),
                CStr.toString(),TStr.toString()));
        Tobjects.add(new tableObject(Integer.toString(PCount),Integer.toString(MCount),
                Integer.toString(CCount),Integer.toString(TCount)));
        IOCCol.setCellValueFactory(new PropertyValueFactory<tableObject,String>("ccolumn"));
        IOMCol.setCellValueFactory(new PropertyValueFactory<tableObject,String>("mcolumn"));
        IOPCol.setCellValueFactory(new PropertyValueFactory<tableObject,String>("pcolumn"));
        IOTCol.setCellValueFactory(new PropertyValueFactory<tableObject,String>("tcolumn"));
        ObservableList<tableObject> objects = FXCollections.observableArrayList(Tobjects);
        IOChapinTable.setItems(objects);
        double resultNum = PModifier*PCount+CCount*CModifier+TCount*TModifier+MCount*MModifier;
        String result = PModifier+" * "+PCount+"+"+MModifier+" * "+MCount+"+"+CModifier+" * "+CCount+"+"+TModifier+" * "
                +TCount+" = "+resultNum;
        IOChapinTextField.setText(result);
    }
    private void updateChapinTable(){
        int CCount = tokenizer.opC.size();
        int PCount = tokenizer.opP.size();
        int TCount = tokenizer.opT.size();
        int MCount = tokenizer.opM.size();
        StringBuilder CStr = new StringBuilder();
        StringBuilder MStr = new StringBuilder();
        StringBuilder PStr = new StringBuilder();
        StringBuilder TStr = new StringBuilder();
        for (Token token:tokenizer.opC) {
            CStr.append(token.get_name());
            CStr.append(",\n");
        }
        for (Token token:tokenizer.opM) {
            MStr.append(token.get_name());
            MStr.append(",\n");
        }
        for (Token token:tokenizer.opP) {
            PStr.append(token.get_name());
            PStr.append(",\n");
        }
        for (Token token:tokenizer.opT) {
            TStr.append(token.get_name());
            TStr.append(",\n");
        }
        ArrayList<tableObject> Tobjects = new ArrayList<>();
        Tobjects.add(new tableObject(PStr.toString(),MStr.toString(),
                CStr.toString(),TStr.toString()));
        Tobjects.add(new tableObject(Integer.toString(PCount),Integer.toString(MCount),
                Integer.toString(CCount),Integer.toString(TCount)));
        PCol.setCellValueFactory(new PropertyValueFactory<tableObject,String>("pcolumn"));
        MCol.setCellValueFactory(new PropertyValueFactory<tableObject,String>("mcolumn"));
        CCol.setCellValueFactory(new PropertyValueFactory<tableObject,String>("ccolumn"));
        TCol.setCellValueFactory(new PropertyValueFactory<tableObject,String>("tcolumn"));
        ObservableList<tableObject> objects = FXCollections.observableArrayList(Tobjects);
        chapinTable.setItems(objects);
        double resultNum = PModifier*PCount+CCount*CModifier+TCount*TModifier+MCount*MModifier;
        String result = PModifier+" * "+PCount+"+"+MModifier+" * "+MCount+"+"+CModifier+" * "+CCount+"+"+TModifier+" * "
                +TCount+" = "+resultNum;
        chapinTextField.setText(result);
    }
    public class tableSpenObject{
        private final SimpleStringProperty name;
        private final SimpleIntegerProperty count;
        tableSpenObject(String name,int count)
        {
            this.name = new SimpleStringProperty(name);
            this.count = new SimpleIntegerProperty(count);
        }
        public String getName(){return  this.name.get();}
        public Integer getCount(){return  this.count.get();}
    }
    public class tableObject{
        private final SimpleStringProperty pcolumn;
        private final SimpleStringProperty mcolumn;
        private final SimpleStringProperty ccolumn;
        private final SimpleStringProperty tcolumn;
        tableObject(String pcolumn,String mcolumn,String ccolumn,String tcolumn)
        {
            this.pcolumn = new SimpleStringProperty(pcolumn);
            this.mcolumn = new SimpleStringProperty(mcolumn);
            this.ccolumn = new SimpleStringProperty(ccolumn);
            this.tcolumn = new SimpleStringProperty(tcolumn);
        }
        public String getPcolumn(){return  this.pcolumn.get();}
        public String getMcolumn(){return  this.mcolumn.get();}
        public String getCcolumn(){return  this.ccolumn.get();}
        public String getTcolumn(){return  this.tcolumn.get();}
    }

}
