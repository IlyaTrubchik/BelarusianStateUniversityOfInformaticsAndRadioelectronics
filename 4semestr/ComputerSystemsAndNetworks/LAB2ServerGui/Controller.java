import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
    private Server server ;
    @FXML
    private TextField portTF;
    @FXML
    private Button startButton;
    @FXML
    private Button  stopButton;

    @FXML
    private TextArea messagesTA ;

    @FXML
    private Label serverIpLabel;
    @FXML
    private void onStart(){
        if(server==null){
            server = new Server();
            server.changePort(Integer.parseInt(portTF.getText()));
            server.start();
        }else this.update("Cant start server on this port");

    }
    @FXML
    private void onStop(){
        if(server!=null) {
            server.stopServer();
            server = null;
            this.messagesTA.appendText("Server stopped");
        }else  this.messagesTA.appendText("Server already disabled");
    }
    public void update(String text){

       this.messagesTA.appendText(text);
        this.messagesTA.appendText("\n");
    }
    @FXML
    public void updateIP()
    {
        serverIpLabel.setText(Server.IP);
    }
    public void close(){
        if(server!=null) {
            server.stopServer();
            server.interrupt();
        }
        else{
            this.messagesTA.appendText("Server already disabled\n");
        }
    }
}