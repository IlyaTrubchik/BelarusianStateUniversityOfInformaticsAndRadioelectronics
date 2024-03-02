import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
    public  Client client ;
    public static StringBuilder sb= new StringBuilder();
    @FXML
    private TextArea messagesTA;
    @FXML
    private Button connectButton;
    @FXML
    private Button disconnectButton;

    @FXML
    private Label nameLabel;
    @FXML
    private Button sendButton;
    @FXML
    private Button changeNameButton;
    @FXML
    private TextField nameTF;
    @FXML
    private TextField portTF;
    @FXML
    private TextField ipTF;
    @FXML
    private TextField enterTF;

    @FXML
    private void onSendClick(){
            if (client != null) {
                client.send(enterTF.getText());
                enterTF.setText("");
            }
    }
    @FXML
    private void onChangeName(){
        client.send("/n "+nameTF.getText());
        nameLabel.setText(nameTF.getText());
    }
    @FXML
    public void onDisconnect(){
        if(Client.isConnected) {
            client.disconnect();
        }
        else{
            ClientApplication.controller.update("Already disconnected");
        }
        client = null;
    }
    @FXML
    private void onConnect(){
        if(client == null || Client.isConnected==false) {
            client = new Client();
            client.changePort(Integer.parseInt(portTF.getText()));
            client.changeIP(ipTF.getText());
            client.connect();
        }
    }
    public  void update(String text){
        if(text!=null) {
            this.messagesTA.appendText(text);
            this.messagesTA.appendText("\n");
        }
    }

}