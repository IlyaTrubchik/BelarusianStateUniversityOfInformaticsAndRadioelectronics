import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class MessageBox {
    transient Label message;
    VBox windowPane;
    Button okButton;
    MessageBox(int xl ,int yl,int xr,int yr,String text)
    {
        windowPane = new VBox();
        windowPane.setPrefSize(((xr-xl)/5)*2,(yr-yl)/5);
        windowPane.setLayoutY(((yr-yl)/5)*2);
        windowPane.setLayoutX(((xr-xl)/5)*1.5);
        windowPane.setAlignment(Pos.CENTER);
        windowPane.setSpacing(10);
        windowPane.setStyle("-fx-background-radius:10; -fx-background-color: white;-fx-border-radius: 10;-fx-border-width: 3;-fx-border-color: orange;-fx-font-size: 20px;-fx-font-weight: 900");
        this.okButton = new Button("OK");
        this.okButton.setAlignment(Pos.CENTER);
        this.okButton.setOnAction(event->{
            this.hide();
        });
        this.message = new Label(text);
        this.message.setTextAlignment(TextAlignment.CENTER);
        this.message.setAlignment(Pos.CENTER);
        this.okButton.getStyleClass().add("buttons");
        windowPane.getChildren().addAll(this.message,this.okButton);
        this.hide();

    }

    MessageBox(){
    }
    public void render(){
        windowPane.setVisible(true);
    }
    public void hide(){
        windowPane.setVisible(false);
    }
    public void addMessageBox(Group root)
    {
        root.getChildren().add(windowPane);
    }
    public void setText(String text)
    {
        this.message.setText(text);
    }

}
